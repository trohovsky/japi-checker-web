package org.fedoraproject.japi.checker.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.fedoraproject.japi.checker.web.model.Library;
import org.fedoraproject.japi.checker.web.model.Release;
import org.fedoraproject.japi.checker.web.service.CheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes(types = Release.class)
public class ReleaseController {
	
	private final CheckerService checkerService;

	@Autowired
	public ReleaseController(CheckerService checkerService) {
		this.checkerService = checkerService;
	}
	
	@InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }
	
	@RequestMapping(value = "libraries/{libraryId}/releases/new", method = RequestMethod.GET)
    public String initCreationForm(@PathVariable("libraryId") int libraryId, Model model) {
		//Library library = this.checkerService.findLibraryById(libraryId);
        Release release = new Release();
        //release.setLibrary(library);
        model.addAttribute(release);
        return "releases/createOrUpdate";
    }
	
	@RequestMapping(value = "/libraries/{libraryId}/releases/new", method = RequestMethod.POST)
	public String processCreationForm(@PathVariable("libraryId") int libraryId,
			@RequestParam("name") String name,
			@RequestParam("file") MultipartFile file) {
		// filename parsing - it is necessary because getOriginalFilename() may return path in Opera, see. API
		// http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/web/multipart/MultipartFile.html#transferTo%28java.io.File%29
		int slashIndex = file.getOriginalFilename().lastIndexOf('/');
		if (slashIndex == -1) {
			slashIndex = file.getOriginalFilename().lastIndexOf('\\');
		}
		String filename;
		if (slashIndex == -1) {
			filename = file.getOriginalFilename();
		} else {
			filename = file.getOriginalFilename().substring(slashIndex);
		}
		// store file temporarily
		// TODO choose better destination than user's home
		File tmpDir = new File("tmpJARs");
		File tmpFile = new File("tmpJARs/" + filename);
		try {
			tmpDir.mkdir();
			tmpFile.createNewFile();
			new FileOutputStream(tmpFile).write(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// parse API
		Library library = this.checkerService.findLibraryById(libraryId);
		// TODO the arguments of this method can be reduced only to file.
		// but file is also not nice, because this file is only on disc
		Release release = this.checkerService.parseAPI(library, name, tmpFile);

		// store release
		this.checkerService.saveRelease(release);

		// remove temporary file
		tmpFile.delete();

		// status.setComplete();
		return "redirect:/libraries/{libraryId}";
    }

    /*@RequestMapping(value = "/libraries/{libraryId}/releases/new", method = RequestMethod.POST)
    public String processCreationForm(Release release, BindingResult result, SessionStatus status) { // TODO @Valid
        if (result.hasErrors()) {
            return "releases/createOrUpdate";
        } else {
            this.checkerService.saveRelease(release);
            status.setComplete();
            return "redirect:/libraries/{libraryId}" + release.getId();
        }
    }*/

    @RequestMapping(value = "/libraries/*/releases/{releaseId}/edit", method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable("releaseId") int releaseId, Model model) {
        Release release = this.checkerService.findReleaseById(releaseId);
        model.addAttribute(release);
        return "releases/createOrUpdate";
    }

    @RequestMapping(value = "/libraries/{libraryId}/releases/{releaseId}/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public String processUpdateForm(@ModelAttribute("release") Release release, BindingResult result, SessionStatus status) {
        if (result.hasErrors()) {
            return "releases/createOrUpdate";
        } else {
            this.checkerService.saveRelease(release);
            status.setComplete();
            return "redirect:/libraries/{libraryId}/releases/{releaseId}";
        }
    }
    
	/**
	 * Custom handler for displaying an release.
	 * 
	 * @param releaseId the ID of the release to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@RequestMapping("/libraries/*/releases/{releaseId}")
    public ModelAndView showLibrary(@PathVariable("releaseId") int releaseId) {
        ModelAndView mav = new ModelAndView("releases/details");
        mav.addObject(this.checkerService.findReleaseById(releaseId));
        return mav;
    }
}
