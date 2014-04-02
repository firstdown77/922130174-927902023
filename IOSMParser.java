package hw2;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import org.json.simple.JSONArray;
import org.xml.sax.SAXException;

public interface IOSMParser {

	JSONArray parse(String osmFile, ITagsRequired tagsRequired);
	
}
