package uk.ac.ebi.bioinvindex.search.hibernatesearch.bridge;

import java.util.ArrayList;
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
		
		ArrayList<String> metabolites = MetaboLightsIndexer.getIndexedFields(file);
		
		
		assertEquals("Metabolites Indexed must be 8.", 8, metabolites.size());
		
		assertEquals("1 metabolite test", "~PC(16:0/16:0)~CHEBI:44843", metabolites.get(0));
		assertEquals("2 metabolite test", "~PC(16:0/16:0)~CHEBI:44841", metabolites.get(1));
		assertEquals("3 metabolite test", "~PC(16:0/18:1)~", metabolites.get(2));
		assertEquals("4 metabolite test", "~PC(16:0/18:2)~", metabolites.get(3));
		assertEquals("5 metabolite test", "~PC(16:0/20:3)/PC(18:2/18:1)~", metabolites.get(4));
		assertEquals("6 metabolite test", "~PC(16:0/20:4)~LMGP01010629", metabolites.get(5));
		assertEquals("7 metabolite test", "~~HMDB07991", metabolites.get(6));
		assertEquals("8 metabolite test", "~PC(18:2/16:0)~", metabolites.get(7));
//		assertEquals("Identifier field values", "~~", indexedFields.get("identifier").toString());
//		assertEquals("Description field values", "~~~~~~~", indexedFields.get("description").toString());
		
		
		
		
		
	}

}
