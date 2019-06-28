package com.portugalruben.DIYDA;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;
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
    LinkedHashMap<String, ArrayList> dataFileMap = new LinkedHashMap<>();

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
        Path path = storageService.load("Titanic_orig_short.csv");

        File file = path.toFile();
        try (

            BufferedReader br = new BufferedReader(new FileReader(file));

        ) {
            CSVReader csvReader = new CSVReader(br);

            // Reading Records One by One in a String array
        String[] nextRecord;
        Boolean firstPass = false;


            ArrayList<String> aLCOL001 = new ArrayList<>();
            ArrayList<String> aLCOL002 = new ArrayList<>();
            ArrayList<String> aLCOL003 = new ArrayList<>();
            ArrayList<String> aLCOL004 = new ArrayList<>();
            ArrayList<String> aLCOL005 = new ArrayList<>();
            ArrayList<String> aLCOL006 = new ArrayList<>();
            ArrayList<String> aLCOL007 = new ArrayList<>();
            ArrayList<String> aLCOL008 = new ArrayList<>();
            ArrayList<String> aLCOL009 = new ArrayList<>();
            ArrayList<String> aLCOL010 = new ArrayList<>();
            ArrayList<String> aLCOL011 = new ArrayList<>();
            ArrayList<String> aLCOL012 = new ArrayList<>();

            String col001 = "";
            String col002 = "";
            String col003 = "";
            String col004 = "";
            String col005 = "";
            String col006 = "";
            String col007 = "";
            String col008 = "";
            String col009 = "";
            String col010 = "";
            String col011 = "";
            String col012 = "";


            while ((nextRecord = csvReader.readNext()) != null) {

            if (! firstPass) {

                col001 = nextRecord[0];
                col002 = nextRecord[1];
                col003 = nextRecord[2];
                col004 = nextRecord[3];
                col005 = nextRecord[4];
                col006 = nextRecord[5];
                col007 = nextRecord[6];
                col008 = nextRecord[7];
                col009 = nextRecord[8];
                col010 = nextRecord[9];
                col011 = nextRecord[10];
                col012 = nextRecord[11];
                firstPass = true;


            } else {
                aLCOL001.add( nextRecord[0]);
                //System.out.println(nextRecord[0] + "===========");
                aLCOL002.add( nextRecord[1]);
                aLCOL003.add( nextRecord[2]);
                aLCOL004.add( nextRecord[3]);
                aLCOL005.add( nextRecord[4]);
                aLCOL006.add( nextRecord[5]);
                aLCOL007.add( nextRecord[6]);
                aLCOL008.add( nextRecord[7]);
                aLCOL009.add( nextRecord[8]);
                aLCOL010.add( nextRecord[9]);
                aLCOL011.add( nextRecord[10]);
                aLCOL012.add( nextRecord[11]);
                //System.out.println("==");
            }
                dataFileMap.put( col001, aLCOL001);
                dataFileMap.put( col002, aLCOL002);
                dataFileMap.put( col003, aLCOL003);
                dataFileMap.put( col004, aLCOL004);
                dataFileMap.put( col005, aLCOL005);
                dataFileMap.put( col006, aLCOL006);
                dataFileMap.put( col007, aLCOL007);
                dataFileMap.put( col008, aLCOL008);
                dataFileMap.put( col009, aLCOL009);
                dataFileMap.put( col010, aLCOL010);
                dataFileMap.put( col011, aLCOL011);
                dataFileMap.put( col012, aLCOL012);
        }

        //for ( int j = 48; j < 50; j++){

           // System.out.println(dataFileMap.get(col004).get(j));
        //}
    }

        return "DIYDA_pages/dataType_pag01";
    }

    @GetMapping("/displayDataFile")
    public String show(Model model) {

        HashMap<String, ArrayList> dataFileMapToPass = dataFileMap;
        model.addAttribute("dataHashMap",dataFileMapToPass);
        return "DIYDA_pages/displayDataFile";
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


