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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

public class AttributionComboBox extends AttributionUIComponent
{

	JComboBox<String> comboBox;
	
	String value;
	private boolean noAction = false;
	
	public AttributionComboBox(Attribution attribution, String[] values)
	{
		super(attribution);
		comboBox = new JComboBox<String>(values);
		comboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (noAction)
				{
					noAction = false;
					return;
				}
				value = comboBox.getSelectedItem().toString();	
				
				if (handler != null)
				{
					grabInput();
				}
			}
			
		});
		
		this.attribution = attribution;
		this.value = values[0];
	}

	@Override
	public boolean grabInput()
	{
		if (required && value.length() == 0)
		{
			return false;
		}
		
		return handler.handleInput(attribution, value);
		
		
	}

	@Override
	public Component getComponent()
	{
		return comboBox;
	}
	
	public void setValue(String v, boolean noAction)
	{
		value = v;
		this.noAction = noAction;
		this.comboBox.setSelectedItem(v);
	}

}
//end class