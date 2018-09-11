package com.adidas.assets.review_service.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ReviewRepositoryTest {

	@Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Before
    public void cleanAllReviews() {
    	reviewRepository.deleteAll();
    }
    
    
    @Test
    public void findById() {
        Review review = new Review("C77154", 4, "Fantastic!");
        entityManager.persist(review);
        entityManager.flush();
        Integer id=(Integer) entityManager.getId(review);
        Optional<Review> found = reviewRepository.findById(id);
     
        // then
        assertTrue(found.isPresent());
        assertThat(found.get().getProductId().equals("C77154"));
        assertThat(found.get().getReviewScore().equals(4));
        assertThat(found.get().getReviewComments().equals("Fantastic!"));
    }
    
    @Test
    public void findByProductId() {
        Review review = new Review("C77154", 4, "Fantastic!");
        entityManager.persist(review);
        entityManager.flush();        
        List<Review> reviews = reviewRepository.findByProductId("C77154");
     
        // then
        assertThat(reviews.size()).isEqualTo(1);
        Review retrievedReview = reviews.get(0);
        assertNotNull(retrievedReview);
        assertThat(retrievedReview.getProductId().equals("C77154"));
        assertThat(retrievedReview.getReviewScore().equals(4));
        assertThat(retrievedReview.getReviewComments().equals("Fantastic!"));
    }
    
    @Test
    public void createReview() {
        Review review = new Review("C77154", 4, "Fantastic!");
        reviewRepository.save(review);
        Integer id=(Integer) entityManager.getId(review);
        Review found = entityManager.find(Review.class, id);
     
        // then
        assertThat(id).isGreaterThan(100);
        assertThat(found.getProductId().equals("C77154"));
        assertThat(found.getReviewScore().equals(4));
        assertThat(found.getReviewComments().equals("Fantastic!"));
        
    }
    
    @Test
    public void updatetReview() {
        Review review = new Review("C77154", 4, "Fantastic!");
        entityManager.persist(review);
        entityManager.flush();
        Integer id=(Integer) entityManager.getId(review);
        Review found = entityManager.find(Review.class, id);
        found.setReviewScore(10);
        found.setReviewComments("Updated comments");
        reviewRepository.save(found);
        
        Review foundUpdated = entityManager.find(Review.class, id);
     
        // then
        assertThat(foundUpdated.getProductId().equals("C77154"));
        assertThat(foundUpdated.getReviewScore().equals(10));
        assertThat(foundUpdated.getReviewComments().equals("Updated comments"));        
    }
    
    @Test
    public void deleteReview() {
        Review review = new Review("C77154", 4, "Fantastic!");
        entityManager.persist(review);
        entityManager.flush();
        Integer id=(Integer) entityManager.getId(review);
        Review found = entityManager.find(Review.class, id);
        
        reviewRepository.delete(found);
        
        Review removed = entityManager.find(Review.class, id);
     
        // then
        assertNull(removed);                
    }


}

