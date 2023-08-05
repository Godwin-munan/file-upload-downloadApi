package com.munan.fileuploaddownloadApi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "fileData")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileData {
    
    @Id
    private String id;
    
    private String name;
    private String type;
    private String filePath;    
    
}
