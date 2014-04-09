package il.technion.cs236369.osmParser;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.json.simple.JSONArray;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * The central OSM Parser class.  The outer class
 * to InnerOSMParser - where the parsing really happens.
 * 
 * @author raphaelas
 *
 */
public class OSMParser implements IOSMParser{
    private XMLReader xmlReader;

	/** Constants used for JAXP 1.2 */
    static final String JAXP_SCHEMA_LANGUAGE =
        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static final String W3C_XML_SCHEMA =
        "http://www.w3.org/2001/XMLSchema";
    static final String JAXP_SCHEMA_SOURCE =
        "http://java.sun.com/xml/jaxp/properties/schemaSource";
    
  /**
   * The central parse method.  Takes the big 2.5 GB OSM file and a
   * class that implements the ITagsRequired interface.
   */
  public JSONArray parse(String osmFile, ITagsRequired tagsRequired) {

	  
        // Create a JAXP SAXParserFactory and configure it
        SAXParserFactory spf = SAXParserFactory.newInstance();

        // Set namespaceAware to true to get a parser that corresponds to
        // the default SAX2 namespace feature setting.  This is necessary
        // because the default value from JAXP 1.0 was defined to be false.
        spf.setNamespaceAware(true);

        // Create a JAXP SAXParser
        SAXParser saxParser = null;
		try {
			saxParser = spf.newSAXParser();
		} catch (Exception e) {
			System.err.println("Exception on create saxParser.");
		}
      
        // Get the encapsulated SAX XMLReader
        xmlReader = null;
		try {
			xmlReader = saxParser.getXMLReader();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		InnerOSMParser theInnerParser = new InnerOSMParser(xmlReader, tagsRequired);
        // Set the ContentHandler of the XMLReader
        xmlReader.setContentHandler(theInnerParser);

        // Tell the XMLReader to parse the XML document
        
        try {
			xmlReader.parse(osmFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
        try {
			xmlReader.parse(osmFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	    return theInnerParser.getJSONArray();
  }
}