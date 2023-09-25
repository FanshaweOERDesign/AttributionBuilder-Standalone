
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
		
		String[] columnNames = {"Tag", "Page", "Book", "Author", "", "", ""};
	  
	  Object[][] data = new Object[attributions.size()][columnNames.length];
	  
	  int dataIdx = 0;
	  for (Map.Entry<String, Attribution> entry : attributions.entrySet())
	  {
	  	Attribution currAttr = entry.getValue();
	  
	  	Object[] row = new Object[columnNames.length];
	  	row[0] = entry.getKey();

	  	row[1] = currAttr.pageTitle;
	  	row[2] = currAttr.bookTitle;
	  	row[3] = currAttr.author;
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
