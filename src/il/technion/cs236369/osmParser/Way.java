/**
 * 
 */
package il.technion.cs236369.osmParser;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author raphaelas
 *
 */
public class Way {
    private String id;
    private String name;
    private String website;
    private String wiki;
    private int numNodes;
    private HashSet<String> users;   
    private ArrayList<String> elementRefs;
    
    public Way(String i, String n, String we, String wi, int num, HashSet<String> u, ArrayList<String> e) {
    	id = i;
    	name = n;
    	website = we;
    	wiki = wi;
    	numNodes = num;
    	users = new HashSet<String>();
    	elementRefs = new ArrayList<String>();
    	users.addAll(u);
    	elementRefs.addAll(e);
    }

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @return the wiki
	 */
	public String getWiki() {
		return wiki;
	}

	/**
	 * @return the numNodes
	 */
	public int getNumNodes() {
		return numNodes;
	}

	/**
	 * @return the users
	 */
	public HashSet<String> getUsers() {
		return users;
	}

	/**
	 * @return the elementRefs
	 */
	public ArrayList<String> getElementRefs() {
		return elementRefs;
	}
	
	public void addUser(String n) {
		users.add(n);
	}

	public void clearElementRefs() {
		elementRefs.clear();
	}

	public void addAllElementRefs(HashSet<String> hs) {
		elementRefs.addAll(hs);
		elementRefs.trimToSize();
	}
	
	public void incrementNumNodes(int n) {
		numNodes += n;
	}
}
