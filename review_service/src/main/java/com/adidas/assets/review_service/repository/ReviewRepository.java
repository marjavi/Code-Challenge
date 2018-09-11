package com.adidas.assets.review_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer>{
	
	 List<Review> findByProductId(String productID);

}
