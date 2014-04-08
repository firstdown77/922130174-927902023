package il.technion.cs236369.osmParser;

import java.io.PrintStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.json.simple.JSONArray;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;


/**
 * The central OSM Parser class.
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
	 * Default constructor.  Called in main function.
	 */
	public OSMParser() {
		
	}
    
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

        // Set the ContentHandler of the XMLReader
        xmlReader.setContentHandler(new InnerOSMParser(xmlReader, tagsRequired));

        // Set an ErrorHandler before parsing
        xmlReader.setErrorHandler(new MyErrorHandler(System.err));

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
        
	    return null;
  }
  
  /**
   *  Error handler to report errors and warnings
   *
   */
  private static class MyErrorHandler implements ErrorHandler {
      /** Error handler output goes here */
      private PrintStream out;

      MyErrorHandler(PrintStream out) {
          this.out = out;
      }

      /**
       * Returns a string describing parse exception details
       */
      private String getParseExceptionInfo(SAXParseException spe) {
          String systemId = spe.getSystemId();
          if (systemId == null) {
              systemId = "null";
          }
          String info = "URI=" + systemId +
              " Line=" + spe.getLineNumber() +
              ": " + spe.getMessage();
          return info;
      }

      // The following methods are standard SAX ErrorHandler methods.
      // See SAX documentation for more info.
      
      public void warning(SAXParseException spe) throws SAXException {
          out.println("Warning: " + getParseExceptionInfo(spe));
      }
      
      public void error(SAXParseException spe) throws SAXException {
          String message = "Error: " + getParseExceptionInfo(spe);
          throw new SAXException(message);
      }

      public void fatalError(SAXParseException spe) throws SAXException {
          String message = "Fatal Error: " + getParseExceptionInfo(spe);
          throw new SAXException(message);
      }
  }

}