package it.micheledallerive;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class Main extends JFrame {
	
	/**
	 * @Author: Michele Dalle Rive
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	static File usingFile = null;
	JMenuItem mntmSave = new JMenuItem("Save");
	JTextField[][] m = new JTextField[9][9];
	String originalSudoku;
	String removedDigitsSudoku;
	public Main() {
		setResizable(false);
		// Icon
		setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/it/micheledallerive/611evW8M3UL.png")));
		setTitle("Sudoku");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 285, 380);
		setMinimumSize(new Dimension(285,380));
		setResizable(true);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("Game");
		menuBar.add(mnFile);
		
		JMenu mnNew = new JMenu("New");
		mnFile.add(mnNew);
		
		JMenuItem mntmEasy = new JMenuItem("Easy");
		mntmEasy.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				newSudoku(0);
			}
			
		});
		mnNew.add(mntmEasy);
		
		JMenuItem mntmMedium = new JMenuItem("Medium");
		mntmMedium.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				newSudoku(1);
			}
			
		});
		mnNew.add(mntmMedium);
		
		JMenuItem mntmHard = new JMenuItem("Hard");
		mntmHard.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				newSudoku(2);
			}
			
		});
		mnNew.add(mntmHard);
		
		JMenuItem mntmInsane = new JMenuItem("Insane");
		mntmInsane.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				newSudoku(3);
			}
			
		});
		mnNew.add(mntmInsane);
		
		mntmSave.setEnabled(false);
		mntmSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(usingFile==null)
					SudokuFile.saveFile(gridToString(), removedDigitsSudoku, originalSudoku);
				else
					SudokuFile.save(gridToString(), usingFile, removedDigitsSudoku, originalSudoku);
			}
		});
		mnFile.add(mntmSave);
		
		JMenuItem mntmLoad = new JMenuItem("Load");
		mntmLoad.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<String> lines = SudokuFile.loadFile();
				createGUI(elaborateSudokuFile(lines));
			}
		});
		mnFile.add(mntmLoad);
		
		mnFile.addSeparator();
		
		JMenuItem mntmSettings = new JMenuItem("Settings");
		mnFile.add(mntmSettings);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnCheck = new JButton("Check");
		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isCompleted())
					System.out.println("COMPLETED!");
				else
					System.out.println("Check your sudoku!");
			}
		});
		btnCheck.setBounds(30, 281, 89, 23);
		contentPane.add(btnCheck);
		
		JButton btnSolve = new JButton("Solve");
		btnSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printSudoku(originalSudoku);
			}
		});
		btnSolve.setBounds(160, 281, 89, 23);
		contentPane.add(btnSolve);
		
	}
	
	protected String elaborateSudokuFile(List<String> lines) {
		String currentLine = lines.get(0);
		String originalLine = lines.get(1);
		String[] originalPieces = originalLine.substring(0, originalLine.length()-1).split(" ");
		String[] currentPieces = currentLine.substring(0, currentLine.length()-1).split(" ");
		String finalString = "";
		for(int i=0;i<originalPieces.length;i++){
			if(currentPieces[i].equals(originalPieces[i])){
				finalString+=currentPieces[i]+" ";
			}else{
				finalString+=(String.valueOf(Integer.valueOf(currentPieces[i])+10))+" ";
			}
		}
		originalSudoku=lines.get(2);
		removedDigitsSudoku=lines.get(1);
		return finalString;
	}

	private String gridToString(){
		String s = "";
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				if(m[j][i].getText().length()!=1)
					s+="0 ";
				else
					s+=m[j][i].getText()+" ";
			}
		}
		return s;
	}
	
	private void deleteGrid(){
		if(m[0][0]!=null){
			for(int i=0;i<9;i++){
				for(int j=0;j<9;j++){
					contentPane.remove(m[j][i]);
					m[j][i]=null;
				}
			}
		}
	}
	
	private void createGUI(String sudoku){
		mntmSave.setEnabled(true);
		deleteGrid();
		int x=0;
		int y=0;
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				JTextField js = new JTextField();
				js.setBounds(x, y, 30, 30);
				js.setName(String.valueOf(i)+" "+String.valueOf(j));
				js.setText("");
				js.setBackground(Color.WHITE);
				js.addKeyListener(new KeyAdapter(){
					public void keyTyped(KeyEvent e){
						char c = e.getKeyChar();
						String allowed = "123456789 ";
						if(!allowed.contains(String.valueOf(c)))
							e.consume();
						if(js.getText().length()==1)
							e.consume();
						else
							if(String.valueOf(c).equals(" "))
								e.consume();
					}
				});
				js.setHorizontalAlignment(JTextField.CENTER);
				int top,bottom,right,left;
				top=bottom=right=left=1;
				if((j+1)%3==0)
					right=3;
				if((i+1)%3==0)
					bottom=3;
				if(j==0)
					left=3;
				if(i==0)
					top=3;
				js.setBorder( new MatteBorder(top,left,bottom,right, Color.BLACK));
				x+=30;
				m[j][i]=js;
				contentPane.add(js);
			}
			y+=30;
			x=0;
		}
		printSudoku(sudoku);
	}
	
	public void printSudoku(String sudoku){
		String[] parts = sudoku.substring(0, sudoku.length()-1).split(" ");
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				String t = parts[(i*9)+j];
				JTextField f = m[j][i];
				f.setDisabledTextColor(Color.GRAY);
				f.setForeground(Color.BLACK);
				if(Integer.parseInt(t)!=0){
					if(Integer.parseInt(t)>9){
						f.setText(String.valueOf(Integer.parseInt(t)%10));
						f.setBackground(new Color(255,250,205));
					}else{
						f.setText(t);
						f.setEnabled(false);
						f.setFont(f.getFont().deriveFont(Font.BOLD, 14f));
					}
				}else
					f.setBackground(new Color(255,250,205));
			}
		}
	}
	
	private boolean isCompleted(){
		String actualS="";
		for(int i=0;i<9;i++)
			for(int j=0;j<9;j++)
				actualS+=m[j][i].getText()+" ";
		return (actualS.equals(originalSudoku));
	}
	
	private void newSudoku(int difficulty){
		int k=0;
		usingFile=null;
		switch(difficulty){
		case 0:
			k=20;
			break;
		case 1:
			k=30;
			break;
		case 2:
			k=40;
			break;
		case 3:
			k=50;
			break;
		}
		SudokuGenerator s = new SudokuGenerator(9, k);
		s.fillValues();
		
		originalSudoku = s.returnSudoku();
		s.removeKDigits();
		removedDigitsSudoku = s.returnSudoku();
		
		createGUI(removedDigitsSudoku);
	}

}
