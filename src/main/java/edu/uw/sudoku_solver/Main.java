package edu.uw.sudoku_solver;

import edu.uw.sudoku_solver.server.Server;

public class Main {
	public static void main(String[] args) throws Exception {
		System.out.println("Program starting...");

		// Create a new server and start it
		Server server = new Server();
		server.start();

		// Wait for input on console to end program
		System.out.print("Press enter to end program");
		System.in.read(); // this is a blocking action

		// End the server and end the program
		server.end();
		System.out.println("Program ending...");
	}
}
