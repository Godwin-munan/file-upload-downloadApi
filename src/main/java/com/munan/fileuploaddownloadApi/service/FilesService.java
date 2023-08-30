
package com.munan.fileuploaddownloadApi.service;

import com.munan.fileuploaddownloadApi.model.FileData;
import com.munan.fileuploaddownloadApi.repository.FilesRepository;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class FilesService {
    
    @Value("${baseUrl}")
    private String baseUrl;
    
    @Value("${fileDir}")
    private String fileDir;
    
    private final FilesRepository repository;
    
    
    public Mono<String> uploadImageToFileSystem(Mono<FilePart> fileMono) 
            throws IOException {
                
          return fileMono.flatMap(file -> {
              String randomName = UUID.randomUUID()
                      .toString()
                      .substring(0, 13);
              
              
              String originalFileName = file.filename();
              
              int lastDotIndex = originalFileName.lastIndexOf(".");
              String extension = (lastDotIndex != -1) ? originalFileName
                .substring(lastDotIndex) : "";
              
              String newFileName = randomName+extension.toLowerCase();
              
              String filePath = Paths.get(fileDir+newFileName)
                      .normalize()
                      .toAbsolutePath()
                      .toString();
              String returnLink = baseUrl+"/api/files/"+newFileName;
              
              return repository.insert(FileData.builder()
                    .name(newFileName)
                    .type(file.headers().getContentType().toString())
                    .filePath(returnLink)
                    .build())
                      .doOnNext(savedFile -> file.transferTo(Path.of(filePath))
                              .subscribe())
                      .flatMap(savedFile -> Mono.just("file uploaded successfully: "+returnLink));
          });

    }

    public Flux<Resource> downloadImageFromFileSystem(String fileName){

            return repository.findByName(fileName)
                    .flatMap(savedFile -> Flux
                            .just(new FileSystemResource(fileDir+savedFile.getName())));

    }
    
    
}


