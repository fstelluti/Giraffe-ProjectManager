package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddActivityDialog extends JDialog 
{

  private JTextField projectName;
  private JLabel projectNameLabel;

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
	  JPanel panName = new JPanel();
	  panName.setBackground(Color.white);
	  panName.setPreferredSize(new Dimension(220, 60));
	  projectName = new JTextField();
	  projectName.setPreferredSize(new Dimension(100, 25));
	  panName.setBorder(BorderFactory.createTitledBorder("Activity Name"));
	  projectNameLabel = new JLabel("Activity Name :");
	  panName.add(projectNameLabel);
	  panName.add(projectName);
	  
	  JPanel control = new JPanel();
	  JButton okButton = new JButton("Enter");
	  control.add(okButton);
	  
	  JPanel content = new JPanel();
	  content.setBackground(Color.white);
	  content.add(panName);
	  
	  this.getContentPane().add(content, BorderLayout.CENTER);
	  this.getContentPane().add(control, BorderLayout.SOUTH);
  }
}
