
package com.munan.fileuploaddownloadApi.service;

import static com.munan.fileuploaddownloadApi.constants.FileConstant.FILE_PATH;
import com.munan.fileuploaddownloadApi.model.FileData;
import com.munan.fileuploaddownloadApi.repository.FilesRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class FilesService {
    
    @Value("${baseUrl}")
    private String baseUrl;
    
    private final FilesRepository repository;
    
    private String PATTERN = "[A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            +"[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    byte[] file;
    
    public Mono<String> uploadImageToFileSystem(Mono<FilePart> fileMono) throws IOException, Exception {
        
          return fileMono.flatMap(file -> {
              String filePath = Paths.get(FILE_PATH+"\\"+file.filename())
                      .normalize()
                      .toAbsolutePath()
                      .toString();
              
                    /*            
                      var randomName = UUID.randomUUID()
                              .toString()
                              .substring(0, 13);
                      file.filename().replaceFirst(PATTERN, randomName);
                    */
                    
              return repository.insert(FileData.builder()
                    .name(file.filename())
                    .type(file.headers().getContentType().toString())
                    .filePath(filePath)
                    .build())
                      .doOnNext(savedFile -> file.transferTo(Path.of(filePath)).subscribe())
                      .flatMap(savedFile -> Mono.just("file uploaded successfully: "+baseUrl+"/api/files/" + savedFile.getName()));
          });

    }

    public Flux<byte[]> downloadImageFromFileSystem(Flux<String> fileNameMono){
        
        return fileNameMono.flatMap(fileName -> repository.findByName(fileName))
                .flatMap(fileData -> {
                    try {
                            file = Files.readAllBytes(new File(fileData.getFilePath()).toPath());
                        } catch (IOException ex) {
                            Logger.getLogger(FilesService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    return Flux.just(file);});
        
    }
    
    
}


