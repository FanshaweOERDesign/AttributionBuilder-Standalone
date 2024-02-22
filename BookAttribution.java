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

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class BookAttribution extends Attribution
{
	public String pageURL;
  public String pageTitle;
  public String bookURL;
  public String bookTitle;
  public String author;
  public String licenseURL;
  public String licenseType;
  public boolean hasCustomLicense = false;
  
  private Set<String> required = new HashSet<String>();
  
  public BookAttribution()
  {
  	super();
  	super.subtype = "BookAttribution";
  	required.add("pageTitle");
  	required.add("bookTitle");
  	required.add("author");
  }
  
  public boolean isComplete()
  {
  	
  	Field[] fields = this.getClass().getFields();
  	for (Field f : fields)
  	{
  		try
  		{
  			if (f.getName().equals("hasCustomLicense"))
				{
					continue;
				}
  			
  			String cur = (String)f.get(this);
  			
  			if (cur == null || cur.trim().length() == 0)
  			{
  				if (required.contains(f.getName()))
  				{
  					return false;
  				}
  				
  				
  				if (f.getName().equals("licenseType"))
  				{
  					 if (!hasData(cur) && licenseKey.equals("Other"))
  					 {
  						 return false;
  					 }
  				}	
  			}
  		}
  		catch (Exception ex)
  		{
  			ex.printStackTrace();
  		}  		
  	}
  	
  	return true;
  }
  
  @Override
  public String toString()
  { 	
  	if (!isComplete())
  	{
  		return null;
  	}
  	
  	String output = hasData(pageURL) ? "\"<a href='" + pageURL + "'>" : "\"";
  	output += pageTitle;
  	output += hasData(pageURL) ? "</a>\" from " : "\" from ";
  	output += hasData(bookURL) ? "<a href='" + bookURL +"'>" + bookTitle + "</a>" : bookTitle;
  	output += " by " + author;
  	
  	if (hasCustomLicense)
  	{
  		output += " is licensed under a ";
  		output += hasData(licenseURL) ? "<a href='" + licenseURL + "'>" + licenseType + " license</a>" : licenseType + " license";
  	}
  	else
  	{
  		output += LicenseMap.get(licenseKey);
  	}
  	
  	output += ", except where otherwise noted.";
  	
  	return output;
  }
  
  public EditAttributionPanel buildEditPanel()
  {
  	EditAttributionPanel panel = new EditAttributionPanel();
  	AttributionTextField pgUrlTxt = new AttributionTextField(this);
  	if (pageURL != null && pageURL.length() > 0)
  	{
  		pgUrlTxt.setText(pageURL);
  	}
  	pgUrlTxt.setInputHandler( new AttributionInputHandler() {

			@Override
			public boolean handleInput(Attribution attr, String input)
			{
				if (input == null || attr == null)
				{
					return false;
				}
				
				BookAttribution ba = (BookAttribution)attr;
				
				ba.pageURL = input;

				return true;
			}		
  	});   	
  	
  	AttributionTextField pgTitleTxt = new AttributionTextField(this);
  	pgTitleTxt.setRequired(true);
  	if (pageTitle != null && pageTitle.length() > 0)
  	{
  		pgTitleTxt.setText(pageTitle);
  	}
  	pgTitleTxt.setInputHandler( new AttributionInputHandler() {

			@Override
			public boolean handleInput(Attribution attr, String input)
			{
				if (input == null || attr == null)
				{
					return false;
				}
				
				BookAttribution ba = (BookAttribution)attr;
				ba.pageTitle = input;

				return true;
			}
  		
  	});   	
  	
  	AttributionTextField bookURLTxt = new AttributionTextField(this);
  	if (bookURL != null && bookURL.length() > 0)
  	{
  		bookURLTxt.setText(bookURL);
  	}
  	bookURLTxt.setInputHandler( new AttributionInputHandler() {

			@Override
			public boolean handleInput(Attribution attr, String input)
			{
				if (input == null || attr == null)
				{
					return false;
				}
				
				BookAttribution ba = (BookAttribution)attr;
				ba.bookURL = input;

				return true;
			}		
  	});   
  	
  	AttributionTextField bookTitleTxt = new AttributionTextField(this);
  	bookTitleTxt.setRequired(true);
  	if (bookTitle != null && bookTitle.length() > 0)
  	{
  		bookTitleTxt.setText(bookTitle);
  	}
  	bookTitleTxt.setInputHandler( new AttributionInputHandler() {

			@Override
			public boolean handleInput(Attribution attr, String input)
			{
				if (input == null || attr == null)
				{
					return false;
				}
				BookAttribution ba = (BookAttribution)attr;
				ba.bookTitle = input;

				return true;
			} 		
  	}); 
  	
  	AttributionTextField authorTxt = new AttributionTextField(this);
  	authorTxt.setRequired(true);
  	if (author != null && author.length() > 0)
  	{
  		authorTxt.setText(author);
  	}
  	authorTxt.setInputHandler( new AttributionInputHandler() {

			@Override
			public boolean handleInput(Attribution attr, String input)
			{
				if (input == null || attr == null)
				{
					return false;
				}
				BookAttribution ba = (BookAttribution)attr;
				ba.author = input;
				attr = ba;
				return true;
			}		
  	}); 
  	
  	AttributionComboBox licenseDropdown = new AttributionComboBox(this, LicenseMap.getKeys());
  	
  	if (licenseKey != null)
  	{
  		licenseDropdown.setValue(licenseKey, false);
  	}
  	
  	licenseDropdown.setInputHandler(new AttributionInputHandler() {

			@Override
			public boolean handleInput(Attribution attr, String input)
			{
				if (input == null || attr == null)
				{
					return false;
				}
				
				BookAttribution ba = (BookAttribution)attr;
				
				if (input.equals("Other"))
				{
					if (!ba.hasCustomLicense)
					{
						ba.licenseType = null;
						ba.licenseURL = null;
					}
					ba.hasCustomLicense = true;	
				}
				else
				{
					ba.hasCustomLicense = false;
				}
				ba.licenseKey = input;
				attr = ba;
				return true;
				
			}
  		
  	});
  	
  	AttributionTextField licenseURLTxt = null;
  	AttributionTextField licenseTxt = null;
  	
  	if (hasCustomLicense)
  	{
  		licenseURLTxt = new AttributionTextField(this);
    	if (licenseURL != null && licenseURL.length() > 0)
    	{
    		licenseURLTxt.setText(licenseURL);
    	}
    	else
    	{
    		licenseURLTxt.setText("");
    	}
    	licenseURLTxt.setInputHandler( new AttributionInputHandler() {

  			@Override
  			public boolean handleInput(Attribution attr, String input)
  			{
  				if (input == null || attr == null)
  				{
  					return false;
  				}
  				BookAttribution ba = (BookAttribution)attr;
  				ba.licenseURL = input;
  				attr = ba;
  				return true;
  			}		
    	}); 
    	
    	licenseTxt = new AttributionTextField(this);
    	licenseTxt.setRequired(true);
    	if (licenseType != null && licenseType.length() > 0)
    	{
    		licenseTxt.setText(licenseType);
    	}
    	else
    	{
    		licenseTxt.setText("");
    	}
    	licenseTxt.setInputHandler( new AttributionInputHandler() {

  			@Override
  			public boolean handleInput(Attribution attr, String input)
  			{
  				if (input == null || attr == null)
  				{
  					return false;
  				}
  				BookAttribution ba = (BookAttribution)attr;
  				ba.licenseType = input;
  				attr = ba;
  				return true;
  			}		
    	}); 
  	}
  	else
  	{
  		licenseURLTxt = null;
  		licenseTxt = null;
  	}
  	
  	panel.add("Page URL", pgUrlTxt)
  			.add("Page Title",pgTitleTxt)
  			.add("Book URL", bookURLTxt)
  			.add("Book Title", bookTitleTxt)
  			.add("Author", authorTxt)
  			.add("License", licenseDropdown);
  	
  	if (licenseKey != null && licenseKey.equals("Other"))
  	{
  		panel.add("License URL", licenseURLTxt)
  			.add("License Type", licenseTxt);
  	}
  	else
  	{
  		panel.remove("License URL").remove("License Type");
  	}

  	
  	return panel.build();
  			
  }

	@Override
	public String getTitle()
	{
		return pageTitle;
	}

	@Override
	public String getSource()
	{
		return bookTitle;
	}

	@Override
	public String getAuthor()
	{
		return author;
	}
	
//	private boolean licenseFieldsValid()
//	{
//		//boolean licenseURLOK = hasData(licenseURL);
//		boolean licenseTypeOK = hasData(licenseType);
//		boolean licenseMessageOK = hasData(licenseMessage);
//		
//		if (!(licenseTypeOK) && !licenseMessageOK)
//		{
//			return false;
//		}
//		
//		return true;
//	}
	
	private boolean hasData (String field)
	{
		return !(field == null || field.trim().length() == 0);
	}

	@Override
	public String getLicense()
	{
		return licenseType;
	}
}
//end class