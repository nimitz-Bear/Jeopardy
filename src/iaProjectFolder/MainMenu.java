package iaProjectFolder;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;

public class MainMenu extends DataSuperClass {

	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainMenu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 300, 750, 400);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 128));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel titleLbl = new JLabel("Jeopardy");
		titleLbl.setForeground(Color.WHITE);
		titleLbl.setFont(new Font("Lucida Grande", Font.PLAIN, 39));
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setBounds(110, 25, 537, 57);
		contentPane.add(titleLbl);

		JButton editQuestionsBtn = new JButton("Edit Questions");
		editQuestionsBtn.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		editQuestionsBtn.setBackground(Color.ORANGE);
		editQuestionsBtn.setOpaque(true);
		editQuestionsBtn.setBorderPainted(false);
		editQuestionsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO make new frame before QuestionEdit, where user can decide which table of
				// questions they want to edit
				QuestionEdit frame = new QuestionEdit();
				frame.setVisible(true);
				dispose();
			}
		});
		editQuestionsBtn.setBounds(135, 94, 465, 48);
		contentPane.add(editQuestionsBtn);

		JButton btnRunGame = new JButton("New Game");
		btnRunGame.setBackground(Color.ORANGE);
		btnRunGame.setOpaque(true);
		btnRunGame.setBorderPainted(false);
		btnRunGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameOptions frame = new GameOptions();
				frame.setVisible(true);
				dispose();
			}
		});
		btnRunGame.setBounds(134, 160, 230, 48);
		contentPane.add(btnRunGame);

//		JButton editClassListBtn = new JButton("Edit Class List");
//		editClassListBtn.setBackground(Color.ORANGE);
//		editClassListBtn.setOpaque(true);
//		editClassListBtn.setBorderPainted(false);
//		editClassListBtn.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				// TODO make class list
//				// may also delete this option entirely
//			}
//		});
//		editClassListBtn.setBounds(136, 213, 465, 48);
//		contentPane.add(editClassListBtn);

		JButton exitBtn = new JButton("Exit Application");
		exitBtn.setBackground(Color.ORANGE);
		exitBtn.setOpaque(true);
		exitBtn.setBorderPainted(false);
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "Sure you want to exit?", "Warning",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

				if (result == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		
				JButton loadGame = new JButton("Load Game");
				loadGame.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						// TODO can make new frame to Load Games from which has list of save files
						// and dates edited
						JFileChooser fc = new JFileChooser(".");
						int returnVal = fc.showOpenDialog(loadGame);
						File selectedFile = null;
						String fileName = "";
						System.out.println(returnVal);

						fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

						if (returnVal == JFileChooser.APPROVE_OPTION) {
							selectedFile = fc.getSelectedFile();
							fileName = selectedFile.getAbsolutePath();
						}
						System.out.println("File Selected: " + fileName);

						// checks if the file is an .sav file
						if (fileName.endsWith(".sav")) {

							// clears data in case a game was run before
							selectedCategories.clear();
							teamPoints.clear();
							questionInfoGenerated = false;

							JeopardyGame frame = new JeopardyGame(0, fileName);
							frame.setVisible(true);
							dispose();
						} else {
							JOptionPane.showMessageDialog(null, "Error: This file doesn't seem like a save file");
						}

					}
				});
				loadGame.setOpaque(true);
				loadGame.setBorderPainted(false);
				loadGame.setBackground(Color.ORANGE);
				loadGame.setBounds(376, 160, 224, 48);
				contentPane.add(loadGame);
		exitBtn.setBounds(135, 274, 465, 48);
		contentPane.add(exitBtn);
	}
}
