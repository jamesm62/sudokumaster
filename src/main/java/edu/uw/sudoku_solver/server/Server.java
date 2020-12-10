package edu.uw.sudoku_solver.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import edu.uw.sudoku_solver.sudoku.SudokuBoard;
import edu.uw.sudoku_solver.sudoku.SudokuGenerator;
import edu.uw.sudoku_solver.sudoku.SudokuSolver;

/**
 * The server for the webpage Sudoku Solver. It sends the webpage and return a
 * solved board when a unsolved is sent as an api request
 * 
 * @author Eli Orona
 *
 */
public class Server {
	/**
	 * The port for the server to run on.
	 */
	public static final int PORT = Integer.parseInt(System.getenv().getOrDefault("PORT", "8000"));

	/**
	 * Constructor for the Server
	 */
	public Server() {
	}

	/**
	 * The {@link HttpServer} for the server. Allows closing the server in a
	 * seperate method.
	 */
	private HttpServer server;

	/**
	 * Creates and starts the server with all paths registered. If this server has
	 * already been started, an {@link IllegalArgumentException} is thrown
	 */
	public void start() {
		// If the server has already started throw an exception
		if (server != null) {
			throw new IllegalArgumentException("Server has already started!");
		}

		try {
			// Create a new server
			server = HttpServer.create(new InetSocketAddress(PORT), 0);

			// Add the root context
			server.createContext("/", httpExchange -> {
				sendWebpageFile(httpExchange, "index.html", "text/html; charset=utf-8");
			});

			// Add the solve page
			server.createContext("/solve", httpExchange -> {
				sendWebpageFile(httpExchange, "solver/solver.html", "text/html;charset=utf-8");
			});

			// Add the generate page
			server.createContext("/generate", httpExchange -> {
				sendWebpageFile(httpExchange, "generator/generator.html", "text/html;charset=utf-8");
			});

			// Add CSS files
			server.createContext("/css/", httpExchange -> {
				sendWebpageFile(httpExchange, httpExchange.getRequestURI().getPath(),
						"text/css;charset=utf-8");
			});

			// Add JS files
			server.createContext("/js/", httpExchange -> {
				sendWebpageFile(httpExchange, httpExchange.getRequestURI().getPath(),
						"text/javascript;charset=utf-8");
			});

			// Create an API for solving boards
			server.createContext("/api/solve/", new ServerApiHandler() {
				@Override
				public String getResponse(JsonObject request) throws Exception {
					if (!request.has("board")) {
						throw new Exception("No board in request");
					}

					JsonArray jsonBoardArray = request.get("board").getAsJsonArray();
					SudokuBoard solution = SudokuSolver.solve(new SudokuBoard(jsonBoardArray));

					if (solution == null) {
						throw new Exception("Unable to solve board");
					}

					return "{\"board\":" + solution.toJson() + "}";
				}
			});

			// Create an API for generating boards
			server.createContext("/api/generate/", new ServerApiHandler() {
				@Override
				public String getResponse(JsonObject request) throws Exception {
					int seed = request.has("seed") ? request.get("seed").getAsInt()
							: (int) (Math.random() * 10000);

					if (!request.has("difficulty")) {
						throw new Exception("No difficulty in request");
					}

					if (!request.has("size")) {
						throw new Exception("No size in request");
					}

					SudokuBoard generated = SudokuGenerator.getRandomPuzzle(
							request.get("difficulty").getAsInt(), request.get("size").getAsInt(),
							new Random(seed));

					if (generated == null) {
						throw new Exception("Unable to generate board");
					}

					return "{\"board\":" + generated.toJson() + "}";
				}
			});

			// Start the server
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Sends the file from the package {@code edu.uw.sudoku_solver.webpage} through
	 * the {@link HttpExchange}
	 * 
	 * @param httpExchange The {@link HttpExchange} to send the file
	 * @param fileName     The name of the file
	 * @param contentType  The contentType (use "" instead of null for no type)
	 */
	private static void sendWebpageFile(HttpExchange httpExchange, String fileName,
			String contentType) {
		httpExchange.getResponseHeaders().set("Content-Type", contentType);
		InputStream index = Server.class
				.getResourceAsStream("/edu/uw/sudoku_solver/webpage/" + fileName);
		try {
			byte[] bytes = index.readAllBytes();
			httpExchange.sendResponseHeaders(200, bytes.length);
			httpExchange.getResponseBody().write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		httpExchange.close();
	}

	/**
	 * Ends the server. All current requests have 1 second to finish.
	 */
	public void end() {
		// End the server and wait one second for any final requests
		server.stop(1);
	}
}
