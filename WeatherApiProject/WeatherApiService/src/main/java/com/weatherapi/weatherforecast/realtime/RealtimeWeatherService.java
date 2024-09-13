package com.weatherapi.weatherforecast.realtime;

import com.weatherapi.weatherforecast.common.Location;
import com.weatherapi.weatherforecast.common.RealtimeWeather;
import com.weatherapi.weatherforecast.location.LocationNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class RealtimeWeatherService {

	private RealtimeWeatherRepository realtimeWeatherRepository;

	public RealtimeWeatherService(RealtimeWeatherRepository realtimeWeatherRepository) {
		super();
		this.realtimeWeatherRepository = realtimeWeatherRepository;
	}

	public RealtimeWeather getByLocation(Location location) throws LocationNotFoundException {

		String countryCode = location.getCountryCode();
		String cityName = location.getCityName();

		RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode, cityName);

		if (realtimeWeather == null) {

			throw new LocationNotFoundException("No location found with the given country code and city name");
		}

		return realtimeWeather;
	}
}
