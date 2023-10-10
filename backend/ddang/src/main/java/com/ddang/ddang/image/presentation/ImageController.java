package com.ddang.ddang.image.presentation;

import com.ddang.ddang.image.application.ImageService;
import java.net.MalformedURLException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/users/images/{id}")
    public ResponseEntity<Resource> downloadProfileImage(@PathVariable Long id) throws MalformedURLException {
        final Resource resource = imageService.readProfileImage(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/auctions/images/{id}")
    public ResponseEntity<Resource> downloadAuctionImage(@PathVariable Long id) throws MalformedURLException {
        final Resource resource = imageService.readAuctionImage(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
