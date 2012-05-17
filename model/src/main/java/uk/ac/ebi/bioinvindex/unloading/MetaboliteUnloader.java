package uk.ac.ebi.bioinvindex.unloading;

import org.apache.log4j.Level;

import uk.ac.ebi.bioinvindex.model.Metabolite;
import uk.ac.ebi.bioinvindex.model.MetaboliteSample;


/**
 * Unloads an {@link Metabolite}. 
 * 
 * <p><b>date</b>: May 09, 2012</p>
 * @author pconesa
 *
 */
public class MetaboliteUnloader extends AbstractUnloader<Metabolite>
{
	public MetaboliteUnloader ( UnloadManager unloadManager ) {
		super ( unloadManager );
		unloadManager.timeStamptCheckExclusionList.add(Metabolite.class.getName());
//		logLevel = Level.DEBUG;
	}
	
	public boolean queue ( Metabolite met ) 
	{
		if ( !super.queue( met ) ) return false;

		// We need to use the specific unloader, so call it for every value, this will pass the exact class every
		// value is instance of
		for ( MetaboliteSample metSample: met.getMetaboliteSamples())
			unloadManager.queue(metSample);

	
		return true;
	}
}