
package com.munan.fileuploaddownloadApi.service;

import static com.munan.fileuploaddownloadApi.constants.FileConstant.FILE_PATH;
import com.munan.fileuploaddownloadApi.model.FileData;
import com.munan.fileuploaddownloadApi.repository.FilesRepository;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    
    private String fileDirectoryPath;
    FileSystemResource file;
    
    
    public Mono<String> uploadImageToFileSystem(Mono<FilePart> fileMono) throws IOException, Exception {
                
          return fileMono.flatMap(file -> {
              String filePath = Paths.get(fileDir+file.filename())
                      .normalize()
                      .toAbsolutePath()
                      .toString();
              String returnLink = baseUrl+"/api/files/"+file.filename();
              
                    /*            
                      var randomName = UUID.randomUUID()
                              .toString()
                              .substring(0, 13);
                      file.filename().replaceFirst(PATTERN, randomName);
                    */
                    
              return repository.insert(FileData.builder()
                    .name(file.filename())
                    .type(file.headers().getContentType().toString())
                    .filePath(returnLink)
                    .build())
                      .doOnNext(savedFile -> file.transferTo(Path.of(filePath)).subscribe())
                      .flatMap(savedFile -> Mono.just("file uploaded successfully: "+returnLink));
          });

    }

    public Flux<Resource> downloadImageFromFileSystem(String fileName){

            return repository.findByName(fileName)
                    .log()
                    .flatMap(savedFile -> Flux.just(new FileSystemResource(fileDir+savedFile.getName())));

    }
    
    
}


