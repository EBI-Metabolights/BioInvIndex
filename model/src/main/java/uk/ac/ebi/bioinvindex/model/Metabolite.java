package uk.ac.ebi.bioinvindex.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import org.hibernate.search.annotations.*;

@Entity
@Table(name = "METABOLITE")
@Indexed(index = "bii")
public class Metabolite extends Identifiable{

	private Collection<MetaboliteSample> msMetSamples = new ArrayList<MetaboliteSample>();
	
	private String description; //	NMR/MS: The small molecule's description/name.  Multiple values separated with | 
	private String identifier; //A list of | separated possible identifiers for these small molecules. e.g. "KEGG:C000017|CHEBI:12345"
	private String unit_id; // NMR/MS: The concatenation of the sample id plus assay id to uniquely identify that sample and run on the machine(or mass concatenated with reten tion index for unknowns)
	private String chemical_formula; // NMR/MS: The chemical formula of the identified compound
	private String chemical_shift; // NMR: Chemical shift
	private String multiplicity; // NMR: Type of multiplets observed of the metabolite (e.g s, d, t, q, dd, ...)
	private String mass_to_charge; // MS: The precursor's experimental mass to charge (m/z)
	private String fragmentation; // Fragmentation
	private String charge; // MS: The precursor's charge
	private String retention_time; // MS: Small molecule retention time
	private String database; //	NMR/MS: Generally references the used spectral library
	private String database_version; //	NMR/MS: Either the version of the used database if available or otherwise the date of creation
	private String reliability; // NMR/MS: Rating
	private String uri; // NMR/MS: The MetaboLights experiment it was identified in (MTBLS id)
	private String search_engine; // NMR/MS: A "|" delimited list of search engine(s) used to identify this metabolite
	private String search_engine_score; // NMR/MS: A "|" delimited list of search engine(s) used to identify this metabolite
	private String modifications; // MS: The small molecules modifications. The position of the modification must be given relative to the small molecules beginning. The exact semantics of this position depends on the type of small molecule identified
	private String smallmolecule_abundance_sub; // The small molecule’s intensity in the respective subsample, [1-n]
	private String smallmolecule_abundance_stdev_sub; // The standard deviation of the small molecule’s abundance, [1-n]
    private String smallmolecule_abundance_std_error_sub; // The standard error of the small molecule’s abundance, [1-n]
    private String chebiId; //Chebi Id, Initially it will be empty. It will have a value when we will curate this metabolite and therefore we, after the submission, will have a chebi id for the metabolite.
	
	@ContainedIn
	private AssayGroup assayGroup;
	
	public Metabolite(){}
	public Metabolite(AssayGroup assayGroup, String description, String identifier){
		this.assayGroup = assayGroup;
		this.description = description;
		this.identifier = identifier;
	}
	
	@ManyToOne(targetEntity = AssayGroup.class)
	@JoinColumn(name = "ASSAYGROUP_ID", nullable = false)
	public AssayGroup getAssayGroup() {
		return assayGroup;
	}
	protected void setAssayGroup(AssayGroup assayGroup) {
		this.assayGroup = assayGroup;
	}
	
	@OneToMany(targetEntity = MetaboliteSample.class,
			cascade = {CascadeType.ALL})
	@JoinColumn(name = "METABOLITE_ID", nullable = true)
	public Collection<MetaboliteSample> getMetaboliteSamples() {
		return msMetSamples;
	}

	public void setMetaboliteSamples(Collection<MetaboliteSample> msMetaboliteSamples) {
		this.msMetSamples = msMetaboliteSamples;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}

	public String getChemical_formula() {
		return chemical_formula;
	}

	public void setChemical_formula(String chemical_formula) {
		this.chemical_formula = chemical_formula;
	}

	public String getChemical_shift() {
		return chemical_shift;
	}

	public void setChemical_shift(String chemical_shift) {
		this.chemical_shift = chemical_shift;
	}

	public String getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}

	public String getMass_to_charge() {
		return mass_to_charge;
	}

	public void setMass_to_charge(String mass_to_charge) {
		this.mass_to_charge = mass_to_charge;
	}

	public String getFragmentation() {
		return fragmentation;
	}

	public void setFragmentation(String fragmentation) {
		this.fragmentation = fragmentation;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getRetention_time() {
		return retention_time;
	}

	public void setRetention_time(String retention_time) {
		this.retention_time = retention_time;
	}
	@Column (name="MOL_DATABASE")
	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getDatabase_version() {
		return database_version;
	}

	public void setDatabase_version(String database_version) {
		this.database_version = database_version;
	}

	public String getReliability() {
		return reliability;
	}

	public void setReliability(String reliability) {
		this.reliability = reliability;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getSearch_engine() {
		return search_engine;
	}

	public void setSearch_engine(String search_engine) {
		this.search_engine = search_engine;
	}

	public String getSearch_engine_score() {
		return search_engine_score;
	}

	public void setSearch_engine_score(String search_engine_score) {
		this.search_engine_score = search_engine_score;
	}

	public String getModifications() {
		return modifications;
	}

	public void setModifications(String modifications) {
		this.modifications = modifications;
	}

	public String getSmallmolecule_abundance_sub() {
		return smallmolecule_abundance_sub;
	}

	public void setSmallmolecule_abundance_sub(String smallmolecule_abundance_sub) {
		this.smallmolecule_abundance_sub = smallmolecule_abundance_sub;
	}
	@Column (name="SMA_STDEV_SUB")
	public String getSmallmolecule_abundance_stdev_sub() {
		return smallmolecule_abundance_stdev_sub;
	}

	public void setSmallmolecule_abundance_stdev_sub(
			String smallmolecule_abundance_stdev_sub) {
		this.smallmolecule_abundance_stdev_sub = smallmolecule_abundance_stdev_sub;
	}
	@Column (name="SMA_STD_ERR_SUB")
	public String getSmallmolecule_abundance_std_error_sub() {
		return smallmolecule_abundance_std_error_sub;
	}

	public void setSmallmolecule_abundance_std_error_sub(
			String smallmolecule_abundance_std_error_sub) {
		this.smallmolecule_abundance_std_error_sub = smallmolecule_abundance_std_error_sub;
	}
	public String getChebiId() {
		return chebiId;
	}

	public void setChebiId(String chebiId) {
		this.chebiId = chebiId;
	}
	
}
