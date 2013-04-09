package org.fedoraproject.japi.checker.web.model;

// Generated Feb 27, 2013 2:23:31 PM by Hibernate Tools 3.4.0.CR1

import java.util.ArrayList;
import java.util.List;

import com.googlecode.japi.checker.DifferenceType;
import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.model.JavaItem;

/**
 * ReleasesComparison generated by hbm2java
 */
public class ReleasesComparison implements java.io.Serializable, Reporter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Release referenceRelease;
	private Release newRelease;
	private List<Difference> differences = new ArrayList<Difference>(0);

	public ReleasesComparison() {
	}

	public ReleasesComparison(Release referenceRelease,
			Release newRelease) {
		this.referenceRelease = referenceRelease;
		this.newRelease = newRelease;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public boolean isNew() {
        return (this.id == null);
    }

	public Release getReferenceRelease() {
		return referenceRelease;
	}

	public void setReferenceRelease(Release referenceRelease) {
		this.referenceRelease = referenceRelease;
	}

	public Release getNewRelease() {
		return newRelease;
	}

	public void setNewRelease(Release newRelease) {
		this.newRelease = newRelease;
	}

	public List<Difference> getDifferences() {
		return this.differences;
	}

	public void setDifferences(List<Difference> differences) {
		this.differences = differences;
	}

	public void report(com.googlecode.japi.checker.Difference difference) {
		// TODO Auto-generated method stub
		
	}

	public void report(JavaItem referenceItem, JavaItem newItem,
			DifferenceType differenceType, Object... args) {
		Difference difference = new Difference(this, referenceItem, newItem, differenceType, args);
		differences.add(difference);		
	}

}
