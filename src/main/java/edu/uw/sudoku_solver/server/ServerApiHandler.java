package edu.uw.sudoku_solver.server;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

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

	public abstract String getResponse(JsonObject request) throws Exception;

}
