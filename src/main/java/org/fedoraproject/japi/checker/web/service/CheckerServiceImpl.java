package org.fedoraproject.japi.checker.web.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Date;
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
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

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
     * Create library for artifact and download all versions of required
     * artifact from Maven repository and store them as releases.
     * 
     * @param groupId
     * @param artifactId
     */
    public void createLibraryFromArtifact(String groupId, String artifactId) {
        // get artifact versions over REST API
        List<ArtifactData> artifactDataList = getArtifactDataList(groupId, artifactId);

        // store artifact files temporarily
        File artifactDir = new File(artifactId);
        artifactDir.mkdir();
        List<File> artifactFiles = new ArrayList<File>();
        for (ArtifactData data : artifactDataList) {
            File artifactFile = downloadArtifact(groupId, artifactId,
                    data.getVersion(), data.getPackaging());
            artifactFiles.add(artifactFile);
        }

        // store library
        Library library = new Library();
        library.setName(artifactId);
        this.saveLibrary(library);

        // parse files and store releases
        for (File file : artifactFiles) {
            Release release = new Release();
            String releaseName = file.getName().substring(0,
                    file.getName().lastIndexOf('.'));
            release.setName(releaseName);
            ArtifactData data = artifactDataList.get(artifactFiles.indexOf(file));
            Date releaseDate = data.getTimestamp();
            release.setDate(releaseDate);
            release.setLibrary(library);

            this.parseAPI(release, file);
            this.saveReleaseWithComparison(release);

            // remove artifact file
            file.delete();
        }

        // remove artifact directory
        artifactDir.delete();
    }
	
    /**
     * Get data about all versions of the artifact.
     * 
     * @param groupId
     * @param artifactId
     * @return
     */
    private List<ArtifactData> getArtifactDataList(String groupId, String artifactId) {
        List<ArtifactData> artifactDataList = new ArrayList<ArtifactData>();

        String url = "http://search.maven.org/solrsearch/select?q=g:\""
                + groupId + "\"+AND+a:\"" + artifactId
                + "\"&core=gav&rows=1000&wt=xml";

        ContentHandler handler = new ArtifactDataHandler();

        XMLReader myReader;
        try {
            InputSource source = new InputSource(new URL(url).openStream());
            myReader = XMLReaderFactory.createXMLReader();
            myReader.setContentHandler(handler);
            myReader.parse(source);
            artifactDataList = ((ArtifactDataHandler) handler).getArtifactDataList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return artifactDataList;
    }
	
    /**
     * Download the file from URL to specified the file.
     * 
     * @param url
     * @param filename
     */
    private void downloadFile(String url, File file) {
        URL website;
        try {
            website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(file);
            // 16 MB per file could be enough
            fos.getChannel().transferFrom(rbc, 0, 1 << 24);
            fos.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Download the artifact.
     * 
     * @param groupId
     * @param artifactId
     * @param version
     * @param packaging
     * @return
     */
    private File downloadArtifact(String groupId, String artifactId, String version, String packaging) {
        String filePath = groupId.replace('.', '/') + "/" + artifactId + "/"
                + version + "/" + artifactId + "-" + version + "." + packaging;
        String artifactURL = "http://search.maven.org/remotecontent?filepath="
                + filePath;
        File artifactFile = new File(artifactId + "/" + artifactId + "-"
                + version + "." + packaging);
        // download
        downloadFile(artifactURL, artifactFile);
        return artifactFile;
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
	}
	
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
	
	public void saveReleasesComparison(ReleasesComparison releasesComparison) throws DataAccessException {
		releasesComparisonDAO.save(releasesComparison);
	}

	public List<ReleasesComparison> findReleasesComparisonsByLibrary(Library library) throws DataAccessException {
        List<Integer> ids = new ArrayList<Integer>();
        // get ids
        for (Release release : library.getReleases()) {
            ids.add(release.getId());
        }

        return releasesComparisonDAO.findByReleasesIds(ids);
    }

	public ReleasesComparison findReleasesComparison(int referenceId, int newId) throws DataAccessException {
		return releasesComparisonDAO.findByReleasesIds(referenceId, newId);
	}

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
