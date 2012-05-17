package uk.ac.ebi.bioinvindex.unloading;

import org.apache.log4j.Level;

import uk.ac.ebi.bioinvindex.model.Metabolite;
import uk.ac.ebi.bioinvindex.model.MetaboliteSample;


/**
 * Unloads an {@link MetaboliteSample}. 
 * 
 * <p><b>date</b>: May 09, 2012</p>
 * @author pconesa
 *
 */
public class MetaboliteSampleUnloader extends AbstractUnloader<MetaboliteSample>
{
	public MetaboliteSampleUnloader ( UnloadManager unloadManager ) {
		super ( unloadManager );
		unloadManager.timeStamptCheckExclusionList.add(MetaboliteSample.class.getName());
//		logLevel = Level.DEBUG;
	}
	
	public boolean queue ( MetaboliteSample metSample ) 
	{
		if ( !super.queue ( metSample ) ) return false;

	
		return true;
	}
}