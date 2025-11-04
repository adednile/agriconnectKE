package com.ecommerce.agriconnectke.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.agriconnectke.models.ListingImage;
import com.ecommerce.agriconnectke.services.ListingImageService;

@RestController
@RequestMapping("/api/listing-images")
@CrossOrigin(origins = "*")
public class ListingImageController {
    private final ListingImageService listingImageService;

    public ListingImageController(ListingImageService listingImageService) {
        this.listingImageService = listingImageService;
    }

    @PostMapping(value = "/{listingId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(
            @PathVariable Long listingId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isPrimary", required = false) Boolean isPrimary) {
        try {
            ListingImage image = listingImageService.uploadImage(listingId, file, isPrimary);
            return ResponseEntity.status(HttpStatus.CREATED).body(image);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/{listingId}/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMultipleImages(
            @PathVariable Long listingId,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            List<ListingImage> images = listingImageService.uploadMultipleImages(listingId, files);
            return ResponseEntity.status(HttpStatus.CREATED).body(images);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listing/{listingId}")
    public ResponseEntity<List<ListingImage>> getImagesByListingId(@PathVariable Long listingId) {
        try {
            List<ListingImage> images = listingImageService.getImagesByListingId(listingId);
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listing/{listingId}/primary")
    public ResponseEntity<ListingImage> getPrimaryImageByListingId(@PathVariable Long listingId) {
        try {
            Optional<ListingImage> primaryImage = listingImageService.getPrimaryImageByListingId(listingId);
            return primaryImage.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{imageId}/set-primary")
    public ResponseEntity<?> setAsPrimaryImage(@PathVariable Long imageId) {
        try {
            ListingImage image = listingImageService.setAsPrimaryImage(null, imageId);
            return ResponseEntity.ok(image);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/{imageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(
            @PathVariable Long imageId,
            @RequestParam("file") MultipartFile file) {
        try {
            ListingImage updatedImage = listingImageService.updateImage(imageId, file);
            return ResponseEntity.ok(updatedImage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        try {
            listingImageService.deleteImage(imageId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/listing/{listingId}")
    public ResponseEntity<Void> deleteAllImagesByListingId(@PathVariable Long listingId) {
        try {
            listingImageService.deleteAllImagesByListingId(listingId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listing/{listingId}/count")
    public ResponseEntity<Long> getImageCountByListingId(@PathVariable Long listingId) {
        try {
            Long count = listingImageService.getImageCountByListingId(listingId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listing/{listingId}/has-primary")
    public ResponseEntity<Boolean> hasPrimaryImage(@PathVariable Long listingId) {
        try {
            boolean hasPrimary = listingImageService.hasPrimaryImage(listingId);
            return ResponseEntity.ok(hasPrimary);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}