package com.munan.fileuploaddownloadApi.controller;


import com.munan.fileuploaddownloadApi.service.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FilesController {
    
    private final FilesService service;
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImageOfFileSystem(@RequestPart("image")Mono<FilePart> fileMono) throws Exception{
        System.out.println("Show Request:::::");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.uploadImageToFileSystem(fileMono));
    }
    
    
    @GetMapping(value = "/{fileName}")
    public ResponseEntity<Flux<Resource>> downloadFile(@PathVariable String fileName){
        
        return ResponseEntity.ok()       
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
              .contentType(MediaType.APPLICATION_OCTET_STREAM)
              .body(service.downloadImageFromFileSystem(fileName));
        
    }
    
}
