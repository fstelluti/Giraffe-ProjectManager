package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddActivityDialog extends JDialog 
{

  private JTextField projectName, activityName;
  private JLabel projectNameLabel, activityNameLabel;

  public AddActivityDialog(JFrame parent, String title, boolean modal)
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
	  //Project Name - Maybe change to drop-down list with all current projects
	  JPanel panName = new JPanel();
	  panName.setBackground(Color.white);
	  panName.setPreferredSize(new Dimension(220, 60));
	  projectName = new JTextField();
	  projectName.setPreferredSize(new Dimension(100, 25));
	  panName.setBorder(BorderFactory.createTitledBorder("Project Name"));
	  projectNameLabel = new JLabel("Project Name :");
	  panName.add(projectNameLabel);
	  panName.add(projectName);
	  
	  //Activity Name
	  JPanel panActivity = new JPanel();
	  panActivity.setBackground(Color.white);
	  panActivity.setPreferredSize(new Dimension(220, 60));
	  activityName = new JTextField();
	  activityName.setPreferredSize(new Dimension(100, 25));
	  panActivity.setBorder(BorderFactory.createTitledBorder("Activity Name"));
	  activityNameLabel = new JLabel("Activity Name :");
	  panActivity.add(activityNameLabel);
	  panActivity.add(activityName);
	  
	  //Duration
	  
	  //Status: to-do, in progress, completed
	  
	  JPanel control = new JPanel();
	  JButton okButton = new JButton("Add Activity");
	  okButton.addActionListener(new ActionListener(){
	      public void actionPerformed(ActionEvent arg0) {
	    	  //Add Activity
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
	  content.add(panName);
	  content.add(panActivity);
	  
	  this.getContentPane().add(content, BorderLayout.CENTER);
	  this.getContentPane().add(control, BorderLayout.SOUTH);
  }
}
