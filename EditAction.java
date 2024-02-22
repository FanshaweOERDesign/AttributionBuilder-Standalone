/**!
Copyright (c) 2023 Jason Benoit and David Giesbrecht

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

**This text is from: http://opensource.org/licenses/MIT**
!**/

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
		boolean isComplete = false;
		
		while (!isComplete && res == JOptionPane.OK_OPTION)
		{
			EditAttributionPanel editPanel = currAttr.buildEditPanel();
			
			
			res = JOptionPane.showConfirmDialog(editPanel, editPanel, 
					"Edit Attribution", 
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (res == JOptionPane.OK_OPTION)
			{
				String[] incompleteFields = editPanel.grabToAttribution();
				
				
				
				if(incompleteFields.length == 0)
				{
					boolean licenseKeyIsOther = currAttr.licenseKey != null && currAttr.licenseKey.equals("Other");
					boolean licenseTypeIsEmpty = currAttr.getLicense() == null || currAttr.getLicense().length() == 0;
					if (licenseKeyIsOther && licenseTypeIsEmpty)
					{
						JPanel pnl = new JPanel();
						JLabel lbl = new JLabel();
						lbl.setText("Please enter additional license details");
						pnl.add(lbl);
						JOptionPane.showMessageDialog(editPanel, pnl, "Additional Info Required", JOptionPane.PLAIN_MESSAGE);
						isComplete = false;
						continue;
					}
					project.updateAttribution(key, currAttr);
					project.save();
					projectWindow.updateAttributionTable(project);
					isComplete = true;
				}
				else if (incompleteFields.length == 1 && incompleteFields[0].contains("License Type") && !currAttr.licenseKey.equals("Other"))
				{ 
					isComplete = true;
				}
				else
				{
					String msg = "Please complete the following fields:\n";
					for (String f : incompleteFields)
					{
						if (f.equals("License Type") && !currAttr.licenseKey.equals("Other"))
						{
							continue;
						}
						msg += "\n- " + f;
					}
					
					JOptionPane.showMessageDialog(editPanel, msg, "Info Missing", JOptionPane.PLAIN_MESSAGE);
					isComplete = false;
				}			
			}
		}
	}
}
//end class