package com.codefellowship.codefellowship;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CodefellowshipApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldReturnSignUpForm() throws Exception {
		String expected ="<form method=\"POST\" action=\"/signup\">";
		this.mockMvc.perform(get("/signup")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(expected)));
	}

	@Test
	public void shouldReturnLoginForm() throws Exception {
		String expected ="<form action=\"perform_login\" method='POST'>";
		this.mockMvc.perform(get("/login")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(expected)));
	}

	@Test
	public void shouldReturnHomePage() throws Exception {
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("<h1>Welcome <span></span> to Code Fellowship App.</h1>")));
	}



}
