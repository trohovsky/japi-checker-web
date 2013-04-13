package org.fedoraproject.japi.checker.web.dao;

import java.util.List;

import org.fedoraproject.japi.checker.web.model.ReleasesComparison;

public interface ReleasesComparisonDAO {

	public void save(ReleasesComparison releaseComparison);

	public List<ReleasesComparison> findAll();

	public ReleasesComparison findByReleasesIds(int referenceId, int newId);

	public void delete(ReleasesComparison persistentInstance);

}