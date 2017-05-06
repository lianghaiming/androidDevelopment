package com.example.androiddevelopment.util;

import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FileUtil {

    public static String getDirectory() {

        File file2 = Environment.getExternalStorageDirectory();
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_REMOVED)) {
            file2 = Environment.getDataDirectory();
        }
        return file2.getAbsolutePath();
    }


    public static void writeToFile() {
        File file = new File(getDirectory(), "ip.txt");
        try {
            if(!file.exists()) {
                file.createNewFile();
                OutputStream out = new FileOutputStream(file);
                OutputStreamWriter writer = new OutputStreamWriter(out);
                writer.write("192.168.1.162");
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static File file = null;
    private static OutputStream out = null;
    static {
        file = new File(getDirectory(), "myLog.txt");
        LogUtil.print("write hehe....................");
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            out = new FileOutputStream(file, true);
        } catch (IOException e) {
            LogUtil.print("write " + e.getMessage());
            e.printStackTrace();
        }
    }



    public static void saveLog(String msg) {
        LogUtil.print(msg);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        try {
            out.write((str + "  " + msg).getBytes());
            out.write("\r\n".getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String readIp() {
        File file = new File(getDirectory(), "ip.inf");
        try {
            InputStream instream = new FileInputStream(file);//读取输入流
            InputStreamReader inputreader = new InputStreamReader(instream);//设置流读取方式
            BufferedReader buffreader = new BufferedReader(inputreader);
            String ip = buffreader.readLine();
            LogUtil.print("camera ip " + ip);
            return ip;
        }catch(Exception ex){
        }
        return null;
    }
	
	public static File byteToFile(byte[] bfile, String fileDir, String fileName) {
		BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(fileDir);
            if (!dir.exists()) {// �ж��ļ�Ŀ¼�Ƿ����
                boolean mkdirs = dir.mkdirs();
            }
            fileName = fileName.replace(":", "��");
            file = new File(dir, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
        
	}

}
