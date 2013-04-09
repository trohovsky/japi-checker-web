package org.fedoraproject.japi.checker.web.service;

import java.io.File;
import java.util.List;

import org.fedoraproject.japi.checker.web.model.Library;
import org.fedoraproject.japi.checker.web.model.Release;
import org.fedoraproject.japi.checker.web.model.ReleasesComparison;
import org.springframework.dao.DataAccessException;

public interface CheckerService {

	public void saveLibrary(Library library) throws DataAccessException;

	public List<Library> findLibraries() throws DataAccessException;

	public Library findLibraryById(int id) throws DataAccessException;
	
	public Library findLibraryWithReleasesById(int libraryId) throws DataAccessException;

	public List<Library> findLibraryByName(String name)
			throws DataAccessException;

	public void deleteLibrary(Library library) throws DataAccessException;

	/**
	 * It parse API of JAR archive and returns it as a Release object.
	 * @param library
	 * @param releaseName
	 * @param file
	 * @return parsed API as a Release object
	 */
	public Release parseAPI(Library library, String releaseName, File file);

	public void saveRelease(Release release) throws DataAccessException;

	public List<Release> findReleases() throws DataAccessException;

	public Release findReleaseById(int id) throws DataAccessException;
	
	public Release findReleaseWithClassesById(int id) throws DataAccessException;

	public List<Release> findReleaseByName(String name) throws DataAccessException;

	public void deleteRelease(Release release);

	public ReleasesComparison checkBackwardCompatibility(Release reference,
			Release newRelease);

	public void saveReleasesComparison(ReleasesComparison releasesComparison)
			throws DataAccessException;

	public List<ReleasesComparison> findReleasesComparisons()
			throws DataAccessException;

	public ReleasesComparison findReleasesComparisonById(int id) throws DataAccessException;

	public void deleteReleasesComparison(ReleasesComparison releasesComparison)
			throws DataAccessException;

}