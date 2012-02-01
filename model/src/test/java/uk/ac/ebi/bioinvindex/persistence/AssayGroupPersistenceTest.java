package uk.ac.ebi.bioinvindex.persistence;

/*
 * __________
 * CREDITS
 * __________
 *
 * Team page: http://isatab.sf.net/
 * - Marco Brandizi (software engineer: ISAvalidator, ISAconverter, BII data management utility, BII model)
 * - Eamonn Maguire (software engineer: ISAcreator, ISAcreator configurator, ISAvalidator, ISAconverter,  BII data management utility, BII web)
 * - Nataliya Sklyar (software engineer: BII web application, BII model,  BII data management utility)
 * - Philippe Rocca-Serra (technical coordinator: user requirements and standards compliance for ISA software, ISA-tab format specification, BII model, ISAcreator wizard, ontology)
 * - Susanna-Assunta Sansone (coordinator: ISA infrastructure design, standards compliance, ISA-tab format specification, BII model, funds raising)
 *
 * Contributors:
 * - Manon Delahaye (ISA team trainee:  BII web services)
 * - Richard Evans (ISA team trainee: rISAtab)
 *
 *
 * ______________________
 * Contacts and Feedback:
 * ______________________
 *
 * Project overview: http://isatab.sourceforge.net/
 *
 * To follow general discussion: isatab-devel@list.sourceforge.net
 * To contact the developers: isatools@googlegroups.com
 *
 * To report bugs: http://sourceforge.net/tracker/?group_id=215183&atid=1032649
 * To request enhancements:  http://sourceforge.net/tracker/?group_id=215183&atid=1032652
 *
 *
 * __________
 * License:
 * __________
 *
 * This work is licenced under the Creative Commons Attribution-Share Alike 2.0 UK: England & Wales License. To view a copy of this licence, visit http://creativecommons.org/licenses/by-sa/2.0/uk/ or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California 94105, USA.
 *
 * __________
 * Sponsors
 * __________
 * This work has been funded mainly by the EU Carcinogenomics (http://www.carcinogenomics.eu) [PL 037712] and in part by the
 * EU NuGO [NoE 503630](http://www.nugo.org/everyone) projects and in part by EMBL-EBI.
 */

import static java.lang.System.out;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Collection;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.bioinvindex.dao.ejb3.DaoFactory;
import uk.ac.ebi.bioinvindex.model.AssayGroup;
import uk.ac.ebi.bioinvindex.model.MZTab;
import uk.ac.ebi.bioinvindex.model.Study;
import uk.ac.ebi.bioinvindex.model.term.Design;
import uk.ac.ebi.bioinvindex.utils.test.TransactionalDBUnitEJB3DAOTest;

/*
 * To test with maven:
 * mvn clean test -Dtest=AssayGroupPersistenceTest -Pindex,h2,index_local -DargLine="-Xms128m -Xmx512m" -e -DfailIfNoTests=false
 * change oracle to whatever profile id you wish to test.
 */

public class AssayGroupPersistenceTest extends TransactionalDBUnitEJB3DAOTest
{
	private StudyPersister persister;


	public AssayGroupPersistenceTest() throws Exception {
		super();
		persister = new StudyPersister ( DaoFactory.getInstance ( entityManager ), new Timestamp ( System.currentTimeMillis () ) );
	}

	@Before
	public void initPersister ()
	{
		// Needs to be instantiated here, so that the internal cache for the ontology terms is cleared
		// before every test.
		//
		persister = new StudyPersister ( DaoFactory.getInstance ( entityManager ), new Timestamp ( System.currentTimeMillis () ) );
	}

	protected void prepareSettings() {
		beforeTestOperations.add(DatabaseOperation.CLEAN_INSERT);
		dataSetLocation = "test_AssayGroupPersistance.xml";
	}

	@Test
	public void testPersistMetabolites () throws Exception
	{

		out.println ( "\n\n\n _______________  testPersistMetabolites, ____________________________\n" );

		Study study = new Study ( "Testing MetaboLights Entities" );
		
		AssayGroup ag1 = new AssayGroup(study, "AssayGroup1.txt");
		MZTab mzTab1 = new MZTab(ag1,"water", "CHEBI:15377");
		ag1.getMzTabs().add(mzTab1);
		
		AssayGroup ag2 = new AssayGroup(study, "AssayGroup2.txt");
		
		study.getAssayGroups().add(ag1);
		study.getAssayGroups().add(ag2);
		
		// Let's go with the persistence job!
		//

		Study studyDB = persister.persist ( study );
		transaction.commit ();
		
		assertNotNull ( "Oh! No study returned by the persister!", studyDB );

		
//	
		out.println ( "\n\nPersisted study: " + studyDB );
		assertNotNull ( "Oh! No study returned by the persister!", studyDB );
		assertEquals ( "Ouch! The study rerturned by the persister should be the original one!", study, studyDB );
		assertTrue ( "Argh! Cannot find the persisted study in the DB!", entityManager.contains ( studyDB ) );
		assertNotNull ( "Urp! The study should have an ID", studyDB.getId () );

		// Test the assaygroup 
		Collection<AssayGroup> assayGroupsDB = studyDB.getAssayGroups();
		assertNotNull (  "Ops! The retuned study has no an assayGroup!", assayGroupsDB );
		assertEquals ( "Oh no! Bad no. of assayGroups persisted!", 2, assayGroupsDB.size () );
		for ( AssayGroup assayGroupDB: assayGroupsDB )
			assertNotNull ( "Urp! The study assay group should have an ID", assayGroupDB.getId () );
		
		

		
		
		
		
		
		out.println ( "\n\n\n _______________  /end: testPersistMetabolites, Testing new Study  ____________________________\n" );

	}

}
