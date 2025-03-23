package rest.client.examples.realtime.get;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class GetRealtimeWeatherByIPAsJSONString {

	public static void main(String[] args) {
		
		String requestURI = "http://localhost:8085/v1/realtime";
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String clientIpAddress = "203.189.88.161";
		
		headers.add("X-FORWARDED-FOR", clientIpAddress);
		
		HttpEntity<String> request = new HttpEntity<>(headers);
		
		var response = restTemplate.exchange(requestURI, HttpMethod.GET, request, String.class);
		
		if (response.getStatusCode().is2xxSuccessful()) {
			System.out.println(response.getBody());
		}
	}

}
