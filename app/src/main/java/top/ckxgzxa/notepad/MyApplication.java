package top.ckxgzxa.notepad;

import android.app.Application;

/**
 * @author CKXG
 */
public class MyApplication extends Application {


    private static boolean passed = false;


    private static String passWord = "123456";

    public static String getPassWord() {
        return passWord;
    }

    public static void setPassed(boolean p) {
        passed = p;
    }

    public static boolean isPassed() {
        return passed;
    }

}
