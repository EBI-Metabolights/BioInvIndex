package uk.ac.ebi.bioinvindex.persistence;

import java.sql.Timestamp;

import uk.ac.ebi.bioinvindex.dao.ejb3.DaoFactory;
import uk.ac.ebi.bioinvindex.model.AssayGroup;
import uk.ac.ebi.bioinvindex.model.Metabolite;

public class MetPersister extends Persister<Metabolite>{

	public MetPersister ( DaoFactory daoFactory, Timestamp submissionTs ) 
	{
		super ( daoFactory, submissionTs );
		dao = daoFactory.getIdentifiableDAO ( Metabolite.class );
	}

	
	/**
	 * It's always new. Returns always the parameter.
	 * Does few checkings (eg: that the linked assays are already saved).
	 * 
	 */
	@Override
	public void preProcess ( Metabolite met ) 
	{
		// The ancestor works with the accession
		super.preProcess ( met );
	}


	
	/** Returns null, an assay result is always new. */
	@Override
	protected Metabolite lookup ( Metabolite met ) {
		return null;
	}

}
