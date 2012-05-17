package uk.ac.ebi.bioinvindex.unloading;

import org.apache.log4j.Level;

import uk.ac.ebi.bioinvindex.model.AssayGroup;
import uk.ac.ebi.bioinvindex.model.Metabolite;


/**
 * Unloads an {@link AssayGroup}. 
 * 
 * <p><b>date</b>: May 09, 2012</p>
 * @author pconesa
 *
 */
public class AssayGroupUnloader extends AbstractUnloader<AssayGroup>
{
	public AssayGroupUnloader ( UnloadManager unloadManager ) {
		super ( unloadManager );
		unloadManager.timeStamptCheckExclusionList.add(AssayGroup.class.getName());
//		logLevel = Level.DEBUG;
	}
	
	public boolean queue ( AssayGroup ag ) 
	{
		if ( !super.queue ( ag ) ) return false;

		// We need to use the specific unloader, so call it for every value, this will pass the exact class every
		// value is instance of
		for ( Metabolite met: ag.getMetabolites() )
			unloadManager.queue ( met );

		
		return true;
	}
}