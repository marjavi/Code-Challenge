package com.adidas.assets.product_service.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.adidas.assets.product_service.service.ProductService;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ProductControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private ProductService productService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void prepareMockMvc() {
		this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	public void getProduct() throws Exception {
		this.mockMvc.perform(get("/product/C77154")).andExpect(status().isOk());
	}
}
