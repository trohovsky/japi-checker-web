package org.fedoraproject.japi.checker.web.controller;

import java.util.List;

import org.fedoraproject.japi.checker.web.model.Release;
import org.fedoraproject.japi.checker.web.model.ReleasesComparison;
import org.fedoraproject.japi.checker.web.service.CheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
			Release reference = this.checkerService.findReleaseWithClassesById(checkingForm.getReferenceId());
			Release newRelease = this.checkerService.findReleaseWithClassesById(checkingForm.getNewId());
			ReleasesComparison comparison = this.checkerService.checkBackwardCompatibility(reference, newRelease);
			model.addAttribute("comparison", comparison);
			status.setComplete();
			return "checker/result";
		}
	}

}
