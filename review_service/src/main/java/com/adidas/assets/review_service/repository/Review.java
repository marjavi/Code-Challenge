package com.adidas.assets.review_service.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "ReviewIdSequenceGenerator", sequenceName = "review_id_sequence", initialValue = 100, allocationSize = 20)
public class Review {
	@Id
	@GeneratedValue(generator = "ReviewIdSequenceGenerator")
	private Integer id;
	private String productId;
	private Integer reviewScore;
	private String reviewComments;

	/**
	 * 
	 */
	public Review() {
		super();
	}

	/**
	 * 
	 * @param id
	 * @param productId
	 * @param reviewScore
	 * @param reviewComments
	 */
	public Review(Integer id, String productId, Integer reviewScore, String reviewComments) {
		super();
		this.id = id;
		this.productId = productId;
		this.reviewScore = reviewScore;
		this.reviewComments = reviewComments;
	}

		
	/**
	 * @param productId
	 * @param reviewScore
	 * @param reviewComments
	 */
	public Review(String productId, Integer reviewScore, String reviewComments) {
		super();
		this.productId = productId;
		this.reviewScore = reviewScore;
		this.reviewComments = reviewComments;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Review [id=" + id + ", productId=" + productId + ", reviewScore=" + reviewScore + ", reviewComments="
				+ reviewComments + "]";
	}
	
	

}
