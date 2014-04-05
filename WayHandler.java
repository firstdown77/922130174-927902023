/**
 * 
 */
package hw2;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

//import org.xml.sax.Attributes;
//import org.xml.sax.SAXException;

/**
 * TODO: Dealing with relations.
 * 
 * @author raphaelas
 *
 */
public class WayHandler extends DefaultHandler implements IInnerParse {
	//This class handles parsing of Way child nodes.
	private OSMParser parentParser;
    private XMLReader theReader;

    /**
     * 
     * @param x
     * @param o
     */
	WayHandler(XMLReader x, OSMParser o) {
		parentParser = o;
		theReader = x;
	}
	
	/**
	 * 
	 */
    public void startElement(String uri, String localName, String qName,
    		Attributes attributes) throws SAXException {
    	if (qName.equals("tag")) {
    		String key = attributes.getValue("k");
    		String value = attributes.getValue("v");
    		parentParser.addWayTag(key, value);
    	}
    	else if (qName.equals("nd")) {
	    	String refValue = attributes.getValue("ref");
	    	parentParser.addRef(refValue);
    	}
    }
    
    /**
     * 
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
    	if (qName.equals("way")) {
        	parentParser.determineClosedWay();
            theReader.setContentHandler(parentParser);
        }
    }
}
