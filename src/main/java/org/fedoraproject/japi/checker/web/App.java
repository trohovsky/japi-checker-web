package org.fedoraproject.japi.checker.web;

import java.io.File;

import org.fedoraproject.japi.checker.web.model.Difference;
import org.fedoraproject.japi.checker.web.model.Library;
import org.fedoraproject.japi.checker.web.model.Release;
import org.fedoraproject.japi.checker.web.model.ReleasesComparison;
import org.fedoraproject.japi.checker.web.service.CheckerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.googlecode.japi.checker.Severity;


/**
 * Testing application
 */
public class App
{
	private CheckerService service;
	
	public App(ApplicationContext context) {
		service = (CheckerService) context.getBean("checkerService");
	}
	
    public static void main( String[] args )
    {
    	ApplicationContext context = new ClassPathXmlApplicationContext("spring/business-config.xml");
        App app = new App(context);
        app.run(args);
    }
	
	public void run(String[] args) {
		
		// creation of library, releases and comparison
		Library library = service.findLibraryById(1);
		if (library == null) {
			library = new Library();
			library.setName("testProject");
			service.saveLibrary(library);
		}
        
        File referenceArtifact = new File(args[0]);
        File newArtifact = new File(args[1]);
        
        Release reference = service.findReleaseById(1);
        if (reference == null) {
        	reference = new Release(library, "testProject1");
        	service.parseAPI(reference, referenceArtifact);
            service.saveRelease(reference);
        }
        Release newRelease = service.findReleaseById(2);
        if (newRelease == null) {
        	newRelease = new Release(library, "testProject2"); 
        	service.parseAPI(newRelease, newArtifact);
        	service.saveRelease(newRelease);
        }   
        
        ReleasesComparison comparison = service.findReleasesComparison(1, 2);
        if (comparison == null) {
        	comparison = service.checkBackwardCompatibility(reference, newRelease);
            service.saveReleasesComparison(comparison);
        }        
		
        // printing of comparison
		printReport(comparison);
	}
	
	public void printReport(ReleasesComparison comparison) {
		for (Difference difference : comparison.getDifferences()) {
			if ((difference.getDifferenceType().getSeverity() == Severity.ERROR || 
	    			difference.getDifferenceType().getSeverity() == Severity.WARNING)) {
				System.out.println(difference.getDifferenceType().getSeverity() + ": " + difference.getSource() + ": " + difference.getMessage()); //getLine(difference) +
			}
		}
	}
}
