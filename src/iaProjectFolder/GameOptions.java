package iaProjectFolder;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

//import javax.swing.table.TableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextArea;
//import java.awt.SystemColor;
import java.awt.Color;

public class GameOptions extends DataSuperClass {

//	private static final Object[][] data = null;
	private JPanel contentPane;
	private JTable categoriesTable;
	private JTable selectedCategoriesTable;
	private JTextField searchInput;
	private JTable questionTable;
	JComboBox<String> teamCountOptions;

	// GUI components for showing whole image
	JDialog displayImage;
	JLabel jdialogImgLabel;

	DefaultTableModel searchDTM = new DefaultTableModel();
//	DefaultTableModel otherCategories = new DefaultTableModel();
	DefaultTableModel selectedCategoriesDTM = new DefaultTableModel();
	private static int milliseconds = 0;
	Timer updateTimer;
	int lastSelectedRow = -1;
	
	int width;
	int height;

	LinkedList<Integer> searchResults = new LinkedList<Integer>();
//	LinkedList<String> chosenCategoryNames = new LinkedList<String>();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameOptions frame = new GameOptions();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GameOptions() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 300, 1000, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 128));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// loading the data into the tables
		loadTableData();
		loadCategories();

//		JOptionPane.showMessageDialog(null, "Please select up to 6 categories for your Jeoprady Game.");

		JScrollPane categoriesScrollPane = new JScrollPane();
		categoriesScrollPane.setToolTipText("category options");
		categoriesScrollPane.setBounds(829, 32, 151, 137);
		contentPane.add(categoriesScrollPane);

		// the code here makes this not editable

		categoriesTable = new JTable() {
//	        private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		categoriesTable.setModel(categoriesDTM);
		categoriesDTM.setColumnIdentifiers(new Object[] { "Categories" });
		categoriesScrollPane.setViewportView(categoriesTable);

		JTableHeader header = categoriesTable.getTableHeader();
	    header.setBackground(Color.ORANGE);
	    header.setForeground(Color.BLACK);
	    
		JButton addCategory = new JButton("Select Category");
//		addCategory.setBackground(Color.ORANGE);
//		addCategory.setOpaque(true);
//		addCategory.setBorderPainted(false);
		addCategory.setBounds(839, 174, 135, 29);
		addCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (categoriesTable.getSelectedRow() >= 0) {
					Object[] temp = new Object[] { categoriesDTM.getValueAt(categoriesTable.getSelectedRow(), 0) };
					selectedCategoriesDTM.addRow(temp);
					categoriesDTM.removeRow(categoriesTable.getSelectedRow());
				} else {

					JOptionPane.showMessageDialog(null, "No row selected from categories");
				}
			}
		});
		contentPane.add(addCategory);

		JScrollPane selectedCategoriesScrollPane = new JScrollPane();
		selectedCategoriesScrollPane.setToolTipText("selected categories");
		selectedCategoriesScrollPane.setBounds(829, 212, 151, 137);
		contentPane.add(selectedCategoriesScrollPane);

		// the code here makes this not edtiable
		selectedCategoriesTable = new JTable() {
//	        private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		selectedCategoriesTable.setModel(selectedCategoriesDTM);
		selectedCategoriesDTM.setColumnIdentifiers(new Object[] { "Selected Categories" });
		selectedCategoriesScrollPane.setViewportView(selectedCategoriesTable);

		JTableHeader header2 = selectedCategoriesTable.getTableHeader();
	    header2.setBackground(Color.ORANGE);
	    header2.setForeground(Color.BLACK);
	    
		JButton removeCategory = new JButton("Deselect Category");
//		removeCategory.setBackground(Color.ORANGE);
//		removeCategory.setOpaque(true);
//		removeCategory.setBorderPainted(false);
		removeCategory.setBounds(829, 360, 151, 29);
		removeCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selectedCategoriesTable.getSelectedRow() >= 0) {
					Object[] temp = new Object[] {
							selectedCategoriesDTM.getValueAt(selectedCategoriesTable.getSelectedRow(), 0) };
					categoriesDTM.addRow(temp);
					selectedCategoriesDTM.removeRow(selectedCategoriesTable.getSelectedRow());
				} else {
					JOptionPane.showMessageDialog(null, "No row selected to deselect category");
				}
			}
		});
		contentPane.add(removeCategory);

		JScrollPane mainScrollPane = new JScrollPane();
		mainScrollPane.setToolTipText("not editable");
		mainScrollPane.setBounds(266, 103, 545, 270);
		contentPane.add(mainScrollPane);

		// the code here makes this not edtiable
		questionTable = new JTable() {
//        private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		questionTable.setToolTipText("not editable");
		questionTable.setShowVerticalLines(false);
		questionTable.setModel(questionDTM);
		searchDTM.setColumnIdentifiers(columns);
		mainScrollPane.setViewportView(questionTable);
	
//		JTableHeader header3 = questionTable.getTableHeader();
//		header3.setBackground(Color.BLACK);
//		header3.setForeground(Color.WHITE);
	      
		searchInput = new JTextField();
		searchInput.setToolTipText("search");
		searchInput.setBounds(266, 71, 545, 20);
		searchInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
//				System.out.println("Key Pressed: " + e.getKeyChar()); // prints which key was pressed
				search(searchResults, searchDTM, questionDTM, questionTable, searchInput);
			}
//			@Override
//			public void keyPressed(KeyEvent e) {
////				int key = e.getKeyCode();
////				System.out.println(key);
////				keyPressed = true;
////				System.out.println("key was pressed");
//			}
//
//			@Override
//			public void keyTyped(KeyEvent e) {
//
//			}
		});

		contentPane.add(searchInput);
		searchInput.setColumns(10);

		teamCountOptions = new JComboBox<String>();
		teamCountOptions.setBounds(6, 367, 258, 27);
		contentPane.add(teamCountOptions);
		teamCountOptions.addItem("Enter number of teams/players");

		for (int i = 2; i <= 6; i++) {
			teamCountOptions.addItem("" + i);
		}

		JLabel titleLabel = new JLabel("New Game Options");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBounds(303, 17, 463, 42);
		titleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 32));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(titleLabel);

		JButton mainMenuBtn = new JButton("Main Menu");
		mainMenuBtn.setBounds(292, 385, 117, 29);
		mainMenuBtn.setBackground(Color.ORANGE);
		mainMenuBtn.setOpaque(true);
		mainMenuBtn.setBorderPainted(false);
		mainMenuBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainMenu frame = new MainMenu();
				frame.setVisible(true);
				dispose();
			}
		});
		contentPane.add(mainMenuBtn);

		JButton newGameBtn = new JButton("Start Game");
		newGameBtn.setBackground(Color.ORANGE);
		newGameBtn.setOpaque(true);
		newGameBtn.setBorderPainted(false);
		newGameBtn.setBounds(461, 385, 117, 29);
		newGameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (optionsAreValid() == true) {

					// pulls the option from a comboBox to tell how many options are needed
					int teamCount = 0;
					String teamCountString = teamCountOptions.getSelectedItem().toString();

					try {
						teamCount = Integer.parseInt(teamCountString);
					} catch (NumberFormatException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "Unsure how many teams were selected.");
					}

					// clears data in case a game was run before
					selectedCategories.clear();
					teamPoints.clear();
					questionInfoGenerated = false;

					for (int i = 0; i < selectedCategoriesDTM.getRowCount(); i++) {
						selectedCategories.add(selectedCategoriesDTM.getValueAt(i, 0).toString());
					}

//					// check and print these guys			
//					for(int i = 0; i < selectedCategories.size(); i++) {
//						System.out.println(selectedCategories.get(i));
//					}

					// make a random team the first team
//					int randomTeam = (int) ((Math.random() * ((6 - 1) + 1)) + 1);

					JeopardyGame frame = new JeopardyGame(teamCount, null);
					frame.setVisible(true);
					dispose();
				}
			}
		});
		contentPane.add(newGameBtn);

		JScrollPane imageScrollPane = new JScrollPane();
		imageScrollPane.setBounds(16, 19, 233, 150);
		contentPane.add(imageScrollPane);

		JLabel imageLabel = new JLabel("No image found.");
		imageLabel.setToolTipText("click to enlarge image");
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("" + e);

				// disposes the image display, if it's already active when the JLabel was
				// clicked
				System.out.println(displayImage);
				if (displayImage != null) {
//					System.out.println("frame is already open:" + displayImage);
					displayImage.dispose();
//					System.out.println(displayImage);
				}

				// makes sure a row from the table is chosen
				if (questionTable.getSelectedRow() >= 0) {
					String filePath = questionTable.getValueAt(questionTable.getSelectedRow(), 3).toString();
					File testFile = new File(filePath);

					// makes sure that the file exists before opening the frame
					// avoids a crash
					if (testFile.exists()) {
//						updateTimer.stop();
//						displayImage = new DisplayImage(filePath);

						displayImage = new JDialog();
						displayImage.setBounds(100, 100, width = 1500, height = 1000);
						displayImage.setTitle("Image Display");
						// creates JLabel only if JDialog is created
						jdialogImgLabel = new JLabel("");
						jdialogImgLabel.setToolTipText("click to close");
						jdialogImgLabel.setSize(width, height);
						displayImage.getContentPane().add(jdialogImgLabel, BorderLayout.CENTER);
						jdialogImgLabel.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
//				                mouseClicked(e);
								System.out.println("" + e);

								// QuestionEdit frame = new QuestionEdit();
//								frame.setVisible(true);
								displayImage.dispose();
							}
						});

						scaleImage(filePath, jdialogImgLabel, displayImage, width, height);
						displayImage.setVisible(true);
//						dispose();
					}
//					else {
//						// updates the imageLabel text, so user is aware
//						imageLabel.setText("File does not exist.");
//					}
				}
			}
		});
		imageScrollPane.setViewportView(imageLabel);

		JScrollPane questionScrollPane = new JScrollPane();
		questionScrollPane.setBounds(16, 177, 233, 99);
		contentPane.add(questionScrollPane);

		JTextArea questionDisplay = new JTextArea("Question");
		questionDisplay.setToolTipText("question");
		questionDisplay.setLineWrap(true);
		questionDisplay.setEditable(false);
		questionScrollPane.setViewportView(questionDisplay);

		JScrollPane answerScrollPane = new JScrollPane();
		answerScrollPane.setBounds(16, 281, 233, 81);
		contentPane.add(answerScrollPane);

		JTextArea answerDisplay = new JTextArea("Answer");
		answerDisplay.setToolTipText("answer");
		answerDisplay.setLineWrap(true);
		answerDisplay.setEditable(false);
		answerScrollPane.setViewportView(answerDisplay);

		displayTimer(answerDisplay, questionDisplay, imageLabel);

		JLabel description = new JLabel("Select up to 6 categories of questions for the Jeopardy Game.");
		description.setForeground(Color.WHITE);
		description.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		description.setHorizontalAlignment(SwingConstants.CENTER);
		description.setBounds(181, 420, 685, 40);
		contentPane.add(description);

		JButton randomizeQuestionsBtn = new JButton("Random Questions");
		randomizeQuestionsBtn.setBackground(Color.ORANGE);
		randomizeQuestionsBtn.setOpaque(true);
		randomizeQuestionsBtn.setBorderPainted(false);
		randomizeQuestionsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		randomizeQuestionsBtn.setBounds(625, 385, 177, 29);
		contentPane.add(randomizeQuestionsBtn);
	}

	private void displayTimer(JTextArea answerDisplay, JTextArea questionDisplay, JLabel imageLabel) {
		ActionListener timerListener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				++milliseconds;

				if (milliseconds % 500 == 0) {
					updateDisplay(answerDisplay, questionDisplay, imageLabel, questionTable, lastSelectedRow,
							searchInput);
				}
			}
		};

		updateTimer = new Timer(1, timerListener);
		updateTimer.start();

	}

	private boolean optionsAreValid() {
		boolean optionsValid = true;
		String errorMessage = "";
		// checks if the user selected an option, other than the first option
		if (teamCountOptions.getSelectedIndex() <= 0) {
//			System.out.println(teamCountOptions.getSelectedIndex());
			optionsValid = false;
			errorMessage = errorMessage + "Please select how many teams you want for your game. \n";
//			JOptionPane.showMessageDialog(null, "Please select how many teams you want for your game.");
		}

		if (selectedCategoriesDTM.getRowCount() <= 0) {
			optionsValid = false;
//			JOptionPane.showMessageDialog(null, "Please select at least one category for the Jeopardy Game.");
			errorMessage = errorMessage + "Please select at least one category for the Jeopardy Game. \n";
		}

		if (selectedCategoriesDTM.getRowCount() > 6) {
			optionsValid = false;
//			JOptionPane.showMessageDialog(null, "Maximum of 6 categories can be selected.");
			errorMessage = errorMessage + "Maximum of 6 categories can be selected.";
		}

		if (optionsValid == false) {
			JOptionPane.showMessageDialog(null, errorMessage);
		}

		return optionsValid;
	}
}
