package com.ddang.ddang.region.presentation;

import com.ddang.ddang.region.application.RegionService;
import com.ddang.ddang.region.application.dto.ReadRegionDto;
import com.ddang.ddang.region.presentation.dto.response.ReadRegionResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<List<ReadRegionResponse>> readAllFirst() {
        final List<ReadRegionDto> readRegionDtos = regionService.readAllFirst();
        final List<ReadRegionResponse> readRegionResponses = readRegionDtos.stream()
                                                                           .map(ReadRegionResponse::from)
                                                                           .toList();

        return ResponseEntity.ok(readRegionResponses);
    }

    @GetMapping("/{firstId}")
    public ResponseEntity<List<ReadRegionResponse>> readAllSecond(@PathVariable final Long firstId) {
        final List<ReadRegionDto> readRegionDtos = regionService.readAllSecondByFirstRegionId(firstId);
        final List<ReadRegionResponse> readRegionResponses = readRegionDtos.stream()
                                                                           .map(ReadRegionResponse::from)
                                                                           .toList();

        return ResponseEntity.ok(readRegionResponses);
    }

    @GetMapping("/{firstId}/{secondId}")
    public ResponseEntity<List<ReadRegionResponse>> readAllSecond(
            @PathVariable final Long firstId,
            @PathVariable final Long secondId
    ) {
        final List<ReadRegionDto> readRegionDtos = regionService.readAllThirdByFirstAndSecondRegionId(
                firstId,
                secondId
        );
        final List<ReadRegionResponse> readRegionResponses = readRegionDtos.stream()
                                                                           .map(ReadRegionResponse::from)
                                                                           .toList();

        return ResponseEntity.ok(readRegionResponses);
    }
}
