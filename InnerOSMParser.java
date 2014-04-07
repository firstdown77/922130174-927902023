/**
 * 
 */
package hw2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author raphaelas
 *
 */
public class InnerOSMParser extends DefaultHandler {
    private String id;
    private String name;
    private String website;
    private String wiki;
    private int numNodes;
    private ArrayList<String> users;    
    boolean inWay = false;
    private ArrayList<String> elementRefs;
	private IJSONArray jArray;
	private ICalculateCircumscribedCircleArea area;
	private ITagsRequired tagsRequired;
	private Map<String, String> tagsMap;
	private String nodeID;
	private Map<String, String> wayTags;
    private XMLReader xmlReader;
	private int parseCount;
	private boolean skip;
	private String nodeLatitude;
	private String nodeLongitude;
	private String nodeUser;
	private HashMap<String, String[]> nodeData;
	
	/**
	 * Constructor that is called in parse function.
	 * 
	 * @param r An XMLReader.
	 * @param i A class implementing ITagsRequired.
	 */
	public InnerOSMParser(XMLReader r, ITagsRequired i) {
		xmlReader = r;
		tagsRequired = i;
    	elementRefs = new ArrayList<String>();
    	tagsMap = tagsRequired.getTags();
    	skip = true;
    	parseCount = 0;
	}
	
    public void startDocument() throws SAXException {
    	parseCount++;
    }
	
    /**
     * Parser calls this for each element in the document
     */
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts)
	throws SAXException
    {
    	if (skip) {
    		if (qName.equals("way") && parseCount == 0 || 
    				qName.equals("node") && parseCount == 1) {
    			skip = false;
    		}
    	}
    	if (!skip) {
	    	if (qName.equals("node")) {
	    		nodeID = atts.getValue("id");
	    		nodeLatitude = atts.getValue("lat");
	    		nodeLongitude = atts.getValue("lon");
	    		nodeUser = atts.getValue("user");
	    		addNodeData();
	    	}
	    	else if (qName.equals("way")) {
	    		if (parseCount == 1) {
	    			skip = true;
	    			return;
	    		}
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
    }
    
	private void addNodeData() {
		String[] toAdd = new String[3];
		toAdd[0] = nodeLatitude;
		toAdd[1] = nodeLongitude;
		toAdd[2] = nodeUser;
		nodeData.put(nodeID, toAdd);
	}

	/**
     * Parser calls this once after parsing a document.
     */
    public void endDocument() throws SAXException {
    	System.out.println("All done!");
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
    /*
    public void addKV(String nodeKey, String nodeValue) {
    	db.saveNodeTag(nodeID, nodeKey, nodeValue);
    	//determineContainsRequiredTags();
    }
    */
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
    		//TODO
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
    	String[] firstNodeData = nodeData.get(firstRef);
    	String[] lastNodeData = nodeData.get(lastRef);
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
    	//String[][] currWayTags = db.getAllNodeTags(id);
    	String[] wayTagsFromWay = (String[]) wayTags.entrySet().toArray();
    	String[][] wayTagsToAdd = new String[wayTagsFromWay.length][1];
    	/*String[][] updatedCurrWayTags = new String[wayTagsFromWay.length +
    	                                           currWayTags.length][1];
    	*/                                      
    	for (int i = 0; i < wayTagsFromWay.length; i++) {
    		//TODO: Make sure this works.  Cloning may be necessary.
    		wayTagsToAdd[i] = wayTagsFromWay[i].split("=");
    		// 2D array concatenation.
    		//updatedCurrWayTags = concatArrays(currWayTags, wayTagsToAdd);
    	}
    	for (int i = 0; i < keys.length; i++) {
    		String currTagValue = tagsMap.get(keys[i]);
    		String[] requiredTagsArray = new String[1];
    		requiredTagsArray[0] = keys[0];
    		requiredTagsArray[1] = currTagValue;
    		List<String> requiredTagsList = Arrays.asList(requiredTagsArray);
    		for (int j = 0; j < wayTagsToAdd.length; j++) {
        		List<String> wayTagsList = Arrays.asList(wayTagsToAdd[j]);
        		if (wayTagsList.containsAll(requiredTagsList)) {
        			containsCount++;
        		}
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
    /*
    private String[][] concatArrays(String[][] currWayTags,
			String[][] wayTagsToAdd) {
    	String[][] result = new String[currWayTags.length + wayTagsToAdd.length][];
    	System.arraycopy(currWayTags, 0, result, 0, currWayTags.length);
    	System.arraycopy(wayTagsToAdd, 0, result, currWayTags.length, wayTagsToAdd.length);
    	return result;
	}
    */
  
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
    		String[] theAtts = nodeData.get(elementRefs.get(i));
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
}


