package org.fedoraproject.japi.checker.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.fedoraproject.japi.checker.web.model.Library;
import org.fedoraproject.japi.checker.web.model.Release;
import org.fedoraproject.japi.checker.web.service.CheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
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
    private TaskExecutor taskExecutor;
    private static final String UPLOAD_PATH = "tmpJARs/";

    @Autowired
    public ReleaseController(CheckerService checkerService,
            TaskExecutor taskExecutor) {
        this.checkerService = checkerService;
        this.taskExecutor = taskExecutor;
        File tmpDir = new File(UPLOAD_PATH);
        tmpDir.mkdirs();
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
        // date format displayed in form
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, false));
    }

    @RequestMapping(value = "/admin/libraries/{libraryId}/releases/new", method = RequestMethod.GET)
    public String initCreationForm(@PathVariable("libraryId") int libraryId,
            Model model) {
        Library library = this.checkerService.findLibraryById(libraryId);
        Release release = new Release();
        release.setLibrary(library);
        model.addAttribute(release);
        return "releases/createOrUpdate";
    }

    @RequestMapping(value = "/admin/libraries/{libraryId}/releases/new", method = RequestMethod.POST)
    public String processCreationForm(@RequestParam("file") MultipartFile file,
            @Valid Release release, BindingResult result, SessionStatus status) {
        if (result.hasErrors()) {
            return "releases/createOrUpdate";
        } else {

            // get file name
            String filename = getFilename(file);

            if (filename.isEmpty()) {
                return "releases/createOrUpdate";
            } else {
                File tmpFile = new File(UPLOAD_PATH + filename);
                try {
                    // save release
                    checkerService.saveRelease(release);

                    // store file temporarily
                    tmpFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(tmpFile);
                    fos.write(file.getBytes());
                    fos.close();

                    // parse API and update release in thread
                    Thread uploadReleaseTask = new ReleaseCreationTask(release, tmpFile);
                    taskExecutor.execute(uploadReleaseTask);
                } catch (IOException e) {
                    // delete saved release
                    checkerService.deleteRelease(release);
                    tmpFile.delete();
                }
            }

            status.setComplete();
            return "redirect:/admin/libraries/{libraryId}";
        }
    }

    /**
     * It returns filename of MultipartFile.
     * 
     * @param file
     * @return
     */
    private String getFilename(MultipartFile file) {
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
        return filename;
    }

    /**
     * Task class for a creation of release.
     */
    class ReleaseCreationTask extends Thread {
        private Release release;
        private File tmpFile;

        public ReleaseCreationTask(Release release, File tmpFile) {
            this.release = release;
            this.tmpFile = tmpFile;
        }

        @Override
        public void run() {

            // parse API
            // long parsingStart = System.nanoTime();
            checkerService.parseAPI(release, tmpFile);
            // double parsingDuration = (System.nanoTime() - parsingStart) * 1.0e-9;

            // store release
            // long savingStart = System.nanoTime();
            checkerService.saveReleaseWithComparison(release);
            // double savingDuration = (System.nanoTime() - savingStart) * 1.0e-9;

            // System.out.println("parsing of API: " + parsingDuration);
            // System.out.println("saving of API: " + savingDuration);

            // remove temporary file
            tmpFile.delete();

        }
    }

    @RequestMapping(value = "/admin/libraries/*/releases/{releaseId}/edit", method = RequestMethod.GET)
    public String initUpdateForm(@PathVariable("releaseId") int releaseId,
            Model model) {
        Release release = this.checkerService.findReleaseById(releaseId);
        model.addAttribute(release);
        return "releases/createOrUpdate";
    }

    @RequestMapping(value = "/admin/libraries/{libraryId}/releases/{releaseId}/edit", method = {
            RequestMethod.PUT, RequestMethod.POST })
    public String processUpdateForm(@Valid Release release,
            BindingResult result, SessionStatus status) {
        if (result.hasErrors()) {
            return "releases/createOrUpdate";
        } else {
            this.checkerService.saveRelease(release);
            status.setComplete();
            return "redirect:/admin/libraries/{libraryId}/releases/{releaseId}";
        }
    }

    /**
     * Custom handler for displaying an release.
     * 
     * @param releaseId the ID of the release to display
     * @return a ModelMap with the model attributes for the view
     */
    @RequestMapping("/admin/libraries/*/releases/{releaseId}")
    public ModelAndView showRelease(@PathVariable("releaseId") int releaseId) {
        ModelAndView mav = new ModelAndView("releases/details");
        mav.addObject(this.checkerService.findReleaseById(releaseId));
        return mav;
    }

    @RequestMapping(value = "/admin/libraries/{libraryId}/releases/{releaseId}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable("releaseId") int releaseId) {
        Release release = this.checkerService.findReleaseById(releaseId);
        this.checkerService.deleteRelease(release);
        return "redirect:/admin/libraries/{libraryId}";
    }
}
