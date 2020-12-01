package edu.uw.sudoku_solver.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

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

	public static final Gson builder = new GsonBuilder().setPrettyPrinting().create();

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

			// add the versus page
			server.createContext("/versus", httpExchange -> {
				sendWebpageFile(httpExchange, "versus/versus.html", "text/html;charset=utf-8");
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
					JsonArray jsonBoardArray = request.get("board").getAsJsonArray();
					int[][] boardArray = new int[9][9];
					for (int i = 0; i < 9; i++) {
						for (int j = 0; j < 9; j++) {
							boardArray[i][j] = jsonBoardArray.get(i).getAsJsonArray().get(j).getAsInt();
						}
					}

					int[][] solution = SudokuSolver.solve(boardArray);

					if (solution == null) {
						throw new Exception();
					}

					return "{\"board\":" + builder.toJson(solution, solution.getClass()) + "}";
				}
			});

			// Create an API for generating boards
			server.createContext("/api/generate/", new ServerApiHandler() {
				@Override
				public String getResponse(JsonObject request) throws Exception {
					int seed = request.has("seed") ? request.get("seed").getAsInt() : (int) (Math.random() * 10000);
					int[][] solution = SudokuGenerator.getRandomPuzzle(request.get("difficulty").getAsInt(), 9, new Random(seed), new Random(seed));

					if (solution == null) {
						throw new Exception();
					}

					return "{\"board\":" + builder.toJson(solution, solution.getClass()) + "}";
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
