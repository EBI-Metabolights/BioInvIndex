package uk.ac.ebi.bioinvindex.persistence;

import java.sql.Timestamp;

import uk.ac.ebi.bioinvindex.dao.ejb3.DaoFactory;
import uk.ac.ebi.bioinvindex.model.AssayGroup;
import uk.ac.ebi.bioinvindex.model.MZTab;

public class MZTabPersister extends Persister<MZTab>{

	public MZTabPersister ( DaoFactory daoFactory, Timestamp submissionTs ) 
	{
		super ( daoFactory, submissionTs );
		dao = daoFactory.getIdentifiableDAO ( MZTab.class );
	}

	
	/**
	 * It's always new. Returns always the parameter.
	 * Does few checkings (eg: that the linked assays are already saved).
	 * 
	 */
	@Override
	public void preProcess ( MZTab mzTab ) 
	{
		// The ancestor works with the accession
		super.preProcess ( mzTab );
	}


	
	/** Returns null, an assay result is always new. */
	@Override
	protected MZTab lookup ( MZTab mzTab ) {
		return null;
	}

}
