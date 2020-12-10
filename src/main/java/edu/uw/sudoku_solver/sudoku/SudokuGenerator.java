package edu.uw.sudoku_solver.sudoku;

import java.util.Random;

/**
 * Generates Sudoku boards
 *
 */
public class SudokuGenerator {
	/**
	 * Generates a sudoku board from the given size, difficulty, and random number
	 * 
	 * @param difficulty The difficult of the board from 1 - 9
	 * @param size       The size of the board (should be a square number)
	 * @param random     The random to generate the board with for repeated
	 *                   generations
	 * @return A randomly generated board
	 */
	public static SudokuBoard getRandomPuzzle(int difficulty, int size, Random random) {
		SudokuBoard board = new SudokuBoard(new int[size][size]);
		SudokuBoard randomSolvedBoard = getRandomSolvedPuzzle(0, 0, board, random);
		return getRandomUnsolvedPuzzle(0, 0, randomSolvedBoard, difficulty / 10.0, random);
	}

	/**
	 * Recursive helper method to generate a random solved puzzle
	 * 
	 * @param row          The current row
	 * @param col          The current col
	 * @param currentBoard The current board state
	 * @param numGenerator The random number generator
	 * @return A randomly generated, solved board
	 */
	private static SudokuBoard getRandomSolvedPuzzle(int row, int col, SudokuBoard currentBoard,
			Random numGenerator) {
		if (row == currentBoard.getSize()) {
			return currentBoard;
		} else if (col == currentBoard.getSize()) {
			return getRandomSolvedPuzzle(row + 1, 0, currentBoard, numGenerator);
		} else {
			SudokuBoard solution = null;

			while (solution == null) {
				int randomNum = 0;
				int count = 0;
				boolean foundNum = true;
				do {
					if (count > 50) {
						foundNum = false;
						break;
					}
					randomNum = (int) Math
							.round(numGenerator.nextDouble() * (currentBoard.getSize() - 1.0) + 1.0);
					count++;
				} while (!currentBoard.canEnter(randomNum, row, col));
				if (foundNum) {
					currentBoard.set(randomNum, row, col);
				}
				solution = getRandomSolvedPuzzle(row, col + 1, currentBoard, numGenerator);
			}

			return solution;
		}
	}

	/**
	 * Randomly removes parts of the board to create an unsolved borad
	 * 
	 * @param row                The current row
	 * @param col                The current col
	 * @param currentBoard       The current board
	 * @param difficulty         The difficulty
	 * @param difficultyAssessor The random generator for the random removal
	 * @return A board with random parts removed
	 */
	private static SudokuBoard getRandomUnsolvedPuzzle(int row, int col, SudokuBoard currentBoard,
			double difficulty, Random difficultyAssessor) {
		if (row == currentBoard.getSize()) {
			return currentBoard;
		} else if (col == currentBoard.getSize()) {
			return getRandomUnsolvedPuzzle(row + 1, 0, currentBoard, difficulty, difficultyAssessor);
		} else if (difficultyAssessor.nextDouble() < difficulty) {
			currentBoard.set(0, row, col);
		}
		return getRandomUnsolvedPuzzle(row, col + 1, currentBoard, difficulty, difficultyAssessor);
	}
}
