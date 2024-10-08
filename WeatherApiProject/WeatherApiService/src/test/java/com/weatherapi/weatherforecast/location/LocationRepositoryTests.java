package com.weatherapi.weatherforecast.location;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import com.weatherapi.weatherforecast.common.Location;
import com.weatherapi.weatherforecast.common.RealtimeWeather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class LocationRepositoryTests {

	@Autowired
	private LocationRepository repository;

	@Test
	public void testAddSuccess() {

		Location location = new Location();

		location.setCode("JKT");
		location.setCityName("Jakarta");
		location.setRegionName("Jakarta Raya");
		location.setCountryCode("ID");
		location.setCountryName("Indonesia");
		location.setEnabled(true);

		Location savedLocation = repository.save(location);

		assertThat(savedLocation).isNotNull();

		assertThat(savedLocation.getCode()).isEqualTo("JKT");
	}

	@Test
	public void testListSuccess() {

		List<Location> locations = repository.findUntrashed();

		assertThat(locations).isNotEmpty();

		locations.forEach(System.out::println);
	}

	@Test
	public void testGetNotFound() {

		String code = "ABCD";

		Location location = repository.findByCode(code);

		assertThat(location).isNull();
	}

	@Test
	public void testGetSuccess() {

		String code = "JKT";

		Location location = repository.findByCode(code);

		assertThat(location).isNotNull();

		assertThat(location).isNotNull();
	}

	@Test
	public void testTrashSuccess() {

		String code = "LACA_USA";

		repository.trashByCode(code);

		Location location = repository.findByCode(code);

		assertThat(location).isNull();
	}

	@Test
	public void testAddRealtimeWeatherData() {

		String code = "NYC_USA";

		Location location = repository.findByCode(code);

		RealtimeWeather realtimeWeather = location.getRealtimeWeather();

		if (realtimeWeather == null) {

			realtimeWeather = new RealtimeWeather();

			realtimeWeather.setLocation(location);

			location.setRealtimeWeather(realtimeWeather);
		}

		realtimeWeather.setTemperature(-1);
		realtimeWeather.setHumidity(30);
		realtimeWeather.setPrecipitation(40);
		realtimeWeather.setStatus("Snowy");
		realtimeWeather.setWindSpeed(15);
		realtimeWeather.setLastUpdatedDtm(new Date());

		Location updateLocation = repository.save(location);

		assertThat(updateLocation.getRealtimeWeather().getLocationCode()).isEqualTo(code);
	}
}
