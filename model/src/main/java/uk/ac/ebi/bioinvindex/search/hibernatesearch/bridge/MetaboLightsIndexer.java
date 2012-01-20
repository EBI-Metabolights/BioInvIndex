package uk.ac.ebi.bioinvindex.search.hibernatesearch.bridge;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.LuceneOptions;
import uk.ac.ebi.bioinvindex.model.processing.Assay;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class MetaboLightsIndexer {

    static final String[] COLUMNS_TO_INDEX = {"description", "identifier"};

    public static Boolean indexMetaboliteFile(String metaboliteFile, Document document, LuceneOptions luceneOptions) {

        System.out.println("Looking to index " + metaboliteFile);
        // If there is no metabolite file...
        if (metaboliteFile == null) return false;
        if (!new File(metaboliteFile).exists()) return false;

        // Get the indexed fields with the correspondent values
        ArrayList<String> metabolites = getIndexedFields(metaboliteFile);

        // For each field indexed
        for (String metabolite: metabolites) {
            // Index the field
            Field fvField = new Field("Metabolite", metabolite, luceneOptions.getStore(), luceneOptions.getIndex());
            document.add(fvField);
        }

        return true;

    }

    public static ArrayList<String> getIndexedFields(String filename) {

        // Create a Hashmap with the index of the columns we want to index (
        HashMap<String, Integer> column_indexes = new HashMap<String, Integer>();
        
        // Hash with the metabolites found
        ArrayList<String> metabolites = new ArrayList<String>();

        // For each field to index
        for (String aCOLUMNS_TO_INDEX : COLUMNS_TO_INDEX) {
            column_indexes.put(aCOLUMNS_TO_INDEX, null);
        }

        // Read the first line
        try {

            // Get the index based on the first row
            File file = new File(filename);

            // Open the file
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = "";
            int linecount = 1;

            //Go through the file
            while ((line = reader.readLine()) != null) {

                String[] lineArray = lineToArray(line);

                // Get the indexes for the columns descriptions
                if (linecount == 1) {

                    // For each field in the line
                    for (int i = 0; i < lineArray.length; i++) {

                        // Get the field
                        String field = lineArray[i];

                        // Check if it is in the hash
                        if (column_indexes.containsKey(field)) {

                            // Add the index
                            column_indexes.put(field, i);
                            
                        }
                    }

                } else {

                	String metabolite = "";
                	boolean isRowEmpty = true;

                	// Get the values
                    // For each column to index
                    for (Entry<String, Integer> ci : column_indexes.entrySet()) {

                    	// Add the name of the field
                    	//metabolite = metabolite.concat("~" + ci.getKey());
                    	
                        // Get the value
                        String value = lineArray[ci.getValue()];
                        
                        // If value is NOT empty
                        if (!value.isEmpty())  isRowEmpty = false;
                        	
                        metabolite = metabolite.concat("~" + value );

                        
                    }
                    
                    // If we have found values add the string to the metabolites array
                    if (!isRowEmpty) metabolites.add(metabolite);
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
        //return indexedFields;
        return metabolites;

    }


    public static String[] lineToArray(String line) {

        if (line.length() > 0) {
            // Remove the first double quote and the last one
            line = line.substring(1, line.length());
            line = line.substring(0, line.length() - 1);
        }

        // Add -1 to get empty strings.
        //return line.split("[^\"|\"\t\"|\"$]", -1);
        return line.split("\"\t\"", -1);
    }

}