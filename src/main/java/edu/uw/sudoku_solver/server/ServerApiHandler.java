package edu.uw.sudoku_solver.server;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * A template for API requests to the server. It automatically parses the JSON Post request and handles sending the data
 * and errors
 */
public abstract class ServerApiHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		try {
			String body = new String(httpExchange.getRequestBody().readAllBytes());
			JsonObject json = JsonParser.parseString(body).getAsJsonObject();

			String returnValue = getResponse(json);

			try {
				byte[] bytes = returnValue.getBytes();
				httpExchange.sendResponseHeaders(200, bytes.length);
				httpExchange.getResponseBody().write(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception exception) {
			try {
				byte[] bytes = ("{\"err\":\"Error processing request\"}").getBytes();
				httpExchange.sendResponseHeaders(200, bytes.length);
				httpExchange.getResponseBody().write(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		httpExchange.close();
	}

	/**
	 * Get the response for the API call
	 * @param request The JSON request for the call
	 * @return The string to return to the client
	 * @throws Exception Throw an exception if there are any issues with the request as this sends an error to the
	 * client
	 */
	public abstract String getResponse(JsonObject request) throws Exception;
}
