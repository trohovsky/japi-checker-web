package org.fedoraproject.japi.checker.web.controller;

import java.util.Collection;
import java.util.List;

import org.fedoraproject.japi.checker.web.model.Library;
import org.fedoraproject.japi.checker.web.model.Release;
import org.fedoraproject.japi.checker.web.model.ReleasesComparison;
import org.fedoraproject.japi.checker.web.service.CheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

@Controller
public class CheckerController {

	private final CheckerService checkerService;

	@Autowired
	public CheckerController(CheckerService checkerService) {
		this.checkerService = checkerService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String initCheckingForm(Model model) {
		List<Release> reference = this.checkerService.findReleases();
		List<Release> newRelease = this.checkerService.findReleases();
		model.addAttribute("reference", reference);
		model.addAttribute("newRelease", newRelease);
		model.addAttribute("checkingForm", new CheckingForm());
		return "checker/checking";
	}

	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public String processCheckingForm(
			@ModelAttribute("checkingForm") CheckingForm checkingForm,
			BindingResult result, SessionStatus status, Model model) {
		if (result.hasErrors()) {
			return "";
		} else {
		    int referenceId = checkingForm.getReferenceId();
		    int newId = checkingForm.getNewId();
		    ReleasesComparison comparison = this.checkerService.getReleasesComparison(referenceId, newId);
			model.addAttribute("comparison", comparison);
			status.setComplete();
			return "checker/result";
		}
	}	

    @RequestMapping(value = "/libraries-compatibility", method = RequestMethod.GET)
    public String showLibraries(Model model) {
        Collection<Library> results = this.checkerService.findLibraries();
        model.addAttribute("libraries", results);
        return "checker/libraries";
    }
	
    @RequestMapping(value = "/libraries-compatibility/{libraryId}/releases", method = RequestMethod.GET)
    public String showLibraryCompatibility(@PathVariable("libraryId") int libraryId, Model model) {
        Library library = this.checkerService.findLibraryWithReleasesById(libraryId);
        List<ReleasesComparison> comparisons = this.checkerService.findReleasesComparisonsByLibrary(library);
        
        // add dummy comparison with initial release
        ReleasesComparison initialComparison = new ReleasesComparison();
        Release initialRelease = library.getReleases().get(library.getReleases().size() - 1);
        initialComparison.setNewRelease(initialRelease);
        comparisons.add(initialComparison);
        
        model.addAttribute("comparisons", comparisons);
        return "checker/comparisons";
    }

    @RequestMapping(value = "/libraries-compatibility/{libraryId}/releases/{referenceId}-{newId}", method = RequestMethod.GET)
    public String showReleasesComparison(
            @PathVariable("referenceId") int referenceId,
            @PathVariable("newId") int newId, Model model) {
        
        ReleasesComparison comparison = this.checkerService.getReleasesComparison(referenceId, newId);
        model.addAttribute("comparison", comparison);

        return "checker/result";

    }

}
