package uk.ac.ebi.bioinvindex.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import org.hibernate.search.annotations.*;

@Entity
@Table(name = "ASSAYGROUP")
public class AssayGroup extends Identifiable{

	@ContainedIn
	private Study study;
	
	private Collection<MZTab> mzTabs = new ArrayList<MZTab>();
	
	//@Field (name="filename", index = Index.TOKENIZED, store = Store.YES)
	private String fileName;
	

	public AssayGroup(Study study, String fileName){
		this.study = study;
		this.fileName = fileName;
	}
	
	@ManyToOne(targetEntity = Study.class)
	@JoinColumn(name = "STUDY_ID", nullable = false)
	public Study getStudy() {
		return study;
	}
	protected void setStudy(Study study) {
		this.study = study;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@OneToMany(targetEntity = MZTab.class)
	@JoinColumn(name = "ASSAYGROUP_ID", nullable = true)
	public Collection<MZTab> getMzTabs() {
		return mzTabs;
	}

	public void setMzTabs(Collection<MZTab> mzTabs) {
		this.mzTabs = mzTabs;
	}
}
