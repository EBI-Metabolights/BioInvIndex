package uk.ac.ebi.bioinvindex.search.hibernatesearch.bridge;

import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.*;


public class MetaboLightsIndexerTest {
	@Test
	public void testlineToArray(){
		
		String line = "";
		String[] lineArray;
		
		lineArray = MetaboLightsIndexer.lineToArray(line);
		
		assertEquals("Testing empty line count", 1, lineArray.length);
		assertEquals("Testing empty line content", "", lineArray[0]);
		
		line = "\"one\"";
		lineArray = MetaboLightsIndexer.lineToArray(line);
		
		assertEquals("Testing one item line count", 1, lineArray.length);
		assertEquals("Testing one item line content", "one", lineArray[0]);
		
		line = "\"one\"\t\"two\"";
		lineArray = MetaboLightsIndexer.lineToArray(line);
		
		assertEquals("Testing two item line count", 2, lineArray.length);
		assertEquals("Testing two item line 1st element", "one", lineArray[0]);
		assertEquals("Testing two item line 2nd element", "two", lineArray[1]);
		
		
		line = "\"one\"\t\"two\"\t\"\"";
		lineArray = MetaboLightsIndexer.lineToArray(line);
		
		assertEquals("Testing 3 item line count", 3, lineArray.length);
		assertEquals("Testing 3 item line 1st element", "one", lineArray[0]);
		assertEquals("Testing 3 item line 2nd element", "two", lineArray[1]);
		assertEquals("Testing 3 item line 3rd element", "", lineArray[2]);
		
		
	}
	@Test
	public void testgetIndexedFields(){
		
		String file = "src/test/resources/testdata/MetabolightsSample.csv";
		
		HashMap<String,StringBuilder> indexedFields = MetaboLightsIndexer.getIndexedFields(file);
		
		
		assertEquals("Field Indexed must be 2, identifier and description.", 2, indexedFields.entrySet().size());
		assertEquals("Identifier field values", "~CHEBI:44843~CHEBI:44841~LMGP01010629~HMDB07991", indexedFields.get("identifier").toString());
		assertEquals("Description field values", "~PC(16:0/16:0)~PC(16:0/16:0)~PC(16:0/18:1)~PC(16:0/18:2)~PC(16:0/20:3)/PC(18:2/18:1)~PC(16:0/20:4)~PC(18:2/16:0)", indexedFields.get("description").toString());
		
		
		
		
		
	}

}
