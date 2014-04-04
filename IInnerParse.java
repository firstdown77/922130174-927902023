package hw2;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


public interface IInnerParse {
	/**
	 * 
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param attributes
	 */
	void startElement(String uri, String localName, String qName,
    		Attributes attributes) throws SAXException;
	
	void endElement(String uri, String localName, String qName) throws SAXException;;
}
