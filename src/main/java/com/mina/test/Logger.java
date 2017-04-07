package com.mina.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private String path = AppConfig.Default.GetString("logger.path");
    private String project = AppConfig.Default.GetString("logger.project");

    private PrintWriter writer;
    private static SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");


    public Logger(String name){

        String file = project +"u"+name + ".log." + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        File filecon = new File(path+"/"+file);
        try {
            if(!filecon.exists()){
                filecon.createNewFile();
                System.out.println("file is not exits,new file has been created!");
                System.out.println(path);
            }
            writer = new PrintWriter(path +"/" +file);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void log(String msg){
        writer.println(format.format(new Date()) + " " + msg);
    }

    public void raw(String msg){
        writer.println(msg);
    }

    public void info(String msg){
        log("[INFO] " + msg);


    }

    public void critical(String msg){
        log("[CRITICAL] "  + msg);


    }

    public void error(String msg){
        log("[ERROR] " + msg);

    }

    public void debug(String msg){
        log("[DEBUG] " + msg);
    }

    public void warn(String msg){
        log("[WARN] " + msg);
    }

    public void exception(Exception ex){

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        ex.printStackTrace(pw);
        pw.flush();
        sw.flush();

        log("[EXCEPTION] " + sw.toString());
        try {
            pw.close();
            sw.close();
        } catch (IOException e) {
        }
    }

    public void flush(){
        writer.flush();

    }
    public void close(){
        writer.flush();
        writer.close();
    }
}
