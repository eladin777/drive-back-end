package com.loivgehoto.disk.Model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class Chunk
{
    private Long id;
    private Boolean skipUpload=false;
//    private Boolean needMerge=false;

     private List uploaded;

//     只有在后端添加对应属性，前端上传器自定的额外参数才能正确发送出来
     private String user_name;

    /**
     * 当前文件块，从1开始
     */


    private Integer chunkNumber;
    /**
     * 分块大小
     */

    private Long chunkSize;
    /**
     * 当前分块大小
     */

    private Long currentChunkSize;
    /**
     * 总大小
     */

    private Long totalSize;
    /**
     * 文件标识
     */

    private String identifier;
    /**
     * 文件名
     */

    private String filename;
    /**
     * 相对路径
     */

    private String relativePath;
    /**
     * 总块数
     */

    private Integer totalChunks;
    /**
     * 文件类型
     */

    private String type;


    private MultipartFile file;
}
