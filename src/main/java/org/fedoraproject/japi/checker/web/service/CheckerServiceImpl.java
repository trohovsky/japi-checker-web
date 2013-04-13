package org.fedoraproject.japi.checker.web.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.fedoraproject.japi.checker.web.dao.LibraryDAO;
import org.fedoraproject.japi.checker.web.dao.ReleaseDAO;
import org.fedoraproject.japi.checker.web.dao.ReleasesComparisonDAO;
import org.fedoraproject.japi.checker.web.model.Library;
import org.fedoraproject.japi.checker.web.model.Release;
import org.fedoraproject.japi.checker.web.model.ReleasesComparison;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.googlecode.japi.checker.BCChecker;

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
	
	/* Library operations */
	
	public void saveLibrary(Library library) throws DataAccessException {
		libraryDAO.save(library);
	}
	
	public List<Library> findLibraries() throws DataAccessException {
		return libraryDAO.findAll();
	}

	public Library findLibraryById(int id) throws DataAccessException {
		return libraryDAO.findById(id);
	}
	
	public Library findLibraryWithReleasesById(int id) throws DataAccessException {
		return libraryDAO.findWithReleasesById(id);
	}

	public List<Library> findLibraryByName(String name) throws DataAccessException {
		return libraryDAO.findByName(name);
	}

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
	
	public void saveRelease(Release release) throws DataAccessException {
		releaseDAO.save(release);
		if (!release.getClasses().isEmpty()) {
		    Release previousRelease = releaseDAO.findPrevious(release);
		    if (previousRelease != null) {
		        ReleasesComparison comparison = checkBackwardCompatibility(previousRelease, release);
		        releasesComparisonDAO.save(comparison);
		    }
		}
	}

	public List<Release> findReleases() throws DataAccessException {
		return releaseDAO.findAll();
	}

	public Release findReleaseById(int id) throws DataAccessException {
		return releaseDAO.findById(id);
	}
	
	public Release findReleaseWithClassesById(int id) throws DataAccessException {
		return releaseDAO.findWithClassesById(id);
	}

	public List<Release> findReleaseByName(String name) throws DataAccessException {
		return releaseDAO.findByName(name);
	}

	public void deleteRelease(Release release) {
		releaseDAO.delete(release);
		// TODO remove comparison and create new
	}
	
	/* ReleasesComparison operations */
	
	public ReleasesComparison checkBackwardCompatibility(Release reference, Release newRelease) {
		ReleasesComparison comparison = new ReleasesComparison(reference, newRelease);
		checker.checkBackwardCompatibility(comparison, reference.getClasses(), newRelease.getClasses());
		return comparison;
	}
	
	public void saveReleasesComparison(ReleasesComparison releasesComparison) throws DataAccessException {
		releasesComparisonDAO.save(releasesComparison);
	}

	public List<ReleasesComparison> findReleasesComparisons() throws DataAccessException {
		return releasesComparisonDAO.findAll();
	}

	public ReleasesComparison findReleasesComparison(int referenceId, int newId) throws DataAccessException {
		return releasesComparisonDAO.findByReleasesIds(referenceId, newId);
	}

	public void deleteReleasesComparison(ReleasesComparison releasesComparison) throws DataAccessException {
		releasesComparisonDAO.delete(releasesComparison);
	}
}
