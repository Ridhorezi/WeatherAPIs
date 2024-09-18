package com.weatherapi.weatherforecast.realtime;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherapi.weatherforecast.GeolocationException;
import com.weatherapi.weatherforecast.GeolocationService;
import com.weatherapi.weatherforecast.common.Location;
import com.weatherapi.weatherforecast.common.RealtimeWeather;
import com.weatherapi.weatherforecast.location.LocationNotFoundException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RealtimeWeatherApiController.class)
public class RealtimeWeatherApiControllerTests {

	private static final String END_POINT_PATH = "/v1/realtime";

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	RealtimeWeatherService realtimeWeatherService;

	@MockBean
	GeolocationService geolocationService;

	@Test
	public void testGetShouldReturnStatus400BadRequest() throws Exception {

		Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenThrow(GeolocationException.class);

		mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testGetShouldReturnStatus404NotFound() throws Exception {

		Location location = new Location();

		Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);

		Mockito.when(realtimeWeatherService.getByLocation(location)).thenThrow(LocationNotFoundException.class);

		mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testGetShouldReturnStatus200OK() throws Exception {

		Location location = new Location();

		location.setCode("JKT");
		location.setCityName("Jakarta");
		location.setRegionName("Jakarta");
		location.setCountryCode("ID");
		location.setCountryName("Indonesia");

		RealtimeWeather realtimeWeather = new RealtimeWeather();

		realtimeWeather.setTemperature(10);
		realtimeWeather.setHumidity(60);
		realtimeWeather.setLastUpdatedDtm(new Date());
		realtimeWeather.setPrecipitation(70);
		realtimeWeather.setStatus("Sunny");
		realtimeWeather.setWindSpeed(10);
		realtimeWeather.setLocation(location);

		location.setRealtimeWeather(realtimeWeather);

		Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);

		Mockito.when(realtimeWeatherService.getByLocation(location)).thenReturn(realtimeWeather);

		String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", "
				+ location.getCountryName();

		mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.location", is(expectedLocation))).andDo(print());
	}

	@Test
	public void testGetByLocationCodeShouldReturnStatus404NotFound() throws Exception {

		String locationCode = "NOT-FOUND";

		Mockito.when(realtimeWeatherService.getByLocationCode(locationCode)).thenThrow(LocationNotFoundException.class);

		String requestURI = END_POINT_PATH + "/" + locationCode;

		mockMvc.perform(get(requestURI)).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testGetByLocationCodeShouldReturnStatus200OK() throws Exception {

		String locationCode = "JKT";

		Location location = new Location();

		location.setCode(locationCode);
		location.setCityName("Jakarta");
		location.setRegionName("Jakarta");
		location.setCountryCode("ID");
		location.setCountryName("Indonesia");

		RealtimeWeather realtimeWeather = new RealtimeWeather();

		realtimeWeather.setTemperature(10);
		realtimeWeather.setHumidity(60);
		realtimeWeather.setLastUpdatedDtm(new Date());
		realtimeWeather.setPrecipitation(70);
		realtimeWeather.setStatus("Sunny");
		realtimeWeather.setWindSpeed(10);
		realtimeWeather.setLocation(location);

		location.setRealtimeWeather(realtimeWeather);

		Mockito.when(realtimeWeatherService.getByLocationCode(locationCode)).thenReturn(realtimeWeather);

		String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", "
				+ location.getCountryName();

		String requestURI = END_POINT_PATH + "/" + locationCode;

		mockMvc.perform(get(requestURI)).andExpect(status().isOk()).andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.location", is(expectedLocation))).andDo(print());
	}

	@Test
	public void testUpdateShouldReturn400BadRequest() throws Exception {

		String locationCode = "JKT";

		String requestURI = END_POINT_PATH + "/" + locationCode;

		RealtimeWeather realtimeWeather = new RealtimeWeather();

		realtimeWeather.setTemperature(100); // out of range
		realtimeWeather.setHumidity(600); // out of range
		realtimeWeather.setLastUpdatedDtm(new Date());
		realtimeWeather.setPrecipitation(700); // out of range
		realtimeWeather.setStatus("Su"); // out of range
		realtimeWeather.setWindSpeed(1000); // out of range

		String bodyContent = objectMapper.writeValueAsString(realtimeWeather);

		mockMvc.perform(put(requestURI).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testUpdateShouldReturn404NotFound() throws Exception {

		String locationCode = "NOT-FOUND";

		String requestURI = END_POINT_PATH + "/" + locationCode;

		RealtimeWeather realtimeWeather = new RealtimeWeather();

		realtimeWeather.setTemperature(10);
		realtimeWeather.setHumidity(60);
		realtimeWeather.setLastUpdatedDtm(new Date());
		realtimeWeather.setPrecipitation(70);
		realtimeWeather.setStatus("Sunny");
		realtimeWeather.setWindSpeed(10);

		Mockito.when(realtimeWeatherService.update(locationCode, realtimeWeather))
				.thenThrow(LocationNotFoundException.class);

		String bodyContent = objectMapper.writeValueAsString(realtimeWeather);

		mockMvc.perform(put(requestURI).contentType("application/json").content(bodyContent))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testUpdateShouldReturn200OK() throws Exception {

		String locationCode = "JKT";

		String requestURI = END_POINT_PATH + "/" + locationCode;

		RealtimeWeather realtimeWeather = new RealtimeWeather();

		realtimeWeather.setTemperature(10);
		realtimeWeather.setHumidity(60);
		realtimeWeather.setLastUpdatedDtm(new Date());
		realtimeWeather.setPrecipitation(70);
		realtimeWeather.setStatus("Sunny");
		realtimeWeather.setWindSpeed(10);

		Location location = new Location();

		location.setCode(locationCode);
		location.setCityName("Jakarta");
		location.setRegionName("Jakarta");
		location.setCountryCode("ID");
		location.setCountryName("Indonesia");

		realtimeWeather.setLocation(location);
		location.setRealtimeWeather(realtimeWeather);

		Mockito.when(realtimeWeatherService.update(locationCode, realtimeWeather)).thenReturn(realtimeWeather);

		String bodyContent = objectMapper.writeValueAsString(realtimeWeather);

		String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", "
				+ location.getCountryName();

		mockMvc.perform(put(requestURI).contentType("application/json").content(bodyContent)).andExpect(status().isOk())
				.andExpect(jsonPath("$.location", is(expectedLocation))).andDo(print());
	}
}
