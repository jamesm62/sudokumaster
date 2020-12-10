package edu.uw.sudoku_solver.sudoku;

/**
 * Solves Sudoku Boards
 * 
 *
 */
public class SudokuSolver {
	/**
	 * Solves a Sudoku Board
	 * 
	 * @param board A board with zeros as the empty squares
	 * @return A solved board or null if no solution exists
	 */
	public static SudokuBoard solve(SudokuBoard board) {
		return solve(0, 0, board);
	}

	/**
	 * Recursive call to loop throw all possible solutions for a board
	 * 
	 * @param row          The current row
	 * @param col          The current column
	 * @param currentBoard The current board
	 * @return The board after this and recursive steps or null if a solution does
	 *         not exist
	 */
	public static SudokuBoard solve(int row, int col, SudokuBoard currentBoard) {
		if (row == currentBoard.getSize()) {
			return currentBoard;
		} else if (col == currentBoard.getSize()) {
			return solve(row + 1, 0, currentBoard);
		} else if (currentBoard.get(row, col) != 0) {
			return solve(row, col + 1, currentBoard);
		} else {
			for (int possibleNumber = 1; possibleNumber <= currentBoard.getSize(); possibleNumber++) {
				if (currentBoard.canEnter(possibleNumber, row, col)) {
					currentBoard.set(possibleNumber, row, col);
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
}
