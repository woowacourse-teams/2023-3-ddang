package com.ddang.ddang.region.presentation;

import com.ddang.ddang.region.application.RegionService;
import com.ddang.ddang.region.application.dto.ReadRegionDto;
import com.ddang.ddang.region.presentation.dto.response.ReadRegionResponse;
import com.ddang.ddang.region.presentation.dto.response.ReadRegionsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<ReadRegionsResponse> readAllFirst() {
        final List<ReadRegionDto> readRegionDtos = regionService.readAllFirst();
        final List<ReadRegionResponse> readRegionResponses = readRegionDtos.stream()
                                                                           .map(ReadRegionResponse::from)
                                                                           .toList();
        final ReadRegionsResponse response = new ReadRegionsResponse(readRegionResponses);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{firstId}")
    public ResponseEntity<ReadRegionsResponse> readAllSecond(@PathVariable final Long firstId) {
        final List<ReadRegionDto> readRegionDtos = regionService.readAllSecondByFirstRegionId(firstId);
        final List<ReadRegionResponse> readRegionResponses = readRegionDtos.stream()
                                                                           .map(ReadRegionResponse::from)
                                                                           .toList();
        final ReadRegionsResponse response = new ReadRegionsResponse(readRegionResponses);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{firstId}/{secondId}")
    public ResponseEntity<ReadRegionsResponse> readAllSecond(
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
        final ReadRegionsResponse response = new ReadRegionsResponse(readRegionResponses);

        return ResponseEntity.ok(response);
    }
}
