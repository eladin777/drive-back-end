package com.loivgehoto.disk.Controller;

import com.loivgehoto.disk.Model.Chunk;
import com.loivgehoto.disk.Model.Shared;
import com.loivgehoto.disk.Service.ChunkService;
import com.loivgehoto.disk.Service.FileService;
import com.loivgehoto.disk.Util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;




@Controller
public class FileController {

    //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓将路径保存在全局配置文件
    @Value("${platform.File_Path}")
    private String File_Path;
    //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑将路径保存在全局配置文件

    //    linux路径是/，windosw是\
    @Value("${platform.slash}")
    char slash;

    @Autowired
    private ChunkService chunkService;

    @Autowired
    private FileService fileService;



//      前端上传器先发送一个get请求,然后每个文件切块发送一个post请求
    @PostMapping("/chunk")
    @ResponseBody
    public String uploadChunk (Chunk chunk) {
        MultipartFile file = chunk.getFile();

        try {
            byte[] bytes = file.getBytes();
            FileUtils utils=new FileUtils();

            Path path = Paths.get(utils.generatePath(File_Path+chunk.getUser_name()+slash+"Upload", chunk));

            //文件写入指定路径
            Files.write(path, bytes);

//             把文件块的信息写入数据库,方便页面刷新后上传相同文件时前端上传器对已上传的文件块的检验以跳过上传的块
            chunkService.saveChunk(chunk);

            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }


//    上传器每次开始上传先发送get请求，
    @GetMapping("/chunk")
    @ResponseBody
    public Chunk checkChunk(Chunk chunk) {
//        if (chunkService.checkChunk(chunk.getIdentifier(), chunk.getChunkNumber())!=null) {
////            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
//        }

//        当界面刷新后，在刷新前上传了一个文件的部分分块，再刷新后再次上传该文件，前端上传器会先发送get请求，后端用得到的chunk信息去数据库里找这个chunk在
//        数据库里是否有记录，把找到的chunk number数组返回回来，这个数组代表这个文件已经上传过的chunk文件块，前端上传器拿到返回的chunk里的uploaded
//        数组信息，根据chunk number跳过已上传的文件块（chunk）
//
        chunk.setUploaded(chunkService.checkChunk(chunk.getIdentifier(),chunk.getUser_name()));

        if (chunk.getUploaded()!=null)
            chunk.setSkipUpload(false);
        else
            chunk.setSkipUpload(true);

        return chunk;
    }


//    一个文件的所有文件块上传成功,由前端上传器globaluoloader内的file success方法调用,所有块上传完毕,则执行合并文件块操作
//    合并文件块并将合并后的文件移动至current_upload_path目标路径下
    @GetMapping("/mergeFile")
    @ResponseBody
    public String mergeFile(String relativePath,String identifier,String user_name,String current_upload_path) throws UnsupportedEncodingException {

        String target_file_path;
        String file_name=relativePath;
        String temp="";
        String folder;

//        relativePath含路径符号（代表前端上传的是文件夹），则处理
        if (relativePath.contains("/"))
        {
            temp=relativePath;
            file_name=temp.substring(temp.lastIndexOf("/")+1,temp.length());//   上传文件夹的话， relativePath是包含父路径的        获取文件名
            temp=temp.substring(0,temp.lastIndexOf("/"));//            获取文件相对父路径

            folder = File_Path+user_name+"/"+"Upload" + "/" + temp+"/"+identifier;//        设置文件绝对父路径

            //        relativePath是包含文件名的，格式为父路径加文件名，如果不是在文件夹内， relativePath就只包含文件名
//            target_file_path = File_Path+"拉丹"+"/"+"Upload" + "/" +identifier+"/"+ relativePath;
            target_file_path = folder+"/"+file_name;
        }
        else
        {
            folder = File_Path+user_name+"/"+"Upload" + "/" + identifier;
            target_file_path=folder+"/"+file_name;
        }

//        这里用file_name是因为relativePath可能包含文件父路径（上传了文件夹），merge是要文件名在最后一个参数，直接让前端发file——name，避免后端截取字符串
//        经过测试上传器的merge请求不发送第三个请求，上面内容作废
        FileUtils utils=new FileUtils();
        utils.merge(target_file_path, folder, file_name);


        File oldName = new File(target_file_path);
//        File newName = new File(folder.substring(0,folder.lastIndexOf("/"))+"/"+file_name);

        File newName=null;

        if (current_upload_path!="")//        current_upload_path为空表示上传保存的目标路径为用户根路径
        {
             newName=new File(URLDecoder.decode(current_upload_path,"UTF-8")+'/'+relativePath);
        }
        else
        {
             newName=new File(File_Path+user_name+"/File/"+relativePath);
        }
        if (!newName.exists())
            newName.getParentFile().mkdirs();

        oldName.renameTo(newName);


//        合并成功，删除数据库内所有包含此identifier的记录，节省空间
        chunkService.delete_chunk_infor(identifier);

//        return "success";

//        返回当前上传的文件设置的保存的目标路径，目的是为了让前端判断用户是否还在目标路径的页面下，如果在则立刻更新文件列表，如果不在则不做任何事
        return current_upload_path;
    }

    @GetMapping("/delete_temp_upload_folder")
    @ResponseBody
    public void delete_temp_upload_folder(String folder_name,String user_name) {

        File t=new File(File_Path+user_name+"/"+"Upload" + "/"+folder_name);
        delete_folder(t);
    }

    //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓分享动作
    @GetMapping("/share_file")
    @ResponseBody
    public Shared share_file(com.loivgehoto.disk.Model.File file, Boolean use_password, String user_name) throws UnsupportedEncodingException {
        String uuid=UUID();
        String password=null;
        if (use_password)
            password=getRandomString();

        String path=URLDecoder.decode(file.getFile_path(),"UTF-8");

        fileService.save_shared_file(user_name, file.getFile_name(), path,uuid, file.getFile_size(), file.getCreate_time(),file.getSuffix(),password);

        Shared s=new Shared();
        s.setPassword(password);
        s.setUUID(uuid);
        return s;
    }

    public String UUID()
    {
        UUID randomUUID = UUID.randomUUID();
        String UUID=(String)randomUUID.toString().replaceAll("-", "");;
        return UUID;
    }
    public static String getRandomString(){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<4;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    @GetMapping("/shared_page")
    @ResponseBody
    public List<Shared> shared_page(String uuid)
    {
      return fileService.shared_page(uuid);
    }


    @GetMapping("/check_share_password")
    @ResponseBody
    public List<Shared> check_share_password(String uuid,String password)
    {
        if (password.equals("no_password"))
        {
            if(fileService.check_share_password_is_exit(uuid)==null)
                return fileService.shared_page(uuid);
            else
                return null;
        }

        return fileService.check_share_password(uuid,password);
    }


    @GetMapping("/share_management_page")
    @ResponseBody
    public List<Shared> share_management_page(String user_name)
    {
        return fileService.share_management_page(user_name);
    }

    @GetMapping("/cancel_multi_share")
    @ResponseBody
    public void cancel_multi_share(String []file_path)
    {
        for (String t:file_path)
        {
            fileService.cancel_share(t);
        }

    }

    @GetMapping("/cancel_share")
    @ResponseBody
    public void cancel_share(String file_path)
    {
        System.out.println("cancel!!!!");

//        file_path= URLEncoder.encode(file_path, StandardCharsets.UTF_8);
        System.out.println(file_path);
        fileService.cancel_share(file_path);
    }
    //-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑分享动作


    @RequestMapping("/download")
    @ResponseBody
    public void download(HttpServletResponse response, @RequestParam("file_path") String file_path) throws UnsupportedEncodingException
    {
      //  String user_name=(String) session.getAttribute("user_name");
      //  String temp=(String)session.getAttribute("Current_Path")+fileName;////从Current_Path取得当前路径
//        System.out.println("download");
        file_path= URLDecoder.decode(file_path,"UTF-8");

   //     byte[]base= Base64.getDecoder().decode(file_path);

    //    file_path= new String(base);

        File file = new File(file_path);
//        if(!file.exists())
//        {
//            return "下载文件不存在";
//        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());

//        解决axios下载请求跨域错误（后端已成功配置运行跨域）
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
//

        response.setHeader("Content-Disposition", "attachment; filename="
                + new String(file.getName().getBytes("utf-8"),"ISO-8859-1"));

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));)
        {
            byte[] buffer = new byte[bis.available()];
//            byte[] buffer = new byte[2048];
            OutputStream out  = response.getOutputStream();
            bis.read(buffer);//读取数据文件
            bis.close();

//            int i = 0;
//            while ((i = bis.read(buffer)) != -1) {
//                out.write(buffer, 0, i);
//                out.flush();
//            }
            out.write(buffer);//输出数据文件
            out.flush();//释放缓存
            out.close();//关闭输出流

        } catch (IOException e)
        {
         //   return "下载失败";
        }
//        return null;
    }

    @RequestMapping("/folder_download")
    @ResponseBody
    public void folder_download(HttpServletResponse response, @RequestParam("file_path") String file_path) throws IOException
    {
        file_path= URLDecoder.decode(file_path,"UTF-8");
        File temp=new File(file_path);

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=a.zip");
        toZip(temp.getPath(),response.getOutputStream(),true);
//        FolderToZipUtil.zip(file_path,response);
    }
    @RequestMapping("/multi_files_download")
    @ResponseBody
    public void multi_files_download(HttpServletResponse response,String[] file_path) throws IOException
    {
//        file_path= URLDecoder.decode(file_path,"UTF-8");
//        File temp=new File(file_path);
        System.out.println("multi_files_download");
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=a.zip");
        toZip2(file_path,response.getOutputStream());
//        FolderToZipUtil.zip(file_path,response);
    }
    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure) throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
//     * 压缩成ZIP 方法2
//       *
//               * @param srcFiles 需要压缩的文件列表
//       * @param out 压缩文件输出流
//       * @throws RuntimeException 压缩失败会抛出运行时异常
//       */
    public static void toZip2(String[] srcFiles, OutputStream out) throws RuntimeException {

//        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            for (String srcFile : srcFiles) {
                srcFile=URLDecoder.decode(srcFile,"UTF-8");
                File temp=new File(srcFile);
                if (temp.isDirectory())
                {
                    compress(temp, zos, temp.getName(),true);
                }
                else
                {
                    byte[] buf = new byte[2048];
                    zos.putNextEntry(new ZipEntry(temp.getName()));
                    System.out.println(temp.getName());
                    int len;
                    FileInputStream in = new FileInputStream(srcFile);
                    while ((len = in.read(buf)) != -1) {
                        zos.write(buf, 0, len);
                    }
                    zos.closeEntry();
                    in.close();
                }
            }
//            long end = System.currentTimeMillis();
//            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure)
            throws Exception {
        byte[] buf = new byte[2048];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }

    @GetMapping("/search_file")
    @ResponseBody
    public  List<com.loivgehoto.disk.Model.File> search_file(String key_name, String user_name) throws UnsupportedEncodingException {
        File file=new File(File_Path+user_name+slash+"File"+slash);
        List<com.loivgehoto.disk.Model.File> list=new ArrayList<>();

        SearchFileController t=new SearchFileController(list);
        t.search_file(file,key_name);

        return list;
    }




    @RequestMapping("/edit_file_name")
    @ResponseBody
    public Boolean edit_file_name(HttpServletResponse response,String old_file_path,String new_file_name) throws IOException {

        old_file_path=URLDecoder.decode(old_file_path,"UTF-8");
        String t=old_file_path;
        t=t.substring(0,t.lastIndexOf('\\')+1);
//        System.out.println(old_file_path);
//        System.out.println(new_file_name);

        File oldName = new File(old_file_path);
        // 新的文件或目录
        File newName = new File(t+new_file_name);
        if (newName.exists()) {  //  确保新的文件名不存在
            return false;
//            throw new java.io.IOException("file exists");
        }
        if(oldName.renameTo(newName)) {
//            System.out.println("已重命名");
            return true;
        } else {
//            System.out.println("Error");
            return false;
        }
    }

    @RequestMapping("/move_file")
    @ResponseBody
    public Boolean move_file(String source_file_path,String destination_folder_path,String user_name) throws IOException {

        source_file_path=URLDecoder.decode(source_file_path,"UTF-8");
        String source_file_name=source_file_path.substring(source_file_path.lastIndexOf(slash)+1);
        File old_path = new File(source_file_path);
        File new_path=null;

//        home表示移动文件对话框是在某个文件夹内打开的，打开后要将此文件夹内的文件移到根目录，设置home将目标文件夹设置为根目录
        if(!destination_folder_path.equals("home"))
        {
            destination_folder_path=URLDecoder.decode(destination_folder_path,"UTF-8");
            // 新的文件或目录
             new_path = new File(destination_folder_path+slash+source_file_name);
        }
        else
        {
            destination_folder_path=File_Path+user_name+slash+"File"+slash;
            new_path = new File(destination_folder_path+source_file_name);
        }
        if (new_path.exists()) {  //  确保新的文件名不存在
            return false;
//            throw new java.io.IOException("file exists");
        }
        if(old_path.renameTo(new_path)) {
//            System.out.println("已重命名");
            return true;
        } else {
//            System.out.println("Error");
            return false;
        }
    }

    @RequestMapping("/multi_files_move")
    @ResponseBody
    public Boolean multi_files_move(String[] source_file_path,String destination_folder_path,String user_name) throws IOException {

        Boolean temp=true;

        for (String file:source_file_path) {
            file=URLDecoder.decode(file,"UTF-8");
            String source_file_name=file.substring(file.lastIndexOf(slash)+1);
            File old_path = new File(file);
            File new_path=null;

//        home表示移动文件对话框是在某个文件夹内打开的，打开后要将此文件夹内的文件移到根目录，设置home将目标文件夹设置为根目录
            if(!destination_folder_path.equals("home"))
            {
                destination_folder_path=URLDecoder.decode(destination_folder_path,"UTF-8");
                // 新的文件或目录
                new_path = new File(destination_folder_path+slash+source_file_name);
            }
            else
            {
                destination_folder_path=File_Path+user_name+slash+"File"+slash;
                new_path = new File(destination_folder_path+source_file_name);
            }
            if (new_path.exists()) {  //  确保新的文件名不存在
                temp=false;
//            throw new java.io.IOException("file exists");
            }
            if(!old_path.renameTo(new_path))
                temp=false;
        }

        return temp;
    }

    @RequestMapping("/create_folder")
    @ResponseBody
    public Boolean create_folder(String user_name,String folder_name,String path) throws IOException
    {
        String p;
        if (path.equals(""))
             p=File_Path+user_name+slash+"File"+slash+folder_name;////将Current_Path初始化（设为根目录路径）
        else
        {
            path=URLDecoder.decode(path,"UTF-8");
            p = path+slash+folder_name;
        }


        File file=new File(p);
        return file.mkdir();
    }



//



//    @RequestMapping("/download_in_recycle_bin")
//    public void fileDownLoad_in_recycle_bin(HttpServletResponse response, @RequestParam("file_name") String file_name,HttpSession session) throws UnsupportedEncodingException {
////        System.out.println("download"+session.getAttribute("parent"));
//
//
//        File file = new File(File_Path+(String) session.getAttribute("user_name")+slash+"Bin"+slash+file_name);
////        if(!file.exists())
////        {
////            return "下载文件不存在";
////        }
//        response.reset();
//        response.setContentType("application/octet-stream");
//        response.setCharacterEncoding("utf-8");
//        response.setContentLength((int) file.length());
//        response.setHeader("Content-Disposition", "attachment; filename="
//                + new String(file.getName().getBytes("utf-8"),"ISO-8859-1"));
//
//        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));)
//        {
//            byte[] buff = new byte[1024];
//            OutputStream os  = response.getOutputStream();
//            int i = 0;
//            while ((i = bis.read(buff)) != -1) {
//                os.write(buff, 0, i);
//                os.flush();
//            }
//        } catch (IOException e)
//        {
//            //   return "下载失败";
//        }
//
//    }


    //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓将文件移至回收站
    @GetMapping( "/move_file_to_recycle_bin")//////////
    @ResponseBody
    public Boolean move_file_to_recycle_bin(String user_name,String file_path) throws IOException
    {
        file_path=URLDecoder.decode(file_path,"UTF-8");
        String source_file_name=file_path.substring(file_path.lastIndexOf(slash)+1);
        File old_path = new File(file_path);

        File new_path=null;

        String destination_folder_path=File_Path+user_name+slash+"Bin"+slash;
        new_path = new File(destination_folder_path+source_file_name);


        if (new_path.exists()) {  //  确保新的文件名不存在
            return false;
        }
        if(old_path.renameTo(new_path)) {
            fileService.move_file_to_recycle_bin(user_name,source_file_name,file_path);
            return true;
        }
        else {

            return false;
        }

    }
    //-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓将文件移至回收站
    @GetMapping( "/move_multi_files_to_recycle_bin")//////////
    @ResponseBody
    public Boolean move_multi_files_to_recycle_bin(String user_name,String[] file_path) throws IOException
    {
        Boolean b = true;
        for (String temp:file_path)
        {
            temp=URLDecoder.decode(temp,"UTF-8");
            String source_file_name=temp.substring(temp.lastIndexOf(slash)+1);
            File old_path = new File(temp);

            File new_path=null;

            String destination_folder_path=File_Path+user_name+slash+"Bin"+slash;
            new_path = new File(destination_folder_path+source_file_name);

            if (new_path.exists()) {  //  确保新的文件名不存在
                b= false;
            }
            if(old_path.renameTo(new_path)) {
                fileService.move_file_to_recycle_bin(user_name,source_file_name,temp);
                b= true;
            }
            else {

                b= false;
            }
        }
        return b;
    }


    @GetMapping("/delete_file_completely")
    @ResponseBody
    public void delete_file_completely( String file_name,String user_name)
    {
        File file=new File(File_Path+user_name+slash+"Bin"+slash+file_name);
        if (!file.isDirectory())
        {
            file.delete();
        }
        else
        {
            delete_folder(file);
        }
       fileService.delete_file_completely(file_name,user_name);
    }
    @GetMapping("/delete_multi_files_completely")
    @ResponseBody
    public void delete_multi_files_completely( String[] file_path,String user_name) throws UnsupportedEncodingException {
        for (String t:file_path) {
            t=URLDecoder.decode(t,"UTF-8");
            String file_name=t.substring(t.lastIndexOf(slash)+1);
            File file=new File(t);
            if (!file.isDirectory())
            {
                file.delete();
            }
            else
            {
                delete_folder(file);
            }
            fileService.delete_file_completely(file_name,user_name);
        }
    }
    public static void delete_folder(File dirFile) {
        if (dirFile.isFile()) {
//            return dirFile.delete();
        } else {

            for (File file : dirFile.listFiles()) {
                delete_folder(file);
            }
        }
        dirFile.delete();
    }


    @GetMapping("/restore_file")
    @ResponseBody
    public Boolean restore_file(String file_name,String user_name)///////////////////////将回收站的文件还原到用户主文件目录内
    {
        File file=new File(File_Path+user_name+slash+"Bin"+slash+file_name);
        File destination_path=new File(fileService.restore_file_get_file_source_path(file_name,user_name));
        if (destination_path.exists()) {  //  确保新的文件名不存在
           return false;
        }
        if(file.renameTo(destination_path)) {
            fileService.delete_file_completely(file_name,user_name);
            return true;
        } else {
          return false;
        }
    }
    @GetMapping("/restore_multi_files")
    @ResponseBody
    public Boolean restore_multi_files(String[] file_path,String user_name) throws UnsupportedEncodingException///////////////////////将回收站的文件还原到用户主文件目录内
    {
        Boolean b=true;

        for (String t:file_path) {
            t=URLDecoder.decode(t,"UTF-8");
            String file_name=t.substring(t.lastIndexOf(slash)+1);

            File file=new File(t);
            File destination_path=new File(fileService.restore_file_get_file_source_path(file_name,user_name));
            if (destination_path.exists()) {  //  确保新的文件名不存在
                b= false;
            }
            if(file.renameTo(destination_path)) {
                fileService.delete_file_completely(file_name,user_name);
                b= true;
            } else {
                b= false;
            }
        }
        return b;

    }



}

