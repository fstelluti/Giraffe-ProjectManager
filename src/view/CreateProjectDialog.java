package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Project;
import model.User;
import controller.ProjectDB;
import controller.UserRolesDB;
import controller.ViewManager;

/**
 * Create a Dialog window, where the user can create a project.
 *
 * @author Anne-Marie Dube, Lukas Cardot-Goyette, Zachary Bergeron
 */

@SuppressWarnings("serial")
public class CreateProjectDialog extends JDialog 
{


	private JTextField projectName;

	private User user;
	boolean exists;
	boolean refresh = false;

	public CreateProjectDialog(User user)
	{
		super(ApplicationWindow.instance(), "Create a New Project", true);
		this.setSize(300, 150);
		this.user = user;
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.initComponent();
		this.setVisible(true);

	}	
	private void initComponent()
	{
		final JPanel content = new JPanel();

		//Project Name
		JPanel panName = new JPanel();
		panName.setBackground(Color.white);
		panName.setPreferredSize(new Dimension(230, 60));
		projectName = new JTextField();
		projectName.setPreferredSize(new Dimension(100, 25));
		panName.setBorder(BorderFactory.createTitledBorder("Project Name"));
		projectName.setPreferredSize(new Dimension(200,30));
		panName.add(projectName);

		JPanel control = new JPanel();
		JButton okButton = new JButton("Create Project");
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				//Checks if the project already exists
				List<Project> projects = ProjectDB.getAll();
				for(Project project:projects){
					if(projectName.getText().equals(project.getName())) { 
						exists = true; break; 
					} else {
						exists = false;
					}
				}
				//Provides error if project name exists
				if(exists){
					JOptionPane.showMessageDialog(content,"Project with this name already exists", "Cannot Create Project", JOptionPane.ERROR_MESSAGE);
				}
				else{

					Project project = new Project();
					project.setName(projectName.getText());
					project.persist();

					//Gets id of project just created

					//Sets initial project manager for project
					ViewManager.setUserRole(user.getId(), project.getId(), 1);
						
					setVisible(false);
					ViewManager.setCurrentProject(project);
					ViewManager.getCurrentUser().setCurrentProject(project);
					ViewManager.reload();
					ViewManager.refresh();

				}
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

		content.setBackground(Color.white);
		content.add(panName);

		this.getContentPane().add(content, BorderLayout.CENTER);
		this.getContentPane().add(control, BorderLayout.SOUTH);
	}

	public boolean isRefresh()
	{
		return refresh;
	}
}
