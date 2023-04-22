package com.loivgehoto.disk.Service;

//import com.loivgehoto.disk.Util.MybatisUtil;
import com.loivgehoto.disk.Mapper.FileMapper;
import com.loivgehoto.disk.Model.File;
import com.loivgehoto.disk.Model.Shared;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileService
{

    @Autowired
    private FileMapper fileMapper;

    //    将文件与文件夹移至回收站
    public void move_file_to_recycle_bin(String user_name,String file_name,String source_file_path)
    {
        fileMapper.move_file_to_recycle_bin(user_name,file_name,source_file_path);
    }
    public void delete_file_completely(String file_name,String user_name)
    {
        fileMapper.delete_file_completely(file_name,user_name);
    }
    public String restore_file_get_file_source_path(String file_name,String user_name)
    {
       return fileMapper.restore_file_get_file_source_path(file_name,user_name);
    }
    public String save_shared_file(String user_name,String file_name,String file_path,String uuid,Long file_size,String create_time,String suffix,String password)
    {
        return fileMapper.save_shared_file( user_name, file_name, file_path, uuid, file_size, create_time, suffix, password);
    }
    public List<Shared> shared_page(String uuid)
    {
        return fileMapper.shared_page(uuid);
    }
    public List<Shared> check_share_password(String uuid,String password)
    {
        return fileMapper.check_share_password(uuid,password);
    }
    public String check_share_password_is_exit(String uuid)
    {
        return fileMapper.check_share_password_is_exit(uuid);
    }
    public List<Shared> share_management_page(String user_name)
    {
        return fileMapper.share_management_page(user_name);
    }
    public void cancel_share(String file_path)
    {
        fileMapper.cancel_share(file_path);
    }



//    public void AddFile(File file)
//    {
//
////        SqlSession sqlSession= MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map=sqlSession.getMapper(FileMapper.class);
////        map.addFile(file);
////        MybatisUtil.closeSqlSession(sqlSession);
//
//    }
//    public List<File> SearchAllFile_by_name_asc(String name)
//    {
//
//        java.io.File file=new java.io.File("/Cloud_Disk/");
//        java.io.File[] tempList = file.listFiles();
//        List<File> list = new ArrayList<>();
////        for (int i = 0; i < tempList.length; i++)
////        {
////            if (tempList[i].isDirectory()) {
////                System.out.println("文件夹：" + tempList[i]);
////                com.loivgehoto.disk.model.File temp=new com.loivgehoto.disk.model.File();
////                temp.setFile_name(tempList[i].getName());
////                temp.setSuffix("folder");
////                list.add(temp);
////            }
////        }
////
////        SqlSession sqlSession= MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map=sqlSession.getMapper(FileMapper.class);
////        list=(map.SearchAllFile_by_name_asc(name));
////        MybatisUtil.closeSqlSession(sqlSession);///////important未写此句关闭该sqlsession会导致在文件列表中快速点击翻页，网络直接卡住，无法跳转
//
////        for (File f:list)
////        {
////            System.out.println("list的内容"+f.getFile_name());
////        }
//
//        return list;
//    }
//
//    public List<File> SearchAllFile_by_name_desc(String name)
//    {
//
//        java.io.File file=new java.io.File("/Cloud_Disk/");
//
//        java.io.File[] tempList = file.listFiles();
//        List<File> list = new ArrayList<>();
//
////
////        SqlSession sqlSession= MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map=sqlSession.getMapper(FileMapper.class);
////        list=(map.SearchAllFile_by_name_desc(name));
////        MybatisUtil.closeSqlSession(sqlSession);///////important未写此句关闭该sqlsession会导致在文件列表中快速点击翻页，网络直接卡住，无法跳转
//
//        return list;
//    }
//
//    public List<File> SearchAllFile_by_size_asc(String name)
//    {
//
//        java.io.File file=new java.io.File("/Cloud_Disk/");
//        List<File> list = new ArrayList<>();
//
////
////        SqlSession sqlSession= MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map=sqlSession.getMapper(FileMapper.class);
////        list=(map.SearchAllFile_by_size_asc(name));
////        MybatisUtil.closeSqlSession(sqlSession);///////important未写此句关闭该sqlsession会导致在文件列表中快速点击翻页，网络直接卡住，无法跳转
//
//        return list;
//    }
//    public List<File> SearchAllFile_by_size_desc(String name)
//    {
//
//        java.io.File file=new java.io.File("/Cloud_Disk/");
//        List<File> list = new ArrayList<>();
////
////
////        SqlSession sqlSession= MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map=sqlSession.getMapper(FileMapper.class);
////        list=(map.SearchAllFile_by_size_desc(name));
////        MybatisUtil.closeSqlSession(sqlSession);///////important未写此句关闭该sqlsession会导致在文件列表中快速点击翻页，网络直接卡住，无法跳转
//
//        return list;
//    }
//
//    public List<File> SearchAllFile_by_time_asc(String name)
//    {
//
//        java.io.File file=new java.io.File("/Cloud_Disk/");
//        List<File> list = new ArrayList<>();
////
////
////        SqlSession sqlSession= MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map=sqlSession.getMapper(FileMapper.class);
////        list=(map.SearchAllFile_by_time_asc(name));
////        MybatisUtil.closeSqlSession(sqlSession);///////important未写此句关闭该sqlsession会导致在文件列表中快速点击翻页，网络直接卡住，无法跳转
//
//        return list;
//    }
//    public List<File> SearchAllFile_by_time_desc(String name)
//    {
//
//        java.io.File file=new java.io.File("/Cloud_Disk/");
//        List<File> list = new ArrayList<>();
//
////
////        SqlSession sqlSession= MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map=sqlSession.getMapper(FileMapper.class);
////        list=(map.SearchAllFile_by_time_desc(name));
////        MybatisUtil.closeSqlSession(sqlSession);///////important未写此句关闭该sqlsession会导致在文件列表中快速点击翻页，网络直接卡住，无法跳转
//
//        return list;
//    }
//
//    public List<File> Return_Search_Result(String user_name,String file_name)
//    {
//        List<File> list = new ArrayList<>();
////        SqlSession sqlSession= MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map=sqlSession.getMapper(FileMapper.class);
////        list=(map.Return_Search_Result(user_name,file_name));
////        MybatisUtil.closeSqlSession(sqlSession);///////important未写此句关闭该sqlsession会导致在文件列表中快速点击翻页，网络直接卡住，无法跳转
//        return list;
//    }
//
////-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓在数据库找所有文件
//    public List<File> SearchAllFile_in_recycle_bin(String use_name)
//    {
//
////        java.io.File file=new java.io.File("D:/TEST/");
////        java.io.File[] tempList = file.listFiles();
//
//        List<File> list = new ArrayList<>();
//
////        SqlSession sqlSession= MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map=sqlSession.getMapper(FileMapper.class);
////        list=(map.SearchAllFile_in_recycle_bin(use_name));
////        MybatisUtil.closeSqlSession(sqlSession);///////important未写此句关闭该sqlsession会导致在文件列表中快速点击翻页，网络直接卡住，无法跳转
//
//        return list;
//    }
//    //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑在数据库找所有文件
//
//    //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓在数据库找所有文件夹
//    public List<File> SearchAllFolder_in_recycle_bin(String use_name)
//    {
//        List<File> list = new ArrayList<>();
//
////        SqlSession sqlSession= MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map=sqlSession.getMapper(FileMapper.class);
////        list=(map.SearchAllFolder_in_recycle_bin(use_name));
////        MybatisUtil.closeSqlSession(sqlSession);///////important未写此句关闭该sqlsession会导致在文件列表中快速点击翻页，网络直接卡住，无法跳转
//
//        return list;
//    }
//    //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑在数据库找所有文件夹
//
//
//    public void delete_file(String file_name, String user_name)
//    {
////        SqlSession sqlSession= MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map=sqlSession.getMapper(FileMapper.class);
////        map.delete_file(file_name,user_name);
////        MybatisUtil.closeSqlSession(sqlSession);
//    }
//
//
//
//
//    public void From_rootFolder_to_recycle_bin_folder(String file_name,String user_name,String file_path,String file_suffix,String create_time)
//    {
////        SqlSession sqlSession= MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map=sqlSession.getMapper(FileMapper.class);
////        map.From_rootFolder_to_recycle_bin_folder(file_name, user_name, file_path,file_suffix,create_time);
////        MybatisUtil.closeSqlSession(sqlSession);
//    }
//    //    将文件与文件夹移至回收站
//
//
//
//    public void From_recycle_bin_to_rootFolder(String file_name)
//    {
////        SqlSession sqlSession= MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map=sqlSession.getMapper(FileMapper.class);
////        map.From_recycle_bin_to_rootFolder(file_name);
////        MybatisUtil.closeSqlSession(sqlSession);
//    }
//
//    public void 5share(String user_name, String file_name,String file_path,String share_url,String file_size,String create_time,String file_suffix) {
////        SqlSession sqlSession = MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map = sqlSession.getMapper(FileMapper.class);
////        map.share(user_name, file_name,file_path,share_url,file_size,create_time,file_suffix);
////        MybatisUtil.closeSqlSession(sqlSession);
//    }
//    public void share_folder(String user_name, String file_name,String file_path,String share_url,String create_time,String file_suffix) {
////        SqlSession sqlSession = MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map = sqlSession.getMapper(FileMapper.class);
////        map.share_folder(user_name, file_name,file_path,share_url,create_time,file_suffix);
////        MybatisUtil.closeSqlSession(sqlSession);
//    }
//
////    public void cancel_share(String share_url)
////    {
//////        SqlSession sqlSession = MybatisUtil.createSQLsession();
//////        FileMapper map = sqlSession.getMapper(FileMapper.class);
//////        map.cancel_share(share_url);
//////        MybatisUtil.closeSqlSession(sqlSession);
////    }
//
//    public List<File> SearchAllFile_shared(String user_name) {
//        List<File> list = new ArrayList<>();
//
////        SqlSession sqlSession = MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map = sqlSession.getMapper(FileMapper.class);
////        list = (map.SearchAllFile_shared(user_name));
////        MybatisUtil.closeSqlSession(sqlSession);///////important未写此句关闭该sqlsession会导致在文件列表中快速点击翻页，网络直接卡住，无法跳转
//
//        return list;
//    }
//
//    public void set_url(String url,String user_name, String file_name) {
////        SqlSession sqlSession = MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map = sqlSession.getMapper(FileMapper.class);
////        map.set_url(url,user_name, file_name);
////        MybatisUtil.closeSqlSession(sqlSession);
//    }
//
//    public File SearchSingleFile_shared(String share_url) {
//
////        SqlSession sqlSession = MybatisUtil.createSQLsession();//////////util类没写好总是空指针
////        FileMapper map = sqlSession.getMapper(FileMapper.class);
////        File list = (map.SearchSingleFile_shared(share_url));
////        MybatisUtil.closeSqlSession(sqlSession);///////important未写此句关闭该sqlsession会导致在文件列表中快速点击翻页，网络直接卡住，无法跳转
//
//
//        File list=new File();
//
//        return list;
//    }
}
