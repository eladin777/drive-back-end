package com.loivgehoto.disk.Controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.loivgehoto.disk.Service.FileService;
import com.loivgehoto.disk.Service.UserService;

import com.loivgehoto.disk.Util.JjwtUtil;
import com.loivgehoto.disk.Model.File_Path;
import com.loivgehoto.disk.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.UUID;


@Controller
public class UserController
{
//    /////////位于application.properties中的全局自定义参数，来切换在windows和linux系统下网站运行时文件的路径
//    @Value("${platform.System_Name}")
//    private String System_Name;
//    ////////////////////////////////////

    //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓将路径保存在全局配置文件
    @Value("${platform.File_Path}")
    private String File_Path;
    //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑将路径保存在全局配置文件

    //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓表示路径，最右侧带符号/
    private String Current_Path=null;
    //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑




//    linux路径是/，windosw是\
    @Value("${platform.slash}")
    char slash;

//
//    @RequestMapping( "/")
//    public String index()
//    {
//
//        return "index";
//    }

    @Autowired
    private UserService userService;


    //    登录按钮后的请求
    @GetMapping("/login")
    @ResponseBody
//    public String LoginJudge (@RequestParam("username")String name, @RequestParam("password")String password,HttpSession session)
    public User LoginJudge (User user)
    {
//        User user=new User();
//        user.setName(name);
//        user.setPassword(password);
//        String Name=name;

        JjwtUtil  jjwtUtil=new JjwtUtil();



//        UserService service=new UserService();
        String check= userService.checkUser(user);


         if(check!=null)//////////之前用check.equals(null)是错的，null不是对象，空指针报错
         {
//             session.setAttribute("user_name",Name);
//             return "right";
             user.setToken(jjwtUtil.jwt_token(user.getName()));
             return user;
//             return "SUCESS!!!!!!!!!!!!!!!";
         }
         else
             return null;

//        System.out.printf("名称："+name+"密码："+password);


    }

    @GetMapping("/check_token")
    @ResponseBody
    public Boolean check_token(HttpServletRequest request)
    {
        String token=request.getHeader("token");
        JjwtUtil  jjwtUtil=new JjwtUtil();

        return jjwtUtil.decrypt_token(token);
//        if(jjwtUtil.decrypt_token(token))
//            return "true";
//        else
//            return "false";
    }



    @RequestMapping( "/register_home")
    public String Register_home()
    {
        return "register";
    }


/*
点击注册后进行注册用户操作
 */
    @GetMapping( "/register")
    @ResponseBody
    public String Register(@RequestParam("username")String name,@RequestParam("password")String password,HttpSession session)
    {
//        UserService service=new UserService();

        User user=new User();
        user.setName(name);
        user.setPassword(password);

        userService.Register(user);
//        session.setAttribute("user_name",name);
        try {
//-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓创建新用户的三个文件夹

            File dictionary=new File(File_Path+name);
            dictionary.mkdir();

            File dictionary2=new File(File_Path+name+"/Bin/");
            dictionary2.mkdir();

            File dictionary3=new File(File_Path+name+"/File/");
            dictionary3.mkdir();

//-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑创建新用户的三个文件夹
        } catch (Exception e) {
//            throw new RuntimeException(e);
            return "false";
        }

        return "true";

    }

/*
注册界面，输入完名字后ajax检查用户是否存在
 */
    @GetMapping("/register_check")
    @ResponseBody
    public String Register_check (@RequestParam("username")String name,HttpSession session)
    {
//        System.out.println("register");
//        UserService service=new UserService();
        String check= userService.Register_check(name);

        if(check!=null)////
        {
//            session.setAttribute("user_name",name);
            return "false";
        }
        else
            return "true";
    }






//    //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓跳转视频播放器
//     @RequestMapping( "/video")//////////
//     public String video( @RequestParam("video_path")String path ,@RequestParam("video_name")String name,HttpSession session) throws UnsupportedEncodingException
//     {
//        String video_path=URLDecoder.decode(path,"UTF-8");
//
//
//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓根据运行的系统的不同切割字符串
//        if(File_Path.equals("D:\\Cloud_Disk\\"))
//             video_path=video_path.substring(14);
//        else
//            video_path=video_path.substring(12);
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑根据运行的系统的不同切割字符串
//
//
//        System.out.println("videopath"+video_path);
//
//        session.setAttribute("video_path",video_path);
//        session.setAttribute("video_name",name);
//        return "video";
//    }
//    //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


    @GetMapping("/home_folder")
    @ResponseBody
    public List<com.loivgehoto.disk.Model.File> home_folder( HttpSession session,String user_name) throws UnsupportedEncodingException
    {
        ////
//        Current_Path=File_Path+user_name+slash+"File"+slash;////将Current_Path初始化（设为根目录路径）
//        session.setAttribute("Current_Path",Current_Path);
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

        List<com.loivgehoto.disk.Model.File> folder_list=searchFolder(File_Path+ user_name+slash+"File");/////session过期无法拿到用户名,在此报错
        session.setAttribute("Folder_Amount",folder_list.size());
//        model.addAttribute("folder_list",list0);
        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑搜索所有文件夹
        return folder_list;
    }

    @GetMapping( "/home_file")
    @ResponseBody
    public List<com.loivgehoto.disk.Model.File> home_file( HttpSession session,String user_name) throws UnsupportedEncodingException {

//        String user_name=(String) session.getAttribute("user_name");

        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓设置目前用户处在的文件路径
        String Current_Path=File_Path+user_name+slash+"File"+slash;////将Current_Path初始化（设为根目录路径）
        session.setAttribute("Current_Path",Current_Path);
        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

        // session.setAttribute("next_folder",null);////


//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓session过期无法拿到用户名，返回登录
//        if(StringUtils.isEmpty(user_name))
//        {
//            return "index";
//        }
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑



//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓搜索所有文件夹，seesion加文件夹数
//        List<com.loivgehoto.disk.Model.File> list0=searchFolder(File_Path+ user_name+slash+"File");/////session过期无法拿到用户名,在此报错
//        session.setAttribute("Folder_Amount",list0.size());
////        model.addAttribute("folder_list",list0);
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑搜索所有文件夹



        ///-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓搜索所有文件
        List<com.loivgehoto.disk.Model.File> file_list= searchFile(Current_Path);
        ///-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑搜索所有文件


        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓向seesion添加所有文件数量。向model添加所有文件集合
//        session.setAttribute("File_Amount",file_list.size());
//        model.addAttribute("file_list",file_list);
        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

        return file_list;

//        return "home";
    }

    @GetMapping( "/bin_file")
    @ResponseBody
    public List<com.loivgehoto.disk.Model.File> bin_file( String user_name) throws UnsupportedEncodingException {
        String Current_Path=File_Path+user_name+slash+"Bin"+slash;////将Current_Path初始化（设为根目录路径）
        ///-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓搜索所有文件
        List<com.loivgehoto.disk.Model.File> file_list= searchFile(Current_Path);
        ///-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑搜索所有文件

        return file_list;
    }
    @GetMapping("/bin_folder")
    @ResponseBody
    public List<com.loivgehoto.disk.Model.File> bin_folder(String user_name) throws UnsupportedEncodingException
    {
        List<com.loivgehoto.disk.Model.File> folder_list=searchFolder(File_Path+ user_name+slash+"Bin");/////session过期无法拿到用户名,在此报错
        return folder_list;
    }



    @GetMapping( "/crumbs_path")
    @ResponseBody
    public  List<com.loivgehoto.disk.Model.File_Path> crumbs_path(String folder_path,String user_name,Boolean multi_files_state) throws UnsupportedEncodingException {

        if (folder_path!=null)
        //        将文件路径url解码
        folder_path=URLDecoder.decode(folder_path,"UTF-8");

        if (multi_files_state==true)
        {
            folder_path=folder_path.substring(0,folder_path.lastIndexOf(slash));
        }

        String path_temp=folder_path;
        List<com.loivgehoto.disk.Model.File_Path> path_list=new ArrayList<File_Path>();

        if (path_temp!=null)
        {

            //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓获取当前路径的所有父路径（全路径名），到用户根目录后一个结束；到界面显示文件路径面包屑
            while(true)
            {
                if(!path_temp.equals(File_Path+user_name+slash+"File"))
                {
                    File_Path temp_file_path_object=new File_Path();

                    String folder_name=path_temp.substring(path_temp.lastIndexOf(slash)+1,path_temp.length());
                    String full_path=path_temp;

//                    System.out.println("folder_name"+folder_name);
//                    System.out.println("full_path"+full_path);

                    temp_file_path_object.setFolder_name(folder_name);
                    temp_file_path_object.setFull_Path(full_path);


                    path_list.add(temp_file_path_object);


                    // .substring(a, b)表示截取下标从a开始到b结束的字符，包含第a个字符但是不包含第b个字符，可以看成[a,b)。
                    //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓获取当前目录的父路径
                    path_temp=path_temp.substring(0,path_temp.lastIndexOf(slash));
                    //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//                    System.out.println("path_temp"+path_temp);

                }
                else
                    break;
            }
            Collections.reverse(path_list);///反转获取到的集合，因为循环是从当前路径往根目录前进的，因此要反转集合，以便于显示文件路径的面包屑，在界面上
            //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑获取当前路径的所有父路径（全路径名），到用户根目录后一个结束

        }

        return path_list;
    }

        ///进入某个目录
    @GetMapping( "/inToFolder_searchFolder")
    @ResponseBody
    public  List<com.loivgehoto.disk.Model.File> inToFolder_searchFolder(String folder_path,Model model, HttpSession session) throws UnsupportedEncodingException {

//        String user_name=(String) session.getAttribute("user_name");

        if (folder_path!=null)
        //        将文件路径url解码
        folder_path=URLDecoder.decode(folder_path,"UTF-8");


//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓session过期无法拿到用户名，返回登录
//        if(StringUtils.isEmpty(user_name))
//        {
//            return "index";
//        }
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


        String Current_Path=folder_path;

        session.setAttribute("Current_Path",Current_Path);


        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓搜索所有文件夹，seesion加文件夹数
        List<com.loivgehoto.disk.Model.File> folder_list=searchFolder(folder_path);

        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑搜索所有文件夹



        return folder_list;
    }


    @GetMapping( "/inToFolder_searchFile")
    @ResponseBody
    public  List<com.loivgehoto.disk.Model.File> inToFolder_searchFile(@RequestParam("folder_path")String folder_path) throws UnsupportedEncodingException
    {
        //        将文件路径url解码
        folder_path=URLDecoder.decode(folder_path,"UTF-8");

//        /-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓搜索所有文件
        List<com.loivgehoto.disk.Model.File> file_list= searchFile(folder_path);
        ///-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑搜索所有文件

        return file_list;
    }


//    ///进入某个目录
//    @RequestMapping( "/folder")
//    public String folder(@RequestParam("folder_path")String folder_path,Model model, HttpSession session) throws UnsupportedEncodingException {
//
//        String user_name=(String) session.getAttribute("user_name");
//
//
//        //        将文件路径url解码
//        folder_path=URLDecoder.decode(folder_path,"UTF-8");
//
//
//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓session过期无法拿到用户名，返回登录
//        if(StringUtils.isEmpty(user_name))
//        {
//            return "index";
//        }
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//
//
//        Current_Path=folder_path;
//        session.setAttribute("Current_Path",Current_Path);
//
//
//        String path_temp=Current_Path;
//        List<com.loivgehoto.disk.Model.File_Path> path_list=new ArrayList<File_Path>();
//
//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓获取当前路径的所有父路径（全路径名），到用户根目录后一个结束；到界面显示文件路径面包屑
//        while(true)
//        {
//            if(!path_temp.equals(File_Path+user_name+slash+"File"))
//            {
//                File_Path temp_file_path_object=new File_Path();
//
//                String folder_name=path_temp.substring(path_temp.lastIndexOf(slash)+1,path_temp.length());
//                String full_path=path_temp;
//
//                System.out.println("folder_name"+folder_name);
//                System.out.println("full_path"+full_path);
//
//                temp_file_path_object.setFolder_name(folder_name);
//                temp_file_path_object.setFull_Path(full_path);
//
//
//                path_list.add(temp_file_path_object);
//
//
//                // .substring(a, b)表示截取下标从a开始到b结束的字符，包含第a个字符但是不包含第b个字符，可以看成[a,b)。
//                //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓获取当前目录的父路径
//                path_temp=path_temp.substring(0,path_temp.lastIndexOf(slash));
//                //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//
//
//                System.out.println("path_temp"+path_temp);
//
//
//            }
//            else
//                break;
//        }
//
//        Collections.reverse(path_list);///反转获取到的集合，因为循环是从当前路径往根目录前进的，因此要反转集合，以便于显示文件路径的面包屑，在界面上
//        model.addAttribute("path_list",path_list);
//
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑获取当前路径的所有父路径（全路径名），到用户根目录后一个结束
//
//
////        for(int i=0;i<=path_list.size()-1;i++)
////        {
////            if(i==0)
////                System.out.println("------------------↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
////            else if(i==path_list.size()-1)
////                System.out.println("-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
////            System.out.println("path_list:"+path_list.get(i).getFolder_name());
////            System.out.println("path_list:"+path_list.get(i).getFull_Path());
////        }
//
//
//
//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓搜索所有文件夹，seesion加文件夹数
//        List<com.loivgehoto.disk.Model.File> list0=searchFolder(Current_Path);
//        session.setAttribute("Folder_Amount",list0.size());
//        model.addAttribute("folder_list",list0);
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑搜索所有文件夹
//
//
//        ///-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓搜索所有文件
//        List<com.loivgehoto.disk.Model.File> file_list= searchFile(Current_Path);
//        ///-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑搜索所有文件
//
//
//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓向seesion添加所有文件数量。向model添加所有文件集合
//        session.setAttribute("File_Amount",file_list.size());
//        model.addAttribute("file_list",file_list);
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//
//
//        return "folder";
//    }







    /*
    进入用户主页
     */
//    @RequestMapping( "/home")
//    public String home( Model model, HttpSession session) throws UnsupportedEncodingException {
//
//        String user_name=(String) session.getAttribute("user_name");
//
//
//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓设置目前用户处在的文件路径
//        Current_Path=File_Path+user_name+slash+"File"+slash;////将Current_Path初始化（设为根目录路径）
//        session.setAttribute("Current_Path",Current_Path);
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//
//       // session.setAttribute("next_folder",null);////
//
//
//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓session过期无法拿到用户名，返回登录
//        if(StringUtils.isEmpty(user_name))
//        {
//            return "index";
//        }
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//
//
//
//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓搜索所有文件夹，seesion加文件夹数
//        List<com.loivgehoto.disk.Model.File> list0=searchFolder(File_Path+ user_name+slash+"File");/////session过期无法拿到用户名,在此报错
//        session.setAttribute("Folder_Amount",list0.size());
//        model.addAttribute("folder_list",list0);
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑搜索所有文件夹
//
//
//
//        ///-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓搜索所有文件
//        List<com.loivgehoto.disk.Model.File> file_list= searchFile(Current_Path);
//        ///-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑搜索所有文件
//
//
//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓向seesion添加所有文件数量。向model添加所有文件集合
//        session.setAttribute("File_Amount",file_list.size());
//        model.addAttribute("file_list",file_list);
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//
//        return "home";
//    }




    /*
    folder.html中返回上一页
     */
//    @RequestMapping( "/previous_path")
//    public String previus_path(@RequestParam("folder_path")String folder_path,Model model, HttpSession session) throws UnsupportedEncodingException {
//        String user_name=(String) session.getAttribute("user_name");
////        if(previous_folder_is_root>0)
////            previous_folder_is_root-=1;
////        session.setAttribute("previous_folder_is_root",previous_folder_is_root);
////
////        Current_Path=previus_path;
//
//        folder_path=URLDecoder.decode(folder_path,"UTF-8");
//
//        File temp_file=new File(folder_path);
//
//        if(temp_file.getParent().equals(File_Path+user_name+"\\File"))
//            return "redirect:/home";
//        else
//        {
//            String temp_path=URLEncoder.encode(temp_file.getParent(),"UTF-8");
//            return "redirect:/folder?folder_path="+temp_path;
//        }
//
//
//            ///session.setAttribute("previous_folder_is_root","true");
//
//
//
////        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓如果Current_Path代表的当前路径不是用户的根目录,则substring获取到Current_Path路径的上一路径
////        if(!Current_Path.equals(File_Path+user_name+"/File/"))
////        {
////            Current_Path=Current_Path.substring(0, Current_Path.lastIndexOf('/'));
////            Current_Path=Current_Path.substring(0, Current_Path.lastIndexOf('/')+1);/////加1以便保留截取后的路径最右边的/符号
////
////            session.setAttribute("Current_Path",Current_Path);
////
////            //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓获取Current_Path的上一路径，以判断上一路径是否为根目录
////            String temp_string=Current_Path;
////            temp_string=temp_string.substring(0, temp_string.lastIndexOf('/'));
////            temp_string=temp_string.substring(0, temp_string.lastIndexOf('/')+1);
////
////            if(temp_string.equals(File_Path+user_name+"/File/"))
////                session.setAttribute("previous_path_is_root","true");
////            else
////                session.setAttribute("previous_path_is_root","false");
////            //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
////        }
////        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//
//
////        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓搜索所有文件夹，seesion加文件夹数
////        List<com.loivgehoto.disk.model.File> list0=searchFolder(Current_Path);
////        session.setAttribute("Folder_Amount",list0.size());
////        model.addAttribute("folder_list",list0);
////        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑搜索所有文件夹
////
////
////
////        ///-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓搜索所有文件
////        List<com.loivgehoto.disk.model.File> file_list= searchFile(Current_Path);
////        ///-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑搜索所有文件
////
////
////        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓向seesion添加所有文件数量。向model添加所有文件集合
////        session.setAttribute("File_Amount",file_list.size());
////        model.addAttribute("file_list",file_list);
////        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//
//    }

    //    public String folder(  @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
//                           @RequestParam("folder_name")String folder_name,@RequestParam("parent")String parent,Model model, HttpSession session)


//    ///进入某个目录
//    @RequestMapping( "/folder")
//    public String folder(@RequestParam("folder_path")String folder_path,Model model, HttpSession session) throws UnsupportedEncodingException {
//
//        String user_name=(String) session.getAttribute("user_name");
//
//
//        //        将文件路径url解码
//        folder_path=URLDecoder.decode(folder_path,"UTF-8");
//
//
//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓session过期无法拿到用户名，返回登录
//        if(StringUtils.isEmpty(user_name))
//        {
//            return "index";
//        }
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//
//
//        Current_Path=folder_path;
//        session.setAttribute("Current_Path",Current_Path);
//
//
//        String path_temp=Current_Path;
//        List<com.loivgehoto.disk.Model.File_Path> path_list=new ArrayList<File_Path>();
//
//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓获取当前路径的所有父路径（全路径名），到用户根目录后一个结束；到界面显示文件路径面包屑
//        while(true)
//        {
//            if(!path_temp.equals(File_Path+user_name+slash+"File"))
//            {
//                File_Path temp_file_path_object=new File_Path();
//
//                String folder_name=path_temp.substring(path_temp.lastIndexOf(slash)+1,path_temp.length());
//                String full_path=path_temp;
//
//                System.out.println("folder_name"+folder_name);
//                System.out.println("full_path"+full_path);
//
//                temp_file_path_object.setFolder_name(folder_name);
//                temp_file_path_object.setFull_Path(full_path);
//
//
//                path_list.add(temp_file_path_object);
//
//
//                // .substring(a, b)表示截取下标从a开始到b结束的字符，包含第a个字符但是不包含第b个字符，可以看成[a,b)。
//                //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓获取当前目录的父路径
//                path_temp=path_temp.substring(0,path_temp.lastIndexOf(slash));
//                //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//
//
//                System.out.println("path_temp"+path_temp);
//
//
//            }
//            else
//                break;
//        }
//
//        Collections.reverse(path_list);///反转获取到的集合，因为循环是从当前路径往根目录前进的，因此要反转集合，以便于显示文件路径的面包屑，在界面上
//        model.addAttribute("path_list",path_list);
//
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑获取当前路径的所有父路径（全路径名），到用户根目录后一个结束
//
//
////        for(int i=0;i<=path_list.size()-1;i++)
////        {
////            if(i==0)
////                System.out.println("------------------↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
////            else if(i==path_list.size()-1)
////                System.out.println("-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
////            System.out.println("path_list:"+path_list.get(i).getFolder_name());
////            System.out.println("path_list:"+path_list.get(i).getFull_Path());
////        }
//
//
//
//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓搜索所有文件夹，seesion加文件夹数
//        List<com.loivgehoto.disk.Model.File> list0=searchFolder(Current_Path);
//        session.setAttribute("Folder_Amount",list0.size());
//        model.addAttribute("folder_list",list0);
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑搜索所有文件夹
//
//
//        ///-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓搜索所有文件
//        List<com.loivgehoto.disk.Model.File> file_list= searchFile(Current_Path);
//        ///-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑搜索所有文件
//
//
//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓向seesion添加所有文件数量。向model添加所有文件集合
//        session.setAttribute("File_Amount",file_list.size());
//        model.addAttribute("file_list",file_list);
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//
//
//        return "folder";
//    }


////-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓回收站页面
//    @RequestMapping( "/bin")//////////
//    public String bin( @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
//                       @RequestParam(defaultValue="10",value="pageSize")Integer pageSize,HttpSession session,Model model) throws UnsupportedEncodingException {
//
//        String user_name=(String) session.getAttribute("user_name");
//
//
////        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓设置目前用户处在的文件路径
////        Current_Path=File_Path+user_name+"\\Bin\\";////将Current_Path初始化（设为根目录路径）
////        session.setAttribute("Current_Path",Current_Path);
////        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//
//
//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓session过期无法拿到用户名，返回登录
//        if(StringUtils.isEmpty(user_name))
//        {
//            return "index";
//        }
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//
//
//        FileService service=new FileService();
//        PageHelper.startPage(pageNum,pageSize);
//
//        List<com.loivgehoto.disk.Model.File> list=service.SearchAllFile_in_recycle_bin(user_name);
//        PageInfo<com.loivgehoto.disk.Model.File> info=new PageInfo<>(list);
//        model.addAttribute("pageInfo",info);
//
//
//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓不要通过pagehelper来获取所有文件夹在数据库中的，只在第一页显示
//        if(pageNum==1)
//        {
//            List<com.loivgehoto.disk.Model.File> list2=service.SearchAllFolder_in_recycle_bin(user_name);
//            model.addAttribute("folder_list",list2);
//        }
//
//        return "bin";
//
//    }
//    //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑回收站页面



//    @RequestMapping( "/search")//////////
//    @ResponseBody/////////////////////////////////////important，不加这个ajax get请求将不会被接受!!!!!!
//    public String search( @RequestParam("file_name")String file_name, HttpSession session)
//    {
//        session.setAttribute("search_file_name",file_name);
//        return "redirect:/search_result";////这里是无效的，由ajax去跳转
//    }
//
//    @RequestMapping( "/search_result")//////////
//    public String search_result( @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
//                                 @RequestParam(defaultValue="12",value="pageSize")Integer pageSize, Model model, HttpSession session)
//    {
//
//        String user_name=(String) session.getAttribute("user_name");//////从session中获取用户名，用于在网页右上角显示并数据库查询用户所有文件
//
//        FileService service=new FileService();
//        System.out.println("search_file_name=="+(String) session.getAttribute("search_file_name"));
//        PageHelper.startPage(pageNum,pageSize);
//        List<com.loivgehoto.disk.Model.File> list=service.Return_Search_Result(user_name,(String) session.getAttribute("search_file_name"));//        list.addAll(service.SearchAllFile(temp));//                searchFolder("D:/TEST/");
//        PageInfo<com.loivgehoto.disk.Model.File> info=new PageInfo<>(list);
//        model.addAttribute("pageInfo",info);
//
//        return "search_result";
//    }


//    @RequestMapping("/share_file")//////////////////////////////////////////////////important!!!!!!这里之前用了List集合，那是错的方法，以为这个方法代表分享文件的下载页面，只显示单个文件
//    //////  用List的话是返回多个File对象，用于给thymeleaf的foreach调用；由于分享文件下载页不会用到thymeleaf forreach，所以后端不能用List拿到结果，只能用单个File对象拿结果!!!!
//    public String share_file(@RequestParam("share_url")String share_url,Model model)
//    {
//        FileService service=new FileService();
//        com.loivgehoto.disk.Model.File file= service.SearchSingleFile_shared(share_url);
//        if(file!=null)
//        {
//            model.addAttribute("share_file",file);
//            return "share_file";
//        }
//        else
//            return "redirect:/404";
//    }
//




//    //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓分享动作
//    @RequestMapping("/share")
//    public String share(@RequestParam("file_name")String file_name,@RequestParam("file_path")String file_path,@RequestParam("file_size")String file_size,
//                        @RequestParam("create_time")String create_time, @RequestParam("suffix")String suffix,
//                        HttpSession session)
//    {
//        String user_name=(String) session.getAttribute("user_name");//////从session中获取用户名，用于在网页右上角显示并数据库查询用户所有文件
//        FileService service=new FileService();
//        String uuid=UUID();
//
//
////        if(suffix.equals("folder"))
////          ///  service.share_folder(user_name,file_name,file_path,uuid,create_time,suffix);
////        else
//            service.share(user_name,file_name,file_path,uuid,file_size,create_time,suffix);
//
//
//        return "redirect:/my_share";
//    }
//    //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑分享动作


    @RequestMapping("/cancel_share")
    @ResponseBody
    public void cancel_share(@RequestParam("share_url")String share_url)
    {
        FileService service=new FileService();
        service.cancel_share(share_url);
    }





//    //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓分享的主页
//    @RequestMapping("/my_share")
//    public String my_share(@RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
//                           @RequestParam(defaultValue="10",value="pageSize")Integer pageSize,HttpSession session,Model model)
//    {
//
//        String user_name=(String) session.getAttribute("user_name");
//
//        //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓session过期无法拿到用户名，返回登录
//        if(StringUtils.isEmpty(user_name))
//        {
//            return "index";
//        }
//        //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//
//        FileService service=new FileService();
//        PageHelper.startPage(pageNum,pageSize);///这一行顺序不能错了，一定得在最前面!!!!!!!!!!!!!!!!!!!!
//
//
//        List<com.loivgehoto.disk.Model.File> list=service.SearchAllFile_shared(user_name);
//        PageInfo<com.loivgehoto.disk.Model.File> info=new PageInfo<>(list);
//        model.addAttribute("pageInfo",info);
//
//        return "my_share";
//    }



    public String UUID()
    {
        UUID randomUUID = UUID.randomUUID();
        String UUID=(String)randomUUID.toString().replaceAll("-", "");;
        return UUID;
    }


    //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓搜索所有文件
    public List<com.loivgehoto.disk.Model.File> searchFile(String path) throws UnsupportedEncodingException {
        List<com.loivgehoto.disk.Model.File> file_list=new ArrayList<>();


        File file=new File(path);

        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++)
        {
            if(tempList[i].isFile())
            {
                String suffix=tempList[i].getName().substring(tempList[i].getName().lastIndexOf(".") + 1);//获取后缀名
                com.loivgehoto.disk.Model.File temp=new com.loivgehoto.disk.Model.File();
                temp.setFile_name(tempList[i].getName());
                temp.setSuffix(suffix);
                temp.setFile_size(tempList[i].length());

                //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓设置当前文件的路径
                String path_temp= URLEncoder.encode(tempList[i].getPath(),"UTF-8");


               // String path_temp=Base64.getEncoder().encodeToString(tempList[i].getPath().getBytes(StandardCharsets.UTF_8));

             //   System.out.println("tempList[i].getPath()"+tempList[i].getPath());

//                path_temp=path_temp.replaceAll("\\+",  "%20"); //处理空格
// 一个URL的基本组成部分包括协议(scheme),域名，端口号，路径和查询字符串（路径参数和锚点标记就暂不考虑了）。路径和查询字符串之间用问号?分离。例如http://www.example.com/index?param=1，路径为index，查询字符串(Query String)为param=1。URL中关于空格的编码正是与空格所在位置相关：空格被编码成加号+的情况只会在查询字符串部分出现，而被编码成%20则可以出现在路径和查询字符串中。
//
//  造成这种混乱局面的原因在于：W3C标准规定，当Content-Type为application/x-www-form-urlencoded时，URL中查询参数名和参数值中空格要用加号+替代，所以几乎所有使用该规范的浏览器在表单提交后，URL查询参数中空格都会被编成加号+。而在另一份规范(RFC 2396，定义URI)里, URI里的保留字符都需转义成%HH格式(Section 3.4 Query Component)，因此空格会被编码成%20，加号+本身也作为保留字而被编成%2B，对于某些遵循RFC 2396标准的应用来说，它可能不接受查询字符串中出现加号+，认为它是非法字符。所以一个安全的举措是URL中统一使用%20来编码空格字符。
//
// Java中的URLEncoder本意是用来把字符串编码成application/x-www-form-urlencoded MIME格式字符串，也就是说仅仅适用于URL中的查询字符串部分，但是URLEncoder经常被用来对URL的其他部分编码，它的encode方法会把空格编成加号+，与之对应的是，URLDecoder的decode方法会把加号+和%20都解码为空格，这种违反直觉的做法造成了当初我对空格URL编码问题的困扰。因此后来我的做法都是，在调用URLEncoder.encode对URL进行编码后(所有加号+已被编码成%2B)，再调用replaceAll(“\\+”, “%20″)，将所有加号+替换为%20。
//
              //  System.out.println("path_temp"+path_temp);


                temp.setFile_path(path_temp);
                //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


                temp.setParent_path(tempList[i].getParent());

                // -------------------------------获取文件最后修改时间并格式化时间
                long create_time=tempList[i].lastModified();
                Date temp_date=new Date(create_time);
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = format1.format(temp_date);
                //-------------------------------

                temp.setCreate_time(time);
                file_list.add(temp);
            }

        }
        return file_list;
    }
    //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


    //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓搜索所有文件夹
    public List<com.loivgehoto.disk.Model.File> searchFolder(String path) throws UnsupportedEncodingException {
        File file = null;
        if (path!=null)
            file=new File(path);
        //System.out.println("searchFolder method :"+path);
        File[] tempList = new File[0];
        if (file!=null)
            tempList = file.listFiles();

        List<com.loivgehoto.disk.Model.File> list = new ArrayList<>();
        if (tempList!=null)
        {
            for (int i = 0; i < tempList.length; i++)
            {
                if (tempList[i].isDirectory()) {
                    System.out.println("文件夹：" + tempList[i]);
                    com.loivgehoto.disk.Model.File temp=new com.loivgehoto.disk.Model.File();
                    temp.setFile_name(tempList[i].getName());
                    temp.setSuffix("folder");

                    String path_temp= URLEncoder.encode(tempList[i].getPath(),"UTF-8");
                    temp.setFile_path(path_temp);

                    temp.setParent_path(tempList[i].getParent());

                    // -------------------------------获取文件夹最后修改时间并格式化时间
                    long create_time=tempList[i].lastModified();
                    Date temp_date=new Date(create_time);
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = format1.format(temp_date);
                    //-------------------------------


                    temp.setCreate_time(time);
                    list.add(temp);
                }
            }
        }

        return list;
    }
    //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


}

