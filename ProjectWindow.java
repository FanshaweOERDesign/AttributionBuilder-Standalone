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

import java.awt.*;
import java.awt.datatransfer.StringSelection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.util.Map;
import java.util.TreeMap;

public class ProjectWindow extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	
	public ProjectWindow(Project project)
	{
		setSize(500, 300);
	  setLayout(new FlowLayout());
	  mainPanel = new JPanel();
	    
	  updateAttributionTable(project);
  
	  add(mainPanel);
	  setVisible(true);
	}
	
	public void updateAttributionTable(Project project)
	{
		
		if (project == null || project.getAttributions().size() == 0)
		{
			JLabel noAttr = new JLabel((project == null ? "No project loaded" : "Project is empty"));
			this.remove(mainPanel);
			this.validate();
			this.repaint();
	  	mainPanel = new JPanel();
	  	mainPanel.add(noAttr);
	  	mainPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
	  	mainPanel.setVisible(true);
	  	this.add(mainPanel);
	  	this.validate();
	  	return;
		}
		
		TreeMap<String, Attribution> attributions = project.getAttributions();
		
		String[] columnNames = {"Tag", "Title", "Source", "Author", "", "", ""};
	  
	  Object[][] data = new Object[attributions.size()][columnNames.length];
	  
	  int dataIdx = 0;
	  for (Map.Entry<String, Attribution> entry : attributions.entrySet())
	  {
	  	Attribution currAttr = entry.getValue();
	  
	  	Object[] row = new Object[columnNames.length];
	  	row[0] = entry.getKey();

	  	row[1] = currAttr.getTitle();
	  	row[2] = currAttr.getSource();
	  	row[3] = currAttr.getAuthor();
	  	row[4] = "Edit";
	  	row[5] = "Copy";
	  	row[6] = "X";
	  	
	  	
	  	data[dataIdx++] = row; 
	  }
	  
	  DefaultTableModel tm = new DefaultTableModel(data, columnNames);
	  JTable table = new JTable(tm);
	  table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	  table.getColumnModel().getColumn(6).setMaxWidth(25);
	  Action copyAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				JTable table = (JTable)e.getSource();
        int modelRow = Integer.valueOf( e.getActionCommand() );
        String key = (String)table.getModel().getValueAt(modelRow, 0);
				Attribution currAttr = attributions.get(key);
				Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(new StringSelection(currAttr.toString()), null);				
			}
	  	
	  };
	  
	  DeleteAction deleteAction = new DeleteAction(project);
	  
	  EditAction editAction = new EditAction(project, this);
	  
	  ButtonColumn cpyButtonColumn = new ButtonColumn(table, copyAction, 5);
	  cpyButtonColumn.setMnemonic(KeyEvent.VK_D);
	  ButtonColumn editButtonColumn = new ButtonColumn(table, editAction, 4);
	  editButtonColumn.setMnemonic(KeyEvent.VK_D);
	  ButtonColumn deleteButtonColumn = new ButtonColumn(table, deleteAction, 6);
	  deleteButtonColumn.setMnemonic(KeyEvent.VK_BACK_SPACE);
	  deleteButtonColumn.setPadding(new Insets(0,0,0,0));
	  JScrollPane scroll = new JScrollPane(table);
	  scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	  scroll.setPreferredSize(new Dimension(table.getPreferredSize().width + 3, 250));
	  JLabel title = new JLabel("<html><h2>" + project.getName() + "</h2></html>");
	  this.remove(mainPanel);
	  this.validate();
	  this.repaint();
	  mainPanel = new JPanel(new BorderLayout());
	  mainPanel.add(title, BorderLayout.NORTH);
  	mainPanel.add(scroll);
  	this.add(mainPanel);
  	this.validate();
	}

}
//end class