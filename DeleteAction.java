/**
 * Program Name: DeleteAction.java
 * Purpose: TODO
 * Coder: Jason Benoit 0885941
 * Date: Sep 25, 2023
 */

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * 
 */
public class DeleteAction extends AbstractAction
{

	private static final long serialVersionUID = 1L;
	
	Project project;
	
	public DeleteAction(Project project)
	{
		this.project = project;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JTable table = (JTable)e.getSource();
    int modelRow = Integer.valueOf( e.getActionCommand() );
    String key = (String)table.getModel().getValueAt(modelRow, 0);
    int res = JOptionPane.showConfirmDialog(table, "Really Delete Attribution '" + key + "'?\nThis action cannot be undone.", "Really Delete Attribution '" + key + "'?\nThis action cannot be undone.", JOptionPane.OK_CANCEL_OPTION);
		
    if (res == JOptionPane.OK_OPTION)
    {
    	project.getAttributions().remove(key);
    	project.save();
    	((DefaultTableModel) table.getModel()).removeRow(modelRow);
    }
	}

}
//end class