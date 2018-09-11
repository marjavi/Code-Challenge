package com.adidas.assets.common_lib.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;


@ApiModel
public class ReviewDTO {
	
	

	@NotNull (message="Product Id can't be a null value")
	@JsonProperty("product_id")	
	private String productId;
	private Integer id;
	
	@NotNull (message="Review score value can't be a null value")
	@Min(value=0, message="Review score must be a possitive value")
	@Max(value=10, message="Review score can't be higher than 10")
	@JsonProperty("review_score")
	private Integer reviewScore;
	
	@JsonProperty("review_comments")
	private String reviewComments;
	
	public ReviewDTO() {
		super();
	}

	@JsonCreator
	  public ReviewDTO(@JsonProperty("product_id") String productId,
	    @JsonProperty("review_score") Integer reviewScore, @JsonProperty("review_comments") String reviewComments) {
	    this.productId = productId;
	    this.reviewScore = reviewScore;
	    this.reviewComments = reviewComments;
	    
	  }
		
	public ReviewDTO(@NotNull String productId, @NotNull Integer id,
			@NotNull @Min(value = 0, message = "Review score must be a possitive value") @Max(value = 10, message = "Review score can't be higher than 10") Integer reviewScore,
			String reviewComments) {
		super();
		this.productId = productId;
		this.id = id;
		this.reviewScore = reviewScore;
		this.reviewComments = reviewComments;
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
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the reviewScore
	 */
	public Integer getReviewScore() {
		return reviewScore;
	}

	/**
	 * @param reviewScore the reviewScore to set
	 */
	public void setReviewScore(Integer reviewScore) {
		this.reviewScore = reviewScore;
	}

	/**
	 * @return the reviewComments
	 */
	public String getReviewComments() {
		return reviewComments;
	}

	/**
	 * @param reviewComments the reviewComments to set
	 */
	public void setReviewComments(String reviewComments) {
		this.reviewComments = reviewComments;
	}
	
}
