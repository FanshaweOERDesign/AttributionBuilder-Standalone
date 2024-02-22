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

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LicenseMap
{
	private static Map<String, String> licenses;
	
	public static String get(String key)
	{
		if (licenses == null)
		{
			initializeLicenses();
		}
		
		return licenses.get(key);
		
	}
	
	public static String[] getKeys()
	{
		if (licenses == null)
		{
			initializeLicenses();
		}
		
		Set<String> keySet = licenses.keySet();
		return keySet.toArray(new String[keySet.size()]);
	}
	
	public static String getKeyByLongName(String type)
	{
		if (licenses == null)
		{
			initializeLicenses();
		}
		type.replace(".", "/.");
		Pattern pattern = Pattern.compile(".*" + type + ".*");
		
		for (Map.Entry<String, String> e : licenses.entrySet())
		{
			Matcher m = pattern.matcher(e.getValue());
			
			if (m.find())
			{
				return e.getKey();
			}
		}
		
		return "Other";
	}
	
	private static void initializeLicenses()
	{
		licenses = new TreeMap<String, String>();
		licenses.put("CC0", " is in the <a href='https://creativecommons.org/publicdomain/zero/1.0/'>public domain (CC0)</a>");
		licenses.put("CC BY", " is licensed under a <a href='https://creativecommons.org/licenses/by/4.0/deed.en'>Creative Commons Attribution 4.0 International License</a>");
		licenses.put("CC BY-SA", " is licensed under a <a href='https://creativecommons.org/licenses/by-sa/4.0/'>Creative Commons Attribution-ShareAlike 4.0 International License</a>");
		licenses.put("CC BY-NC", " is licensed under a <a href='https://creativecommons.org/licenses/by-nc/4.0/'>Creative Commons Attribution-NonCommercial 4.0 International License</a>");
		licenses.put("CC BY-NC-SA", " is licensed under a <a href='https://creativecommons.org/licenses/by-nc-sa/4.0/'>Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>");
		licenses.put("CC BY-ND", " is licensed under a <a href='https://creativecommons.org/licenses/by-nd/4.0/'>Creative Commons Attribution-NoDerivs 4.0 International License</a>");
		licenses.put("CC BY-NC-ND", " is licensed under a <a href='https://creativecommons.org/licenses/by-nc-nd/4.0/'>Creative Commons Attribution-NonCommercial-NoDerivs 4.0 International License</a>");
		licenses.put("Fair Dealing", " is used under fair dealing");
		licenses.put("Unsplash", " is licensed under the <a href='https://unsplash.com/license'>Unsplash License</a>");
		licenses.put("Pixabay", " is licensed under the <a href='https://pixabay.com/service/license-summary/'>Pixabay License</a>");
		licenses.put("Pexels", " is licensed under the <a href='https://www.pexels.com/license/'>Pexels License</a>");
		licenses.put("CC0 - Short", " <a href='https://creativecommons.org/publicdomain/zero/1.0/'>CC0</a>");
		licenses.put("CC BY - Short", " <a href='https://creativecommons.org/licenses/by/4.0/deed.en'>CC BY 4.0</a>");
		licenses.put("CC BY-SA - Short", " <a href='https://creativecommons.org/licenses/by-sa/4.0/'>CC BY-SA 4.0</a>");
		licenses.put("CC BY-NC - Short", " <a href='https://creativecommons.org/licenses/by-nc/4.0/'>CC BY-NC 4.0</a>");
		licenses.put("CC BY-NC-SA - Short", " <a href='https://creativecommons.org/licenses/by-nc-sa/4.0/'>CC BY-NC-SA 4.0</a>");
		licenses.put("CC BY-ND - Short", " <a href='https://creativecommons.org/licenses/by-nd/4.0/'>CC BY-ND 4.0</a>");
		licenses.put("CC BY-NC-ND - Short", " <a href='https://creativecommons.org/licenses/by-nc-nd/4.0/'>CC BY-NC-ND 4.0</a>");
		licenses.put("Unsplash - Short", " <a href='https://unsplash.com/license'>Unsplash Licenese</a>");
		licenses.put("Pixabay - Short", " <a href='https://pixabay.com/service/license-summary/'>Pixabay License</a>");
		licenses.put("Pexels - Short", " <a href='https://www.pexels.com/license/'>Pexels License</a>");
		licenses.put("Other", " has an unknown license");	
	}
}
//end class