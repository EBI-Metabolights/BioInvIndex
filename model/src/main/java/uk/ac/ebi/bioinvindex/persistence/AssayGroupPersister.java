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

import java.sql.Timestamp;
import java.util.LinkedList;

import uk.ac.ebi.bioinvindex.dao.AccessibleDAO;
import uk.ac.ebi.bioinvindex.dao.ejb3.DaoFactory;
import uk.ac.ebi.bioinvindex.model.AssayGroup;
import uk.ac.ebi.bioinvindex.model.Data;
import uk.ac.ebi.bioinvindex.model.Metabolite;
import uk.ac.ebi.bioinvindex.persistence.pipeline.DataPersister;

/**
 * Persists an {@link }.
 * 
 * date: Jan 26, 2012
 * @author conesa
 *
 */
public class AssayGroupPersister extends Persister<AssayGroup>{
	
	private final MetPersister metPersister;
	
	public AssayGroupPersister ( DaoFactory daoFactory, Timestamp submissionTs ) 
	{
		super ( daoFactory, submissionTs );
		dao = daoFactory.getIdentifiableDAO ( AssayGroup.class );
		
		metPersister = new MetPersister(daoFactory, submissionTs);
	}

	
	/**
	 * It's always new. Returns always the parameter.
	 * Does few checkings (eg: that the linked assays are already saved).
	 * 
	 */
	@Override
	public void preProcess ( AssayGroup ag ) 
	{
		// The ancestor works with the accession
		super.preProcess ( ag );
	}

	@Override
	public void postProcess(AssayGroup ag){
		
		// Persist mzTab Information
		for (Metabolite mzTab:ag.getMetabolites()) metPersister.persist(mzTab);
	}

	
	/** Returns null, an assay result is always new. */
	@Override
	protected AssayGroup lookup ( AssayGroup ag ) {
		return null;
	}


}
