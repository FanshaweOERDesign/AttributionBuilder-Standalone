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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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
		
		this.path = file;
		AttributionDeserializer attribDeserializer = new AttributionDeserializer("subtype");
		attribDeserializer.registerAttributionType("BookAttribution", BookAttribution.class);
		attribDeserializer.registerAttributionType("ImageAttribution", ImageAttribution.class);
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Attribution.class, attribDeserializer)
				.create();
		
		try
		{
			File filePath = new File(path);
			String json = Files.readString(filePath.toPath());
			
			if (json.length() > 0)
			{
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