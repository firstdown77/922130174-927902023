/**
 * 
 */
package hw2;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author raphaelas
 *
 */
public class NodeHandler implements IInnerParse {
	private OSMParser parentParser;
    private XMLReader theReader;

    /**
     * 
     * @param x
     * @param o
     */
	NodeHandler(XMLReader x, OSMParser o) {
		parentParser = o;
		theReader = x;
	}
	
	/* (non-Javadoc)
	 * @see hw2.IInnerParse#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	/**
	 * 
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
    	if (qName.equals("tag")) {
    		String key = attributes.getValue("k");
    		String value = attributes.getValue("v");
    		parentParser.addKV(key, value);
    	}

	}

	/* (non-Javadoc)
	 * @see hw2.IInnerParse#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	/**
	 * 
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
        if (qName.equals("node")) {
        	theReader.setContentHandler(parentParser);
        }
	}

}
