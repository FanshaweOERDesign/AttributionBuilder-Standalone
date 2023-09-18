import java.lang.reflect.Field;

/**
 * Program Name: Attribution.java
 * Purpose: TODO
 * Coder: Jason Benoit 0885941
 * Date: Sep 16, 2023
 */


public class Attribution
{
		public String pageURL;
    public String pageTitle;
    public String bookURL;
    public String bookTitle;
    public String author;
    public String licenseURL;
    public String licenseType;
    
    public boolean isComplete()
    {
    	Field[] fields = this.getClass().getFields();
    	for (Field f : fields)
    	{
    		try
    		{
    			String cur = (String)f.get(this);
    			if (cur.length() == 0)
    			{
    				return false;
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
    	
    	String output = "\"<a href='" + pageURL + "'>" + pageTitle;
    	output += "</a>\" from <a href='" + bookURL +"'>" + bookTitle + "</a>";
    	output += " by " + author + " is licensed under a <a href='" + licenseURL;
    	output += "'>" + licenseType + "</a>, except where otherwise noted.";
    	
    	return output;
    }

}
//end class