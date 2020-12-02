package edu.uw.sudoku_solver.sudoku;

import java.util.Random;

public class SudokuGenerator {
	// pre: 1. difficulty must be between 0 and 10 and represents the desired
	// difficulty of the sudoku puzzle
	// 2. size must be a perfect square and represents the desired number of rows
	// and columns in the puzzle
	// post: returns a 2D array with the data for a random unsolved sudoku puzzle
	public static SudokuBoard getRandomPuzzle(int difficulty, int size, Random random) {
		SudokuBoard board = new SudokuBoard(new int[size][size]);
		SudokuBoard randomSolvedBoard = getRandomSolvedPuzzle(0, 0, board, random);
		return getRandomUnsolvedPuzzle(0, 0, randomSolvedBoard, difficulty / 10.0, random);
	}

	// helper method for getRandomPuzzle() that recursively constructs a random
	// sudoku puzzle
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

	public static void main(String[] args) {
		SudokuBoard random = getRandomPuzzle(8, 9, new Random());
		if (random != null) {
			System.out.println(random);
		} else {
			System.out.println("No solution");
		}

		System.out.println();
		SudokuBoard randomSolved = SudokuSolver.solve(random);

		if (randomSolved != null) {
			System.out.println(randomSolved);
		} else {
			System.out.println("No solution");
		}
	}
}
