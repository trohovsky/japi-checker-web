package org.fedoraproject.japi.checker.web;

import java.io.File;
import java.util.Date;
import java.util.List;

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

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "spring/business-config.xml");
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

        Release reference = service.findReleaseWithClassesById(1);
        if (reference == null) {
            reference = new Release(library, "testProject1");
            reference.setDate(new Date(System.currentTimeMillis()));
            service.parseAPI(reference, referenceArtifact);
            service.saveReleaseWithComparison(reference);
        }
        Release newRelease = service.findReleaseWithClassesById(2);
        if (newRelease == null) {
            newRelease = new Release(library, "testProject2");
            newRelease.setDate(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 30));
            service.parseAPI(newRelease, newArtifact);
            service.saveReleaseWithComparison(newRelease);
        }

        ReleasesComparison comparison = service.findReleasesComparison(1, 2);
        if (comparison == null) {
            comparison = service.checkBackwardCompatibility(reference,
                    newRelease);
            service.saveReleasesComparison(comparison);
        }

        // printing of comparison
        printReport(comparison);

        // printing of librarie's compatibility overview
        List<Release> releases = service.findReleasesByLibraryId(1);
        List<ReleasesComparison> releasesComparisons = service.findReleasesComparisonsByReleases(releases);
        for (int i = 0; i < releases.size(); i++) {
            Release release = releases.get(i);
            System.out.print(release.getDate() + " " + release.getName());
            if (i < releasesComparisons.size()) {
                System.out.println(releasesComparisons.get(i).isCompatible() ? " compatible" : " incompatible");
            }
        }
    }

    public void printReport(ReleasesComparison comparison) {
        for (Difference difference : comparison.getDifferences()) {
            if ((difference.getSeverity() == Severity.ERROR 
                    || difference.getSeverity() == Severity.WARNING)) {
                System.out.println(difference.getSeverity() + ": " + difference.getSource() + ": " + difference.getMessage()); // getLine(difference) +
            }
        }
    }
}
