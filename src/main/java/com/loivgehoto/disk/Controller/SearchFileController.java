package com.loivgehoto.disk.Controller;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchFileController
{
    private List<com.loivgehoto.disk.Model.File> list=null;

    SearchFileController(List<com.loivgehoto.disk.Model.File> list)
    {
        this.list=list;
    }

    public void search_file(File file, String key_name) throws UnsupportedEncodingException {
        if(file.isDirectory())
        {
            if (file.getName().toLowerCase().contains(key_name.toLowerCase()))
            {
                com.loivgehoto.disk.Model.File temp=new com.loivgehoto.disk.Model.File();
                temp.setFile_name(file.getName());
                temp.setSuffix("folder");

                String path_temp= URLEncoder.encode(file.getPath(),"UTF-8");
                temp.setFile_path(path_temp);

                temp.setParent_path(file.getParent());
                // -------------------------------获取文件夹最后修改时间并格式化时间
                long create_time=file.lastModified();
                Date temp_date=new Date(create_time);
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = format1.format(temp_date);
                //-------------------------------
                temp.setCreate_time(time);
                list.add(temp);
            }
            File[] listFiles = file.listFiles();
            for(File f:listFiles) {
                search_file(f,key_name);
            }
        }
        else
        {
            if (file.getName().toLowerCase().contains(key_name.toLowerCase()))
            {
                com.loivgehoto.disk.Model.File temp=new com.loivgehoto.disk.Model.File();
                temp.setFile_name(file.getName());
                String suffix=file.getName().substring(file.getName().lastIndexOf(".") + 1);//获取后缀名
                temp.setSuffix(suffix);
                temp.setFile_size(file.length());

                //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓设置当前文件的路径
                String path_temp= URLEncoder.encode(file.getPath(),"UTF-8");

                temp.setFile_path(path_temp);
                //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

                temp.setParent_path(file.getParent());

                // -------------------------------获取文件最后修改时间并格式化时间
                long create_time=file.lastModified();
                Date temp_date=new Date(create_time);
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = format1.format(temp_date);
                //-------------------------------

                temp.setCreate_time(time);
                list.add(temp);
            }
        }
    }



}