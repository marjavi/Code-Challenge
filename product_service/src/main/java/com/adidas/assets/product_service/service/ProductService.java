package com.adidas.assets.product_service.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.adidas.assets.common_lib.domain.ReviewDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class ProductService {
	@Value("${adidas.product.api.uri}")
	private String productApiUri;

	@Value("${adidas.review.api.uri}")
	private String reviewApiUri;

	@Autowired
	public ProductService() {
	}

	/**
	 * Returns Json node modified to include list of reviews of the product
	 * @param product_id
	 * @return
	 * @throws IOException
	 */
	public JsonNode getProduct(String product_id) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		JsonNode productNodeRoot = null;
		// Invoke Product API to get product details
		ResponseEntity<String> productResponse = restTemplate.getForEntity(productApiUri + "/" + product_id,
				String.class);
		//Check that response status is OK
		if (productResponse.getStatusCode().equals(HttpStatus.OK)) {
			//Conversion of response body into JSON node object
			ObjectMapper mapper = new ObjectMapper();
			productNodeRoot = mapper.readTree(productResponse.getBody());
			//Invocation to review service to get all reviews of the product
			ResponseEntity<List<ReviewDTO>> reviewsResponse = restTemplate.exchange(
					reviewApiUri + "/" + product_id + "/reviews", HttpMethod.GET, null,
					new ParameterizedTypeReference<List<ReviewDTO>>() {
					});
			//Create an Array node inside product json structure
			ArrayNode reviewsNode = ((ObjectNode) productNodeRoot).putArray("reviews");
			if (reviewsResponse.getStatusCode().equals(HttpStatus.OK)) {
				List<ReviewDTO> reviewsList = reviewsResponse.getBody();
				//For each review, we need to include review details in the JSON node
				for (ReviewDTO review : reviewsList) {
					ObjectNode reviewNode = reviewsNode.addObject();
					reviewNode.put("product_id", review.getProductId());
					reviewNode.put("review_score", review.getReviewScore());
					reviewNode.put("review_comments", review.getReviewComments());
					reviewNode.put("id", review.getId());
				}
			}			
		}
		return productNodeRoot;
	}
}
