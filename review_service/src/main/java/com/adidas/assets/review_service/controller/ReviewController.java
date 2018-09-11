package com.adidas.assets.review_service.controller;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.adidas.assets.common_lib.domain.ReviewDTO;
import com.adidas.assets.common_lib.exception.ProductNotFoundException;
import com.adidas.assets.review_service.domain.ReviewSummaryDTO;
import com.adidas.assets.review_service.exception.WrongProductIdException;
import com.adidas.assets.review_service.repository.Review;
import com.adidas.assets.review_service.service.ReviewService;

import io.swagger.annotations.Api;

@RestController
@Api
public class ReviewController {

	@Autowired
	private final ReviewService reviewService;

	@Autowired
	private ModelMapper modelMapper;

	@Value("${adidas.product.api.uri}")
	private String productApiUri;

	@Autowired
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@GetMapping("/review/{product_id}")
	public ReviewSummaryDTO getProductReview(@PathVariable String product_id) {
		if (!validateProduct(product_id)) {
			throw new ProductNotFoundException("Product provided does not exist with id:" + product_id);
		}
		List<Review> reviews = this.reviewService.getAllReviews(product_id);
		IntSummaryStatistics stats = reviews.stream().collect(Collectors.summarizingInt(Review::getReviewScore));
		ReviewSummaryDTO reviewSummaryDTO = new ReviewSummaryDTO(product_id, Integer.valueOf(reviews.size()),
				Double.valueOf(stats.getAverage()));
		return reviewSummaryDTO;
	}

	@GetMapping("/review/{product_id}/reviews")
	@ResponseBody
	public List<ReviewDTO> getAllReviews(@PathVariable String product_id) {
		if (!validateProduct(product_id)) {
			throw new ProductNotFoundException("Product provided does not exist with id:" + product_id);
		}
		List<Review> reviews = reviewService.getAllReviews(product_id);
		List<ReviewDTO> reviewsDTO = reviews.stream().map(review -> convertToDto(review)).collect(Collectors.toList());
		return reviewsDTO;
	}

	@GetMapping(path = "/review/{product_id}/reviews/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> retrieveReview(@PathVariable String product_id, @PathVariable int id) {
		if (!validateProduct(product_id)) {
			throw new ProductNotFoundException("Product provided does not exist with id:" + product_id);
		}
		Review review = reviewService.retrieveReview(product_id, id);
		return new ResponseEntity<>(convertToDto(review), HttpStatus.OK);
	}

	@PostMapping("/review/{product_id}/reviews")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ResponseEntity<Object> createReview(@PathVariable String product_id,
			@RequestBody @Valid ReviewDTO reviewDTO) {
		if (!validateProduct(product_id)) {
			throw new ProductNotFoundException("Product provided does not exist with id:" + product_id);
		}
		Review review = convertToEntity(reviewDTO);
		Review reviewCreated = reviewService.createReview(review);
		return new ResponseEntity<>(convertToDto(reviewCreated), HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/review/{product_id}/reviews/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> updateReview(@PathVariable String product_id, @PathVariable int id,
			@RequestBody @Valid ReviewDTO reviewDTO) {
		if (!validateProduct(product_id)) {
			throw new ProductNotFoundException("Product provided does not exist with id:" + product_id);
		}
		Review currentReview = reviewService.retrieveReview(product_id, id);
		if(!currentReview.getProductId().equals(product_id) || !reviewDTO.getProductId().equals(product_id)) {
			throw new WrongProductIdException("Product Id provided does not correspond with resource's current produt id"+ product_id);
		}
		
		Review review = convertToEntity(reviewDTO);
		Review reviewUpdated = reviewService.updateReview(review);
		return new ResponseEntity<>(convertToDto(reviewUpdated), HttpStatus.OK);
	}

	@DeleteMapping(path = "/review/{product_id}/reviews/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> deleteReview(@PathVariable String product_id, @PathVariable int id) {
		if (!validateProduct(product_id)) {
			throw new ProductNotFoundException("Product provided does not exist with id:" + product_id);
		}
		reviewService.deleteReview(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Converts JPA Entity to Rest API Review
	 * 
	 * @param review
	 * @return
	 */
	private ReviewDTO convertToDto(Review review) {
		ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
		return reviewDTO;
	}

	/**
	 * Converts REST API Review to JPA Entity
	 * 
	 * @param reviewDTO
	 * @return
	 */
	private Review convertToEntity(ReviewDTO reviewDTO) {
		Review review = modelMapper.map(reviewDTO, Review.class);
		return review;
	}

	/**
	 * Verifies that the product exists by connecting to Adidas product API
	 * @param product_id
	 * @return
	 */
	private boolean validateProduct(String product_id) {
		RestTemplate restTemplate = new RestTemplate();
		boolean productFound = false;
		try {
			ResponseEntity<String> response = restTemplate.getForEntity(productApiUri + "/" + product_id, String.class);
			productFound = response.getStatusCode().equals(HttpStatus.OK);
		} catch (HttpClientErrorException e) {
			if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				productFound = false;
			}						
		}
		return productFound;
	}
}
