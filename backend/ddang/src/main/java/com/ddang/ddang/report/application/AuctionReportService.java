package com.ddang.ddang.report.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.report.application.dto.CreateAuctionReportDto;
import com.ddang.ddang.report.application.dto.ReadAuctionReportDto;
import com.ddang.ddang.report.application.exception.AlreadyReportAuctionException;
import com.ddang.ddang.report.application.exception.InvalidReportAuctionException;
import com.ddang.ddang.report.application.exception.InvalidReporterToAuctionException;
import com.ddang.ddang.report.domain.AuctionReport;
import com.ddang.ddang.report.infrastructure.persistence.JpaAuctionReportRepository;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuctionReportService {

    private final JpaAuctionRepository auctionRepository;
    private final JpaUserRepository userRepository;
    private final JpaAuctionReportRepository auctionReportRepository;

    @Transactional
    public Long create(final CreateAuctionReportDto auctionReportDto) {
        // TODO: 2023/08/08 추후 User 패키지 내에 UserNotFoundException이 생긴다면 해당 예외를 사용하도록 수정 하겠습니다.
        final User reporter = userRepository.findById(auctionReportDto.reporterId())
                                            .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));
        final Auction auction = auctionRepository.findById(auctionReportDto.auctionId())
                                                 .orElseThrow(() -> new AuctionNotFoundException("해당 경매를 찾을 수 없습니다."));
        checkInvalidAuctionReport(reporter, auction);

        final AuctionReport auctionReport = auctionReportDto.toEntity(reporter, auction);

        return auctionReportRepository.save(auctionReport)
                                      .getId();
    }

    private void checkInvalidAuctionReport(final User reporter, final Auction auction) {
        if (auction.isOwner(reporter)) {
            throw new InvalidReporterToAuctionException("본인 경매글입니다.");
        }
        if (auction.isDeleted()) {
            throw new InvalidReportAuctionException("이미 삭제된 경매입니다.");
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