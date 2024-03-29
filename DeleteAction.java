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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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