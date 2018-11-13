package org.nodiaboi.ius.database;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;

public class RemoteExecutor {

	private static final ObjectMapper mapper = new ObjectMapper();

	private static final RestTemplate template = new RestTemplate();

	static {
		template.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
	}

	public static final JsonNode executeSelect(String sqlQuery){
		try {
			String jsonString = sendPostRequest(sqlQuery, "executeSelect");
			JsonNode json = mapper.readTree(jsonString);
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final void executeDml(String sqlQuery){
		sendPostRequest(sqlQuery, "executeDml");
	}

	public static final String [] executeShellCommand(String command){
		return sendPostRequest(command, "executeShellCommand").split("[/n/t]");
	}

	private static String sendPostRequest(String body, String endPointUrl) {
		HttpEntity<String> entity = new HttpEntity(body, null);
		ResponseEntity<String> response = template.exchange("http://172.20.37.194:8080/" + endPointUrl, HttpMethod.POST, entity, String.class);
		return response.getBody();
	}
}
