package com.ecommerce.agriconnectke.services;

import com.ecommerce.agriconnectke.exceptions.InvalidImageException;
import com.ecommerce.agriconnectke.exceptions.ListingImageNotFoundException;
import com.ecommerce.agriconnectke.exceptions.ListingNotFoundException;
import com.ecommerce.agriconnectke.models.Listing;
import com.ecommerce.agriconnectke.models.ListingImage;
import com.ecommerce.agriconnectke.repositories.ListingImageRepository;
import com.ecommerce.agriconnectke.repositories.ListingRepository;
import com.ecommerce.agriconnectke.utils.FileUploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ListingImageService {
    private final ListingImageRepository listingImageRepository;
    private final ListingRepository listingRepository;
    private final FileUploadUtil fileUploadUtil;

    public ListingImageService(ListingImageRepository listingImageRepository, 
                             ListingRepository listingRepository,
                             FileUploadUtil fileUploadUtil) {
        this.listingImageRepository = listingImageRepository;
        this.listingRepository = listingRepository;
        this.fileUploadUtil = fileUploadUtil;
    }

    public ListingImage uploadImage(Long listingId, MultipartFile file, Boolean isPrimary) {
        // Validate listing exists
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException(listingId));

        // Validate file
        validateImageFile(file);

        try {
            // Upload file and get URL
            String imageUrl = fileUploadUtil.saveFile(file);

            // Create image record
            ListingImage image = new ListingImage();
            image.setListingId(listingId);
            image.setImageUrl(imageUrl);
            
            // Handle primary image logic
            if (isPrimary != null && isPrimary) {
                setAsPrimaryImage(listingId, null); // Clear existing primary first
                image.setIsPrimary(true);
            } else {
                image.setIsPrimary(false);
            }

            return listingImageRepository.save(image);
        } catch (IOException e) {
            throw new InvalidImageException("Failed to upload image: " + e.getMessage());
        }
    }

    public List<ListingImage> getImagesByListingId(Long listingId) {
        // Validate listing exists
        if (!listingRepository.existsById(listingId)) {
            throw new ListingNotFoundException(listingId);
        }
        
        return listingImageRepository.findByListingIdOrderByPrimaryAndUploadDate(listingId);
    }

    public Optional<ListingImage> getPrimaryImageByListingId(Long listingId) {
        return listingImageRepository.findPrimaryImageByListingId(listingId);
    }

    public ListingImage setAsPrimaryImage(Long listingId, Long imageId) {
        // Validate listing exists
        if (!listingRepository.existsById(listingId)) {
            throw new ListingNotFoundException(listingId);
        }

        // Clear existing primary images
        listingImageRepository.clearPrimaryImages(listingId);

        // Set new primary image
        ListingImage image = listingImageRepository.findById(imageId)
                .orElseThrow(() -> new ListingImageNotFoundException(imageId));

        // Verify image belongs to the listing
        if (!image.getListingId().equals(listingId)) {
            throw new InvalidImageException("Image does not belong to the specified listing");
        }

        image.setIsPrimary(true);
        return listingImageRepository.save(image);
    }

    public void deleteImage(Long imageId) {
        ListingImage image = listingImageRepository.findById(imageId)
                .orElseThrow(() -> new ListingImageNotFoundException(imageId));

        try {
            // Delete physical file
            fileUploadUtil.deleteFile(image.getImageUrl());
            
            // Delete database record
            listingImageRepository.deleteById(imageId);
        } catch (IOException e) {
            throw new InvalidImageException("Failed to delete image file: " + e.getMessage());
        }
    }

    public void deleteAllImagesByListingId(Long listingId) {
        // Get all images for the listing
        List<ListingImage> images = listingImageRepository.findByListingId(listingId);
        
        // Delete physical files
        for (ListingImage image : images) {
            try {
                fileUploadUtil.deleteFile(image.getImageUrl());
            } catch (IOException e) {
                // Log error but continue with other images
                System.err.println("Failed to delete image file: " + image.getImageUrl());
            }
        }
        
        // Delete database records
        listingImageRepository.deleteByListingId(listingId);
    }

    public ListingImage updateImage(Long imageId, MultipartFile file) {
        ListingImage existingImage = listingImageRepository.findById(imageId)
                .orElseThrow(() -> new ListingImageNotFoundException(imageId));

        // Validate new file
        validateImageFile(file);

        try {
            // Delete old physical file
            fileUploadUtil.deleteFile(existingImage.getImageUrl());

            // Upload new file
            String newImageUrl = fileUploadUtil.saveFile(file);
            existingImage.setImageUrl(newImageUrl);

            return listingImageRepository.save(existingImage);
        } catch (IOException e) {
            throw new InvalidImageException("Failed to update image: " + e.getMessage());
        }
    }

    public Long getImageCountByListingId(Long listingId) {
        return listingImageRepository.countByListingId(listingId);
    }

    public boolean hasPrimaryImage(Long listingId) {
        return listingImageRepository.existsByListingIdAndIsPrimaryTrue(listingId);
    }

    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidImageException("Image file is required");
        }

        if (!fileUploadUtil.isImageFile(file)) {
            throw new InvalidImageException("File must be an image (JPEG, PNG, GIF, etc.)");
        }

        // Validate file size (max 5MB)
        fileUploadUtil.validateFileSize(file, 5 * 1024 * 1024); // 5MB in bytes
    }

    public List<ListingImage> uploadMultipleImages(Long listingId, List<MultipartFile> files) {
        // Validate listing exists
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException(listingId));

        // Validate number of images (max 10 per listing)
        long currentImageCount = listingImageRepository.countByListingId(listingId);
        if (currentImageCount + files.size() > 10) {
            throw new InvalidImageException("Maximum 10 images allowed per listing");
        }

        return files.stream()
                .map(file -> uploadImage(listingId, file, false))
                .toList();
    }
}