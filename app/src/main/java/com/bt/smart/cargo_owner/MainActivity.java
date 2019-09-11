package com.bt.smart.cargo_owner;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bt.smart.cargo_owner.activity.userAct.AuthenticationActivity;
import com.bt.smart.cargo_owner.fragment.home.Home_F;
import com.bt.smart.cargo_owner.fragment.mineOrders.MyOrders_F;
import com.bt.smart.cargo_owner.fragment.sameDay.SameDay_F;
import com.bt.smart.cargo_owner.fragment.user.User_F;
import com.bt.smart.cargo_owner.messageInfo.ApkInfo;
import com.bt.smart.cargo_owner.messageInfo.CommenInfo;
import com.bt.smart.cargo_owner.util.UpApkDataFile.UpdateAppUtil;
import com.bt.smart.cargo_owner.utils.HttpOkhUtils;
import com.bt.smart.cargo_owner.utils.MyAlertDialog;
import com.bt.smart.cargo_owner.utils.MyAlertDialogHelper;
import com.bt.smart.cargo_owner.utils.RequestParamsFM;
import com.bt.smart.cargo_owner.utils.ToastUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "MainActivity";
    // 界面底部的菜单按钮
    private ImageView[] bt_menu = new ImageView[4];
    // 界面底部的未选中菜单按钮资源
    private int[] select_off = {R.drawable.bt_menu_0_select, R.drawable.bt_menu_1_select, R.drawable.bt_menu_2_select, R.drawable.bt_menu_3_select};
    // 界面底部的选中菜单按钮资源
    private int[] select_on = {R.drawable.icon_home_sel, R.drawable.icon_same_day_sel, R.drawable.icon_service_apply_sel, R.drawable.icon_me_sel};
    // 界面底部的菜单按钮id
    private int[] bt_menu_id = {R.id.iv_menu_0, R.id.iv_menu_1, R.id.iv_menu_2, R.id.iv_menu_3};
    //底部布局按钮的id
    private int[] linear_id = {R.id.linear0, R.id.linear1, R.id.linear2, R.id.linear3};
    //底部字体
    private TextView tv_menu_0, tv_menu_1, tv_menu_2, tv_menu_3;
    private List<TextView> tv_menu;
    private LinearLayout linear_home;//发货
    private LinearLayout linear_shopp;//车源信息
    private LinearLayout linear_play;//我的订单
    private LinearLayout linear_mine;//个人中心
    private Home_F home_F;//配送大厅
    private SameDay_F sameDay_F;//车源信息
    private MyOrders_F myOrders_F;//我的订单
    private User_F user_F;//个人中心
    private int REQUEST_CHECK_FACE = 1017;//跳转人脸认证界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        setData();
    }

    private void setView() {
        linear_home = findViewById(R.id.linear0);
        linear_shopp = findViewById(R.id.linear1);
        linear_play = findViewById(R.id.linear2);
        linear_mine = findViewById(R.id.linear3);
        tv_menu_0 = findViewById(R.id.tv_menu_0);
        tv_menu_1 = findViewById(R.id.tv_menu_1);
        tv_menu_2 = findViewById(R.id.tv_menu_2);
        tv_menu_3 = findViewById(R.id.tv_menu_3);
        //获取最新的版本
        getNewApkInfo();
        //检查认证状态，提醒认证
        checkStatus();
    }

    private void setData() {
        tv_menu = new ArrayList<>();
        tv_menu.add(tv_menu_0);
        tv_menu.add(tv_menu_1);
        tv_menu.add(tv_menu_2);
        tv_menu.add(tv_menu_3);
        // 找到底部菜单的按钮并设置监听
        for (int i = 0; i < bt_menu.length; i++) {
            bt_menu[i] = (ImageView) findViewById(bt_menu_id[i]);
        }
        linear_home.setOnClickListener(this);
        linear_shopp.setOnClickListener(this);
        linear_play.setOnClickListener(this);
        linear_mine.setOnClickListener(this);
        // 初始化默认显示的界面
        if (home_F == null) {
            home_F = new Home_F();
            addFragment(home_F);
            showFragment(home_F);
            changeTVColor(0);
        } else {
            showFragment(home_F);
            changeTVColor(0);
        }
        // 设置默认首页为点击时的图片
        bt_menu[0].setImageResource(select_on[0]);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear0://发货界面
                if (home_F == null) {
                    home_F = new Home_F();
                    // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                    addFragment(home_F);
                    showFragment(home_F);
                    //改变字体
                    changeTVColor(0);
                } else {
                    if (home_F.isHidden()) {
                        showFragment(home_F);
                        changeTVColor(0);
                    }
                }
                break;
            case R.id.linear1:// 车源信息
                if ("1".equals(MyApplication.checkStatus)) {
                    if (sameDay_F == null) {
                        sameDay_F = new SameDay_F();
                        // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                        addFragment(sameDay_F);
                        showFragment(sameDay_F);
                        changeTVColor(1);
                    } else {
                        if (sameDay_F.isHidden()) {
                            showFragment(sameDay_F);
                            changeTVColor(1);
                        }
                    }
                } else {
                    //提示还未通过审核
                    ToastUtils.showToast(this, "您还未通过审核，请先去个人中心查看审核状态!");
                    //跳转个人中心
                    linear_mine.performClick();
//                    new MyAlertDialog(this, MyAlertDialog.NORMAL_TYPE).setTitleText("您还未通过审核，请先去个人中心查看审核状态!")
//                            .setConfirmText("确定")
//                            .setCancelText("取消")
//                            .showCancelButton(false)
//                            .setConfirmClickListener(new MyAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(MyAlertDialog sweetAlertDialog) {
//                                    //跳转个人中心
//                                    linear_mine.performClick();
//                                    sweetAlertDialog.dismiss();
//                                }
//                            })
//                            .setCancelClickListener(null)
//                            .show();
                }
                //                if (sameDay_F == null) {
                //                    sameDay_F = new SameDay_F();
                //                    // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                //                    addFragment(sameDay_F);
                //                    showFragment(sameDay_F);
                //                    changeTVColor(1);
                //                } else {
                //                    if (sameDay_F.isHidden()) {
                //                        showFragment(sameDay_F);
                //                        changeTVColor(1);
                //                    }
                //                }
                break;
            case R.id.linear2://我的订单
                if (myOrders_F == null) {
                    myOrders_F = new MyOrders_F();
                    // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                    addFragment(myOrders_F);
                    showFragment(myOrders_F);
                    changeTVColor(2);
                } else {
                    if (myOrders_F.isHidden()) {
                        showFragment(myOrders_F);
                        changeTVColor(2);
                    }
                }
                break;
            case R.id.linear3:// 个人界面
                if (user_F == null) {
                    user_F = new User_F();
                    // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                    addFragment(user_F);
                    showFragment(user_F);
                    changeTVColor(3);
                } else {
                    if (user_F.isHidden()) {
                        showFragment(user_F);
                        changeTVColor(3);
                    }
                }
                break;
        }
        // 设置按钮的选中和未选中资源
        for (int i = 0; i < bt_menu.length; i++) {
            bt_menu[i].setImageResource(select_off[i]);
            if (view.getId() == linear_id[i]) {
                bt_menu[i].setImageResource(select_on[i]);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != home_F)
            home_F.onActivityResult(requestCode, resultCode, data);
        if (null != sameDay_F)
            sameDay_F.onActivityResult(requestCode, resultCode, data);
        if (null != user_F)
            user_F.onActivityResult(requestCode, resultCode, data);
    }

    public LinearLayout getLinear_mine() {
        return linear_mine;
    }

    private void changeTVColor(int item) {
        for (int i = 0; i < tv_menu.size(); i++) {
            if (i == item) {
                tv_menu.get(i).setTextColor(getResources().getColor(R.color.blue_icon));
            } else {
                tv_menu.get(i).setTextColor(getResources().getColor(R.color.lin_black));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final MyAlertDialogHelper dialogHelper = new MyAlertDialogHelper();
            View view = View.inflate(this, R.layout.dialog_commen_title, null);
            dialogHelper.setDIYView(this, view);
            dialogHelper.show();
            TextView tv_sure = view.findViewById(R.id.tv_sure);
            TextView tv_cancel = view.findViewById(R.id.tv_cancel);
            tv_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    MyApplication.exit();
                }
            });
            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogHelper.disMiss();
                }
            });
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 添加Fragment
     **/
    public void addFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frame, fragment);
        ft.commit();
    }

    /**
     * 显示Fragment
     **/
    public void showFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        // 设置Fragment的切换动画
        // ft.setCustomAnimations(R.anim.cu_push_right_in, R.anim.cu_push_left_out);

        // 判断页面是否已经创建，如果已经创建，那么就隐藏掉
        if (home_F != null) {
            ft.hide(home_F);
        }
        if (sameDay_F != null) {
            ft.hide(sameDay_F);
        }
        if (myOrders_F != null) {
            ft.hide(myOrders_F);
        }
        if (user_F != null) {
            ft.hide(user_F);
        }
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    protected void getNewApkInfo(){
        RequestParamsFM headparam = new RequestParamsFM();
        headparam.put(NetConfig.TOKEN,MyApplication.userToken);
        HttpOkhUtils.getInstance().doGetWithOnlyHeader(NetConfig.CHECKUPDATE + "/0", headparam, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                Log.i(TAG,"网络错误!");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                if(code!=200){
                    Log.i(TAG,"网络错误!");
                    return;
                }
                ApkInfo info = new Gson().fromJson(resbody,ApkInfo.class);
                if(getAppVersionCode(MainActivity.this)<info.getData().getVersionCode()){
                    showDialogToDown(info);
                }
            }
        });
    }

    public static int getAppVersionCode(Context context) {
        int appVersionCode = 0;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionCode;
    }

    private void showDialogToDown(ApkInfo apkInfo) {
        MyApplication.loadUrl = NetConfig.IMG_HEAD + apkInfo.getData().getApkPath();
        UpdateAppUtil.from(this)
                .serverVersionCode(apkInfo.getData().getVersionCode())  //服务器versionCode
                .serverVersionName(apkInfo.getData().getVersionName()) //服务器versionName
                .apkPath(MyApplication.loadUrl) //最新apk下载地址
                .updateInfo(apkInfo.getData().getApkInfo())
                .update();
    }

    protected void checkStatus(){
        final MyAlertDialog dialog = new MyAlertDialog(MainActivity.this);
        if(MyApplication.checkStatus.equals("0")){
            dialog.setTitleText("尚未认证")
                    .setContentText("现在认证？")
                    .setCancelText("稍后再说")
                    .setCancelClickListener(new MyAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(MyAlertDialog sweetAlertDialog) {
                            dialog.dismiss();
                        }
                    }).setConfirmText("立即认证")
                    .setConfirmClickListener(new MyAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(MyAlertDialog sweetAlertDialog) {
                            Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
                            startActivityForResult(intent, REQUEST_CHECK_FACE);
                            dialog.dismiss();
                        }
                    }).show();
        }else if(MyApplication.checkStatus.equals("2")){
            dialog.setTitleText("账号异常")
                    .setContentText("账号被禁用，具体原因请联系客服：400-8888-8888")
                    .setConfirmText("知道了")
                    .setConfirmClickListener(new MyAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(MyAlertDialog sweetAlertDialog) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }
}
