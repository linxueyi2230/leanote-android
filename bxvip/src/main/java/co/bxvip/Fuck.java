package co.bxvip;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;

public final class Fuck {
    public static void init(Application application){
        JPushInterface.setDebugMode(true);
        JPushInterface.init(application);
        CrashReport.initCrashReport(application,"a9f5019d2f",true);
    }
}
