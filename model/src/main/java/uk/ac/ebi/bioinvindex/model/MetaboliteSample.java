package uk.ac.ebi.bioinvindex.model;

import javax.persistence.*;

import org.hibernate.search.annotations.*;

@Entity
@Table(name = "METABOLITE_SAMPLE")
@Indexed(index = "bii")
public class MetaboliteSample extends Identifiable{

	private String sampleName; //	NMR/MS: The small molecule's description/name.  Multiple values separated with | 
	private double value; 
	
	@ContainedIn
	private Metabolite metabolite;
	
	public MetaboliteSample(){}
	public MetaboliteSample(Metabolite met, String sampleName, double value){
		this.sampleName = sampleName;
		this.value = value;
		this.metabolite = met;
	}
	
	@ManyToOne(targetEntity = Metabolite.class)
	@JoinColumn(name = "METABOLITE_ID", nullable = false)
	public Metabolite getMetabolite() {
		return metabolite;
	}
	protected void setMetabolite(Metabolite met) {
		this.metabolite = met;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
