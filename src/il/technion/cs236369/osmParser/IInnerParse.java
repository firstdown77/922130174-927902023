package il.technion.cs236369.osmParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Interface for parsing of child nodes.
 * 
 * @author raphaelas
 */
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
	
	/**
	 * 
	 * @param uri extended from DefaultHandler
	 * @param localName extended from DefaultHandler
	 * @param qName extended from DefaultHandler
	 * @throws SAXException
	 */
	void endElement(String uri, String localName, String qName) throws SAXException;;
}
