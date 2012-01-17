package uk.ac.ebi.bioinvindex.search.hibernatesearch.bridge;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.LuceneOptions;

import uk.ac.ebi.bioinvindex.dao.ejb3.DaoFactory;
import uk.ac.ebi.bioinvindex.model.Annotation;
import uk.ac.ebi.bioinvindex.model.AssayResult;
import uk.ac.ebi.bioinvindex.model.processing.Assay;
import uk.ac.ebi.bioinvindex.model.term.AnnotationTypes;
import uk.ac.ebi.bioinvindex.model.xref.Xref;
import uk.ac.ebi.bioinvindex.utils.datasourceload.DataLocationManager;
import uk.ac.ebi.bioinvindex.utils.processing.ProcessingUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MetaboLightsIndexer {
	
	static Collection<Assay> assays;
	static Document document;
	static LuceneOptions luceneOptions;
	static Collection<String> assayGroupIds = new ArrayList<String>();
	static String dataLocation;
	
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
		assayGroupIds.clear();

		// For each assay (lines associated with one assay)
		for (Assay assay: assays){
			
			// Infer the assayGroup (file) the assay line belongs to
			String assayGroupId = inferAssayGroupId(assay);
			
			// If we haven't indexed it
			if (!assayGroupIds.contains(assayGroupId)){

				// Index the metabolite assignment file associated with the assay.
				indexAssay(assay); //TODO, test

				// Add the assayGroup as indexed
				assayGroupIds.add(assayGroupId);
			}
			
			
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
	/**
	 * Assay are really assay lines. Our index is for the whole group of assay lines that has no correspondence in the model (so far)
	 * This method will return a String representing the assay group (file) the assay line belongs to.
	 * @param assay (This represent an assay line!!!)
	 * @return
	 */
	public static String inferAssayGroupId(Assay assay){
		//TODO: if there are 2 assayGroups (files) within the same study and with the same technology, measurement, and platform.
		return (assay.getTechnologyName() + assay.getMeasurement().getName() + assay.getAssayPlatform() );
		
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
		String fileName = null;
		
		// TODO: how to filter to do this only for metabolights experiments (mass spectrometry could be proteomics as well).
        if (assay.getTechnologyName().equals("mass spectrometry") || assay.getTechnologyName().equals("NMR spectroscopy")) {
            Collection<AssayResult> assayResults = ProcessingUtils.findAssayResultsFromAssay(assay);

            for (AssayResult result : assayResults) {
                for (Annotation annotation : result.getData().getAnnotation("metaboliteFile")) {
                    System.out.printf("Type: %s -> Value: %s\n", annotation.getType().getValue(), annotation.getText());
                    fileName = annotation.getText();
                    break;
                }
                
                if (!fileName.equals("")) break;
            }
        }
		
        // Get the data location where the file is expected to be
        String path = getDataLocation(assay);
        
        if (path == null){
        	return null;
        } else if (fileName==null){
        	return null;
        }else{
        	return path + fileName;
        }

		
		//return  null; //"/Users/kenneth/Development/ISAtab/ISAcreatorWithMetabolitePlugin_BETA/isatab files/Ken1/a_study_metabolite profiling_NMR spectroscopy_maf.csv";
		//return "/Users/conesa/Desktop/untitled folder/firstupload/a_study_metabolite profiling_mass spectrometry_maf.csv";
	}

	private static String getDataLocation(Assay assay){
		
		// If we still do not have a data location
		if (dataLocation == null){
		
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("BIIEntityManager");
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			DataLocationManager dataLocationManager = new DataLocationManager();
			dataLocationManager.setEntityManager(entityManager);
			
            System.out.println("Obfuscation code for study is: " + assay.getStudy().getObfuscationCode());

            String fileLink = dataLocationManager.getDataLocationLink(assay.getMeasurement().getName(), assay.getTechnologyName(), assay.getStudy().getObfuscationCode(),
                    AnnotationTypes.GENERIC_DATA_FILE_LINK);

            System.out.println("File link: " + fileLink);

            String pathLink = dataLocationManager.getDataLocationLink(assay.getMeasurement().getName(), assay.getTechnologyName(), assay.getStudy().getObfuscationCode(),
                    AnnotationTypes.GENERIC_DATA_FILE_PATH);

            System.out.println("Path link: " + pathLink);
            
            dataLocation = pathLink;
		
		}
		
		return dataLocation;
		
		//return "/Users/conesa/Desktop/untitled folder/firstupload/";



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

