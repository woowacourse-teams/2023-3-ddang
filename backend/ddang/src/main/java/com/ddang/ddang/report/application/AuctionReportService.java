package com.ddang.ddang.report.application;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.report.application.dto.CreateAuctionReportDto;
import com.ddang.ddang.report.application.dto.ReadAuctionReportDto;
import com.ddang.ddang.report.application.exception.AlreadyReportAuctionException;
import com.ddang.ddang.report.application.exception.InvalidReporterToAuctionException;
import com.ddang.ddang.report.domain.AuctionReport;
import com.ddang.ddang.report.domain.repository.AuctionReportRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuctionReportService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final AuctionReportRepository auctionReportRepository;

    @Transactional
    public Long create(final CreateAuctionReportDto auctionReportDto) {
        final User reporter = userRepository.getByIdOrThrow(auctionReportDto.reporterId());
        final Auction auction = auctionRepository.getTotalAuctionByIdOrThrow(auctionReportDto.auctionId());

        checkInvalidAuctionReport(reporter, auction);

        final AuctionReport auctionReport = auctionReportDto.toEntity(reporter, auction);

        return auctionReportRepository.save(auctionReport)
                                      .getId();
    }

    private void checkInvalidAuctionReport(final User reporter, final Auction auction) {
        if (auction.isOwner(reporter)) {
            throw new InvalidReporterToAuctionException("본인 경매글입니다.");
        }
        if (auctionReportRepository.existsByAuctionIdAndReporterId(auction.getId(), reporter.getId())) {
            throw new AlreadyReportAuctionException("이미 신고한 경매입니다.");
        }
    }

    public List<ReadAuctionReportDto> readAll() {
        final List<AuctionReport> auctionReports = auctionReportRepository.findAll();

        return auctionReports.stream()
                             .map(ReadAuctionReportDto::from)
                             .toList();
    }
}
