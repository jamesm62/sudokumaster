package edu.uw.sudoku_solver.sudoku;

public class SudokuSolver {
	// pre: puzzle must be a 2D array with a perfect square number of rows and
	// columns where empty blanks are represented as zero
	// post: returns a 2D array with the data for the puzzle's solution or null if
	// there is no solution
	public static SudokuBoard solve(SudokuBoard board) {
		return solve(0, 0, board);
	}

	// helper method for solve() that recursively constructs a solution for the
	// unsolved sudoku puzzle
	public static SudokuBoard solve(int row, int col, SudokuBoard currentBoard) {
		if (row == currentBoard.getSize()) {
			return currentBoard;
		} else if (col == currentBoard.getSize()) {
			return solve(row + 1, 0, currentBoard);
		} else if (currentBoard.get(row, col) != 0) {
			return solve(row, col + 1, currentBoard);
		} else {
			for (int i = 1; i < currentBoard.getSize() + 1; i++) {
				if (currentBoard.canEnter(i, row, col)) {
					currentBoard.set(i, row, col);
					SudokuBoard solution = solve(row, col + 1, currentBoard);
					if (solution != null) {
						return solution;
					}
					currentBoard.remove(row, col);
				}
			}

			return null;
		}
	}

	public static void main(String[] args) {
		int[][] board = { { 0, 0, 0, 2, 6, 0, 7, 0, 1 }, { 6, 8, 0, 0, 7, 0, 0, 9, 0 },
				{ 1, 9, 0, 0, 0, 4, 5, 0, 0 }, { 8, 2, 0, 1, 0, 0, 0, 4, 0 },
				{ 0, 0, 4, 6, 0, 2, 9, 0, 0 }, { 0, 5, 0, 0, 0, 3, 0, 2, 8 },
				{ 0, 0, 9, 3, 0, 0, 0, 7, 4 }, { 0, 4, 0, 0, 5, 0, 0, 3, 6 },
				{ 7, 0, 3, 0, 1, 8, 0, 0, 0 } };

		SudokuBoard solution = solve(new SudokuBoard(board));

		if (solution != null) {
			System.out.println(board);
		} else {
			System.out.println("No solution");
		}
	}

}
