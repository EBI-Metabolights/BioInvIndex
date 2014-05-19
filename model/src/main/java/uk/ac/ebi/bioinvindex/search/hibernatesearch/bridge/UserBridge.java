package uk.ac.ebi.bioinvindex.search.hibernatesearch.bridge;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import uk.ac.ebi.bioinvindex.model.security.Person;
import uk.ac.ebi.bioinvindex.model.security.User;

import java.util.Collection;


public class UserBridge extends IndexFieldDelimiters implements FieldBridge {


    public void set(String s, Object o, Document document, LuceneOptions luceneOptions) {
        if (o == null || document == null || luceneOptions == null) return;

        System.out.println("_________UserBridge.set__________\n");

        Collection<User> users = (Collection<User>) o;

        try {
            for (User user : users) {

                Person p = (Person) user;

                if (p != null) {

                    String representation = buildRepresentationForIndex("username:" + p.getUserName(),
                            "forename:" + p.getFirstName(), "surname:" + p.getLastName(), "email:" + user.getEmail());

                    Field fvField =
                            new Field("user", representation, luceneOptions.getStore(), luceneOptions.getIndex());

                    document.add(fvField);

                }

            }
        } catch (Exception e){
            System.out.println("ERROR: UserBridge.set caused a NPE, ignoring\n");
            return;
        }
    }
}
