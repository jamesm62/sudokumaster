package edu.uw.sudoku_solver.sudoku;

public class SudokuBoard {
	private int[][] values;
	private int squareHeight;
	private int squareWidth;

	public SudokuBoard(int[][] values) {
		this(values, 3, 3);
	}

	public SudokuBoard(int[][] values, int squareHeight, int squareWidth) {
		if (values.length % squareHeight != 0 || values[0].length % squareWidth != 0) {
			throw new IllegalArgumentException(String.format(
					"sqaureHeight: %d or squareWidth: %d does not divide evenly into board height: %d or board width: %d",
					squareHeight, squareWidth, values.length, values[0].length));
		}

		this.values = values;
		this.squareHeight = squareHeight;
		this.squareWidth = squareWidth;
	}

	@Override
	public String toString() {
		String board = "";
		String rowSeparator = "++";

		for (int i = 0; i < values[0].length; i++) {
			rowSeparator += "---+" + (i % squareWidth == squareWidth - 1 ? "+" : "");
		}

		for (int i = 0; i < values.length; i++) {
			board += rowSeparator + "\n";
			if (i % squareHeight == 0) {
				board += rowSeparator + "\n";
			}
			board += "||";
			for (int j = 0; j < values[0].length; j++) {
				board += " " + (values[i][j] == 0 ? " " : values[i][j]) + " |"
						+ (j % squareWidth == squareWidth - 1 ? "|" : "");
			}
			board += "\n";
		}
		board += rowSeparator + "\n" + rowSeparator;

		return board;
	}

	public int getValue(int row, int col) {
		if (row < 0 || col < 0 || row > values.length || col > values[0].length) {
			throw new IllegalArgumentException(
					String.format("row: %d or col: %d is out of range 0<=row<=%d, 0<=col<=%d", row, col,
							values.length, values[0].length));
		}

		return values[row][col];
	}
}
