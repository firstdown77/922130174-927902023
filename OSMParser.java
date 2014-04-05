package hw2;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.simple.JSONArray;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.*;


/**
 * The central OSM Parser class.
 * 
 * @author raphaelas
 *
 */
public class OSMParser extends DefaultHandler implements IOSMParser{
	/** Constants used for JAXP 1.2 */
    static final String JAXP_SCHEMA_LANGUAGE =
        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static final String W3C_XML_SCHEMA =
        "http://www.w3.org/2001/XMLSchema";
    static final String JAXP_SCHEMA_SOURCE =
        "http://java.sun.com/xml/jaxp/properties/schemaSource";
    private String id;
    private String name;
    private String website;
    private String wiki;
    private int numNodes;
    private ArrayList<String> users;    
    boolean inWay = false;
    private XMLReader xmlReader;
    private ArrayList<String> elementRefs;
	private IJSONArray jArray;
	private IDatabaseRequirements db;
	private ICalculateCircumscribedCircleArea area;
	private ITagsRequired tagsRequired;
	private Map<String, String> tagsMap;
	private String nodeID;
	private Map<String, String> wayTags;
	
	/**
	 * Default constructor.  Called in main function.
	 */
	public OSMParser() {
		
	}
	
	/**
	 * Constructor that is called in parse function.
	 * 
	 * @param r An XMLReader.
	 * @param i A class implementing ITagsRequired.
	 */
	public OSMParser(XMLReader r, ITagsRequired i) {
		xmlReader = r;
		tagsRequired = i;
    	elementRefs = new ArrayList<String>();
    	tagsMap = tagsRequired.getTags();
	}
	
    /**
     * Parser calls this for each element in the document
     */
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts)
	throws SAXException
    {
    	if (qName.equals("node")) {
    		nodeID = atts.getValue("id");
    		String nodeLatitude = atts.getValue("lat");
    		String nodeLongitude = atts.getValue("lon");
    		String nodeUser = atts.getValue("user");
    		db.saveNodeData(nodeID, nodeLatitude, nodeLongitude, nodeUser);
		    xmlReader.setContentHandler(new NodeHandler(xmlReader, this));
    	}
    	else if (qName.equals("way")) {
    		inWay = true;
		    users = new ArrayList<String>();
		    id = atts.getValue("id");
		    name = atts.getValue("name");
		    website = atts.getValue("website");
		    wiki = atts.getValue("wiki");
		    users.add(atts.getValue("user"));
		    xmlReader.setContentHandler(new WayHandler(xmlReader, this));
    	}
    }
    
    /**
     * This is called from the WayHandler class as it iterates through
     * all of a way's node references.
     * 
     * @param refToAdd A reference to a node.
     */
    public void addRef(String refToAdd) {
    	elementRefs.add(refToAdd);
    }
    
    /**
     * Saves a node's tag to the database by nodeID.
     * 
     * @param nodeKey A node's tag's key.
     * @param nodeValue A node's tag's value.
     */
    public void addKV(String nodeKey, String nodeValue) {
    	db.saveNodeTag(nodeID, nodeKey, nodeValue);
    	//determineContainsRequiredTags();
    }
    
    /**
     * Adds a tag to the current way's tags Map.
     * 
     * @param key A way's tag's key.
     * @param value A way's tag's value.
     */
    public void addWayTag(String key, String value) {
    	wayTags.put(key, value);
    }
    
    /**
     * Determines if the current way should be outputted.
     * The assignment calls for ways that contain the required
     * tags and are closed ways to be outputted.
     */
    public void determineIfShouldPrint() {
    	if (determineContainsRequiredTags() && determineClosedWay()) {
    		printJSONArray();
    	}
    }
    
    /**
     * Prints a JSONArray containing the way's information.
     * Printing preparations include getting the way coordinates,
     * and making the node references and way coordinates unique.
     */
    private void printJSONArray() {
		uniqueifyNodes(); //Makes elementRefs unique.
		String[][] wayCoordinates = getWayCoordinates();
		String[][] uniqueWayCoordinates = uniqueifyCoordinates(wayCoordinates);
    	double circumscribedCircleArea = area.circumscribedArea(uniqueWayCoordinates);
    	String[] usersArray = (String[]) users.toArray();
    	JSONArray jArrayToPrint = jArray.printJSONArray(id, name, website, wiki, numNodes,
    			uniqueWayCoordinates, circumscribedCircleArea, usersArray);
    	System.out.println(jArrayToPrint);
    	elementRefs.clear();
    }
    /**
     *  Determines if a recently parsed way is a closed way or not.
     *	A closed way is a way that begins and ends at the same coordinates.
     *
     *  This method checks if nodes with different ID's might
     *	have the same coordinates - and really be a closed way when
     *  it may seem that it's an open way.
     */
    public boolean determineClosedWay() {
    	// Lots of preparations before determining if a node is a closed way:
    	// Note: the elementRefs have not been made unique yet.
    	int theSize = elementRefs.size();
    	String firstRef = elementRefs.get(0);
    	String lastRef = elementRefs.get(theSize - 1);
    	String[] firstNodeData = db.getNodeData(firstRef);
    	String[] lastNodeData = db.getNodeData(lastRef);
    	String[] firstRefCoords = new String[1];
    	String[] lastRefCoords = new String[1];
    	firstRefCoords[0] = firstNodeData[0];
    	firstRefCoords[1] = firstNodeData[1];
    	lastRefCoords[0] = lastNodeData[0];
    	lastRefCoords[1] = lastNodeData[1];
    	/* If this is a closed way (the first and last nodes have the same ID
    	   or have the same coordinates): */
    	if (theSize > 0 &&
    			(firstRef.equals(lastRef) || firstRefCoords.equals(lastRefCoords))) {
    		return true;
    	}
    	return false;
	}
    
    /**
     * This method concatenates the way's tags from the WayHandler
     * and also from the NodeHandler - because both Way elements 
     * and a Way's node elements contain tags.
     * 
     * @return true if the current Way contains all the given required tags
     * and false if the Way does not contain all of them.
     */
    public boolean determineContainsRequiredTags() {
    	int containsCount = 0;
    	String[] keys = (String[]) tagsMap.keySet().toArray();
    	String[][] currWayTags = db.getAllNodeTags(id);
    	String[] wayTagsFromWay = (String[]) wayTags.entrySet().toArray();
    	String[][] wayTagsToAdd = new String[wayTagsFromWay.length][1];
    	String[][] updatedCurrWayTags = new String[wayTagsFromWay.length +
    	                                           currWayTags.length][1];
    	for (int i = 0; i < wayTagsFromWay.length; i++) {
    		//TODO: Make sure this works.  Cloning may be necessary.
    		wayTagsToAdd[i] = wayTagsFromWay[i].split("=");
    		// 2D array concatenation.
    		updatedCurrWayTags = concatArrays(currWayTags, wayTagsToAdd);
    	}
    	for (int i = 0; i < keys.length; i++) {
    		String currTagValue = tagsMap.get(keys[i]);
    		String[] requiredTagsArray = new String[1];
    		requiredTagsArray[0] = keys[0];
    		requiredTagsArray[1] = currTagValue;
    		List<String> requiredTagsList = Arrays.asList(requiredTagsArray);
    		List<String> wayTagsList = Arrays.asList(updatedCurrWayTags[i]);
    		if (wayTagsList.containsAll(requiredTagsList)) {
    			containsCount++;
    		}
    	}
    	if (keys.length == containsCount) {
    		return true;
    	}
    	return false;
    }

    /**
     * Taken from http://stackoverflow.com/questions/13722538/
     * how-to-concatenate-two-dimensional-arrays-in-java
     * 
     * @param currWayTags
     * @param wayTagsToAdd
     * @return
     */
    private String[][] concatArrays(String[][] currWayTags,
			String[][] wayTagsToAdd) {
    	String[][] result = new String[currWayTags.length + wayTagsToAdd.length][];
    	System.arraycopy(currWayTags, 0, result, 0, currWayTags.length);
    	System.arraycopy(wayTagsToAdd, 0, result, currWayTags.length, wayTagsToAdd.length);
    	return result;
	}

	/**
     * Parser calls this once after parsing a document.
     */
    public void endDocument() throws SAXException {
    	System.out.println("All done!");
    }
  
    /**
     * Removes nodes with duplicate IDs from the elementRefs ArrayList.
     */
    public void uniqueifyNodes() {
    	HashSet<String> hs = new HashSet<String>();
    	hs.addAll(elementRefs);
    	elementRefs.clear();
    	elementRefs.addAll(hs);
    	elementRefs.trimToSize();
    	numNodes += elementRefs.size();
    }
    
    /**
     * Gets a way's coordinates from its corresponding node.  The corresponding
     * node is the node that has an 'nd ref' defined as one of the way's children.
   	 * Also adds contributing users to the 'users' array.
     * Note: elementRefs must already have been made unique.
     * @return a double array of all a Way's coordinates.
     */
    public String[][] getWayCoordinates() {
    	String[][] theCoords = new String[elementRefs.size()][1]; 
    	for (int i = 0; i < elementRefs.size(); i++) {
    		String[] theAtts = db.getNodeData(elementRefs.get(i));
    		addUser(theAtts);
    		//TODO: Check that this works and also check that cloning is necessary.
    		String[] toClone = Arrays.copyOfRange(theAtts, 0, 1);
    		theCoords[i] = toClone.clone();
    	}
    	return theCoords;
    }
    
    public void addUser(String[] theAtts) {
    	users.add(theAtts[2]);
    }
    
    /**
     * Removes duplicate coordinate pairs from the 'wayCoordinates' 2D array.
     * 
     * @param wayCoords a double array of all an array's coordinates
     * which may or may not be unique.
     * @return a double array of all an array's unique coordinates.
     */
    public String[][] uniqueifyCoordinates(String[][] wayCoords) {
    	HashSet<String[]> keys = new HashSet<String[]>();
    	for (int i = 0; i < wayCoords.length; i++) {
    		keys.add(wayCoords[i]);
    	}
    	return (String[][]) keys.toArray();
    }
    
  /**
   * The central parse method.  Takes the big 2.5 GB OSM file and a
   * class that implements the ITagsRequired interface.
   */
  public JSONArray parse(String osmFile, ITagsRequired tagsRequired) {

	  
        // Create a JAXP SAXParserFactory and configure it
        SAXParserFactory spf = SAXParserFactory.newInstance();

        // Set namespaceAware to true to get a parser that corresponds to
        // the default SAX2 namespace feature setting.  This is necessary
        // because the default value from JAXP 1.0 was defined to be false.
        spf.setNamespaceAware(true);

        // Create a JAXP SAXParser
        SAXParser saxParser = null;
		try {
			saxParser = spf.newSAXParser();
		} catch (Exception e) {
			System.err.println("Exception on create saxParser.");
		}
      
        // Get the encapsulated SAX XMLReader
        xmlReader = null;
		try {
			xmlReader = saxParser.getXMLReader();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Set the ContentHandler of the XMLReader
        xmlReader.setContentHandler(new OSMParser(xmlReader, tagsRequired));

        // Set an ErrorHandler before parsing
        xmlReader.setErrorHandler(new MyErrorHandler(System.err));

        // Tell the XMLReader to parse the XML document
        
        try {
			xmlReader.parse(osmFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	    return null;
  }
  
  /**
   * The main method starts the parser using a default constructor
   * and not the constructor that takes an XMLReader as a parameter.
   * The 1 parameter constructor is used in the parse(..) method.
   * 
   * @param args this parameter is not used.
   */
  public static void main(String[] args) {
	  OSMParser theWorkingParser = new OSMParser();
	  ITagsRequired theMap = new TempTagsRequired();
	  theWorkingParser.parse("./lib/new-york-latest-full.osm", theMap);
  }
  
  /**
   *  Error handler to report errors and warnings
   *
   */
  private static class MyErrorHandler implements ErrorHandler {
      /** Error handler output goes here */
      private PrintStream out;

      MyErrorHandler(PrintStream out) {
          this.out = out;
      }

      /**
       * Returns a string describing parse exception details
       */
      private String getParseExceptionInfo(SAXParseException spe) {
          String systemId = spe.getSystemId();
          if (systemId == null) {
              systemId = "null";
          }
          String info = "URI=" + systemId +
              " Line=" + spe.getLineNumber() +
              ": " + spe.getMessage();
          return info;
      }

      // The following methods are standard SAX ErrorHandler methods.
      // See SAX documentation for more info.
      
      public void warning(SAXParseException spe) throws SAXException {
          out.println("Warning: " + getParseExceptionInfo(spe));
      }
      
      public void error(SAXParseException spe) throws SAXException {
          String message = "Error: " + getParseExceptionInfo(spe);
          throw new SAXException(message);
      }

      public void fatalError(SAXParseException spe) throws SAXException {
          String message = "Fatal Error: " + getParseExceptionInfo(spe);
          throw new SAXException(message);
      }
  }

}