package iaProjectFolder;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class QuestionDisplay extends DataSuperClass {

	private JPanel contentPane;

	// GUI components for showing whoel image
	JDialog displayImage;
	JLabel jdialogImgLabel;
	
	
	int width, height;
	// public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					QuestionDisplay frame = new QuestionDisplay();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	public QuestionDisplay(String saveFileName, int column, int row) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 200, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		loadTableData();

//		questionInfo = new String[selectedCategories.size()][5];
//		answerInfo = new String[selectedCategories.size()][5];
//		imageInfo = new String[selectedCategories.size()][5];

		JButton answerRightBtn = new JButton("Answer is correct.");
		answerRightBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int teamIndex = currentTeam - 1;

				// updates the point value based on the row
				teamPoints.set(teamIndex, teamPoints.get(teamIndex) + (row + 1) * 100);

				// Continuously updates the current team
				if (currentTeam < teamPoints.size()) {
					System.out.println("team updated");
					currentTeam++;
				} else {
					// resets the current team to the first, if all teams had a
					// chance to answer a question
					currentTeam = 1;
					System.out.println("team reset");
				}

				// clears the question from the 2-d arrays with questions
				// so that the button will be disabled
				questionInfo[column][row] = "";
				answerInfo[column][row] = "";
				imageInfo[column][row] = "";

				for (int column = 0; column < questionInfo.length; column++) {
					for (int row = 0; row < questionInfo[column].length; row++) {
						System.out.println(questionInfo[column][row]);
						System.out.println(answerInfo[column][row]);
						System.out.println(imageInfo[column][row]);
					}
				}

				// makes sure selected categories wont be duplicated in JeopardyGame
//				selectedCategories.clear();

				JeopardyGame frame = new JeopardyGame(0, saveFileName);
				frame.setVisible(true);
				dispose();
			}
		});
		answerRightBtn.setBounds(460, 519, 144, 29);
		answerRightBtn.setVisible(false);
		contentPane.add(answerRightBtn);

		JButton answerWrongBtn = new JButton("Answer is Wrong");
		answerWrongBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// Continuously updates the current team
				if (currentTeam < teamPoints.size()) {
					currentTeam++;
					System.out.println("team updated");
				} else {
					// resets the current team to the first, if all teams had a
					// chance to answer a question
					currentTeam = 1;
					System.out.println("team reset");
				}

				// makes sure selected categories wont be duplicated in JeopardyGame
//				selectedCategories.clear();

				// clears the question from the 2-d arrays with questions
				// so that the button will be disabled
				questionInfo[column][row] = "";
				answerInfo[column][row] = "";
				imageInfo[column][row] = "";

				for (int column = 0; column < questionInfo.length; column++) {
					for (int row = 0; row < questionInfo[column].length; row++) {
						System.out.println(questionInfo[column][row]);
						System.out.println(answerInfo[column][row]);
						System.out.println(imageInfo[column][row]);
					}
				}

				JeopardyGame frame = new JeopardyGame(0, saveFileName);
				frame.setVisible(true);
				dispose();
			}
		});
		answerWrongBtn.setBounds(209, 519, 149, 29);
		answerWrongBtn.setVisible(false);
		contentPane.add(answerWrongBtn);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(54, 6, 783, 253);
		contentPane.add(scrollPane);

		JLabel imageLbl = new JLabel("No Image found");
		scrollPane.setViewportView(imageLbl);
		imageLbl.setHorizontalAlignment(SwingConstants.CENTER);
		imageLbl.addMouseListener(new MouseAdapter() {
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
//				if (table.getSelectedRow() >= 0) {
					String filePath = imageInfo[column][row];
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
//				}
			}
		});
		
//		JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
//
//		imageLbl.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				System.out.println("" + e);
//
//				// makes sure something is chosen
////				if (table.getSelectedRow() >= 0) {
//				String filePath = imageInfo[column][row];
//				File fileToCheck = new File(filePath);
//
//				// makes sure that the file exists before opening the frame
//				// avoids a crash
//				if (fileToCheck.exists()) {
////						updateTimer.stop();
////						DisplayImage frame = new DisplayImage(filePath);
////						frame.setVisible(true);
////						dispose();
//					// create a dialog Box
//
//					JDialog d = new JDialog(currentFrame, "dialog Box");
//
//					// create a label
//					JLabel l = new JLabel("this is a dialog box");
//
//					d.add(l);
//
//					// set size of dialog
//					d.setBounds(100, 100, 100, 100);
////				            d.setSize(100, 100); 
//
//					// set visibility of dialog
//					d.setVisible(true);
//				}
////					else {
////						// updates the imageLabel text, so user is aware
////						imageLabel.setText("File does not exist.");
////					}
////				}
//			}
//		});

		File imageFile = new File(imageInfo[column][row]);
		if (imageFile.exists()) {
			ImageIcon icon = new ImageIcon(imageInfo[column][row]);
			imageLbl.setIcon(icon);
		}

		JScrollPane questionScrollPane = new JScrollPane();
		questionScrollPane.setBounds(178, 271, 486, 77);
		contentPane.add(questionScrollPane);

		JTextArea questionTextArea = new JTextArea(questionInfo[column][row]);
		questionTextArea.setEditable(false);
		questionTextArea.setWrapStyleWord(true);
		questionTextArea.setLineWrap(true);
		questionScrollPane.setViewportView(questionTextArea);

//		JButton btnExit = new JButton("Exit");
//		btnExit.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				
//				JeopardyGame frame = new JeopardyGame(teamCount, saveFileName);
//				frame.setVisible(true);
//				dispose();
//			}
//		});
//		btnExit.setBounds(321, 519, 149, 29);
//		contentPane.add(btnExit);

		JScrollPane answerScrollPane = new JScrollPane();
		answerScrollPane.setBounds(178, 365, 486, 78);
		answerScrollPane.setVisible(false);
		contentPane.add(answerScrollPane);

		JTextArea answerTextArea = new JTextArea(answerInfo[column][row]);
		answerTextArea.setEditable(false);
		answerScrollPane.setViewportView(answerTextArea);
		answerTextArea.setWrapStyleWord(true);
		answerTextArea.setLineWrap(true);

		JButton showAnswerBtn = new JButton("Show Answer");
		showAnswerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				answerScrollPane.setVisible(true);
				answerWrongBtn.setVisible(true);
				answerRightBtn.setVisible(true);
				showAnswerBtn.setVisible(false);
			}
		});
		showAnswerBtn.setBounds(350, 519, 117, 29);
		contentPane.add(showAnswerBtn);
//
//		JComboBox<String> comboBox = new JComboBox<String>();
//		comboBox.setBounds(23, 520, 158, 20);
//		contentPane.add(comboBox);
//		comboBox.addItem("Please select which team answered the question");
//
//		// adds the numbers of all the teams present using a for loop
//		for (int i = 0; i < teamCount; i++) {
//			comboBox.addItem("Team " + i + 1);
//		}

	}
}
