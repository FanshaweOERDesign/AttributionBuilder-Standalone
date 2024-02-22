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

public class ImageAttribution extends Attribution
{
	String imageURL;
	String imageTitle;
	String sourceURL;
	String sourceName;
	String author;
	String authorURL;
  public String licenseURL;
  public String licenseType;
  
	boolean hasCustomLicense = false;

	private Set<String> required = new HashSet<String>();
	
	public ImageAttribution()
	{
		super();
		super.subtype = "ImageAttribution";
		required.add("imageTitle");
		required.add("sourceName");
		required.add("author");
		required.add("licenseMessage");
	}
	
	@Override
	public String toString()
	{
		String output = hasData(imageURL) ? "\"<a href='" + imageURL + "'>" + imageTitle + "</a>\"" : "\"" + imageTitle + "\"";
		output += " by " + (hasData(authorURL) ? "<a href='" + authorURL + "'>" + author + "</a>" : author);
		output += ", " + (hasData(sourceURL) ? "<a href='" + sourceURL + "'>" + sourceName + "</a>" : sourceName);
		if (hasCustomLicense)
  	{
  		output += ", " + (hasData(licenseURL) ? "<a href='" + licenseURL + "'>" + licenseType + " license</a>" : licenseType + " license");
  	}
  	else
  	{
  		output += ", " + LicenseMap.get(licenseKey);
  	}
		
		return output;
	}

	@Override
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
	public EditAttributionPanel buildEditPanel()
	{
		EditAttributionPanel panel = new EditAttributionPanel();
  	AttributionTextField imgUrlTxt = new AttributionTextField(this);
  	if (imageURL != null && imageURL.length() > 0)
  	{
  		imgUrlTxt.setText(imageURL);
  	}
  	imgUrlTxt.setInputHandler(new AttributionInputHandler() {

			@Override
			public boolean handleInput(Attribution attr, String input)
			{
				if (input == null || attr == null)
				{
					return false;
				}
				
				ImageAttribution ia = (ImageAttribution)attr;
				ia.imageURL = input;
				
				return true;
			}
  	});
  	
  	AttributionTextField imgTitleTxt = new AttributionTextField(this);
  	imgTitleTxt.setRequired(true);
  	if (imageTitle != null && imageTitle.length() > 0)
  	{
  		imgTitleTxt.setText(imageTitle);
  	}
  	imgTitleTxt.setInputHandler(new AttributionInputHandler() {

			@Override
			public boolean handleInput(Attribution attr, String input)
			{
				if (input == null || attr == null)
				{
					return false;
				}
				
				ImageAttribution ia = (ImageAttribution)attr;
				ia.imageTitle = input;
				
				return true;
			}
  	});
  	
  	AttributionTextField imgSrcURLTxt = new AttributionTextField(this);
  	if (sourceURL != null && sourceURL.length() > 0)
  	{
  		imgSrcURLTxt.setText(sourceURL);
  	}
  	imgSrcURLTxt.setInputHandler(new AttributionInputHandler() {

			@Override
			public boolean handleInput(Attribution attr, String input)
			{
				if (input == null || attr == null)
				{
					return false;
				}
				
				ImageAttribution ia = (ImageAttribution)attr;
				ia.sourceURL = input;
				
				return true;
			}
  	});
  	
  	AttributionTextField imgSrcTxt = new AttributionTextField(this);
  	imgSrcTxt.setRequired(true);
  	if (sourceName != null && sourceName.length() > 0)
  	{
  		imgSrcTxt.setText(sourceName);
  	}
  	imgSrcTxt.setInputHandler(new AttributionInputHandler() {

			@Override
			public boolean handleInput(Attribution attr, String input)
			{
				if (input == null || attr == null)
				{
					return false;
				}
				
				ImageAttribution ia = (ImageAttribution)attr;
				ia.sourceName = input;
				
				return true;
			}
  	});
  	
  	AttributionTextField imgAuthorURLTxt = new AttributionTextField(this);
  	if (authorURL != null && authorURL.length() > 0)
  	{
  		imgAuthorURLTxt.setText(authorURL);
  	}
  	imgAuthorURLTxt.setInputHandler(new AttributionInputHandler() {

			@Override
			public boolean handleInput(Attribution attr, String input)
			{
				if (input == null || attr == null)
				{
					return false;
				}
				
				ImageAttribution ia = (ImageAttribution)attr;
				ia.authorURL = input;
				
				return true;
			}
  	});
  	
  	AttributionTextField imgAuthorTxt = new AttributionTextField(this);
  	imgAuthorTxt.setRequired(true);
  	if (author != null && author.length() > 0)
  	{
  		imgAuthorTxt.setText(author);
  	}
  	imgAuthorTxt.setInputHandler(new AttributionInputHandler() {

			@Override
			public boolean handleInput(Attribution attr, String input)
			{
				if (input == null || attr == null)
				{
					return false;
				}
				
				ImageAttribution ia = (ImageAttribution)attr;
				ia.author = input;
				
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
				
				ImageAttribution ia = (ImageAttribution)attr;
				if (input.equals("Other"))
				{
					if (!ia.hasCustomLicense)
					{
						ia.licenseType = null;
						ia.licenseURL = null;
					}
					ia.hasCustomLicense = true;	
				}
				else
				{
					ia.hasCustomLicense = false;
				}
				ia.licenseKey = input;
				attr = ia;
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
  				ImageAttribution ia = (ImageAttribution)attr;
  				ia.licenseURL = input;
  				attr = ia;
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
  				ImageAttribution ia = (ImageAttribution)attr;
  				ia.licenseType = input;
  				attr = ia;
  				return true;
  			}		
    	}); 
  	}
  	else
  	{
  		licenseURLTxt = null;
  		licenseTxt = null;
  	}
  	
  	panel.add("Image URL", imgUrlTxt)
				.add("Image Title", imgTitleTxt)
				.add("Source URL", imgSrcURLTxt)
				.add("Source", imgSrcTxt)
				.add("Author URL", imgAuthorURLTxt)
				.add("Author", imgAuthorTxt)
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
		return imageTitle;
	}

	@Override
	public String getSource()
	{
		return sourceName;
	}

	@Override
	public String getAuthor()
	{
		return author;
	}
	
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