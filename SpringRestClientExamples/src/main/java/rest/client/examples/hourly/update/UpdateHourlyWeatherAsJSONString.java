package rest.client.examples.hourly.update;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UpdateHourlyWeatherAsJSONString {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8085/v1/hourly/JKT";

		RestTemplate restTemplate = new RestTemplate();

		String json = 
				"""
				[
				    {
				        "hour_of_day": 8,
				        "temperature": 10,
				        "precipitation": 70,
				        "status": "Sunny"
				    },
				    {
				        "hour_of_day": 9,
				        "temperature": 10,
				        "precipitation": 70,
				        "status": "Sunny"
				    }
				]
				""";

		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_JSON);

		var request = new HttpEntity<String>(json, headers);

		try {
			restTemplate.put(requestURI, request);
			System.out.println("Hourly weather data updated");
		} catch (RestClientResponseException ex) {
			System.out.println("Errro status code: " + ex.getStatusCode());
			ex.printStackTrace();
		}
	}

}
