package uk.ac.ebi.bioinvindex.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import org.hibernate.search.annotations.*;

@Entity
@Table(name = "ASSAYGROUP")
public class AssayGroup extends Identifiable{

	private Collection<Metabolite> metMetabolites = new ArrayList<Metabolite>();
	
	//@Field (name="filename", index = Index.TOKENIZED, store = Store.YES)
	private String fileName;

	public AssayGroup(){}
	public AssayGroup(String fileName){
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@OneToMany(targetEntity = Metabolite.class,
			cascade = {CascadeType.ALL})
	@JoinColumn(name = "ASSAYGROUP_ID", nullable = true)
	public Collection<Metabolite> getMetabolites() {
		return metMetabolites;
	}

	public void setMetabolites(Collection<Metabolite> metMetabolites) {
		this.metMetabolites = metMetabolites;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		AssayGroup ag = (AssayGroup) o;


		if (fileName != null ? !fileName.equals(ag.fileName) : ag.fileName != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
		return result;
	}
}
