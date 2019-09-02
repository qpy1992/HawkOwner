package com.bt.smart.cargo_owner;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.bt.smart.cargo_owner.utils.ExceptionUtil;
import com.bt.smart.cargo_owner.utils.SoundPoolUtil;
import com.bt.smart.cargo_owner.utils.SpUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.ArrayList;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/22 8:51
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class MyApplication extends Application {
    public static MyApplication application;
    public static boolean isRelease = false;//判断程序是否异常
    public static ArrayList<Activity> listActivity = new ArrayList<Activity>();//用来装载activity
    public static int flag = -1;//判断是否被回收
    public static int isLogin = 0;//判断是否登录
    public static String userID;
    public static String userCode;//
    public static String userType;//2是企业，3是个人
    public static String userFccountid;
    public static String userPhone;
    public static String userToken;
    public static String checkStatus;//审核状态
    public static String companyContract;//平台签署协议
    public static String userHeadPic;//头像
    public static String userName;
    public static double money;//余额
    public static int userOrderNum;//运单数
    public static boolean checkFace;
    public static String  loadUrl;
    public static String paccountid;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        ZXingLibrary.initDisplayOpinion(this);
        openVoice();
    }

    private void openVoice() {
        SoundPoolUtil.getInstance(this);
        Boolean isOpenVoice = SpUtils.getBoolean(MyApplication.this, "isOpenVoice", false);
        if (isOpenVoice) {
            SoundPoolUtil.restartSoundPlay();
        } else {
            SoundPoolUtil.pauseSoundPlay();
        }
    }

    public static void exit() {
        try {
            for (Activity activity : listActivity) {
                activity.finish();
            }
            // 结束进程
            System.exit(0);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

    public static int getNowVersionCode(Context context) {
        try {
            PackageInfo packageInfo = (context).getPackageManager().getPackageInfo((context).getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Context getContext() {
        return application;
    }
}
