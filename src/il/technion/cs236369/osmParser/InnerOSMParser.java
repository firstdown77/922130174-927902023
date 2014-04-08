/**
 * 
 */
package il.technion.cs236369.osmParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
    private HashSet<String> users;    
    boolean inWay = false;
    private ArrayList<String> elementRefs;
	private IJSONArray jArray;
	private ICalculateCircumscribedCircleArea area;
	private ITagsRequired tagsRequired;
	private Map<String, String> tagsMap;
	private String nodeID;
	private HashMap<String, String> wayTags;
    private XMLReader xmlReader;
	private int parseCount;
	private boolean skip;
	private String nodeLatitude;
	private String nodeLongitude;
	private String nodeUser;
	private HashMap<String, String[]> nodeData;
	private HashSet<Way> savedWays;
	private int kSize;
	private Set<String> keys;
	
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
    	parseCount = -1;
    	savedWays = new HashSet<Way>();
    	wayTags = new HashMap<String, String>();
    	keys = tagsMap.keySet();
    	kSize = keys.size();
    	nodeData = new HashMap<String, String[]>(); //TODO maybe change.

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
    		if (((qName.equals("way") && parseCount == 0) || 
    				(qName.equals("node") && parseCount == 1))) {
    			skip = false;
    		}
    	}
    	if (!skip) {
	    	if (qName.equals("node")) {
	    		nodeID = atts.getValue("id");
	    		if (belongsToSavedWay()) {
	    			nodeLatitude = atts.getValue("lat");
	    			nodeLongitude = atts.getValue("lon");
	    			nodeUser = atts.getValue("user");
	    			addNodeData();
	    		}
	    	}
	    	else if (qName.equals("way")) {
	    		if (parseCount == 1) {
	    			skip = true;
	    			return;
	    		}
	    		inWay = true;
			    users = new HashSet<String>();
			    id = atts.getValue("id");
			    name = atts.getValue("name");
			    website = atts.getValue("website");
			    wiki = atts.getValue("wiki");
			    users.add(atts.getValue("user"));
			    xmlReader.setContentHandler(new WayHandler(xmlReader, this));
	    	}
    	}
    }
    
	private boolean belongsToSavedWay() {
		for (Way w : savedWays) {
			if (w.getElementRefs().contains(nodeID)) {
				return true;
			}
		}
		return false;
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
    	if (parseCount == 0) System.out.println("Halfway complete!");
    	else {
    		for (Way w : savedWays) {
    			printJSONArray(w);
    		}
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
    		saveWay();
    	}
    	elementRefs.clear();
    	wayTags.clear();
    }
    
    public void saveWay() {
    	Way toAdd = new Way(id, name, website, wiki, numNodes, users, elementRefs);
    	savedWays.add(toAdd);
    }
    
    /**
     * Prints a JSONArray containing the way's information.
     * Printing preparations include getting the way coordinates,
     * and making the node references and way coordinates unique.
     */
    private void printJSONArray(Way w) {
		uniqueifyNodes(w); //Makes elementRefs unique.
		String[][] wayCoordinates = getWayCoordinates(w);
		//String[][] uniqueWayCoordinates = uniqueifyCoordinates(wayCoordinates);
    	double circumscribedCircleArea = 2.0; //area.circumscribedArea(uniqueWayCoordinates);
    	//JSONArray jArrayToPrint = jArray.printJSONArray(w.getId(), w.getName(), w.getWebsite(), w.getWiki(), w.getNumNodes(),
    	//		wayCoordinates, circumscribedCircleArea, w.getUsers());
    	//System.out.println(jArrayToPrint);
    	System.out.println("" + w.getId() + " " + w.getName() + " " + w.getWebsite() +
    			" " + w.getWiki() + " " + w.getNumNodes() + " " + wayCoordinates +
    			" " + circumscribedCircleArea + " " + w.getUsers());
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
    	/*String[] firstNodeData = nodeData.get(firstRef);
    	String[] lastNodeData = nodeData.get(lastRef);
    	String[] firstRefCoords = new String[1];
    	String[] lastRefCoords = new String[1];
    	firstRefCoords[0] = firstNodeData[0];
    	firstRefCoords[1] = firstNodeData[1];
    	lastRefCoords[0] = lastNodeData[0];
    	lastRefCoords[1] = lastNodeData[1]; */
    	/* If this is a closed way (the first and last nodes have the same ID
    	   or have the same coordinates): */
    	if (theSize > 0 &&
    			(firstRef.equals(lastRef))) { // || firstRefCoords.equals(lastRefCoords))) {
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
    	for (String k : keys) {
        	if (wayTags.containsKey(k) && wayTags.containsValue(tagsMap.get(k))) {
        		containsCount++;
        	}
    	}
    	if (kSize == containsCount) {
    		return true;
    	}
    	return false;
    }
    	//String[][] wayTagsToAdd = new String[wayTagsFromWay.length][1];                                
    	//for (int i = 0; i < wayTagsFromWay.length; i++) {
    		//TODO: Make sure this works.  Cloning may be necessary.
    		//wayTagsToAdd[i] = wayTagsFromWay[i].split("=");
    		// 2D array concatenation.
    		//updatedCurrWayTags = concatArrays(currWayTags, wayTagsToAdd);
    	//}
    	/*
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
    	}*/


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
    public void uniqueifyNodes(Way w) {
    	HashSet<String> hs = new HashSet<String>();
    	hs.addAll(w.getElementRefs());
    	w.clearElementRefs();
    	w.addAllElementRefs(hs);
    	w.incrementNumNodes(w.getElementRefs().size());
    }
    
    /**
     * Gets a way's coordinates from its corresponding node.  The corresponding
     * node is the node that has an 'nd ref' defined as one of the way's children.
   	 * Also adds contributing users to the 'users' array.
     * Note: elementRefs must already have been made unique.
     * @return a double array of all a Way's coordinates.
     */
    public String[][] getWayCoordinates(Way w) {
    	ArrayList<String> elRefs = w.getElementRefs();
    	String[][] theCoords = new String[elRefs.size()][2]; 
    	for (int i = 0; i < elRefs.size(); i++) {
    		String currRef = elRefs.get(i);
    		String[] currNodeArray = nodeData.get(currRef);
    		w.addUser(currNodeArray[2]); //Add the node's user.
    		theCoords[i][0] = currNodeArray[0]; //nodeLatitude
    		theCoords[i][1] = currNodeArray[1]; //nodeLongitude
    	}
    	return theCoords;
    }
    
    /**
     * Removes duplicate coordinate pairs from the 'wayCoordinates' 2D array.
     * 
     * @param wayCoords a double array of all an array's coordinates
     * which may or may not be unique.
     * @return a double array of all an array's unique coordinates.
     */
    //TODO decide if this is necessary.
    /*
    public String[][] uniqueifyCoordinates(String[][] wayCoords) {
    	HashSet<String[]> keys = new HashSet<String[]>();
    	for (int i = 0; i < wayCoords.length; i++) {
    		keys.add(wayCoords[i]);
    	}
    	return (String[][]) keys.toArray();
    }
    */
}


