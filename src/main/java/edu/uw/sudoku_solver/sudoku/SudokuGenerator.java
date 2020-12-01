package edu.uw.sudoku_solver.sudoku;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SudokuGenerator {
	// pre: 1. difficulty must be between 0 and 10 and represents the desired difficulty of the sudoku puzzle
	//		2. size must be a perfect square and represents the desired number of rows and columns in the puzzle
	// post: returns a 2D array with the data for a random unsolved sudoku puzzle
	public static int[][] getRandomPuzzle(int difficulty, int size, Random difficultyAssessor, Random numGenerator) {
		int[][] randomPuzzle = new int[size][size];
		
		List<Set<Integer>> rows = new LinkedList<Set<Integer>>();
		List<Set<Integer>> cols = new LinkedList<Set<Integer>>();
		List<Set<Integer>> squares = new LinkedList<Set<Integer>>();
		for (int i = 0; i < size; i++) {
			rows.add(new HashSet<Integer>());
			cols.add(new HashSet<Integer>());
			squares.add(new HashSet<Integer>());
		}
		return getRandomPuzzle(0, 0, rows, cols, squares, randomPuzzle, (double)difficulty/20.0 + 0.3, (int)Math.sqrt((double)size), difficultyAssessor, numGenerator);
	}
	
	// helper method for getRandomPuzzle() that recursively constructs a random sudoku puzzle
	public static int[][] getRandomPuzzle(int row, int col, List<Set<Integer>> rows, List<Set<Integer>> cols, List<Set<Integer>> squares, int[][] currentPuzzle, double difficulty, int squareSize, Random difficultyAssessor, Random numGenerator) {
		if (row == currentPuzzle.length) {
			return currentPuzzle;
		} else if (col == currentPuzzle.length) {
			return getRandomPuzzle(row + 1, 0, rows, cols, squares, currentPuzzle, difficulty, squareSize, difficultyAssessor, numGenerator);
		} else if (currentPuzzle[row][col] != 0) {
			return getRandomPuzzle(row, col + 1, rows, cols, squares, currentPuzzle, difficulty, squareSize, difficultyAssessor, numGenerator);
		} else {
			int[][] solution = null;
			if (difficultyAssessor.nextDouble() > difficulty) {
				while (solution == null) {
					int randomNum;
					int count = 0;
					do {
						if (count > 50) {
							solution = null;
						}
						randomNum = (int)Math.round(numGenerator.nextDouble()*((double)currentPuzzle.length - 1.0) + 1.0);
						count++;
					} while (rows.get(row).contains(randomNum) || cols.get(col).contains(randomNum) || squares.get(row/squareSize*squareSize + col/squareSize).contains(randomNum));
					
					currentPuzzle[row][col] = randomNum;
					rows.get(row).add(randomNum);
					cols.get(col).add(randomNum);
					squares.get(row/squareSize*squareSize + col/squareSize).add(randomNum);
					solution = getRandomPuzzle(row, col + 1, rows, cols, squares, currentPuzzle, difficulty, squareSize, difficultyAssessor, numGenerator);
					rows.get(row).remove(randomNum);
					cols.get(col).remove(randomNum);
					squares.get(row/squareSize*squareSize + col/squareSize).remove(randomNum);
				}
			} else {
				solution = getRandomPuzzle(row, col + 1, rows, cols, squares, currentPuzzle, difficulty, squareSize, difficultyAssessor, numGenerator);
			}
			
			return solution;
		}
	}
	
	public static void main(String[] args) {
		int[][] random = getRandomPuzzle(8, 9, new Random(), new Random());
		
		if (random != null) {
			for (int i = 0; i < random.length; i++) {
				for (int e = 0; e < random.length; e++) {
					System.out.print(random[i][e] + (e == (random.length-1) ? "" : ","));
				}
				System.out.println();
			}
		} else {
			System.out.println("No solution");
		}
		
		System.out.println();
		
		int[][] randomSolved = SudokuSolver.solve(random);
		
		if (randomSolved != null) {
			for (int i = 0; i < randomSolved.length; i++) {
				for (int e = 0; e < randomSolved.length; e++) {
					System.out.print(randomSolved[i][e] + (e == (randomSolved.length-1) ? "" : ","));
				}
				System.out.println();
			}
		} else {
			System.out.println("No solution");
		}
	}
}
