package edu.uw.sudoku_solver.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerTest {
	private Server server;

	@Before
	public void startServer() {
		server = new Server();
		server.start();
	}

	@Test
	public void testIndexHtmlIsSent() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:3000/"))
				.build();

		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
		assertEquals("response body is index.html", response.body(), new String(Server.class
				.getResourceAsStream("/edu/uw/sudoku_solver/webpage/index.html").readAllBytes()));
	}

	@After
	public void endServer() {
		server.end();
	}
}
