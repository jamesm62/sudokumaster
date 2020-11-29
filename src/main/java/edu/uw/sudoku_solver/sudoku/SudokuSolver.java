package edu.uw.sudoku_solver.sudoku;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SudokuSolver {
	// stores the data of an unsolved sudoku puzzle
	private int[][] puzzle;
	
	// pre: puzzle must be a 9x9 array where empty blanks are represented as zero
	// post: constructs an instance of SudokuSolver with the given unsolved puzzle data
	public SudokuSolver(int[][] puzzle) {
		this.puzzle = puzzle;
	}
	
	// returns a 2D array with the data for the puzzle's solution or null if there is no solution
	public int[][] solve() {
		List<Set<Integer>> rows = new LinkedList<Set<Integer>>();
		List<Set<Integer>> cols = new LinkedList<Set<Integer>>();
		List<Set<Integer>> squares = new LinkedList<Set<Integer>>();
		for (int i = 0; i < 9; i++) {
			rows.add(new HashSet<Integer>());
			cols.add(new HashSet<Integer>());
			squares.add(new HashSet<Integer>());
		}
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (puzzle[row][col] != 0) {
					rows.get(row).add(puzzle[row][col]);
					cols.get(col).add(puzzle[row][col]);
					squares.get(row/3*3 + col/3).add(puzzle[row][col]);
				}
			}
		}
		return solve(0, 0, rows, cols, squares, this.puzzle);
	}
	
	// helper method for solve() that recursively constructs a solution for the unsolved sudoku puzzle
	private int[][] solve(int row, int col, List<Set<Integer>> rows, List<Set<Integer>> cols, List<Set<Integer>> squares, int[][] currentPuzzle) {
		if (row == 9) {
			return currentPuzzle;
		} else if (col == 9) {
			return solve(row + 1, 0, rows, cols, squares, currentPuzzle);
		} else if (currentPuzzle[row][col] != 0) {
			return solve(row, col + 1, rows, cols, squares, currentPuzzle);
		} else {
			for (int i = 1; i < 10; i++) {
				if (!rows.get(row).contains(i) && !cols.get(col).contains(i) && !squares.get(row/3*3 + col/3).contains(i)) {
					currentPuzzle[row][col] = i;
					rows.get(row).add(i);
					cols.get(col).add(i);
					squares.get(row/3*3 + col/3).add(i);
					int[][] solution = solve(row, col + 1, rows, cols, squares, currentPuzzle);
					rows.get(row).remove(i);
					cols.get(col).remove(i);
					squares.get(row/3*3 + col/3).remove(i);
					if (solution != null) {
						return solution;
					}
				}
			}
			
			currentPuzzle[row][col] = 0;
			return null;
		}
	}
	
	// pre: difficulty must be between 0 and 10 and represents the desired difficulty of the sudoku puzzle
	// post: returns a 2D array with the data for a random unsolved sudoku puzzle
	public int[][] getRandomPuzzle(int difficulty) {
		int[][] randomPuzzle = {{0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0}};
		
		List<Set<Integer>> rows = new LinkedList<Set<Integer>>();
		List<Set<Integer>> cols = new LinkedList<Set<Integer>>();
		List<Set<Integer>> squares = new LinkedList<Set<Integer>>();
		for (int i = 0; i < 9; i++) {
			rows.add(new HashSet<Integer>());
			cols.add(new HashSet<Integer>());
			squares.add(new HashSet<Integer>());
		}
		return getRandomPuzzle(0, 0, rows, cols, squares, randomPuzzle, (double)difficulty/10.0);
	}
	
	// helper method for getRandomPuzzle() that recursively constructs a random sudoku puzzle
	private int[][] getRandomPuzzle(int row, int col, List<Set<Integer>> rows, List<Set<Integer>> cols, List<Set<Integer>> squares, int[][] currentPuzzle, double difficulty) {
		if (row == 9) {
			return currentPuzzle;
		} else if (col == 9) {
			return getRandomPuzzle(row + 1, 0, rows, cols, squares, currentPuzzle, difficulty);
		} else if (currentPuzzle[row][col] != 0) {
			return getRandomPuzzle(row, col + 1, rows, cols, squares, currentPuzzle, difficulty);
		} else {
			int[][] solution = null;
			if (Math.random() > difficulty) {
				while (solution == null) {
					int randomNum;
					int count = 0;
					do {
						if (count > 50) {
							solution = null;
						}
						randomNum = (int)Math.round(Math.random()*8.0 + 1.0);
						count++;
					} while (rows.get(row).contains(randomNum) || cols.get(col).contains(randomNum) || squares.get(row/3*3 + col/3).contains(randomNum));
					
					currentPuzzle[row][col] = randomNum;
					rows.get(row).add(randomNum);
					cols.get(col).add(randomNum);
					squares.get(row/3*3 + col/3).add(randomNum);
					solution = getRandomPuzzle(row, col + 1, rows, cols, squares, currentPuzzle, difficulty);
					rows.get(row).remove(randomNum);
					cols.get(col).remove(randomNum);
					squares.get(row/3*3 + col/3).remove(randomNum);
				}
			} else {
				solution = getRandomPuzzle(row, col + 1, rows, cols, squares, currentPuzzle, difficulty);
			}
			
			return solution;
		}
	}

	public static void main(String[] args) {
		int[][] board = { { 0, 0, 0, 2, 6, 0, 7, 0, 1 }, 
				{ 6, 8, 0, 0, 7, 0, 0, 9, 0 },
				{ 1, 9, 0, 0, 0, 4, 5, 0, 0 }, 
				{ 8, 2, 0, 1, 0, 0, 0, 4, 0 },
				{ 0, 0, 4, 6, 0, 2, 9, 0, 0 }, 
				{ 0, 5, 0, 0, 0, 3, 0, 2, 8 },
				{ 0, 0, 9, 3, 0, 0, 0, 7, 4 }, 
				{ 0, 4, 0, 0, 5, 0, 0, 3, 6 },
				{ 7, 0, 3, 0, 1, 8, 0, 0, 0 } };

		SudokuSolver solver = new SudokuSolver(board);

		int[][] solution = solver.solve();

		if (solution != null) {
			System.out.println(new SudokuBoard(solution));
		} else {
			System.out.println("No solution");
		}

		System.out.println();

		int[][] random = solver.getRandomPuzzle(8);

		if (random != null) {
			System.out.println(new SudokuBoard(random));
		} else {
			System.out.println("No solution");
		}

		System.out.println();

		SudokuSolver randomSolver = new SudokuSolver(random);

		int[][] randomSolution = randomSolver.solve();

		if (randomSolution != null) {
			System.out.println(new SudokuBoard(randomSolution));
		} else {
			System.out.println("No solution");
		}
	}

}
