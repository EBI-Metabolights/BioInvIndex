package uk.ac.ebi.bioinvindex.search.hibernatesearch.bridge;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.LuceneOptions;
import uk.ac.ebi.bioinvindex.model.Annotation;
import uk.ac.ebi.bioinvindex.model.processing.Assay;
import uk.ac.ebi.bioinvindex.model.xref.Xref;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

public class MetaboLightsIndexer {
	
	static Collection<Assay> assays;
	static Document document;
	static LuceneOptions luceneOptions;
	
	static final String[] COLUMNS_TO_INDEX = {"description","identifier"};
	// Method called from:
	// AssayBridge.set() (at the bottom, currenlty line: 123)
	// 
	//          // Index Metabolite files
    //			MetaboLightsIndexer.index(assays, document, luceneOptions);
	public static void index( Collection<Assay> assays, Document document, LuceneOptions luceneOptions){
		
		MetaboLightsIndexer.assays = assays;
		MetaboLightsIndexer.document = document;
		MetaboLightsIndexer.luceneOptions = luceneOptions;

		// For each assay (lines associated with one assay)
		for (Assay assay: assays){
			
			// Index the metabolite assignment file associated with the assay.
			// TODO, Note that we don't have to loop through all assay rows, so we can probably skip out after the first metabolite file has been found
			if (indexAssay(assay)) break; //TODO, test
			
		}
		
	}

	public static Boolean indexAssay(Assay assay){

		// Check if the assay has a metabolites identifier file
		String metabolitesFileS = getIdentifierFile(assay);
		
		// If there is no metabolite file...
		if (metabolitesFileS == null) return false;

		// Get the indexed fields with the correspondent values
		HashMap<String, StringBuilder> indexedFields = getIndexedFields(metabolitesFileS);
				
		// For each field indexed
		for (Entry<String,StringBuilder> fld: indexedFields.entrySet()){
			// Index the field
			Field fvField = new Field("Metabolites_" + fld.getKey(), fld.getValue().toString(), luceneOptions.getStore(), luceneOptions.getIndex());
	        document.add(fvField);
		}

        return true;
		
	}

	public static HashMap<String,StringBuilder> getIndexedFields(String filename){
		
		// Create a Hashmap with the index of the columns we want to index (
		HashMap<String,Integer> column_indexes = new HashMap<String,Integer>();
		
		// Hash with the field as the key and the values (Separated by ~) as values.
		HashMap <String, StringBuilder> indexedFields = new HashMap<String,StringBuilder>();
		
		// For each field to index
		for (int i =0;i < COLUMNS_TO_INDEX.length; i++){
			column_indexes.put(COLUMNS_TO_INDEX[i], null);
			indexedFields.put(COLUMNS_TO_INDEX[i], new StringBuilder(""));
		}

		// Read the first line
		try {
			
			// Get the index based on the first row
			File file = new File(filename);
			
			// Open the file
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String line = "";
			int linecount =1;
			
			//Go through the file
			while((line = reader.readLine()) != null)
			{
				
				String[] lineArray = lineToArray(line);
				
				// Get the indexes for the columns descriptions
				if (linecount == 1){
					
					// For each field in the line
					for (int i =0;i < lineArray.length; i++){
						
						// Get the field
						String field = lineArray[i];
						
						// Check if it is in the hash
						if (column_indexes.containsKey(field)){
							
							// Add the index
							column_indexes.put(field, i);
						}
					}
					
				}else{
					
					// Get the values
					// For each column to index
					for (Entry<String,Integer> ci:column_indexes.entrySet()){
						
						// Get the value
						String value = lineArray[ci.getValue()];
						
						// If value is NOT empty
						if (!value.isEmpty()){
							
							// Get the previous value
							StringBuilder previousValue = indexedFields.get(ci.getKey());
							
							// Add the new value
							previousValue.append("~" + value) ;
							
						}
					}
				}
				
				
				linecount++;
			}
			
			//Close the reader
			reader.close();
			
			
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		
		// Return indexed fields
		return indexedFields;
		
	}

	public static String getIdentifierFile(Assay assay){
		// TODO: how to know this??
		
		// Look up inside Xref collection
		Collection<Xref> xrefs = assay.getXrefs();

        String fileName = null;

        for(Annotation annotation : assay.getAnnotation("Metabolite Assignment File")) {
            fileName = annotation.getText().replaceAll(".txt","_maf.csv");
            System.out.println("Annotation found:"+ annotation.getText());
        }
		
		for (Xref xref:xrefs){

			System.out.println("xref found: getAcc():" + xref.getAcc() + ", getId():" + xref.getId() + ", suorce:" + xref.getSource().toString());
		}
		
		return fileName; //"/Users/kenneth/Development/ISAtab/ISAcreatorWithMetabolitePlugin_BETA/isatab files/Ken1/a_study_metabolite profiling_NMR spectroscopy_maf.csv";
		
	}

	public static String[] lineToArray(String line){
		
		if (line.length()>0){
			// Remove the first double quote and the last one
			line = line.substring(1,line.length());
			line = line.substring(0, line.length()-1);
		}
		
		// Add -1 to get empty strings.
		//return line.split("[^\"|\"\t\"|\"$]", -1);
		return line.split("\"\t\"", -1);
	}

}

