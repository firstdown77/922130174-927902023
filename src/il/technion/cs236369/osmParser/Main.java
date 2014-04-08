/**
 * 
 */
package il.technion.cs236369.osmParser;

/**
 * @author raphaelas
 *
 */
public class Main {
	private static ITagsRequired tagsRequired;

	  /**
	   * The main method starts the parser using a default constructor
	   * and not the constructor that takes an XMLReader as a parameter.
	   * The 1 parameter constructor is used in the parse(..) method.
	   * 
	   * @param args this parameter is not used.
	   */
	  public static void main(String[] args) {
		  OSMParser theWorkingParser = new OSMParser();
		  tagsRequired = new TempTagsRequired();
		  theWorkingParser.parse("./../hw2try/lib/new-york-latest-full.osm", tagsRequired);
	  }

}
