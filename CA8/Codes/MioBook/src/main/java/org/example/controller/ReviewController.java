package org.example.controller;

import jakarta.validation.Valid;
import org.example.exception.BadRequestException;
import org.example.exception.ForbiddenException;
import org.example.exception.NotFoundException;
import org.example.exception.UnauthorizedException;
import org.example.request.AddReviewRequest;
import org.example.response.Response;
import org.example.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public Response<Object> addReview(@Valid @RequestBody AddReviewRequest addReviewRequest)
            throws ForbiddenException, UnauthorizedException, NotFoundException, BadRequestException {

        reviewService.addReview(addReviewRequest.getTitle(), addReviewRequest.getRate(),
                addReviewRequest.getComment(), LocalDateTime.now());
        return Response.ok("Review added successfully");
    }
}
