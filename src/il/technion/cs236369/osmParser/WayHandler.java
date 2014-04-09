package il.technion.cs236369.osmParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A class to parse a Way's child nodes, where its Node
 * References and Tags are found.
 * 
 * @author raphaelas
 *
 */
public class WayHandler extends DefaultHandler implements IInnerParse {
	//This class handles parsing of Way child nodes.
	private InnerOSMParser parentParser;
    private XMLReader theReader;

    /**
     * Constructor.
     * 
     * @param x The XMLReader class - where ContentHandlers are changed.
     * @param o The InnerOSMParser from which this class is called upon.
     */
	WayHandler(XMLReader x, InnerOSMParser o) {
		parentParser = o;
		theReader = x;
	}
	
	/**
	 * Parser calls this for each element in the document.
	 * Evaluates a Way's child elements - tags and nd refs -
	 * and calls the appropriate methods in InnerOSMParser
	 * to evaluate the Way's tags and Node references.
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
     * Parser calls this for each element in the document.
     * Resets parser's ContentHandler.
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
    	if (qName.equals("way")) {
        	parentParser.determineIfShouldPrint();
            theReader.setContentHandler(parentParser);
        }
    }
}
