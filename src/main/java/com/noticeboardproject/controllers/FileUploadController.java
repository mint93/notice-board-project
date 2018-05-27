package com.noticeboardproject.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.noticeboardproject.storage.StorageService;

@Controller
public class FileUploadController {

	private final StorageService storageService;
	
	@Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }
	
	@PostMapping("/uploadFile")
	@ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile, Principal principal) {
        	if (!uploadfile.isEmpty()) {
                storageService.store(uploadfile, principal.getName());
                return new ResponseEntity<>("Successfully uploaded - " + uploadfile.getOriginalFilename(), HttpStatus.OK);
            }else {
            	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
    }
	
	@GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, Principal principal) {
        Resource file = storageService.loadAsResource(filename, principal.getName());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
	
	@GetMapping("/files/{filename:.+}/delete")
    @ResponseBody
    public ResponseEntity<?> deleteFile(@PathVariable String filename, Principal principal) {

        storageService.delete(filename, principal.getName());
        return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
    }
}
