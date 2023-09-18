
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Program Name: Project.java
 * Purpose: Defines a class to store attributions for a given project
 * Coder: Jason Benoit
 * Date: Sep 16, 2023
 */

public class Project
{
	private TreeMap<String, Attribution> attributions;
	private String path;
	
	public Project()
	{
		attributions = new TreeMap<String, Attribution>();
	}
	
	public Project(String file)
	{
		Gson gson = new Gson();
		this.path = file;
		
		try
		{
			File filePath = new File(path);
			String json = Files.readString(filePath.toPath());
			
			if (json.length() > 0)
			{
				//Type mapType = new TypeToken<TreeMap<String, Attribution>>(){}.getType();
				//attributions = gson.fromJson(json, mapType);
				Type projectType = new TypeToken<Project>(){}.getType();
				Project temp = gson.fromJson(json, projectType);
				this.attributions = temp.attributions;
			}
			else
			{
				attributions = new TreeMap<String, Attribution>();
			}		
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}		
	}
	
	public void save()
	{
		Gson gson = new Gson();
		try
		{
			FileWriter fw = new FileWriter(path);
			PrintWriter pw = new PrintWriter(fw);
			String json = gson.toJson(this);
			pw.print(json);
			fw.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public boolean addAttribution(String name, Attribution attribution)
	{
		if (!attributions.containsKey(name))
		{
			attributions.put(name, attribution);
			return true;
		}
		
		return false;		
	}
	
	public boolean updateAttribution(String name, Attribution attribution)
	{
		if (attributions.containsKey(name))
		{
			attributions.put(name, attribution);
			return true;
		}
		
		return false;
	}
	
	public Attribution getAttribution(String name)
	{
		return attributions.get(name);
	}
	
	public TreeMap<String, Attribution> getAttributions()
	{
		return attributions;
	}
	
	public String getName()
	{
		File filePath = new File(path);
		return filePath.toPath().getFileName().toString();
	}

}
//end class