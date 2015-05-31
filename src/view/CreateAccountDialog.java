package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class CreateAccountDialog extends JDialog
{
	 private JTextField userName, email, firstName, lastName;
	 private JPasswordField userPassword, repeatPassword;
	 private JFileChooser userPicChooser;
	 private JLabel userNameLabel, userPasswordLabel, repeatPasswordLabel, emailLabel, firstNameLabel, lastNameLabel;
	 //private JFormattedTextField dueDate, startDate;
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
		  firstName.setPreferredSize(new Dimension(100, 25));
		  firstNameContainer.setBorder(BorderFactory.createTitledBorder("First Name"));
		  firstNameLabel = new JLabel("First Name :");
		  firstNameContainer.add(firstNameLabel);
		  firstNameContainer.add(firstName);
		  
		  userPicChooser = new JFileChooser();
		  userPicChooser.showOpenDialog(null);
		  
		  //User Name
		  JPanel userNameContainer = new JPanel();
		  userNameContainer.setBackground(Color.white);
		  userNameContainer.setPreferredSize(new Dimension(220, 60));
		  userName = new JTextField();
		  userName.setPreferredSize(new Dimension(100, 25));
		  userNameContainer.setBorder(BorderFactory.createTitledBorder("User Name"));
		  userNameLabel = new JLabel("User Name :");
		  userNameContainer.add(userPicChooser);
		  userNameContainer.add(userNameLabel);
		  userNameContainer.add(userName);
		  
		  //Password
		  JPanel userPasswordContainer = new JPanel();
		  userPasswordContainer.setBackground(Color.white);
		  userPasswordContainer.setPreferredSize(new Dimension(220, 60));
		  userPassword = new JPasswordField();
		  userPassword.setPreferredSize(new Dimension(100, 25));
		  userPasswordContainer.setBorder(BorderFactory.createTitledBorder("Password"));
		  userPasswordLabel = new JLabel("Password :");
		  userPasswordContainer.add(userPasswordLabel);
		  userPasswordContainer.add(userPassword);
		  
		  //Repeat Password
		  JPanel repeatPasswordContainer = new JPanel();
		  repeatPasswordContainer.setBackground(Color.white);
		  repeatPasswordContainer.setPreferredSize(new Dimension(220, 60));
		  repeatPassword = new JPasswordField();
		  repeatPassword.setPreferredSize(new Dimension(100, 25));
		  repeatPasswordContainer.setBorder(BorderFactory.createTitledBorder("Repeat password"));
		  repeatPasswordLabel = new JLabel("Repeat password :");
		  repeatPasswordContainer.add(repeatPasswordLabel);
		  repeatPasswordContainer.add(repeatPassword);
		  
		  
		  JPanel control = new JPanel();
		  JButton okButton = new JButton("Create Project");
		  okButton.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent arg0) {
		    	  //Create Project
		      }      
		    });
		  JButton cancelButton = new JButton("Cancel");
		  cancelButton.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent arg0) {
		        setVisible(false);
		      }      
		    });
		  control.add(okButton);
		  control.add(cancelButton);
		  
		  JPanel content = new JPanel();
		  content.setBackground(Color.white);
		  content.add(firstNameContainer);
		  content.add(userNameContainer);
		  content.add(userPasswordContainer);
		  content.add(repeatPasswordContainer);
		  
		  this.getContentPane().add(content, BorderLayout.CENTER);
		  this.getContentPane().add(control, BorderLayout.SOUTH);
	  }

}
