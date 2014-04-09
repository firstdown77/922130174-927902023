/**
 * 
 */
package il.technion.cs236369.osmParser;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 * A temporary class to implement ITagsRequired.
 * Called in the temporary main method.
 * @author raphaelas
 *
 */
public class TempTagsRequired implements ITagsRequired {

	/* (non-Javadoc)
	 * @see hw2.ITagsRequired#getTags()
	 */
	@Override
	public Map<String, String> getTags() {
		Map<String, String> theMap = new HashMap<String, String>();
		theMap.put("name", "Roosevelt Island Tramway");
		return theMap;
	}

}
