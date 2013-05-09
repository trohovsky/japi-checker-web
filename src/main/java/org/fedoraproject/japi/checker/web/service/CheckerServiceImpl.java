package org.fedoraproject.japi.checker.web.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.fedoraproject.japi.checker.web.dao.LibraryDAO;
import org.fedoraproject.japi.checker.web.dao.ReleaseDAO;
import org.fedoraproject.japi.checker.web.dao.ReleasesComparisonDAO;
import org.fedoraproject.japi.checker.web.model.Artifact;
import org.fedoraproject.japi.checker.web.model.ArtifactVersion;
import org.fedoraproject.japi.checker.web.model.Library;
import org.fedoraproject.japi.checker.web.model.Release;
import org.fedoraproject.japi.checker.web.model.ReleasesComparison;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.japi.checker.BCChecker;
import com.googlecode.japi.checker.Severity;

@Service
public class CheckerServiceImpl implements CheckerService {

	private LibraryDAO libraryDAO;
	private ReleaseDAO releaseDAO;
	private ReleasesComparisonDAO releasesComparisonDAO;
	private BCChecker checker;
	
	@Autowired
	public CheckerServiceImpl(LibraryDAO libraryDAO, ReleaseDAO releaseDAO, ReleasesComparisonDAO releasesComparisonDAO, BCChecker checker) {
		this.libraryDAO = libraryDAO;
		this.releaseDAO = releaseDAO;
		this.releasesComparisonDAO = releasesComparisonDAO;
		this.checker = checker;
	}

    /* Maven artifact operations */

    /**
     * Create library for artifact. and download all versions of required
     * artifact from Maven repository and store them as releases.
     * 
     * @param artifact
     */
    public void createLibraryFromArtifact(Artifact artifact) {
        // get artifact versions over REST API
        artifact.initVersions();
        if (artifact.getVersions().isEmpty()) {
            return;
        }

        // store artifact files temporarily
        File artifactDir = new File(artifact.getArtifactId());
        artifactDir.mkdir();
        for (ArtifactVersion version : artifact.getVersions()) {
            // download artifact version
            version.download(artifact.getArtifactId());
        }

        // store library
        Library library = new Library();
        library.setName(artifact.getArtifactId());
        this.saveLibrary(library);

        // parse files and store releases
        for (ArtifactVersion version : artifact.getVersions()) {
            Release release = new Release();
            release.setName(version.getName());
            release.setDate(version.getTimestamp());
            release.setLibrary(library);

            this.parseAPI(release, version.getFile());
            this.saveReleaseWithComparison(release);

            // remove artifact file
            version.getFile().delete();
        }

        // remove artifact directory
        artifactDir.delete();
    }

    /**
     * Create libraries from artifacts.
     * 
     * @param artifacts
     */
    @Transactional
    public void createLibrariesFromArtifacts(List<Artifact> artifacts) {
        for (Artifact artifact : artifacts) {
            createLibraryFromArtifact(artifact);
        }
    }
      
    /* Library operations */
    
    @Transactional
    public void saveLibrary(Library library) throws DataAccessException {
        libraryDAO.save(library);
    }

    @Transactional(readOnly = true)
	public List<Library> findLibraries() throws DataAccessException {
		return libraryDAO.findAll();
	}

    @Transactional(readOnly = true)
	public Library findLibraryById(int id) throws DataAccessException {
		return libraryDAO.findById(id);
	}
	
    @Transactional(readOnly = true)
	public Library findLibraryWithReleasesById(int id) throws DataAccessException {
		return libraryDAO.findWithReleasesById(id);
	}

    @Transactional(readOnly = true)
	public List<Library> findLibraryByName(String name) throws DataAccessException {
		return libraryDAO.findByName(name);
	}

    @Transactional
	public void deleteLibrary(Library library) throws DataAccessException {
		libraryDAO.delete(library);
	}
	
	/* Release operations */
	
	/**
	 * It parse API of JAR archive and to the given Release object.
	 * @param release
	 * @param file
	 */
	public void parseAPI(Release release, File file) {
		try {
			release.read(file.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    @Transactional
	public void saveRelease(Release release) throws DataAccessException {
	    releaseDAO.save(release);
	}
	
    @Transactional
    public void saveReleaseWithComparison(Release release) throws DataAccessException {
        releaseDAO.save(release);
        Release previousRelease = releaseDAO.findPrevious(release);
        Release nextRelease = releaseDAO.findNext(release);
        // create the comparison with the previous release
        if (previousRelease != null) {
            ReleasesComparison comparison = checkBackwardCompatibility(previousRelease, release);
            releasesComparisonDAO.save(comparison);
        }
        // create the comparison with the next release
        if (nextRelease != null) {
            ReleasesComparison comparison = checkBackwardCompatibility(release, nextRelease);
            releasesComparisonDAO.save(comparison);
        }
        // remove the old comparison if the new release was added between releases
        if (previousRelease != null & nextRelease != null) {
            releasesComparisonDAO.delete(previousRelease.getId(), nextRelease.getId());
        }
    }
    
    @Transactional(readOnly = true)
    public List<Release> findReleases() throws DataAccessException {
		return releaseDAO.findAll();
	}

    @Transactional(readOnly = true)
    public Release findReleaseById(int id) throws DataAccessException {
		return releaseDAO.findById(id);
	}
	
    @Transactional(readOnly = true)
	public Release findReleaseWithClassesById(int id) throws DataAccessException {
		return releaseDAO.findWithClassesById(id);
	}

    @Transactional(readOnly = true)
	public List<Release> findReleaseByName(String name) throws DataAccessException {
		return releaseDAO.findByName(name);
	}

    @Transactional
	public void deleteRelease(Release release) {
	    Release previousRelease = releaseDAO.findPrevious(release);
	    Release nextRelease = releaseDAO.findNext(release);
	    // delete comparison and create new
		releaseDAO.delete(release);
		if (previousRelease != null && nextRelease != null) {
		    ReleasesComparison comparison = checkBackwardCompatibility(previousRelease, nextRelease);
		    releasesComparisonDAO.save(comparison);
		}
	}
	
	/* ReleasesComparison operations */
	
    public ReleasesComparison checkBackwardCompatibility(Release reference, Release newRelease) {
		ReleasesComparison comparison = new ReleasesComparison(reference, newRelease);
		checker.checkBackwardCompatibility(comparison, reference.getClasses(), newRelease.getClasses());
		// set compatibility info
		comparison.setErrorCount(comparison.getDifferencesCount(Severity.ERROR, true));
		comparison.setWarningCount(comparison.getDifferencesCount(Severity.WARNING, true));
		return comparison;
	}
	
    @Transactional
	public void saveReleasesComparison(ReleasesComparison releasesComparison) throws DataAccessException {
		releasesComparisonDAO.save(releasesComparison);
	}

    @Transactional(readOnly = true)
	public List<ReleasesComparison> findReleasesComparisonsByLibrary(Library library) throws DataAccessException {
        List<Integer> ids = new ArrayList<Integer>();
        // get ids
        for (Release release : library.getReleases()) {
            ids.add(release.getId());
        }

        return releasesComparisonDAO.findByReleasesIds(ids);
    }

    @Transactional(readOnly = true)
	public ReleasesComparison findReleasesComparison(int referenceId, int newId) throws DataAccessException {
		return releasesComparisonDAO.findByReleasesIds(referenceId, newId);
	}

    @Transactional(readOnly = true)
    public ReleasesComparison getReleasesComparison(int referenceId, int newId) {
        ReleasesComparison comparison = releasesComparisonDAO.findByReleasesIds(referenceId, newId);
        // compute the comparison if it is not in the database
        if (comparison == null) {
            Release reference = releaseDAO.findWithClassesById(referenceId);
            Release newRelease = releaseDAO.findWithClassesById(newId);
            comparison = this.checkBackwardCompatibility(reference, newRelease);
        }
        return comparison;
    }
}
