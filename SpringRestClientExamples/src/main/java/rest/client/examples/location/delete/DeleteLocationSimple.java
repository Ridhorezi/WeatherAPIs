package rest.client.examples.location.delete;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class DeleteLocationSimple {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8085/v1/locations/{code}";
		
		Map<String, String> params = new HashMap<>();
		
		params.put("code", "NEW_DELHI");
		
		RestTemplate restTemplate = new RestTemplate();
		
		try {
			restTemplate.delete(requestURI, params);
			
			System.out.println("Location deleted");
		} catch (RestClientResponseException ex) {
			System.out.println("Error status code: " + ex);
			ex.printStackTrace();
		}
	}

}
