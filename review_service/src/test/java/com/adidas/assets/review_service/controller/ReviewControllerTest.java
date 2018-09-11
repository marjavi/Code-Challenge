package com.adidas.assets.review_service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.adidas.assets.review_service.repository.Review;
import com.adidas.assets.review_service.service.ReviewService;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ReviewControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void prepareMockMvc() {
		this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

		// Reset the existing accounts before each test.
		reviewService.getReviewRepository().deleteAll();
	}

	@Test
	public void getAllReviews() throws Exception {
		Review review1 = new Review("C77154", 4, "Fantastic!");
		Review review2 = new Review("C77154", 10, "Great product");
		reviewService.createReview(review1);
		reviewService.createReview(review2);
		this.mockMvc.perform(get("/review/C77154/reviews")).andExpect(status().isOk())
				.andExpect(content().string(
						containsString("\"product_id\":\"C77154\",\"review_score\":4,\"review_comments\":\"Fantastic!\"")))
				.andExpect(content().string(containsString(
						"\"product_id\":\"C77154\",\"review_score\":10,\"review_comments\":\"Great product\"")));
	}

	@Test
	public void retrieveReview() throws Exception {
		Review review = new Review("C77154", 4, "Fantastic!");
		Review createdReview = reviewService.createReview(review);
		this.mockMvc.perform(get("/review/C77154/reviews/" + createdReview.getId())).andExpect(status().isOk())
				.andExpect(content()
						.string("{\"product_id\":\"C77154\",\"review_score\":4,\"review_comments\":\"Fantastic!\",\"id\":"
								+ createdReview.getId() + "}"));
	}

	@Test
	public void retrieveReviewNotFound() throws Exception {
		this.mockMvc.perform(get("/review/C77154/reviews/1")).andExpect(status().isNotFound());
	}

	@Test
	public void createReview() throws Exception {
		this.mockMvc
				.perform(post("/review/C77154/reviews").contentType(MediaType.APPLICATION_JSON)
						.content("{\"product_id\":\"C77154\",\"review_score\":4,\"review_comments\":\"Fantastic!\"}"))
				.andExpect(status().isCreated());
		List<Review> reviews = reviewService.getAllReviews("C77154");
		assertThat(reviews.size()).isEqualTo(1);
		Review review = reviews.get(0);
		assertThat(review.getProductId()).isEqualTo("C77154");
		assertThat(review.getReviewScore()).isEqualTo(4);
		assertThat(review.getReviewComments()).isEqualTo("Fantastic!");
	}

	@Test
	public void createReviewNoProductId() throws Exception {
		this.mockMvc
				.perform(post("/review/C77154/reviews").contentType(MediaType.APPLICATION_JSON)
						.content("{\"review_score\":4,\"review_comments\":\"Fantastic!\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Product Id can't be a null value")));
	}

	@Test
	public void createReviewNoReviewScore() throws Exception {
		this.mockMvc
				.perform(post("/review/C77154/reviews").contentType(MediaType.APPLICATION_JSON)
						.content("{\"product_id\":\"C77154\",\"review_comments\":\"Fantastic!\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Review score value can't be a null value")));
	}

	@Test
	public void createReviewProductNotFound() throws Exception {
		this.mockMvc
				.perform(post("/review/AB1234/reviews").contentType(MediaType.APPLICATION_JSON)
						.content("{\"product_id\":\"AB1234\",\"review_score\":4,\"review_comments\":\"Fantastic!\"}"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void createReviewNegativeScore() throws Exception {
		this.mockMvc
				.perform(post("/review/C77154/reviews").contentType(MediaType.APPLICATION_JSON)
						.content("{\"product_id\":\"C77154\",\"review_score\":-4,\"review_comments\":\"Fantastic!\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Review score must be a possitive value")));
	}

	@Test
	public void createReviewScoreHigherThenAllowed() throws Exception {
		this.mockMvc
				.perform(post("/review/C77154/reviews").contentType(MediaType.APPLICATION_JSON)
						.content("{\"product_id\":\"C77154\",\"review_score\":100,\"review_comments\":\"Fantastic!\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Review score can't be higher than 10")));
	}

	@Test
	public void createReviewNoBody() throws Exception {
		this.mockMvc.perform(post("/review/C77154/reviews").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void updateReview() throws Exception {
		Review review = new Review("C77154", 4, "Fantastic!");
		Review createdReview = reviewService.createReview(review);
		this.mockMvc
				.perform(put("/review/C77154/reviews/" + createdReview.getId()).contentType(MediaType.APPLICATION_JSON)
						.content("{\"product_id\":\"C77154\",\"review_score\":8,\"review_comments\":\"Updated!\",\"id\":"
								+ createdReview.getId() + "}"))
				.andExpect(status().isOk());
		Review updatedReview = reviewService.retrieveReview(createdReview.getProductId(),createdReview.getId());
		assertThat(updatedReview.getProductId()).isEqualTo("C77154");
		assertThat(updatedReview.getReviewScore()).isEqualTo(8);
		assertThat(updatedReview.getReviewComments()).isEqualTo("Updated!");
	}

	@Test
	public void updateReviewNotFound() throws Exception {
		this.mockMvc
				.perform(put("/review/C77154/reviews/12").contentType(MediaType.APPLICATION_JSON).content(
						"{\"product_id\":\"C77154\",\"review_score\":8,\"review_comments\":\"Updated!\",\"id\":12}"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void updateReviewWrongProductId() throws Exception {
		Review review = new Review("C77154", 4, "Fantastic!");
		Review createdReview = reviewService.createReview(review);
		this.mockMvc
				.perform(put("/review/B42000/reviews/" + createdReview.getId()).contentType(MediaType.APPLICATION_JSON)
						.content("{\"product_id\":\"C77154\",\"review_score\":8,\"review_comments\":\"Updated!\",\"id\":"
								+ createdReview.getId() + "}"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void updateReviewWrongProductId2() throws Exception {
		Review review = new Review("C77154", 4, "Fantastic!");
		Review createdReview = reviewService.createReview(review);
		this.mockMvc
				.perform(put("/review/C77154/reviews/" + createdReview.getId()).contentType(MediaType.APPLICATION_JSON)
						.content("{\"product_id\":\"B42000\",\"review_score\":8,\"review_comments\":\"Updated!\",\"id\":"
								+ createdReview.getId() + "}"))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void deleteReview() throws Exception {
		Review review = new Review("C77154", 4, "Fantastic!");
		Review createdReview = reviewService.createReview(review);
		this.mockMvc
				.perform(delete("/review/C77154/reviews/" + createdReview.getId())).andExpect(status().isOk());
		List<Review> reviews = reviewService.getAllReviews(createdReview.getProductId());
		assertThat(reviews.size()).isEqualTo(0);		
	}
	
	@Test
	public void deleteReviewNotFound() throws Exception {
		this.mockMvc
				.perform(delete("/review/C77154/reviews/1")).andExpect(status().isNotFound());		
	}
	
	@Test
	public void getProductReview() throws Exception {
		Review review1 = new Review("C77154", 4, "Fantastic!");
		Review review2 = new Review("C77154", 10, "Great product");
		reviewService.createReview(review1);
		reviewService.createReview(review2);
		this.mockMvc.perform(get("/review/C77154")).andExpect(status().isOk())
		.andExpect(content().string("{\"product_id\":\"C77154\",\"number_of_reviews\":2,\"average_review_score\":7.0}"));
	}
	
	@Test
	public void getProductReview2() throws Exception {
		Review review1 = new Review("C77154", 4, "Fantastic!");
		Review review2 = new Review("C77154", 10, "Great product");
		reviewService.createReview(review1);
		reviewService.createReview(review2);
		this.mockMvc.perform(get("/review/C77154")).andExpect(status().isOk())
		.andExpect(content().string("{\"product_id\":\"C77154\",\"number_of_reviews\":2,\"average_review_score\":7.0}"));
		Review review3 = new Review("C77154", 4, "Great product");
		reviewService.createReview(review3);
		this.mockMvc.perform(get("/review/C77154")).andExpect(status().isOk())
		.andExpect(content().string("{\"product_id\":\"C77154\",\"number_of_reviews\":3,\"average_review_score\":6.0}"));
	}

}
