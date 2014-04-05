package hw2;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


public interface IInnerParse {
	/**
	 * 
	 * @param uri extended from DefaultHandler
	 * @param localName extended from DefaultHandler
	 * @param qName extended from DefaultHandler
	 * @param attributes extended from DefaultHandler
	 */
	void startElement(String uri, String localName, String qName,
    		Attributes attributes) throws SAXException;
	
	void endElement(String uri, String localName, String qName) throws SAXException;;
}
