package com.ddang.ddang.image.presentation;

import com.ddang.ddang.image.application.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/users/images/{storeName}")
    public ResponseEntity<Resource> downloadProfileImage(@PathVariable final String storeName) throws MalformedURLException {
        final Resource resource = imageService.readProfileImage(storeName);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/auctions/images/{storeName}")
    public ResponseEntity<Resource> downloadAuctionImage(@PathVariable final String storeName) throws MalformedURLException {
        final Resource resource = imageService.readAuctionImage(storeName);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
