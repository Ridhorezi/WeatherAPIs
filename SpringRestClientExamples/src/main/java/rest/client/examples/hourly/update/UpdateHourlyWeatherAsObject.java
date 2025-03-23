package rest.client.examples.hourly.update;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.hourly.HourlyWeather;

public class UpdateHourlyWeatherAsObject {

	public static void main(String[] args) {
		
		String requestURI = "http://localhost:8080/v1/hourly/{code}";
		
		String locationCode = "JKT";
		
		RestTemplate restTemplate = new RestTemplate();
		
		HourlyWeather forecast1 = new HourlyWeather();
		forecast1.setHourOfDay(8);
		forecast1.setTemperature(10);
		forecast1.setPrecipitation(70);
		forecast1.setStatus("Sunny");
		
		HourlyWeather forecast2 = new HourlyWeather();
		forecast1.setHourOfDay(9);
		forecast1.setTemperature(10);
		forecast1.setPrecipitation(70);
		forecast1.setStatus("Sunny");	
	
		HourlyWeather[] hourlyForecast = new HourlyWeather[] {forecast1, forecast2};
		
		var request = new HttpEntity<Object>(hourlyForecast);
		
		var response = restTemplate.exchange(
				requestURI, HttpMethod.PUT, request, Object.class, locationCode);
		
		HttpStatusCode statusCode = response.getStatusCode();
		
		System.out.println("Status code: " + statusCode);
		
		if (statusCode.value() == HttpStatus.OK.value()) {
			System.out.println("Hourly weather data updated");
			System.out.println(response.getBody());
		}
	}
}
