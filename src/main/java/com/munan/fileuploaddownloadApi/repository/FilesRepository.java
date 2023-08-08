
package com.munan.fileuploaddownloadApi.repository;

import com.munan.fileuploaddownloadApi.model.FileData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface FilesRepository extends ReactiveMongoRepository<FileData, String>{
        Flux<FileData> findByName(String name);    
}
