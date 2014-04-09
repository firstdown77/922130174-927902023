/**
 * 
 */
package il.technion.cs236369.osmParser;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * The Way class - containing information about Ways that
 * are needed in various methods.
 * 
 * @author raphaelas
 */
public class Way {
    private String id;
    private String name;
    private String website;
    private String wiki;
    private int numNodes;
    private HashSet<String> users;   
    private ArrayList<String> elementRefs;
    
    /**
     * 
     * @param i A String form of the Way's ID number.
     * @param n The Way's name - usually null.
     * @param we The Way's website - usually null.
     * @param wi The Way's wikipedia entry - usually null.
     * @param u The Way's users - a HashSet in order to eliminate duplicates.
     * @param e The Way's Node references - an ArrayList for easy access
     * of the first and last Nodes added to the Way - useful when determining
     * if the Way is a Closed Way.
     */
    public Way(String i, String n, String we, String wi, HashSet<String> u, ArrayList<String> e) {
    	id = i;
    	name = n;
    	website = we;
    	wiki = wi;
    	numNodes = 0;
    	users = new HashSet<String>();
    	elementRefs = new ArrayList<String>();
    	users.addAll(u);
    	elementRefs.addAll(e);
    }

	/**
	 * Way ID getter.
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Name getter.  Usually returns null.
	 * @return the name - may be null.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Website getter.  Usually returns null.
	 * @return the website - may be null.
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * Wikipedia getter.  Usually returns null.
	 * @return the wiki - may be null.
	 */
	public String getWiki() {
		return wiki;
	}

	/**
	 * Gets the number of unique nodes belonging to the Way.
	 * @return the numNodes - the quantity of this Way's unique Nodes.
	 */
	public int getNumNodes() {
		return numNodes;
	}

	/**
	 * Users HashSet getter.  HashSet is used for easy filtering
	 * of duplicate users.
	 * @return the users - a HashSet of unique users contributing
	 * to the Way.
	 */
	public HashSet<String> getUsers() {
		return users;
	}

	/**
	 * ElementRefs are this Way's references to Nodes.
	 * @return the elementRefs - the Way's references to Nodes.
	 */
	public ArrayList<String> getElementRefs() {
		return elementRefs;
	}
	
	/**
	 * Adds a user to the Way's HashSet of users.
	 * @param u - A new user's name to add to this Way's users.
	 */
	public void addUser(String u) {
		users.add(u);
	}

	/**
	 * Clears the array containing all of the Way's Nodes.
	 * This is called during the process of filtering out
	 * duplicate Node references.
	 */
	public void clearElementRefs() {
		elementRefs.clear();
	}

	/**
	 * Re-populates the elementRefs array during the process
	 * of filtering out duplicate Node references.
	 * 
	 * @param hs A HashSet of all the Way's unique Node references.
	 */
	public void addAllElementRefs(HashSet<String> hs) {
		elementRefs.addAll(hs);
		elementRefs.trimToSize();
	}
	
	/**
	 * Add to the counter of this Way's unique Node references.
	 * 
	 * @param n Number of Nodes to increment the numNodes
	 * counter by.
	 */
	public void incrementNumNodes(int n) {
		numNodes += n;
	}
}
