package com.loivgehoto.disk.Model;

import lombok.Data;

@Data
public class File {
//    public String uuid;

    public String suffix;
    //    private int file_id;
    private String file_name;
    private String file_path;
    private String parent_path;

    private String create_time;
//    private int in_folder;
    private Long file_size;

//    函数名字代表了thymeleaf取model中的属性名的名称，例如${file.file_name}代表文件对象的文件名属性（file_name是public String getFile_name()的函数名名称）
//    ${file.suffix}代表文件对象的扩展名属性（suffix是 public String getSuffix()的函数名名称，${file.file_suffix}会出错）


}
