package com.loivgehoto.disk.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.loivgehoto.disk.Model.File;
import com.loivgehoto.disk.Model.Shared;


import java.util.List;

public interface FileMapper extends BaseMapper<File> {

    public void move_file_to_recycle_bin(String user_name,String file_name,String source_file_path);

    public void delete_file_completely(String file_name,String user_name);

    public String restore_file_get_file_source_path(String file_name,String user_name);

    public String save_shared_file(String user_name,String file_name,String file_path,String uuid,Long file_size,String create_time,String suffix,String password);

    public List<Shared> shared_page(String uuid);

    public List<Shared> check_share_password(String uuid,String password);
    public String check_share_password_is_exit(String uuid);
    public List<Shared> share_management_page(String user_name);
    public void cancel_share(String file_path);


//
//    public void addFile(File file);
//
//    public List<File> SearchAllFile_by_name_asc(String name);
//
//    public List<File> SearchAllFile_by_name_desc(String name);
//
//    public List<File> SearchAllFile_by_size_asc(String name);
//
//    public List<File> SearchAllFile_by_size_desc(String name);
//
//    public List<File> SearchAllFile_by_time_asc(String name);
//
//    public List<File> SearchAllFile_by_time_desc(String name);
//
//
//
//    public List<File>  Return_Search_Result(String user_name,String file_name);
//
//    public  void delete_file(String file_name,String user_name);
//
//
//    public List<File> SearchAllFile_in_recycle_bin(String use_name);
//    public List<File> SearchAllFolder_in_recycle_bin(String use_name);
//
//
////    将文件与文件夹移至回收站
//
//
//    public void From_rootFolder_to_recycle_bin_folder(String file_name,String user_name,String file_path,String file_suffix,String create_time);
//
//
//
//
//    public void From_recycle_bin_to_rootFolder(String file_name);

    public void share(String user_name,String file_name,String file_path,String share_url,String file_size,String create_time,String file_suffix);
//    public void share_folder(String user_name,String file_name,String file_path,String share_url,String create_time,String file_suffix);
//
////    public void cancel_share(String share_url);
//
//    public void set_url(String url, String user_name,String file_name);
//
//    public List<File> SearchAllFile_shared(String user_name);
//
//    public File SearchSingleFile_shared(String share_url);
}
