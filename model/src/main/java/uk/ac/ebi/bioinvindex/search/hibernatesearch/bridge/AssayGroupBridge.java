package uk.ac.ebi.bioinvindex.search.hibernatesearch.bridge;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import uk.ac.ebi.bioinvindex.model.AssayGroup;
import uk.ac.ebi.bioinvindex.model.Metabolite;

import java.util.Collection;


public class AssayGroupBridge extends IndexFieldDelimiters implements FieldBridge {


    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {

    	System.out.println("_________AssayGroupBridge.set__________\n");
    	
    	
    	System.out.println("o object has: " + o);
    	
    	if (o == null) return;

        Collection<AssayGroup> ags = (Collection<AssayGroup>) o;

        // For each AssayGroup in the collection
        for (AssayGroup ag : ags) {

                String representation = ag.getFileName();

                System.out.println("Indexing AssayGroup: " + representation);
                
                Field fvField =
                        new Field("assayGroup", representation, luceneOptions.getStore(), luceneOptions.getIndex());

                document.add(fvField);
                
                indexMetabolites(ag, document, luceneOptions);
            
        }
    }

    private void indexMetabolites(AssayGroup ag, Document doc, LuceneOptions luceneOptions){
    	
    	// Go through the list of metabolites
    	for (Metabolite met: ag.getMetabolites()){

            String metabolite = met.getCleanDescription();
    		//String representation = met.getDescription() + "~" + met.getIdentifier();
            String representation = metabolite + "~" + met.getIdentifier();

            //Ken 20121203, should not be required anymore as this is "cleaned" in the Metabolite class
            //Skip if submitter has listed more than one metabolite per line ("/", "?", " or ")
            /*if (!metabolite.contains("?")
                    && !metabolite.contains("/")
                    && !metabolite.contains("|")
                    && !metabolite.contains("+")
                    && !metabolite.contains(", ")
                    && !metabolite.contains("_")
                    && !metabolite.contains("- ") && !metabolite.contains(" -")
                    && !metabolite.contains("unknown") && !metabolite.contains("unidentified") && !metabolite.contains("unassiged") && !metabolite.contains("unk-")
                    && !metabolite.contains("#")
                    && !metabolite.contains(" or ") && !metabolite.contains(" and ")
                    ){
              */

                System.out.println("Indexing Metabolite: " + representation);


                Field fvField =
                    new Field("Metabolite", representation, luceneOptions.getStore(), luceneOptions.getIndex());

                doc.add(fvField);
            //}
    
    	}
    	
    }
}