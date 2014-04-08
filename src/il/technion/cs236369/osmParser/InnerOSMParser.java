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
	private HashMap<String, Node> nodeData;
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
    	nodeData = new HashMap<String, Node>();

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
		Node toAdd = new Node(nodeID, nodeLatitude, nodeLongitude, nodeUser);
		nodeData.put(nodeID, toAdd);
	}

	/**
     * Parser calls this once after parsing a document.
     */
    public void endDocument() throws SAXException {
    	if (parseCount == 0) System.out.println("Halfway complete!");
    	else {
    		printJSONArray();
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
    private void printJSONArray() {
    	HashMap<Way, Position[]> positionMap = new HashMap<Way, Position[]>();
    	for (Way w : savedWays) {
    		uniqueifyNodes(w); //Makes elementRefs unique.
    		positionMap.put(w, getWayCoordinates(w));
    	}
    	/*Note: area circle is not being given to JSONArray the way this method
    	is currently implemented.*/
    	JSONArray jArrayToPrint = jArray.printJSONArray(savedWays, positionMap);
    	System.out.println(jArrayToPrint);
    }
    /**
     *  Determines if a recently parsed way is a closed way or not.
     *	A closed way is a way that begins and ends at the same coordinates.
     *
     *  This method does not check if nodes with different ID's might
     *	have the same coordinates - and really be a closed way when
     *  it may seem that it's an open way.
     *  Note: the elementRefs have not been made unique yet.
     */
    private boolean determineClosedWay() {
    	int theSize = elementRefs.size();
    	String firstRef = elementRefs.get(0);
    	String lastRef = elementRefs.get(theSize - 1);
    	if (theSize > 0 &&
    			(firstRef.equals(lastRef))) {
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
    private boolean determineContainsRequiredTags() {
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
  
    /**
     * Removes nodes with duplicate IDs from the elementRefs ArrayList.
     */
    private void uniqueifyNodes(Way w) {
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
    private Position[] getWayCoordinates(Way w) {
    	ArrayList<String> elRefs = w.getElementRefs();
    	Position[] theCoords = new Position[elRefs.size()]; 
    	for (int i = 0; i < elRefs.size(); i++) {
    		String currRef = elRefs.get(i);
    		Node currNode = nodeData.get(currRef);
    		w.addUser(currNode.getUser()); //Add the node's user.
    		double lat = Double.parseDouble(currNode.getLatitude());
    		double lon = Double.parseDouble(currNode.getLongitude());
    		theCoords[i] = new Position(lat, lon);
    	}
    	return theCoords;
    }
}


