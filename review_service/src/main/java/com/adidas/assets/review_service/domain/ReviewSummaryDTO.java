package com.adidas.assets.review_service.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;

@ApiModel
public class ReviewSummaryDTO {

	@JsonProperty("product_id")
	private String productId;
	@JsonProperty("number_of_reviews")
	private Integer numberOfReviews;
	@JsonProperty("average_review_score")
	private Double averageReviewScore;

	/**
	 * 
	 */
	public ReviewSummaryDTO() {
		super();
	}

	/**
	 * @param productId
	 * @param numberOfReviews
	 * @param averageReviewScore
	 */
	public ReviewSummaryDTO(String productId, Integer numberOfReviews, Double averageReviewScore) {
		super();
		this.productId = productId;
		this.numberOfReviews = numberOfReviews;
		this.averageReviewScore = averageReviewScore;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the numberOfReviews
	 */
	public Integer getNumberOfReviews() {
		return numberOfReviews;
	}

	/**
	 * @param numberOfReviews the numberOfReviews to set
	 */
	public void setNumberOfReviews(Integer numberOfReviews) {
		this.numberOfReviews = numberOfReviews;
	}

	/**
	 * @return the averageReviewScore
	 */
	public Double getAverageReviewScore() {
		return averageReviewScore;
	}

	/**
	 * @param averageReviewScore the averageReviewScore to set
	 */
	public void setAverageReviewScore(Double averageReviewScore) {
		this.averageReviewScore = averageReviewScore;
	}
	
	
	
}
