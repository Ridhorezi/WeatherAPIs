package com.weatherapi.weatherforecast.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherapi.weatherforecast.common.Location;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(LocationApiController.class)
public class LocationApiControllerTests {

	private static final String END_POINT_PATH = "/v1/locations";

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	LocationService locationService;

	@Test
	public void testAddShouldReturn400BadRequest() throws Exception {

		Location location = new Location();

		String bodyContent = objectMapper.writeValueAsString(location);

		mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testAddShouldReturn201Created() throws Exception {

		Location location = new Location();

		location.setCode("JKT");
		location.setCityName("Jakarta");
		location.setRegionName("Jakarta");
		location.setCountryCode("ID");
		location.setCountryName("Indonesia");
		location.setEnabled(true);

		Mockito.when(locationService.add(location)).thenReturn(location);

		String bodyContent = objectMapper.writeValueAsString(location);

		mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isCreated()).andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.code", is("JKT"))).andExpect(jsonPath("$.city_name", is("Jakarta")))
				.andExpect(header().string("Location", "/v1/locations/JKT")).andDo(print());
	}

	@Test
	public void testListShouldReturn204NoContent() throws Exception {

		Mockito.when(locationService.list()).thenReturn(Collections.emptyList());

		mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isNoContent()).andDo(print());
	}

	@Test
	public void testListShouldReturn200OK() throws Exception {

		Location location1 = new Location();

		location1.setCode("JKT");
		location1.setCityName("Jakarta");
		location1.setRegionName("Jakarta");
		location1.setCountryCode("ID");
		location1.setCountryName("Indonesia");
		location1.setEnabled(true);

		Location location2 = new Location();

		location2.setCode("DELHI_IN");
		location2.setCityName("New Delhi");
		location2.setRegionName("Delhi");
		location2.setCountryCode("IN");
		location2.setCountryName("India");
		location2.setEnabled(true);

		Mockito.when(locationService.list()).thenReturn(List.of(location1, location2));

		mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isOk()).andExpect(jsonPath("$[0].code", is("JKT")))
				.andExpect(jsonPath("$[0].city_name", is("Jakarta"))).andExpect(jsonPath("$[1].code", is("DELHI_IN")))
				.andExpect(jsonPath("$[1].city_name", is("New Delhi"))).andDo(print());
	}

	@Test
	public void testGetShouldReturn405MethodNotAllowed() throws Exception {

		String requestURI = END_POINT_PATH + "/ABCDEF";

		mockMvc.perform(post(requestURI)).andExpect(status().isMethodNotAllowed()).andDo(print());
	}

	@Test
	public void testGetShouldReturn404NotFound() throws Exception {

		String requestURI = END_POINT_PATH + "/ABCDEF";

		mockMvc.perform(get(requestURI)).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testGetShouldReturn200OK() throws Exception {

		String code = "JKT";

		String requestURI = END_POINT_PATH + "/" + code;

		Location location = new Location();

		location.setCode("JKT");
		location.setCityName("Jakarta");
		location.setRegionName("Jakarta");
		location.setCountryCode("ID");
		location.setCountryName("Indonesia");
		location.setEnabled(true);

		Mockito.when(locationService.get(code)).thenReturn(location);

		mockMvc.perform(get(requestURI)).andExpect(status().isOk()).andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.code", is(code))).andExpect(jsonPath("$.city_name", is("Jakarta")))
				.andDo(print());
	}

	@Test
	public void testUpdateShouldReturn404NotFound() throws Exception {

		Location location = new Location();

		location.setCode("ABCDEF");
		location.setCityName("Jakarta");
		location.setRegionName("Jakarta");
		location.setCountryCode("ID");
		location.setCountryName("Indonesia");
		location.setEnabled(true);

		Mockito.when(locationService.update(location))
				.thenThrow(new LocationNotFoundException("No location found with the given code"));

		String bodyContent = objectMapper.writeValueAsString(location);

		mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testUpdateShouldReturn400BadRequest() throws Exception {

		Location location = new Location();

		location.setCityName("Jakarta");
		location.setRegionName("Jakarta");
		location.setCountryCode("ID");
		location.setCountryName("Indonesia");
		location.setEnabled(true);

		String bodyContent = objectMapper.writeValueAsString(location);

		mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testPutShouldReturn200OK() throws Exception {

		Location location = new Location();

		location.setCode("JKT");
		location.setCityName("Jakarta");
		location.setRegionName("Jakarta");
		location.setCountryCode("ID");
		location.setCountryName("Indonesia");
		location.setEnabled(true);

		Mockito.when(locationService.update(location)).thenReturn(location);

		String bodyContent = objectMapper.writeValueAsString(location);

		mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.code", is("JKT"))).andExpect(jsonPath("$.city_name", is("Jakarta")))
				.andDo(print());
	}

	@Test
	public void testDeleteShouldReturn404NotFound() throws Exception {

		String code = "LACA_USA";

		String requestURI = END_POINT_PATH + "/" + code;

		Mockito.doThrow(LocationNotFoundException.class).when(locationService).delete(code);

		mockMvc.perform(delete(requestURI)).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testDeleteShouldReturn204NoContent() throws Exception {

		String code = "LACA_USA";

		String requestURI = END_POINT_PATH + "/" + code;

		Mockito.doNothing().when(locationService).delete(code);

		mockMvc.perform(delete(requestURI)).andExpect(status().isNoContent()).andDo(print());
	}

	@Test
	public void testValidateRequestBodyLocationCodeNotNull() throws Exception {

		Location location = new Location();

		// location.setCode("JKT");
		location.setCityName("Jakarta");
		location.setRegionName("Jakarta");
		location.setCountryCode("ID");
		location.setCountryName("Indonesia");
		location.setEnabled(true);

		String bodyContent = objectMapper.writeValueAsString(location);

		mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest()).andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.errors[0]", is("Location code cannot be null"))).andDo(print());
	}

	@Test
	public void testValidateRequestBodyLocationCodeLength() throws Exception {

		Location location = new Location();

		location.setCode("");
		location.setCityName("Jakarta");
		location.setRegionName("Jakarta");
		location.setCountryCode("ID");
		location.setCountryName("Indonesia");
		location.setEnabled(true);

		String bodyContent = objectMapper.writeValueAsString(location);

		mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest()).andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.errors[0]", is("Location code must have 3-12 characters"))).andDo(print());
	}

	@Test
	public void testValidateRequestBodyAllFieldInvalid() throws Exception {

		Location location = new Location();

		String bodyContent = objectMapper.writeValueAsString(location);

		MvcResult mcvResult = mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest()).andExpect(content().contentType("application/json")).andDo(print())
				.andReturn();

		String responseBody = mcvResult.getResponse().getContentAsString();

		assertThat(responseBody).contains("Location code cannot be null");
		assertThat(responseBody).contains("City name cannot be null");
		assertThat(responseBody).contains("Country name cannot be null");
		assertThat(responseBody).contains("Country code cannot be null");
	}

}
