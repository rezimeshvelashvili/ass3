package assign3;

import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 0 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	int[][] firstSol = new int[SIZE][SIZE];

	private int[][] gr = new int[SIZE][SIZE];
	
	
	private List<Spot> allSpots = new ArrayList<Spot>();
	
	
	private class Spot{
		private int row;
		private int col;
		private int value;
		
		private HashSet<Integer> possibleValues;
		
		public Spot(int row, int col, int value) {
			this.row = row;
			this.col = col;
			this.value = value;
			
		}
		
		public int getRow() {
			return row;
		}
		
		public int getCol() {
			return col;
		}
				
		
		public void genPossibleValues() {
			HashSet<Integer> hs = new HashSet();
			if(gr[row][col]==0) {	
				 int[] arr = new int[9];
				 int boxR = row/3;
				 int boxC = col/3;
				 for(int r=boxR*3; r<(boxR+1)*3; r++) {
					 for(int c=boxC*3; c<(boxC+1)*3; c++) {
						 if(gr[r][c]>0) {
							 arr[gr[r][c]-1] = 1;
						 }
					 }
				 }
				 for(int r=0; r<SIZE; r++) {
					 if(gr[row][r]>0) {
						 arr[gr[row][r]-1] = 1;
					 }
				 }
				 
				 for(int r=0; r<SIZE; r++) {
					 if(gr[r][col]>0) {
						 arr[gr[r][col]-1] = 1;
					 }
				 }
				 for(int p=0; p<arr.length; p++) {
					 if(arr[p]==0) {
						 hs.add(p+1);
					 }
				 }
			}
			possibleValues = hs;
		}
	
		public int sizeOfPossibleValues() {
			return possibleValues.size();
		}
	}
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	
	

	/**
	 * Sets up based on the given ints.
	 */
	
	
	
	public Sudoku(int[][] ints) {
		gr=ints;
		for(int i=0; i<SIZE; i++) {
			for(int j=0; j<SIZE; j++) {
				if(ints[i][j]==0) {
					Spot sp = new Spot(i,j ,ints[i][j]);
					sp.genPossibleValues();
					allSpots.add(sp);
				}
			}
		}

		Collections.sort(allSpots, (sp1, sp2) -> {
			return sp1.sizeOfPossibleValues() - sp2.sizeOfPossibleValues();
		});
	}
	
	
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	
	private int pos=0;
	private boolean b = true;
	
	private long toRet = 0;
	public int solve() {
		long currTime = System.currentTimeMillis();
		if(pos==allSpots.size()) {
			if(b) {
				saveSolution();
				b=false;
			}
			return 1;
		}
		int answer=0;
		Spot curr = allSpots.get(pos);
		curr.genPossibleValues();
		if(curr.sizeOfPossibleValues()>0) {
			for(int val : curr.possibleValues) {
				gr[curr.getRow()][curr.getCol()] = val;
				pos++;
				answer += solve();
				pos--;
				gr[curr.getRow()][curr.getCol()] = 0;
			}
		}else {
			return 0;
		}
		toRet = System.currentTimeMillis() - currTime;
		return answer;
	}
	
	
	
	private void saveSolution() {
		firstSol = new int[SIZE][SIZE];
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				firstSol[i][j] = gr[i][j];
			}
		}
	}
	
	public String getSolutionText() {
		if(b) return "There is no solution";
		String st = "";
		for(int i=0; i<SIZE; i++) {
			for(int j=0; j<SIZE; j++) {
				st += " " + firstSol[i][j];
			}
			st += "\n";
		}
		return st;
	}
	
	public long getElapsed() {
		return toRet;
	}
	
	@Override
	public String toString() {
		String st = "";
		for(int i=0; i<SIZE; i++) {
			for(int j=0; j<SIZE; j++) {
				st += " " + gr[i][j];
			}
			st += "\n";
		}
		return st;
	}

}


