package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


// sheidzleba m.nazghaidzis kods gavda, ragacis gamokeneba vnaxe magastan////

 public class SudokuFrame extends JFrame {
	
	 
	 // private JComponents
	 private JTextArea leftArea;
	 private JTextArea rightArea;
	 private JButton check;
	 private JCheckBox autoCheck;
	 
	 
	/**
	 * Constructor
	 */
	public SudokuFrame() {
		super("Sudoku Solver");
		setLayout(new BorderLayout(4,4));
		initWindow();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	// init window 
	private void initWindow() {
		
		addBottomLayout();
		
		addLeftandRightArea();
		
		addListeners();
	}
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {	}	
		SudokuFrame sudokuFrame = new SudokuFrame();
	}

	//init bottom layout ( check button and autocheck checkbox)
	private void addBottomLayout() {
		Box bottomLayout = new Box(BoxLayout.X_AXIS);
		check = new JButton("Check");
		autoCheck = new JCheckBox("Auto Check", true);
		
		bottomLayout.add(check, BorderLayout.EAST);
		bottomLayout.add(autoCheck, BorderLayout.WEST);
		
		add(bottomLayout, BorderLayout.SOUTH);
	}
	
	// init text area for sudoku puzzle and solution view
	private void addLeftandRightArea() {
		leftArea = new JTextArea(15,20);
		leftArea.setBorder(new TitledBorder("Puzzle"));
	
		rightArea = new JTextArea(15,20);
		rightArea.setBorder(new TitledBorder("Solution"));
		
		add(leftArea, BorderLayout.WEST);
		add(rightArea, BorderLayout.EAST);
	}
	
	
	// adding listeners
	private void addListeners() {
		leftArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(autoCheck.isSelected()) {
					solutionView();
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				if(autoCheck.isSelected()) {
					solutionView();
				}
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				if(autoCheck.isSelected()) {
					solutionView();				
				}
			}

		});
		
		check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				solutionView();
			}
		});
	}
	

	// returns solution string. solved sudoku, number of solutions and elapsed time
	private String solve() {
		int[][] grid = Sudoku.textToGrid(leftArea.getText());
		Sudoku sudoku = new Sudoku(grid);
		int solutionsNumber = sudoku.solve();
		return sudoku.getSolutionText() + "solutions:" + " " + solutionsNumber+ "\n" + "elapsed:" + " " +  sudoku.getElapsed();
	}
	
	//seting solution text returned in upper method and catches exeption if there is problem
	private void solutionView() {
		try {
			rightArea.setText(solve());
		} catch (Exception e) {
			rightArea.setText("Parsing problem");
		}
	}

}
