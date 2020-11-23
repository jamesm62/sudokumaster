package edu.uw.sudoku_solver.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.*;

public class Server {

	private static final int PORT = Integer.parseInt(System.getenv().getOrDefault("PORT", "8000"));

	private HttpServer server;

	public void start() {
		// If the server has already started throw an exception
		if (server != null) {
			throw new IllegalArgumentException("Server has already started!");
		}

		try {
			// Create a new server
			server = HttpServer.create(new InetSocketAddress(PORT), 0);
			// Add the root context
			server.createContext("/", t -> {
				t.sendResponseHeaders(200, 1);
				t.getResponseBody().write('a');
			});

			// Start the server
			server.start();
			System.out.println("Server running on port: " + PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void end() {
		// End the server and wait one second for any final requests
		server.stop(1);
	}
}
