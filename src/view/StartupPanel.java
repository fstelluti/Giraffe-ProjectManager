package view;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 * This abstract class contains generic start panel content, such as the Girrafe logo
 * @author Anne-Marie Dube, Francois Stelluti
 */

public abstract class StartupPanel extends JPanel {	

	//Protected attributes that will be accessed in subclasses
	protected JPanel startupPanel;
	protected JPanel logoPanel;
	
	public StartupPanel() {	
		startupPanel = new JPanel();
		startupPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Welcome"));
		startupPanel.setLayout(new BoxLayout(startupPanel, BoxLayout.Y_AXIS));
		startupPanel.add(createLogoPanel());

		this.add(startupPanel);
	}
	
	/**
	 * Creates the basic display logo
	 * @return JPanel
	 */
	private JPanel createLogoPanel(){
		logoPanel = new JPanel();
		ImageIcon icon = new ImageIcon(MainViewPanel.class.getResource("images/giraffe.png"));
		JLabel label = new JLabel(icon);
		logoPanel.add(label);
		
		return logoPanel;
	}
	
	/**
	 * Getter used to set the default button
	 * @return JButton 
	 */
	public abstract JButton getDefaultButton();
		
}
