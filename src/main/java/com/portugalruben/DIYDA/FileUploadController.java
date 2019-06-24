package com.portugalruben.DIYDA;

import java.io.IOException;
import java.util.stream.Collectors;

import com.portugalruben.DIYDA.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.portugalruben.DIYDA.storage.StorageFileNotFoundException;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "DIYDA_pages/uploadForm_home";
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Here is the place where we start managing our pages except the home, ///////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/dataType")
    public String displayDataTypePage(Model model) throws IOException {

        return "DIYDA_pages/dataType_pag01";
    }

    @GetMapping("/sampleData")
    public String displaySampleDataPage(Model model) throws IOException {

        return "DIYDA_pages/sampleData_pag02";
    }

    @GetMapping("/cleanOne")
    public String displayCleanOnePage(Model model) throws IOException {

        return "DIYDA_pages/cleanOne_pag03";
    }

    @GetMapping("/cleanTwo")
    public String displayCleanTwoPage(Model model) throws IOException {

        return "DIYDA_pages/cleanTwo_pag04";
    }

    @GetMapping("/visualGraphs")
    public String displayViasualGraphsPage(Model model) throws IOException {

        return "DIYDA_pages/visualGraphs_pag05";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}


