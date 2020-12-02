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
		
		SudokuBoard board = new SudokuBoard(randomPuzzle);
		SudokuBoard randomSolvedBoard = getRandomSolvedPuzzle(0, 0, board, numGenerator);
		return getRandomUnsolvedPuzzle(0, 0, randomSolvedBoard, (double)difficulty/10.0, difficultyAssessor).getBoard();
	}
	
	// helper method for getRandomPuzzle() that recursively constructs a random sudoku puzzle
	private static SudokuBoard getRandomSolvedPuzzle(int row, int col, SudokuBoard currentBoard, Random numGenerator) {
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
					randomNum = (int)Math.round(numGenerator.nextDouble()*((double)currentBoard.getSize() - 1.0) + 1.0);
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

	private static SudokuBoard getRandomUnsolvedPuzzle(int row, int col, SudokuBoard currentBoard, double difficulty, Random difficultyAssessor) {
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
