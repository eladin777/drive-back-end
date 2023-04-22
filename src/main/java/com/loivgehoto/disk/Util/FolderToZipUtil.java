package com.loivgehoto.disk.Util;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FolderToZipUtil {
    public static void zip(String sourceFileName, HttpServletResponse response) {
        ZipOutputStream out = null;
        BufferedOutputStream bos = null;
        File file = new File(sourceFileName);
        try {
            //将zip以流的形式输出到前台
            response.setHeader("content-type", "application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.setContentLength((int) file.length());
//            //        解决axios下载请求跨域错误（后端已成功配置运行跨域）
//            response.addHeader("Access-Control-Allow-Origin", "*");
//            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
//            //

            // 设置浏览器响应头对应的Content-disposition
            //参数中 testZip 为压缩包文件名，尾部的.zip 为文件后缀
            response.setHeader("Content-Disposition",
                    "attachment;filename=a.zip");

            //创建zip输出流
            out = new ZipOutputStream(response.getOutputStream());
            //创建缓冲输出流
            bos = new BufferedOutputStream(out);
            File sourceFile = new File(sourceFileName);
            //调用压缩函数
            compress(out, bos, sourceFile, sourceFile.getName());
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            close(bos, out);
        }

    }

    public static void compress(ZipOutputStream out, BufferedOutputStream bos, File sourceFile, String base) {
        FileInputStream fos = null;
        BufferedInputStream bis = null;
        try {
            //如果路径为目录（文件夹）
            if (sourceFile.isDirectory()) {
                //取出文件夹中的文件（或子文件夹）
                File[] flist = sourceFile.listFiles();
                if (flist.length == 0) {//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                    out.putNextEntry(new ZipEntry(base + "\\"));
                } else {//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                    for (int i = 0; i < flist.length; i++) {
                        compress(out, bos, flist[i], base + "\\" + flist[i].getName());
                    }
                }
            } else {//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
                out.putNextEntry(new ZipEntry(base));
                fos = new FileInputStream(sourceFile);
                bis = new BufferedInputStream(fos);

                int tag;
                //将源文件写入到zip文件中
                while ((tag = bis.read()) != -1) {
                    out.write(tag);
                }
                bis.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            close(bis, fos);
        }
    }

    public static void close(Closeable... io) {
        for (Closeable temp : io) {
            try {
                if (null != temp)
                    temp.close();
            } catch (IOException e) {
                System.out.println("" + e.getMessage());
            }
        }
    }

}
