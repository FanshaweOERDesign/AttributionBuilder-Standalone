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

public abstract class AttributionUIComponent
{
	AttributionInputHandler handler;
	
	Attribution attribution;
	
	boolean required = false;
	
	public AttributionUIComponent(Attribution attribution) 
	{
		this.attribution = attribution;		
	}
	
	public void setInputHandler(AttributionInputHandler handler)
	{
		this.handler = handler;
	}
	
	public void setRequired(boolean isRequired)
	{
		this.required = isRequired;
	}
	
	public abstract boolean grabInput();
	
	public abstract Component getComponent();
	
}
//end class