package com.adidas.assets.product_service.controller;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.adidas.assets.common_lib.exception.ProductNotFoundException;
import com.adidas.assets.product_service.service.ProductService;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Rest Controller for Product Service.
 * @author Javi
 *
 */
@RestController
public class ProductController {

	@Autowired
	private final ProductService productService;

	@Autowired
	private ModelMapper modelMapper;

	@Value("${adidas.product.api.uri}")
	private String productApiUri;
	
	@Value("${adidas.review.api.uri}")
	private String reviewApiUri;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping(path="/product/{product_id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JsonNode> getProduct(@PathVariable String product_id) throws IOException {
		if (!validateProduct(product_id)) {
			throw new ProductNotFoundException("Product provided does not exist with id:" + product_id);
		}		
		JsonNode node = productService.getProduct(product_id);
		return new ResponseEntity<JsonNode>(node, HttpStatus.OK);				
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
