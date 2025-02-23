package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;  // Import necessário

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class ApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@GetMapping("/")
	public ResponseEntity<List<String>> defaultRoute() {
		List<String> routes = List.of("teste", "teste2", "teste3");
		return ResponseEntity.ok(routes);
	}

	@PostMapping("/error")
	public ResponseEntity<?> defaultError() {
		return ResponseEntity.ok("");
	}

	@GetMapping("/client")
	public ResponseEntity<?> clientApiJson() throws IOException {
		String url = "https://jsonplaceholder.typicode.com/todos/1";

		// Crie um objeto URL
		URL obj = new URL(url);

		// Abra uma conexão HTTP
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// Defina o método da requisição como GET
		con.setRequestMethod("GET");

		// Obtenha o código de resposta da requisição
		int responseCode = con.getResponseCode();
		System.out.println("Response Code: " + responseCode);

		// Se a resposta for 200 OK, leia os dados
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			// Ler a resposta linha por linha
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// Exiba o conteúdo da resposta no console
			System.out.println(response.toString());

			// Retorne o conteúdo da resposta como o corpo da resposta da API
			return ResponseEntity.ok(response);
		} else {
			System.out.println("GET request failed");
			// Se a requisição falhar, retorna uma mensagem de erro
			return ResponseEntity.status(responseCode).body("GET request failed");
		}
	}
}