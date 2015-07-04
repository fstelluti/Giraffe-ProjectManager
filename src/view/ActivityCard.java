package view;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import model.Activity;

// Only a skeleton right now, doesn't do anything
// Will house the activity "cards" for the activity view in UI
class ActivityCard extends JPanel {

    private static final long serialVersionUID = 6392559939590478667L;
    public static final int SIZE_X = 240;
    public static final int SIZE_Y = 400;
    private JLabel title;
    private JTextPane text;
    
    
    public ActivityCard(Activity activity) {
	this.setLayout(new BorderLayout());	
    }
}
