/**
 * Program Name: EditAction.java
 * Purpose: Defines a class to handle the click event on the "Edit" button for a row
 * 					in the ProjectWindow JTable listing the project attributions
 * Coder: Jason Benoit
 * Date: Sep 17, 2023
 */

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class EditAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;
	
	private Project project;
	private ProjectWindow projectWindow;
	
	public EditAction(Project project, ProjectWindow window)
	{
		this.project = project;
		this.projectWindow = window;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JTable table = (JTable)e.getSource();
    int modelRow = Integer.valueOf( e.getActionCommand() );
    String key = (String)table.getModel().getValueAt(modelRow, 0);
		Attribution currAttr = project.getAttributions().get(key);
		int res = JOptionPane.OK_OPTION;
		Attribution temp = null;
		
		while (temp == null && res == JOptionPane.OK_OPTION)
		{
			EditAttributionPanel editPanel = new EditAttributionPanel(currAttr);
			res = JOptionPane.showConfirmDialog(editPanel, editPanel, 
					"Edit Attribution", 
					JOptionPane.OK_CANCEL_OPTION);
			if (res == JOptionPane.OK_OPTION)
			{
				temp = editPanel.getAttribution();
				
				if (temp != null)
				{
					project.updateAttribution(key, temp);
					project.save();
					projectWindow.updateAttributionTable(project);
				}
				else
				{
					JOptionPane.showMessageDialog(editPanel, "Please complete all fields");
				}			
			}
		}
		
	}
}
//end class