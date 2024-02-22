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

import java.util.ArrayList;

import javax.swing.*;

public class EditAttributionPanel extends JPanel
{

	private static final long serialVersionUID = 1L;
	
	ArrayList<TitleFieldPair> fields;
	
	public EditAttributionPanel()
	{
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		fields = new ArrayList<>();
			
	}	
	
	public EditAttributionPanel add(String title, AttributionTextField textField)
	{
		fields.add(new TitleFieldPair(title, textField));
		
		return this;
	}
	
	public EditAttributionPanel add(String title, AttributionComboBox comboBox)
	{
		fields.add(new TitleFieldPair(title, comboBox));
		return this;
	}
	
	public EditAttributionPanel remove(String title)
	{
		int i = 0;
		while (i < fields.size() && !fields.get(i).title.equals(title))
		{
			i++;
		}
		
		if (i < fields.size())
		{
			fields.remove(i);
		}
		
		return this;
	}
	
	public EditAttributionPanel build()
	{
		for (TitleFieldPair p : fields)
		{
			JPanel titlePanel = new JPanel();
			JLabel title = new JLabel(p.title);
			titlePanel.add(title);
			
			JPanel textPanel = new JPanel();
			textPanel.add(p.field.getComponent());
			
			this.add(titlePanel);
			this.add(textPanel);
		}
		
		return this;
	}
	
	public String[] grabToAttribution()
	{
		ArrayList<String> incompleteFields = new ArrayList<String>();
		for (TitleFieldPair p : fields)
		{		
			if (!p.field.grabInput())
			{
				incompleteFields.add(p.title);
			}
		}
		
		return incompleteFields.toArray(new String[0]);
	}
	
	private class TitleFieldPair
	{
		public String title;
		public AttributionUIComponent field;
		
		public TitleFieldPair (String title, AttributionUIComponent field)
		{
			this.title = title;
			this.field = field;
		}
	}	
}
//end class