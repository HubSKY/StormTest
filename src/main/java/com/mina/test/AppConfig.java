package com.mina.test;


import net.sf.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by User on 2017/4/7.
 */
public class AppConfig {
    private String config_file;
    private JSONObject config;
    public static AppConfig Default = new AppConfig("/home/gongguiwei/traffic.json");

    public AppConfig(String config_file)
    {
        this.config_file = config_file;
        init();
    }

    private void init() {
        String jsonString = ReadFile(this.config_file);
        this.config = JSONObject.fromObject(jsonString);
    }

    public static String ReadFile(String filePath) {
        try {
            String encoding = "UTF-8";
            File file = new File(filePath);
            if ((file.isFile()) && (file.exists())) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);

                BufferedReader bufferedReader = new BufferedReader(read);
                StringBuilder sb = new StringBuilder();
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    sb.append(lineTxt);
                }
                read.close();
                return sb.toString();
            }
            System.out.println("Can't find the config file:" + filePath);
        }
        catch (Exception e)
        {
            System.out.println("Read config file error:" + filePath);
            e.printStackTrace();
        }
        return "";
    }

    public int GetInt(String key)
    {
        Integer value = (Integer)Get(key);
        return value.intValue();
    }

    public String GetString(String key) {
        String value = (String)Get(key);
        return value;
    }

    public double GetDouble(String key) {
        Double value = (Double)Get(key);
        return value.doubleValue();
    }

    public boolean GetBoolean(String key) {
        Boolean value = (Boolean)Get(key);
        return value.booleanValue();
    }

    public Object Get(String key) {
        try {
            JSONObject obj = this.config;
            Object result = null;
            String[] keys = key.split("\\.");
            for (int i = 0; i < keys.length; i++) {
                if (i != keys.length - 1)
                    obj = obj.getJSONObject(keys[i]);
                else {
                    result = obj.get(keys[i]);
                }
            }
            return result; } catch (Exception ex) {
        }
        return null;
    }

    public boolean KeyExists(String key)
    {
        Object obj = Get(key);
        return obj != null;
    }

    private static String FindConfigPath(String cmd) {
        for (String arg : cmd.split(" ")) {
            if (arg.contains("config.json")) {
                return arg;
            }
        }
        return "config.json";
    }

}
