
package com.munan.fileuploaddownloadApi.service;

import static com.munan.fileuploaddownloadApi.constants.FileConstant.FILE_PATH;
import com.munan.fileuploaddownloadApi.model.FileData;
import com.munan.fileuploaddownloadApi.repository.FilesRepository;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class FilesService {
    
    private final FilesRepository repository;
    
    byte[] file;
    
    public Mono<String> uploadImageToFileSystem(Mono<FilePart> fileMono) throws IOException, Exception {
        
          return fileMono.flatMap(file -> {
              String filePath = Paths.get(FILE_PATH+"\\"+file.filename())
                      .normalize()
                      .toAbsolutePath()
                      .toString();
              
              return repository.insert(FileData.builder()
                    .name(file.filename())
                    .type(file.headers().getContentType().toString())
                    .filePath(filePath)
                    .build())
                      .doOnNext(savedFile -> file.transferTo(Path.of(filePath)).subscribe())
                      .flatMap(savedFile -> Mono.just("file uploaded successfully " + savedFile.getFilePath()));
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


