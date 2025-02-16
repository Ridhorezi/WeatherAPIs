package com.weatherapi.weatherforecast.realtime;

import java.util.Date;

import com.weatherapi.weatherforecast.common.Location;
import com.weatherapi.weatherforecast.common.RealtimeWeather;
import com.weatherapi.weatherforecast.location.LocationNotFoundException;
import com.weatherapi.weatherforecast.location.LocationRepository;

import org.springframework.stereotype.Service;

@Service
public class RealtimeWeatherService {

	private RealtimeWeatherRepository realtimeWeatherRepository;
	private LocationRepository locationRepository;

	public RealtimeWeatherService(RealtimeWeatherRepository realtimeWeatherRepository,
			LocationRepository locationRepository) {
		super();
		this.realtimeWeatherRepository = realtimeWeatherRepository;
		this.locationRepository = locationRepository;
	}

	public RealtimeWeather getByLocation(Location location) {

		String countryCode = location.getCountryCode();
		String cityName = location.getCityName();

		RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode, cityName);

		if (realtimeWeather == null) {

			throw new LocationNotFoundException(countryCode, cityName);
		}

		return realtimeWeather;
	}

	public RealtimeWeather getByLocationCode(String locationCode) {

		RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(locationCode);

		if (realtimeWeather == null) {

			throw new LocationNotFoundException(locationCode);
		}

		return realtimeWeather;
	}

	public RealtimeWeather update(String locationCode, RealtimeWeather realtimeWeather) {

		Location location = locationRepository.findByCode(locationCode);

		if (location == null) {

			throw new LocationNotFoundException(locationCode);
		}

		if (location.getRealtimeWeather() == null) {

			location.setRealtimeWeather(realtimeWeather);

			Location updatedLocation = locationRepository.save(location);

			return updatedLocation.getRealtimeWeather();
		}

		realtimeWeather.setLocation(location);

		realtimeWeather.setLastUpdatedDtm(new Date());

		return realtimeWeatherRepository.save(realtimeWeather);
	}
}
