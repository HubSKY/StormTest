package com.mina.test;

/**
 * Created by User on 2017/4/7.
 */
public class Test {

    public static void main(String []args){
        Test a =new Test();
        System.out.println(AppConfig.Default.GetString("data."));
//        Logger log = new Logger("mylog");
//        log.info("hello word ");
//        log.error("Message Error!");

    }
}
