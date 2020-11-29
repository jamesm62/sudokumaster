package edu.uw.sudoku_solver.sudoku;

public class SudokuBoard {
	private int[][] values;

	public SudokuBoard(int[][] values) {
		this.values = values;
	}

	@Override
	public String toString() {
		String board = "";
		for (int i = 0; i < 9; i++) {
			board += "++---+---+---++---+---+---++---+---+---++\n";
			if (i % 3 == 0) {
				board += "++---+---+---++---+---+---++---+---+---++\n";
			}
			board += "||";
			for (int j = 0; j < 9; j++) {
				board += " " + (values[i][j] == 0 ? " " : values[i][j]) + " |"
						+ (j % 3 == 2 ? "|" : "");
			}
			board += "\n";
		}
		board += "++---+---+---++---+---+---++---+---+---++\n++---+---+---++---+---+---++---+---+---++";

		return board;
	}
}
