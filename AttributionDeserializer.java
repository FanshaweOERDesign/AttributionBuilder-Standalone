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

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class AttributionDeserializer implements JsonDeserializer<Attribution>
{
	private String attributionTypeElementName;
  private Gson gson;
  private Map<String, Class<? extends Attribution>> attributionTypeRegistry;
  
  public AttributionDeserializer(String attributionTypeElementName) {
    this.attributionTypeElementName = attributionTypeElementName;
    this.gson = new Gson();
    this.attributionTypeRegistry = new HashMap<>();
}
  
  public void registerAttributionType(String attributionTypeName, Class<? extends Attribution> attributionType) {
    attributionTypeRegistry.put(attributionTypeName, attributionType);
}
  
	@Override
	public Attribution deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		JsonObject attributionObject = json.getAsJsonObject();
    JsonElement attributionTypeElement = attributionObject.get(attributionTypeElementName);

    Class<? extends Attribution> attributionType = attributionTypeRegistry.get(attributionTypeElement.getAsString());
    return gson.fromJson(attributionObject, attributionType);

	}

}
//end class