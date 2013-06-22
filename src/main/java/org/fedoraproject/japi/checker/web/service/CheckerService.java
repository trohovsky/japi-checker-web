package org.fedoraproject.japi.checker.web.service;

import java.io.File;
import java.util.List;

import org.fedoraproject.japi.checker.web.model.Artifact;
import org.fedoraproject.japi.checker.web.model.Library;
import org.fedoraproject.japi.checker.web.model.Release;
import org.fedoraproject.japi.checker.web.model.ReleasesComparison;
import org.springframework.dao.DataAccessException;

public interface CheckerService {

    public void createLibraryFromArtifact(Artifact artifact) throws DataAccessException;

    public void createLibrariesFromArtifacts(List<Artifact> artifacts) throws DataAccessException;

    public void saveLibrary(Library library) throws DataAccessException;

    public List<Library> findLibraries() throws DataAccessException;

    public Library findLibraryById(int id) throws DataAccessException;

    public Library findLibraryWithReleasesById(int libraryId) throws DataAccessException;

    public List<Library> findLibraryByName(String name) throws DataAccessException;

    public void deleteLibrary(Library library) throws DataAccessException;

    /**
     * It parse API of JAR archive and to the given Release object.
     * 
     * @param release
     * @param file
     */
    public void parseAPI(Release release, File file);

    public void saveRelease(Release release) throws DataAccessException;

    public void saveReleaseWithComparison(Release release) throws DataAccessException;

    public List<Release> findReleasesByLibraryId(int libraryId) throws DataAccessException;
    
    public Release findReleaseById(int id) throws DataAccessException;

    public Release findReleaseWithClassesById(int id) throws DataAccessException;

    public List<Release> findReleaseByName(String name) throws DataAccessException;

    public void deleteRelease(Release release) throws DataAccessException;

    public ReleasesComparison checkBackwardCompatibility(Release reference, Release newRelease);

    public void saveReleasesComparison(ReleasesComparison releasesComparison) throws DataAccessException;

    /**
     * Find the comparison by library. Library has to has initialized releases
     * (i.e. loaded by findWithReleasesById(int id)).
     * 
     * @param library
     * @return
     * @throws DataAccessException
     */
    public List<ReleasesComparison> findReleasesComparisonsByReleases(List<Release> releases) throws DataAccessException;

    /**
     * Return the comparison. If it is not stored in a database, it will compute
     * it.
     * 
     * @param referenceId
     * @param newId
     * @return
     */
    public ReleasesComparison findReleasesComparison(int referenceId, int newId) throws DataAccessException;

}