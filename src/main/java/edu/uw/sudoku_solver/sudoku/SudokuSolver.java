package edu.uw.sudoku_solver.sudoku;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SudokuSolver {
	// pre: puzzle must be a 2D array with a perfect square number of rows and columns where empty blanks are represented as zero
	// post: returns a 2D array with the data for the puzzle's solution or null if there is no solution
	public static int[][] solve(int[][] puzzle) {
		List<Set<Integer>> rows = new LinkedList<Set<Integer>>();
		List<Set<Integer>> cols = new LinkedList<Set<Integer>>();
		List<Set<Integer>> squares = new LinkedList<Set<Integer>>();
		int squareSize = (int)Math.sqrt((double)puzzle.length);
		for (int i = 0; i < puzzle.length; i++) {
			rows.add(new HashSet<Integer>());
			cols.add(new HashSet<Integer>());
			squares.add(new HashSet<Integer>());
		}
		for (int row = 0; row < puzzle.length; row++) {
			for (int col = 0; col < puzzle.length; col++) {
				if (puzzle[row][col] != 0) {
					rows.get(row).add(puzzle[row][col]);
					cols.get(col).add(puzzle[row][col]);
					squares.get(row/squareSize*squareSize + col/squareSize).add(puzzle[row][col]);
				}
			}
		}
		return solve(0, 0, rows, cols, squares, puzzle, squareSize);
	}
	
	// helper method for solve() that recursively constructs a solution for the unsolved sudoku puzzle
	public static int[][] solve(int row, int col, List<Set<Integer>> rows, List<Set<Integer>> cols, List<Set<Integer>> squares, int[][] currentPuzzle, int squareSize) {
		if (row == currentPuzzle.length) {
			return currentPuzzle;
		} else if (col == currentPuzzle.length) {
			return solve(row + 1, 0, rows, cols, squares, currentPuzzle, squareSize);
		} else if (currentPuzzle[row][col] != 0) {
			return solve(row, col + 1, rows, cols, squares, currentPuzzle, squareSize);
		} else {
			for (int i = 1; i < currentPuzzle.length + 1; i++) {
				if (!rows.get(row).contains(i) && !cols.get(col).contains(i) && !squares.get(row/squareSize*squareSize + col/squareSize).contains(i)) {
					currentPuzzle[row][col] = i;
					rows.get(row).add(i);
					cols.get(col).add(i);
					squares.get(row/squareSize*squareSize + col/squareSize).add(i);
					int[][] solution = solve(row, col + 1, rows, cols, squares, currentPuzzle, squareSize);
					rows.get(row).remove(i);
					cols.get(col).remove(i);
					squares.get(row/squareSize*squareSize + col/squareSize).remove(i);
					if (solution != null) {
						return solution;
					}
				}
			}
			
			currentPuzzle[row][col] = 0;
			return null;
		}
	}

	public static void main(String[] args) {
		/*
		int[][] board = {{0,0,0,2,6,0,7,0,1},
						 {6,8,0,0,7,0,0,9,0},
						 {1,9,0,0,0,4,5,0,0},
						 {8,2,0,1,0,0,0,4,0},
						 {0,0,4,6,0,2,9,0,0},
						 {0,5,0,0,0,3,0,2,8},
						 {0,0,9,3,0,0,0,7,4},
						 {0,4,0,0,5,0,0,3,6},
						 {7,0,3,0,1,8,0,0,0}};
		*/
		
		int[][] board = {{0,0,0,0},
				 		 {0,0,0,0},
				 		 {0,0,0,0},
				 		 {0,0,0,0}};
		
		int[][] solution = solve(board);
		
		if (solution != null) {
			for (int i = 0; i < solution.length; i++) {
				for (int e = 0; e < solution.length; e++) {
					System.out.print(solution[i][e] + (e == solution.length-1 ? "" : ","));
				}
				System.out.println();
			}
		} else {
			System.out.println("No solution");
		}
	}

}
