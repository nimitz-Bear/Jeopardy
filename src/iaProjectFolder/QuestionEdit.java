package iaProjectFolder;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.imageio.ImageIO;
//import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
//import java.awt.Image;

import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.awt.image.BufferedImage;
import java.io.File;
//import java.io.IOException;
import java.util.LinkedList;
import java.awt.event.ActionEvent;

public class QuestionEdit extends DataSuperClass {

	private JPanel contentPane;
	private JTextField searchInput;
	private JTable table;
	private JTable categoriesTable;
	JLabel imageLabel;
	int width, height;

	// GUI components for showing whoel image
	JDialog displayImage;
	JLabel jdialogImgLabel;

	DefaultTableModel searchDTM = new DefaultTableModel();
	LinkedList<Integer> searchResults = new LinkedList<Integer>();
	LinkedList<String> categoriesList = new LinkedList<String>();

	int lastSelectedRow = -1;
	private static int milliseconds = 0, seconds = 0;
	Timer updateTimer;
	Timer autosaveTimer;
//	boolean userEditing = false;
	boolean autoSave = true;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuestionEdit frame = new QuestionEdit();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public QuestionEdit() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 300, 910, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane mainScrollPane = new JScrollPane();
		mainScrollPane.setBounds(160, 116, 503, 299);
		contentPane.add(mainScrollPane);
//		String[] columns = { "Question", "Answer", "Difficulty", "Image Path", "Category"};

		table = new JTable();
		table.setModel(questionDTM);
		questionDTM.setColumnIdentifiers(columns);
		searchDTM.setColumnIdentifiers(columns);
		table.setFillsViewportHeight(true);
		mainScrollPane.setViewportView(table);

		// changes the color of the header of the table
//		JTableHeader header = table.getTableHeader();
//		header.setBackground(Color.GRAY);
//
//		JTableHeader searchHeader = searchTable.getTableHeader();
//		searchHeader.setBackground(Color.GRAY);

		JButton backBtn = new JButton("Main Menu");
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateTimer.stop();
				MainMenu frame = new MainMenu();
				frame.setVisible(true);
				dispose();
			}
		});
		backBtn.setBounds(12, 432, 117, 29);
		contentPane.add(backBtn);

		searchInput = new JTextField();
		searchInput.setToolTipText("search");
		searchInput.setBounds(167, 85, 481, 26);
		searchInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
//				System.out.println("Key Pressed: " + e.getKeyChar()); // prints which key was pressed
				search(searchResults, searchDTM, questionDTM, table, searchInput);
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

		JScrollPane categoriesScrollPane = new JScrollPane();
		categoriesScrollPane.setBounds(12, 48, 132, 172);
		contentPane.add(categoriesScrollPane);

		categoriesTable = new JTable();
		categoriesDTM.setColumnIdentifiers(new String[] { "Tags/Categories" });
		categoriesTable.setModel(categoriesDTM);
		categoriesScrollPane.setViewportView(categoriesTable);

		// pre-loading data
		loadTableData();
		categoriesList = loadCategories();
//
//		System.out.println("categories");
//
//		for (int i = 0; i < categoriesList.size(); i++) {
//			System.out.println(categoriesList.get(i));
//		}

//		tempWriteData();
//		deleteQuestion();
//		readSpecificRow();

		// test data and loading data
		// can only add to questionDTM when its column names are established.
//		questionDTM.addRow(new Object[] { "Meaning of life", "Hakuna Matata", 5, "Null", "Lol" });
//		questionDTM.addRow(new Object[] { "7", "Number", 3, "/user/Desktop", "Lol" });
//		questionDTM.addRow(new Object[] { "To what extent is this a good idea?", "its not", 5, "Null", "Memes" });
//		insertQuestion("Meaning of life", "Hakuna Matata", 5, "Null", "Lol" );
//		insertQuestion("7", "Number", 3, "/user/Desktop/noice.jpg", "Lol");
//		insertQuestion("To what extent is this a good idea?", "its not", 5, "Null", "Memes");

//		JButton addTagBtn = new JButton("Add Tag");
//		addTagBtn.setBounds(16, 230, 117, 29);
//		contentPane.add(addTagBtn);
//
//		JButton btnDeleteTag = new JButton("Delete Tag");
//		btnDeleteTag.setBounds(16, 260, 117, 29);
//		contentPane.add(btnDeleteTag);

		JLabel titleLbl = new JLabel("Question List");
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setFont(new Font("Lucida Grande", Font.PLAIN, 45));
		titleLbl.setBounds(157, 24, 496, 67);
		contentPane.add(titleLbl);

		JScrollPane imageScrollPane = new JScrollPane();
		imageScrollPane.setBounds(676, 6, 220, 165);
		contentPane.add(imageScrollPane);

		imageLabel = new JLabel("No question selected.");
		imageLabel.setToolTipText("Click to Enlarge");
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
				if (table.getSelectedRow() >= 0) {
					String filePath = table.getValueAt(table.getSelectedRow(), 3).toString();
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
						displayImage.add(jdialogImgLabel, BorderLayout.CENTER);
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

		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Question Image");
		FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files",
				ImageIO.getReaderFileSuffixes());
		fc.addChoosableFileFilter(imageFilter);
		fc.setAcceptAllFileFilterUsed(false);

		JButton btnChangeAddImage = new JButton("Change/Add Image");
		btnChangeAddImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fc.showOpenDialog(btnChangeAddImage);
				File selectedFile = null;
				String fileName = "";
				System.out.println(returnVal);

				// ensures a row is selected to add an image to
				if (table.getSelectedRow() >= 0 && returnVal == JFileChooser.APPROVE_OPTION) {
					imageLabel.setText(""); // clears the text on the screen so it won't show up

					// takes into account if searching
					if (searchInput.getText().contentEquals("")) {
						selectedFile = fc.getSelectedFile();
						fileName = selectedFile.getAbsolutePath();

						System.out.println("Image Selected: " + fileName);
						questionDTM.setValueAt(fileName, table.getSelectedRow(), 3);
					} else {
						selectedFile = fc.getSelectedFile();
						fileName = selectedFile.getAbsolutePath();

						System.out.println("Image Selected: " + fileName);

						// set value for both the question and search DTMs

						// if statement below means that if user cancels, current image won't be
						// overwritten
						questionDTM.setValueAt(fileName, searchResults.get(table.getSelectedRow()), 3);
						searchDTM.setValueAt(fileName, table.getSelectedRow(), 3);
					}
				}
			}
		});
		btnChangeAddImage.setBounds(710, 173, 160, 29);
		contentPane.add(btnChangeAddImage);

		// TODO import and exporting the question list
		JButton btnImportList = new JButton("Import List");
		btnImportList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Please ensure that every question is in the following format:"
						+ "\n question \n answer \n difficulty \n Image Path \n category");
			}
		});
		btnImportList.setBounds(7, 232, 141, 29);
		contentPane.add(btnImportList);
//
//		JButton btnExportList = new JButton("Export List");
//		btnExportList.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO make export list
//			}
//		});
//		btnExportList.setBounds(3, 283, 141, 29);
//		contentPane.add(btnExportList);

		for (int i = 0; i < categoriesDTM.getRowCount(); i++) {
			System.out.println(categoriesDTM.getValueAt(i, 0));
		}
//		
		JButton btnAddQuestion = new JButton("Add Question");
		btnAddQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddQuestion frame = new AddQuestion();
				frame.setVisible(true);
				dispose();
				updateTimer.stop(); // otherwise timer would keep running in AddQuestion
				// TODO test if the sql adding actually works
			}
		});
		btnAddQuestion.setBounds(235, 432, 160, 29);
		contentPane.add(btnAddQuestion);

		JButton btnDeleteQuestion = new JButton("Delete Question");
		btnDeleteQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO make it work when deleting multiple
				// make sure delete works in both search and main dtm
				if (table.getSelectedRow() >= 0) {
					if (searchInput.getText().contentEquals("") == false) {
						// the above if statement is also the condition for the search to reset
						// it tests for when table is being searched

						if (searchResults.get(table.getSelectedRow()) >= 0) {
							System.out.println(searchResults.get(table.getSelectedRow())
									+ " is the part of questionDTM that has been deleted");
							questionDTM.removeRow(searchResults.get(table.getSelectedRow())); // more validation?
							searchDTM.removeRow(table.getSelectedRow()); // removes entry from searchDTM
							// keep at end of block after if statement
						}
					} else if (table.getSelectedRow() >= 0) {
						questionDTM.removeRow(table.getSelectedRow());
					}
				} else {
					JOptionPane.showMessageDialog(null, "Please select a row to delete.");
				}

				// TODO delete using SQL

			}
		});
		btnDeleteQuestion.setBounds(407, 432, 160, 29);
		contentPane.add(btnDeleteQuestion);

		JScrollPane questionScrollPane = new JScrollPane();
		questionScrollPane.setBounds(678, 214, 218, 99);
		contentPane.add(questionScrollPane);

		JTextArea questionDisplay = new JTextArea("Question");
		questionDisplay.setLineWrap(true);
		questionScrollPane.setViewportView(questionDisplay);

		JScrollPane answerScrollPane = new JScrollPane();
		answerScrollPane.setBounds(678, 320, 218, 105);
		contentPane.add(answerScrollPane);

		JTextArea answerDisplay = new JTextArea("Answer");
		answerDisplay.setLineWrap(true);
		answerScrollPane.setViewportView(answerDisplay);

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tempWriteData();
			}
		});
		saveButton.setBounds(762, 432, 117, 29);
		contentPane.add(saveButton);

		JButton autosaveToggle = new JButton("Autosave Off");
		autosaveToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("button pressed state: " + autoSave);
				// TODO toggle auto-save
				if (autoSave == false) {
					// start the timer
					autoSave();
					autosaveTimer.restart();
//					autosaveTimer.start();
					autosaveToggle.setText("Autosave Off");			
					autoSave = true;
					System.out.println("restart autosave Timer");
				} else {
					// stop the timer
					autosaveTimer.stop();
					autosaveToggle.setText("Autosave On");
					autoSave = false;
					System.out.println("stop autosave timer");
				}
			}
		});
		autosaveToggle.setBounds(7, 261, 141, 29);
		contentPane.add(autosaveToggle);

		updateTimer(answerDisplay, questionDisplay);
		autoSave();
		autosaveTimer.start();
	}

//	private void updateDatabase() {
//		if (table.getSelectedRow() >= 0 && table.getSelectedColumn() >= 0 && table.isEditing() == false
//				&& userEditing == true) {
//			userEditing = false;
//			updateSQL(table.getSelectedRow(), table.getSelectedColumn(), searchInput, searchResults, questionDTM);
//		}
//
//		if (table.getSelectedRow() >= 0 && table.getSelectedColumn() >= 0 && table.isEditing()) {
////			System.out.println("Row" + table.getSelectedRow());
////			System.out.println("Column" + table.getSelectedColumn());
////			updateSQL(table.getSelectedRow(), table.getSelectedColumn(), searchInput, searchResults, questionDTM);
//			// TODO adding an ID system
//			userEditing = true;
////			System.out.println("User is editing");
//		}
//	}

	// updates the text in the image preview

	private void updateCategories() {
		// TODO
		// update the names of categories when edited in the menu
		// add to the categories if a category name is edited
		// delete from the categories list, if there are no more questions
	}

	private boolean tableEditingValidation() {
		// can't be more than 5 questions a category
		for (int i = 0; i < categoriesList.size(); i++) {

			int questionIntCategory = 0;

			for (int row = 0; row < questionDTM.getRowCount(); row++) {
				String categoryFromTable = questionDTM.getValueAt(row, 4).toString();
				if (categoryFromTable.contentEquals(categoriesList.get(i))) {
					questionIntCategory++;
				}
			}

			if (questionIntCategory > 5) {
				JOptionPane.showMessageDialog(null,
						"Don't include more than 5 questions for the " + categoriesList.get(i) + " category.");
				return false;
			}
		}

		// TODO difficulty has to be a number, can't be higher than 5 and less than 1
		int row = 0;
		try {
			for (row = 0; row < questionDTM.getRowCount(); row++) {
				int intToCheck = (int) questionDTM.getValueAt(row, 2);
				if ((intToCheck > 0 && intToCheck <= 5) == false) {
					table.setRowSelectionInterval(row, row);
					JOptionPane.showMessageDialog(null, "");
					return false;
				}

			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println("Non-number found for difficult");
			JOptionPane.showMessageDialog(null, "Difficulty has to be a number between 1 and 5.");
			table.setRowSelectionInterval(row, row);
			return false;
		}

		// shouldn't reach this if a false is returned prior
		return true;
	}

	private void autoSave() {
		// this listener updates the display for question and answer
		ActionListener timerListener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				++milliseconds;
				if (milliseconds == 1000) {
					seconds++;
					milliseconds = 0;
				}

				if (seconds == 10) {
					tempWriteData();
					seconds = 0;
				}
			}
		};

		autosaveTimer = new Timer(1, timerListener);
//		autosaveTimer.start();
	}

	private void updateTimer(JTextArea answerDisplay, JTextArea questionDisplay) {
		// this listener updates the display for question and answer
		ActionListener timerListener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				++milliseconds;
				if (milliseconds == 1000) {
					seconds++;
					milliseconds = 0;
				}
//				if (milliseconds % 50 == 0) {
//					updateDatabase();
//				}

				if (milliseconds % 500 == 0) {
					lastSelectedRow = updateDisplay(answerDisplay, questionDisplay, imageLabel, table, lastSelectedRow,
							searchInput);
//					updateImageText();
				}

//				if (seconds == 10) {
//					tempWriteData();
//					seconds = 0;
//				}
			}
		};

		updateTimer = new Timer(1, timerListener);
		updateTimer.start();
	}

}
