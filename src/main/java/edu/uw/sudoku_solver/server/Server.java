package edu.uw.sudoku_solver.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

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

			server.createContext("/css/", httpExchange -> {
				sendWebpageFile(httpExchange, httpExchange.getRequestURI().getPath(), "");
			});

			server.createContext("/js/", httpExchange -> {
				sendWebpageFile(httpExchange, httpExchange.getRequestURI().getPath(), "");
			});

			server.createContext("/api/solve/", httpExchange -> {
				try {
					String body = new String(httpExchange.getRequestBody().readAllBytes());

					JsonObject json = JsonParser.parseString(body).getAsJsonObject();

					JsonArray jsonBoardArray = json.get("board").getAsJsonArray();
					int[][] boardArray = new int[9][9];
					for (int i = 0; i < 9; i++) {
						for (int j = 0; j < 9; j++) {
							boardArray[i][j] = jsonBoardArray.get(i).getAsJsonArray().get(j).getAsInt();
						}
					}

					SudokuSolver solver = new SudokuSolver(boardArray);
					int[][] solution = solver.solve();
					Gson builder = new GsonBuilder().setPrettyPrinting().create();
					String returnValue = "{\"board\":" + builder.toJson(solution, solution.getClass())
							+ "}";

					try {
						byte[] bytes = returnValue.getBytes();
						httpExchange.sendResponseHeaders(200, bytes.length);
						httpExchange.getResponseBody().write(bytes);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (Exception exception) {
					try {
						byte[] bytes = ("{\"err\":\"Error solving board\"}").getBytes();
						httpExchange.sendResponseHeaders(200, bytes.length);
						httpExchange.getResponseBody().write(bytes);
					} catch (IOException e) {
						e.printStackTrace();
					}
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
