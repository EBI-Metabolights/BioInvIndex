package uk.ac.ebi.bioinvindex.model;

import javax.persistence.*;

import org.hibernate.search.annotations.*;

@Entity
@Table(name = "MZTAB")
@Indexed(index = "bii")
public class MZTab extends Identifiable{

	@Field (name="DESCRIPTION")
	private String description;
	
	@Field (name="IDENTIFIER")
	private String identifier;
	
	@ContainedIn
	private AssayGroup assayGroup;
	
	public MZTab(AssayGroup assayGroup, String description, String identifier){
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

	
}
