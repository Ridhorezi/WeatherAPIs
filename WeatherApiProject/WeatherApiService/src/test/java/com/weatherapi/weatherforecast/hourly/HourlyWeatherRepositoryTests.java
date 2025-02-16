package com.weatherapi.weatherforecast.hourly;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import com.weatherapi.weatherforecast.common.HourlyWeather;
import com.weatherapi.weatherforecast.common.HourlyWeatherId;
import com.weatherapi.weatherforecast.common.Location;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class HourlyWeatherRepositoryTests {

	@Autowired
	private HourlyWeatherRepository hourlyWeatherRepository;

	@Test
	public void testAdd() {

		String locationCode = "JKT";

		int hourOfDay = 10;

		Location location = new Location().code(locationCode);

		HourlyWeather forecast = new HourlyWeather().location(location).hourOfDay(hourOfDay).temperature(10)
				.precipitation(70).status("Sunny");

		HourlyWeather createdForecast = hourlyWeatherRepository.save(forecast);

		assertThat(createdForecast.getId().getLocation().getCode()).isEqualTo(locationCode);
		
		assertThat(createdForecast.getId().getHourOfDay()).isEqualTo(hourOfDay);
	}
	
	@Test
	public void testDelete() {
		
		Location location = new Location().code("JKT");
		
		HourlyWeatherId id = new HourlyWeatherId(10, location);
		
		hourlyWeatherRepository.deleteById(id);
		
		Optional<HourlyWeather> result = hourlyWeatherRepository.findById(id);
		
		assertThat(result).isNotPresent();
	}
	
	@Test
	public void testFindByLocationCodeFound() {
		
		String locationCode = "JKT";
		
		int currentHour = 8;
		
		List<HourlyWeather> hourlyForecast = hourlyWeatherRepository.findByLocationCode(locationCode, currentHour);
		
		assertThat(hourlyForecast).isNotEmpty();
	}
	
	@Test
	public void testFindByLocationCodeNotFound() {
		
		String locationCode = "NOT-FOUND";
		
		int currentHour = 8;
		
		List<HourlyWeather> hourlyForecast = hourlyWeatherRepository.findByLocationCode(locationCode, currentHour);
		
		assertThat(hourlyForecast).isEmpty();
	}
}
