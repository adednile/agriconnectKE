package com.ecommerce.agriconnectke.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle custom business exceptions
    @ExceptionHandler(AgriconnectException.class)
    public ResponseEntity<ErrorResponse> handleAgriconnectException(AgriconnectException ex, WebRequest request) {
        HttpStatus status = determineHttpStatus(ex);
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            status.value(),
            LocalDateTime.now(),
            getCleanPath(request)
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            "VALIDATION_ERROR",
            "Validation failed for one or more fields",
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now(),
            errors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle Spring Security access denied
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            "ACCESS_DENIED",
            "You don't have permission to access this resource",
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now(),
            getCleanPath(request)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // Handle your custom unauthorized access exception
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccessException(UnauthorizedAccessException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now(),
            getCleanPath(request)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // Handle file upload size exceeded
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            "FILE_TOO_LARGE",
            "File size exceeds maximum allowed limit",
            HttpStatus.PAYLOAD_TOO_LARGE.value(),
            LocalDateTime.now(),
            getCleanPath(request)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        // Log the exception for debugging
        ex.printStackTrace();
        
        ErrorResponse errorResponse = new ErrorResponse(
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred. Please try again later.",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now(),
            getCleanPath(request)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Helper method to determine appropriate HTTP status based on exception type
    private HttpStatus determineHttpStatus(AgriconnectException ex) {
        if (ex instanceof UserNotFoundException || 
            ex instanceof ListingNotFoundException || 
            ex instanceof BidNotFoundException || 
            ex instanceof OrderNotFoundException ||
            ex instanceof DeliveryNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof UnauthorizedAccessException) {
            return HttpStatus.FORBIDDEN;
        } else if (ex instanceof InsufficientQuantityException ||
                   ex instanceof InvalidListingException ||
                   ex instanceof InvalidBidException ||
                   ex instanceof InvalidOrderException ||
                   ex instanceof InvalidImageException ||
                   ex instanceof DuplicateResourceException ||
                   ex instanceof InvalidStatusException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof PaymentProcessingException) {
            return HttpStatus.PAYMENT_REQUIRED;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    // Helper method to clean up the request path
    private String getCleanPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}