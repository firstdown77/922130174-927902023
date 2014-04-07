/**
 * 
 */
package il.technion.cs236369.osmParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author raphaelas
 *
 */
public class TempTagsRequired implements ITagsRequired {

	/* (non-Javadoc)
	 * @see hw2.ITagsRequired#getTags()
	 */
	@Override
	public Map<String, String> getTags() {
		// TODO Auto-generated method stub
		Map<String, String> theMap = new HashMap<String, String>();
		theMap.put("name", "Roosevelt Island Tramway");
		return theMap;
	}

}
