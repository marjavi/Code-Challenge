package com.adidas.assets.review_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adidas.assets.review_service.exception.ReviewNotFoundException;
import com.adidas.assets.review_service.repository.Review;
import com.adidas.assets.review_service.repository.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
	private final ReviewRepository reviewRepository;

	@Autowired
	public ReviewService(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}

	/**
	 * Adss a new review in review repository
	 * 
	 * @param review
	 * @return
	 */
	public Review createReview(Review review) {
		return this.reviewRepository.save(review);
	}

	/**
	 * Updates specified review in review repository
	 * 
	 * @param review
	 * @return
	 */
	public Review updateReview(Review review) {
		Optional<Review> persistedReview = this.reviewRepository.findById(review.getId());
		if (!persistedReview.isPresent()) {
			throw new ReviewNotFoundException("Review with id " + review.getId() + " was not found");		
		}
		return this.reviewRepository.save(review);
	}

	/**
	 * Deletes the review from review repository
	 * 
	 * @param id
	 */
	public void deleteReview(int id) {
		Optional<Review> review = this.reviewRepository.findById(id);
		if (!review.isPresent()) {
			throw new ReviewNotFoundException("Review with id " + id + " was not found");
		}
		this.reviewRepository.delete(review.get());
	}

	/**
	 * Returns all reviews belonging to specified productId
	 * 
	 * @param productId
	 * @return
	 */
	public List<Review> getAllReviews(String productId) {
		return this.reviewRepository.findByProductId(productId);
	}

	/**
	 * Returns the review specified by identifier
	 * @param productId 
	 * 
	 * @param id
	 * @return
	 */
	public Review retrieveReview(String productId, int id) {
		Optional<Review> optReview = this.reviewRepository.findById(id);
		if (!optReview.isPresent()) {
			throw new ReviewNotFoundException("Review with id " + id + " was not found");
		}
		Review review = optReview.get();
		if(!review.getProductId().equals(productId))
		{
			throw new ReviewNotFoundException("Review with id " + id + " does not belong to specified product.");
		}
		return review;

	}

	/**
	 * @return the reviewRepository
	 */
	public ReviewRepository getReviewRepository() {
		return reviewRepository;
	}
	

}
