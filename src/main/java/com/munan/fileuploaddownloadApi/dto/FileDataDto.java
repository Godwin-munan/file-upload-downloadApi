
package com.munan.fileuploaddownloadApi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDataDto {

    private String id;
    private String name;
    private String type;
    private String filePath;    
}
