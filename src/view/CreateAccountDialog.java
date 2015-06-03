package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

///THIS CLASS IS NOT FINISHED YET - Andrey Uspenskiy
public class CreateAccountDialog extends JDialog
{
	private JTextField userName, email, firstName, lastName;
	private JPasswordField userPassword, repeatPassword;
	private JFileChooser userPicChooser;
	private JButton openFileButton;
	private JLabel fileChooserLabel, imageLabel;
	private JPanel imagePanel;
	// private JFormattedTextField dueDate, startDate;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public CreateAccountDialog(JFrame parent, String title, boolean modal)
	{
		super(parent, title, modal);
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.initComponent();
		this.setVisible(true);

	}

	private void initComponent()
	{
		JPanel firstNameContainer = new JPanel();
		firstNameContainer.setBackground(Color.white);
		firstNameContainer.setPreferredSize(new Dimension(220, 60));
		firstName = new JTextField();
		firstName.setPreferredSize(new Dimension(200, 25));
		firstNameContainer.setBorder(BorderFactory
				.createTitledBorder("First Name"));
		firstNameContainer.add(firstName);

		JPanel lastNameContainer = new JPanel();
		lastNameContainer.setBackground(Color.white);
		lastNameContainer.setPreferredSize(new Dimension(220, 60));
		lastName = new JTextField();
		lastName.setPreferredSize(new Dimension(200, 25));
		lastNameContainer.setBorder(BorderFactory
				.createTitledBorder("Last Name"));
		lastNameContainer.add(lastName);

		openFileButton = new JButton("Open a File...",
                createImageIcon("images/Open16.gif"));
		
		JPanel fileChooserContainer = new JPanel();
		fileChooserContainer.setBackground(Color.white);
		fileChooserContainer.setPreferredSize(new Dimension(430, 60));
		userPicChooser = new JFileChooser();

		fileChooserLabel = new JLabel("Choose image file :");
		fileChooserContainer.add(fileChooserLabel);
		fileChooserContainer.add(openFileButton);
		imagePanel = new JPanel();
		openFileButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int choice = userPicChooser.showOpenDialog(null);
				if (choice == JFileChooser.APPROVE_OPTION) {
	                File file = userPicChooser.getSelectedFile();
	                String sname = file.getAbsolutePath();
	                imageLabel = new JLabel("", new ImageIcon(sname), JLabel.CENTER);
	                imagePanel.add(imageLabel, BorderLayout.CENTER);
	                imagePanel.revalidate();
	                imagePanel.repaint();
				}
			}
		});

		// User Name
		JPanel userNameContainer = new JPanel();
		userNameContainer.setBackground(Color.white);
		userNameContainer.setPreferredSize(new Dimension(220, 60));
		userName = new JTextField();
		userName.setPreferredSize(new Dimension(200, 25));
		userNameContainer.setBorder(BorderFactory
				.createTitledBorder("User Name"));
		userNameContainer.add(userName);
		
		// Email
		JPanel emailContainer = new JPanel();
		emailContainer.setBackground(Color.white);
		emailContainer.setPreferredSize(new Dimension(220, 60));
		email = new JTextField();
		email.setPreferredSize(new Dimension(200, 25));
		emailContainer.setBorder(BorderFactory
				.createTitledBorder("Email"));
		emailContainer.add(email);

		// Password
		JPanel userPasswordContainer = new JPanel();
		userPasswordContainer.setBackground(Color.white);
		userPasswordContainer.setPreferredSize(new Dimension(220, 60));
		userPassword = new JPasswordField();
		userPassword.setPreferredSize(new Dimension(200, 25));
		userPasswordContainer.setBorder(BorderFactory
				.createTitledBorder("Password"));
		userPasswordContainer.add(userPassword);

		// Repeat Password
		JPanel repeatPasswordContainer = new JPanel();
		repeatPasswordContainer.setBackground(Color.white);
		repeatPasswordContainer.setPreferredSize(new Dimension(220, 60));
		repeatPassword = new JPasswordField();
		repeatPassword.setPreferredSize(new Dimension(200, 25));
		repeatPasswordContainer.setBorder(BorderFactory
				.createTitledBorder("Repeat password"));
		repeatPasswordContainer.add(repeatPassword);

		JPanel control = new JPanel();
		JButton okButton = new JButton("Create account");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				// Create Project
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				setVisible(false);
			}
		});
		control.add(okButton);
		control.add(cancelButton);

		JPanel content = new JPanel();
		content.setBackground(Color.white);
		content.add(firstNameContainer);
		content.add(lastNameContainer);
		content.add(userNameContainer);
		content.add(emailContainer);
		content.add(userPasswordContainer);
		content.add(repeatPasswordContainer);
		content.add(fileChooserContainer);
		content.add(imagePanel);

		this.getContentPane().add(content, BorderLayout.CENTER);
		this.getContentPane().add(control, BorderLayout.SOUTH);
	}
	
	protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = CreateAccountDialog.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Could not find file: " + path);
            return null;
        }
    }

}
