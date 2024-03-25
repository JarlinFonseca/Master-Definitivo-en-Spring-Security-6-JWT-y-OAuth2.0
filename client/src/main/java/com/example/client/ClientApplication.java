package com.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@RestController
@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}

	@PostMapping("/login")
	public Map login(@RequestBody LoginRequest loginRequest){

		Map jwt = doAuthenticate(loginRequest);
		Map productMap = doGetProductOne(jwt);

		return productMap;
	}

	private Map doGetProductOne(Map jwtToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Bearer ".concat((String) jwtToken.get("jwt")));
		HttpEntity requestWithHeaders = new HttpEntity<>(headers);
		String url = "http://my-app:9191/api/v1/products/1";
		ResponseEntity<Map> product = restTemplate().exchange(url, HttpMethod.GET, requestWithHeaders, Map.class);

		Map productMap = product.getBody();
		return productMap;
	}

	private Map doAuthenticate(LoginRequest loginReq) {
		String url = "http://my-app:9191/api/v1/auth/authenticate";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(loginReq, headers);
		ResponseEntity<Map> responseEntity =  restTemplate().postForEntity(url, requestEntity, Map.class);

		Map jwtDto = responseEntity.getBody();
		System.out.println("Respuesta: " + jwtDto.get("jwt"));
		return jwtDto;
	}

	@Bean
	public RestTemplate restTemplate(){
		RestTemplate restTemplate = new RestTemplate();

		ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
			HttpHeaders headers = request.getHeaders();
			headers.set("Origin", "http://client"); // Cambia esto con tu origen permitido
			return execution.execute(request, body);
		};
		restTemplate.setInterceptors(Collections.singletonList(interceptor));

		return restTemplate;
	}

}
