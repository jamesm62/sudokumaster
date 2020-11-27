import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SudokuSolver {
	
	private int[][] board;
	
	public SudokuSolver(int[][] board) {
		this.board = board;
	}
	
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
				if (board[row][col] != 0) {
					rows.get(row).add(board[row][col]);
					cols.get(col).add(board[row][col]);
					squares.get(row/3*3 + col/3).add(board[row][col]);
				}
			}
		}
		return solve(0, 0, rows, cols, squares, this.board);
	}
	
	private int[][] solve(int row, int col, List<Set<Integer>> rows, List<Set<Integer>> cols, List<Set<Integer>> squares, int[][] currentBoard) {
		if (row == 9) {
			return currentBoard;
		} else if (col == 9) {
			return solve(row + 1, 0, rows, cols, squares, currentBoard);
		} else if (currentBoard[row][col] != 0) {
			return solve(row, col + 1, rows, cols, squares, currentBoard);
		} else {
			for (int i = 1; i < 10; i++) {
				if (!rows.get(row).contains(i) && !cols.get(col).contains(i) && !squares.get(row/3*3 + col/3).contains(i)) {
					currentBoard[row][col] = i;
					rows.get(row).add(i);
					cols.get(col).add(i);
					squares.get(row/3*3 + col/3).add(i);
					int[][] solution = solve(row, col + 1, rows, cols, squares, currentBoard);
					rows.get(row).remove(i);
					cols.get(col).remove(i);
					squares.get(row/3*3 + col/3).remove(i);
					if (solution != null) {
						return solution;
					}
				}
			}
			
			currentBoard[row][col] = 0;
			return null;
		}
	}

	public static void main(String[] args) {
		int[][] board = {{0,0,0,2,6,0,7,0,1},
						 {6,8,0,0,7,0,0,9,0},
						 {1,9,0,0,0,4,5,0,0},
						 {8,2,0,1,0,0,0,4,0},
						 {0,0,4,6,0,2,9,0,0},
						 {0,5,0,0,0,3,0,2,8},
						 {0,0,9,3,0,0,0,7,4},
						 {0,4,0,0,5,0,0,3,6},
						 {7,0,3,0,1,8,0,0,0}};
		
		SudokuSolver solver = new SudokuSolver(board);
		
		int[][] solution = solver.solve();
		
		if (solution != null) {
			for (int i = 0; i < solution.length; i++) {
				for (int e = 0; e < solution[i].length; e++) {
					System.out.print(solution[i][e]);
				}
				System.out.println();
			}
		} else {
			System.out.println("No solution");
		}
	}

}
