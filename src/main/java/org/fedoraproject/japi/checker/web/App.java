package org.fedoraproject.japi.checker.web;

import java.io.File;
import java.io.IOException;

import org.fedoraproject.japi.checker.web.dao.LibraryManager;
import org.fedoraproject.japi.checker.web.dao.ReleaseManager;
import org.fedoraproject.japi.checker.web.dao.ReleasesComparisonManager;
import org.fedoraproject.japi.checker.web.model.Difference;
import org.fedoraproject.japi.checker.web.model.Library;
import org.fedoraproject.japi.checker.web.model.Release;
import org.fedoraproject.japi.checker.web.model.ReleasesComparison;
import org.fedoraproject.japi.checker.web.persistence.HibernateUtil;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.googlecode.japi.checker.BCChecker;
import com.googlecode.japi.checker.Severity;
import com.googlecode.japi.checker.model.MethodData;


/**
 * Testing application
 */
public class App
{
	// managers
	private LibraryManager libraryManager;
	private ReleaseManager releaseManager;
	private ReleasesComparisonManager releasesComparisonManager;
	
	public App(ApplicationContext context) {
		libraryManager = (LibraryManager) context.getBean("libraryManager");
		releaseManager = (ReleaseManager) context.getBean("releaseManager");
		releasesComparisonManager = (ReleasesComparisonManager) context.getBean("releasesComparisonManager");
	}
	
    public static void main( String[] args )
    {
    	ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        App app = new App(context);
        app.run(args);
    }
	
	public Release parseAPI(Library library, String releaseName, File file) {
		Release release = new Release(library, releaseName);
		try {
			release.read(file.toURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return release;
	}
	
	public ReleasesComparison checkBackwardCompatibility(Release reference, Release newRelease) {
		ReleasesComparison comparison = new ReleasesComparison(reference, newRelease);
		BCChecker checker = new BCChecker();
		checker.checkBackwardCompatibility(comparison, reference.getClasses(), newRelease.getClasses());
		return comparison;
	}
	
	public void printReport(ReleasesComparison comparison) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		for (Difference difference : comparison.getDifferences()) {
			if ((difference.getDifferenceType().getServerity() == Severity.ERROR || 
	    			difference.getDifferenceType().getServerity() == Severity.WARNING)) {
				System.out.println(difference.getDifferenceType().getServerity() + ": " + difference.getSource() + ": " + difference.getMessage()); //getLine(difference) +
			}
		}
		session.getTransaction().commit();
	}
	
    private String getLine(Difference difference) {
        if (difference.getNewItem() instanceof MethodData) {
        	Integer lineNumber = ((MethodData)difference.getNewItem()).getLineNumber();
        	if (lineNumber != null) {
        		return "(" + lineNumber + ")";
        	} else {
        		return "";
        	}
        }
        return "";
    }
	
	public void run(String[] args) {
		
		// creation of library, releases and comparison
		Library library = libraryManager.findById(1);
		if (library == null) {
			library = new Library();
			library.setName("testProject");
			libraryManager.persist(library);
		}
        
        File referenceArtifact = new File(args[0]);
        File newArtifact = new File(args[1]);
        
        Release reference = releaseManager.findById(1);
        if (reference == null) {
        	reference = parseAPI(library, "testProject1", referenceArtifact);
            releaseManager.persist(reference);
        }
        Release newRelease = releaseManager.findById(2);
        if (newRelease == null) {
        	newRelease = parseAPI(library, "testProject2", newArtifact);
        	releaseManager.persist(newRelease);
        }   
        
        ReleasesComparison comparison = releasesComparisonManager.findById(1);
        if (comparison == null) {
        	comparison = checkBackwardCompatibility(reference, newRelease);
            releasesComparisonManager.persist(comparison);
        }        
		
        // printing of comparison
		printReport(comparison);
	}
}
