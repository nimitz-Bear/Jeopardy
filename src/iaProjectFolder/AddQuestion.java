package iaProjectFolder;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.image.ImageFilter;
import java.io.File;
//import java.io.FileFilter;

import javax.imageio.ImageIO;
//import javax.activation.MimetypesFileTypeMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Font;

public class AddQuestion extends DataSuperClass {

	private JPanel contentPane;
	JTextArea questionInput;
	JTextArea answerInput;
	JComboBox<String> categoryOptions;
	JComboBox<String> difficultyOptions;
	JLabel imageLabel;

	String fileName = "";
	int addImageState = 0;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddQuestion frame = new AddQuestion();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AddQuestion() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(450, 200, 550, 400);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 128));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		questionDTM.setColumnIdentifiers(columns);
		categoriesDTM.setColumnIdentifiers(new Object[] { "Categories" });
		loadTableData();
		loadCategories();

		JLabel frameTitle = new JLabel("Add Question");
		frameTitle.setFont(new Font("Lucida Grande", Font.PLAIN, 26));
		frameTitle.setForeground(Color.white);
		frameTitle.setHorizontalAlignment(SwingConstants.CENTER);
		frameTitle.setBounds(147, 6, 275, 39);
		contentPane.add(frameTitle);

		JScrollPane questionScrollPane = new JScrollPane();
		questionScrollPane.setBounds(135, 49, 374, 73);
		contentPane.add(questionScrollPane);

		questionInput = new JTextArea("");
		questionScrollPane.setViewportView(questionInput);

		JScrollPane answerScrollPane = new JScrollPane();
		answerScrollPane.setBounds(135, 145, 374, 60);
		contentPane.add(answerScrollPane);

		answerInput = new JTextArea("");
		answerScrollPane.setViewportView(answerInput);

		categoryOptions = new JComboBox<String>();
		categoryOptions.setBounds(41, 298, 204, 27);
		contentPane.add(categoryOptions);
		categoryOptions.addItem("Pick a Category");

//		System.out.println("test");
		for (int i = 0; i < categoriesDTM.getRowCount(); i++) {
//			System.out.println(categoriesDTM.getValueAt(i, 0));
			categoryOptions.addItem((String) categoriesDTM.getValueAt(i, 0));
		}

		difficultyOptions = new JComboBox<String>();
//		difficultyOptions.setBackground(Color.Orange);
		difficultyOptions.setBounds(297, 298, 195, 27);
		contentPane.add(difficultyOptions);
		difficultyOptions.addItem("Pick a Difficulty");
		for (int i = 1; i <= 5; i++) {
			difficultyOptions.addItem("" + i + " (" + i * 100 + " points)");
		}

		JButton backBtn = new JButton("Back");
		backBtn.setOpaque(true);
		backBtn.setBorderPainted(false);
		backBtn.setBackground(Color.ORANGE);
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				QuestionEdit frame = new QuestionEdit();
				frame.setVisible(true);
				dispose();
			}
		});
		backBtn.setBounds(6, 337, 117, 29);
		contentPane.add(backBtn);

		JButton addQuestionBtn = new JButton("Add Question");
		addQuestionBtn.setOpaque(true);
		addQuestionBtn.setBorderPainted(false);
		addQuestionBtn.setBackground(Color.ORANGE);
		addQuestionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (entryIsValid() == true) {

					Object[] newRecord = new Object[5];
					newRecord[0] = questionInput.getText();
					newRecord[1] = answerInput.getText();
					newRecord[2] = difficultyOptions.getSelectedItem();
					newRecord[3] = fileName;
					newRecord[4] = categoryOptions.getSelectedItem();
					// TODO adding a primary key and table name
					questionDTM.addRow(newRecord);
					tempWriteData();
					QuestionEdit frame = new QuestionEdit();
					frame.setVisible(true);
					dispose();
				}

			}
		});
		addQuestionBtn.setBounds(395, 337, 149, 29);
		contentPane.add(addQuestionBtn);

		JLabel lblQuestion = new JLabel("Question");
		lblQuestion.setBackground(Color.ORANGE);
		lblQuestion.setForeground(Color.white);
		lblQuestion.setBounds(41, 78, 82, 16);
		contentPane.add(lblQuestion);

		JLabel lblAnswer = new JLabel("Answer");
		lblAnswer.setBackground(Color.ORANGE);
		lblAnswer.setForeground(Color.white);
		lblAnswer.setBounds(41, 169, 82, 16);
		contentPane.add(lblAnswer);

		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Question Image");
		FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files",
				ImageIO.getReaderFileSuffixes());
		fc.addChoosableFileFilter(imageFilter);
		fc.setAcceptAllFileFilterUsed(false);

		JButton addCategoryButton = new JButton("Add Category");
		addCategoryButton.setOpaque(true);
		addCategoryButton.setBorderPainted(false);
		addCategoryButton.setBackground(Color.ORANGE);
		addCategoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String newCategoryInput = JOptionPane.showInputDialog("Input the name of the category you want to add");

				if (newCategoryInput != null) { // if user presses cancel on InputDialog, value is null
					if (newCategoryInput.contentEquals("") == false) {
						for (int i = 0; i < categoriesDTM.getRowCount(); i++) {
							if (newCategoryInput.contentEquals((String) categoriesDTM.getValueAt(i, 0))) {
								JOptionPane.showMessageDialog(null, "The category you want to add already exists");
								break;
							} else if (i == categoriesDTM.getRowCount() - 1) {
								categoryOptions.addItem(newCategoryInput);
								categoryOptions.setSelectedItem(newCategoryInput);
							}
						}
					} else {
						JOptionPane.showMessageDialog(null, "Please input a valid value.");
					}
				}

				// sets selected to the new category
				for (int i = 0; i < categoryOptions.getItemCount(); i++) {
					if (categoryOptions.getItemAt(i).contentEquals(newCategoryInput)) {
						categoryOptions.setSelectedIndex(i);
					}
				}

			}
		});
		addCategoryButton.setBounds(187, 337, 154, 29);
		contentPane.add(addCategoryButton);

		JButton removeImageBtn = new JButton("Remove Image");
//		removeImageBtn.setBackground(Color.ORANGE);
//		removeImageBtn.setBackground(Color.ORANGE);
//		removeImageBtn.setOpaque(true);
//		removeImageBtn.setBorderPainted(false);
		removeImageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (fileName.contentEquals("") == false) {
					fileName = "";
					imageLabel.setText("Image Path: ");

				}
			}
		});
		
				imageLabel = new JLabel("Image Path:");
				imageLabel.setForeground(Color.white);
				imageLabel.setBounds(147, 238, 362, 39);
				contentPane.add(imageLabel);
		removeImageBtn.setBounds(18, 257, 128, 29);
		contentPane.add(removeImageBtn);
		
				JButton addImageButton = new JButton("Add Image");
				//		addImageButton.setBackground(Color.ORANGE);
				//		addImageButton.setOpaque(true);
				//		addImageButton.setBorderPainted(false);
				//		addImageButton.setBackground(Color.ORANGE);
						addImageButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
				
				//				if (fileName.contentEquals("")) {
								int returnVal = fc.showOpenDialog(addImageButton);
								File selectedFile = null;
								if (returnVal == JFileChooser.APPROVE_OPTION) {
									selectedFile = fc.getSelectedFile();
									fileName = selectedFile.getAbsolutePath();
								}
								System.out.println("Image Selected: " + fileName);
				//					addImageButton.setText("Remove Image");
				//				}
								imageLabel.setText("Image Path: " + fileName);
							}
						});
						addImageButton.setBounds(18, 227, 128, 29);
						contentPane.add(addImageButton);

	}

	private boolean entryIsValid() {
		boolean temp = true;

		// checks if all are non null values
		if (questionInput.getText().contentEquals("") || answerInput.getText().contentEquals("")) {
			temp = false;
			JOptionPane.showMessageDialog(null, "Please put a value for the question and answer fields.");
		}

//		System.out.println("Category: " + categoryOptions.getSelectedIndex());
		// check if an option was selected for the comboboxes
		if (categoryOptions.getSelectedIndex() <= 0 || difficultyOptions.getSelectedIndex() <= 0) {
			temp = false;
			JOptionPane.showMessageDialog(null, "Please choose an option for difficulty and category.");
		}

		// check if the image actually exists and is an image
		if (fileName.contentEquals("") == false) {
			File test = new File(fileName);
			if (test.exists()) {
//				if(!fileName.endsWith(".jpg") &&! fileName.endsWith(".jpeg") && !fileName.endsWith(".png")) {
//					temp = false;
//					JOptionPane.showMessageDialog(null, "The file should be an image with a .jpg");
//				}
			} else {
				temp = false;
				JOptionPane.showMessageDialog(null, "No image file found.");
			}
		}

		return temp;
	}
}
