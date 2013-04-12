package org.fedoraproject.japi.checker.web.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.fedoraproject.japi.checker.web.model.Library;
import org.fedoraproject.japi.checker.web.service.CheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes(types = Library.class)
public class LibraryController {
	
	private final CheckerService checkerService;

	@Autowired
	public LibraryController(CheckerService checkerService) {
		this.checkerService = checkerService;
	}
	
	@InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @RequestMapping(value = "/libraries/new", method = RequestMethod.GET)
    public String initCreationForm(Model model) {
        Library library = new Library();
        model.addAttribute(library);
        return "libraries/createOrUpdate";
    }

    @RequestMapping(value = "/libraries/new", method = RequestMethod.POST)
    public String processCreationForm(@Valid Library library, BindingResult result, SessionStatus status) { // TODO @Valid
        if (result.hasErrors()) {
            return "libraries/createOrUpdate";
        } else {
            this.checkerService.saveLibrary(library);
            status.setComplete();
            return "redirect:/libraries/" + library.getId();
        }
    }
    
    @RequestMapping(value = "/libraries", method = RequestMethod.GET)
    public String showLibraries(Model model) {
    	Collection<Library> results = this.checkerService.findLibraries();
    	model.addAttribute("selections", results);
    	return "libraries/list";
    }
    
    /*@RequestMapping(value = "/libraries/find", method = RequestMethod.GET)
    public String initFindForm(Model model) {
        model.addAttribute("library", new Library());
        return "library/findLibraries";
    }

    @RequestMapping(value = "/libraries", method = RequestMethod.GET)
    public String processFindForm(Library library, BindingResult result, Model model) {

        // allow parameterless GET request for /libraries to return all records
        if (library.getName() == null) {
            library.setName(""); // empty string signifies broadest possible search
        }

        // find libraries by last name
        Collection<Library> results = this.checkerService.findLibraryByName(library.getName());
        if (results.size() < 1) {
            // no libraries found
            result.rejectValue("name", "notFound", "not found");
            return "libraries/findLibrariess";
        }
        if (results.size() > 1) {
            // multiple libraries found
            model.addAttribute("selections", results);
            return "libraries/list";
        } else {
            // 1 library found
            library = results.iterator().next();
            return "redirect:/libraries/" + library.getId();
        }
    }*/

    @RequestMapping(value = "/libraries/{libraryId}/edit", method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable("libraryId") int libraryId, Model model) {
        Library library = this.checkerService.findLibraryById(libraryId);
        model.addAttribute(library);
        return "libraries/createOrUpdate";
    }

    @RequestMapping(value = "/libraries/{libraryId}/edit", method = RequestMethod.PUT)
    public String processUpdateForm(Library library, BindingResult result, SessionStatus status) { // TODO @Valid 
        if (result.hasErrors()) {
            return "libraries/createOrUpdate";
        } else {
            this.checkerService.saveLibrary(library);
            status.setComplete();
            return "redirect:/libraries/{libraryId}";
        }
    }

	/**
	 * Custom handler for displaying an library.
	 * 
	 * @param libraryId the ID of the library to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@RequestMapping("/libraries/{libraryId}")
    public ModelAndView showLibrary(@PathVariable("libraryId") int libraryId) {
        ModelAndView mav = new ModelAndView("libraries/details");
        mav.addObject(this.checkerService.findLibraryWithReleasesById(libraryId));
        return mav;
    }

}
