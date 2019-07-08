package com.bt.smart.cargo_owner.fragment.user;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bt.smart.cargo_owner.MyApplication;
import com.bt.smart.cargo_owner.NetConfig;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.messageInfo.UpPicInfo;
import com.bt.smart.cargo_owner.utils.GlideLoaderUtil;
import com.bt.smart.cargo_owner.utils.HttpOkhUtils;
import com.bt.smart.cargo_owner.utils.MyTextUtils;
import com.bt.smart.cargo_owner.utils.ProgressDialogUtil;
import com.bt.smart.cargo_owner.utils.RequestParamsFM;
import com.bt.smart.cargo_owner.utils.ToastUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.Request;

/**
 * @创建者 AndyYan
 * @创建时间 2019/1/8 9:54
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class OrganizeInfoFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    private ImageView img_back;
    private TextView tv_title;
    private ImageView img_up_lic;
    private RelativeLayout rlt_lic;
    private ImageView img_lic;
    private EditText et_name;
    private EditText et_person;
    private EditText et_phone;
    private EditText et_tt;
    private EditText et_khh;
    private EditText et_yhzh;
    private EditText et_khdz;
    private EditText et_shuihao;
    private TextView tv_submit;

    private PopupWindow popupWindow;
    private int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 1001;//相册权限申请码
    private int SHOT_CODE = 10069;//调用系统相册-选择图片
    private int IMAGE = 10068;//调用系统相册-选择图片
    private String licPhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.frame_organiz_info, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        img_back = mRootView.findViewById(R.id.img_back);
        tv_title = mRootView.findViewById(R.id.tv_title);
        et_name = mRootView.findViewById(R.id.et_name);
        et_person = mRootView.findViewById(R.id.et_person);
        et_phone = mRootView.findViewById(R.id.et_phone);
        et_tt = mRootView.findViewById(R.id.et_tt);
        et_khh = mRootView.findViewById(R.id.et_khh);
        et_yhzh = mRootView.findViewById(R.id.et_yhzh);
        et_khdz = mRootView.findViewById(R.id.et_khdz);
        et_shuihao = mRootView.findViewById(R.id.et_shuihao);
        img_up_lic = mRootView.findViewById(R.id.img_up_lic);
        rlt_lic = mRootView.findViewById(R.id.rlt_lic);
        img_lic = mRootView.findViewById(R.id.img_lic);
        tv_submit = mRootView.findViewById(R.id.tv_submit);
    }

    private void initData() {
        img_back.setVisibility(View.VISIBLE);
        tv_title.setText("信息认证");
        img_back.setOnClickListener(this);
        img_up_lic.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.img_up_lic:
                //选择营业执照
                toGetLicPic(img_up_lic);
                break;
            case R.id.tv_submit:
                //下一步，填写资料信息
                toWriteLicInfo();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //相册返回，获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            licPhoto = c.getString(columnIndex);
            showImage(licPhoto);
            c.close();
        }

        //获取拍摄的图片
        if (requestCode == SHOT_CODE && resultCode == Activity.RESULT_OK) {
            if (null == licPhoto || "".equals(licPhoto)) {
                ToastUtils.showToast(getContext(), "未获取到照片");
                return;
            }
            showImage(licPhoto);
        }
    }

    //加载图片
    private void showImage(String imgPath) {
        GlideLoaderUtil.showImageView(getContext(), imgPath, img_lic);
    }

    private void toGetLicPic(ImageView img_up_lic) {
        showChoose(img_up_lic);
    }

    private void showChoose(ImageView v) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_camera_pic_popup, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        //设置消失监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //设置背景色
                setBackgroundAlpha(1.0f);
            }
        });
        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view);
        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    //设置PopupWindow的View点击事件
    private void setOnPopupViewClick(View view) {
        TextView tv_xc, tv_pz, tv_cancel;
        tv_xc = (TextView) view.findViewById(R.id.tv_xc);//相册
        tv_pz = (TextView) view.findViewById(R.id.tv_pz);//拍照
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);//取消
        tv_xc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //第二个参数是需要申请的权限
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);
                } else {
                    //权限已经被授予，在这里直接写要执行的相应方法即可
                    //调用相册
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    getActivity().startActivityForResult(intent, IMAGE);
                    popupWindow.dismiss();
                }
            }
        });
        tv_pz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //第二个参数是需要申请的权限
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    //权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);
                } else {
                    String mFilePath = Environment.getExternalStorageDirectory().getPath();//获取SD卡路径
                    long photoTime = System.currentTimeMillis();
                    mFilePath = mFilePath + "/temp" + photoTime + ".jpg";//指定路径
                    //权限已经被授予，在这里直接写要执行的相应方法即可
                    //调用相机
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri photoUri = Uri.fromFile(new File(mFilePath)); // 传递路径
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);// 更改系统默认存储路径
                    //把指定路径传递给需保存的字段
                    licPhoto = mFilePath;
                    getActivity().startActivityForResult(intent, SHOT_CODE);
                    popupWindow.dismiss();
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != popupWindow) {
                    popupWindow.dismiss();
                }
            }
        });
    }


    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        getActivity().getWindow().setAttributes(lp);
    }

    private void toWriteLicInfo() {
        if (MyTextUtils.isEditTextEmpty(et_name, "名称必须和证件上相同")) {
            ToastUtils.showToast(getContext(), "名称不能为空");
            return;
        }
        if (MyTextUtils.isEditTextEmpty(et_person, "请输入联系人")) {
            ToastUtils.showToast(getContext(), "请输入联系人");
            return;
        }
        if (MyTextUtils.isEditTextEmpty(et_phone, "请输入联系人手机号")) {
            ToastUtils.showToast(getContext(), "请输入联系人手机号");
            return;
        }
        if (MyTextUtils.isEditTextEmpty(et_tt, "请输入抬头")) {
            ToastUtils.showToast(getContext(), "请输入抬头");
            return;
        }
        if (MyTextUtils.isEditTextEmpty(et_khh, "请输入开户行")) {
            ToastUtils.showToast(getContext(), "请输入开户行");
            return;
        }
        if (MyTextUtils.isEditTextEmpty(et_yhzh, "请输入银行账号")) {
            ToastUtils.showToast(getContext(), "请输入银行账号");
            return;
        }
        if (MyTextUtils.isEditTextEmpty(et_khdz, "请输入开户地址")) {
            ToastUtils.showToast(getContext(), "请输入开户地址");
            return;
        }
        if (MyTextUtils.isEditTextEmpty(et_shuihao, "请输入税号")) {
            ToastUtils.showToast(getContext(), "请输入税号");
            return;
        }

        //提交信息
        submitInfo();
    }

    private void submitInfo() {
        File file = new File(licPhoto);
        if (!file.exists()) {
            ToastUtils.showToast(getContext(), "图片读取失败");
            return;
        }
        RequestParamsFM headParam = new RequestParamsFM();
        headParam.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("folder", "Auth");
        params.put("kind", 1);
        params.put("type", 1);
        params.setUseJsonStreamer(true);
        ProgressDialogUtil.startShow(getContext(), "正在提交图片...");
        HttpOkhUtils.getInstance().upDateFile(NetConfig.PHOTO, headParam, params, "file", file, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                if (200 != code) {
                    ToastUtils.showToast(getContext(), "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                UpPicInfo upPicInfo = gson.fromJson(resbody, UpPicInfo.class);
                ToastUtils.showToast(getContext(), upPicInfo.getMessage());
                if (upPicInfo.isOk()) {
                    //图片上传成功后，上传信息
                    sendMessage(upPicInfo.getData());
                }
            }
        });
    }

    private void sendMessage(String filePath) {
        RequestParamsFM headParams = new RequestParamsFM();
        headParams.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("id", MyApplication.userID);
        params.put("company_name", MyTextUtils.getEditTextContent(et_name));
        params.put("company_lxr", MyTextUtils.getEditTextContent(et_person));
        params.put("company_licence", filePath);
        params.put("company_contract", "");
        params.put("company_phone", MyTextUtils.getEditTextContent(et_phone));
        params.put("faddress", MyTextUtils.getEditTextContent(et_khdz));
        params.put("tai_tou", MyTextUtils.getEditTextContent(et_tt));
        params.put("f_bank", MyTextUtils.getEditTextContent(et_khh));
        params.put("f_bank_no", MyTextUtils.getEditTextContent(et_yhzh));
        params.put("tax_number", MyTextUtils.getEditTextContent(et_shuihao));
        params.setUseJsonStreamer(true);
        ProgressDialogUtil.startShow(getContext(), "正在提交信息...");
        HttpOkhUtils.getInstance().doPostWithHeader(NetConfig.DOUPDATEHZ, headParams, params, new HttpOkhUtils.HttpCallBack() {
            @Override
            public void onError(Request request, IOException e) {
                ProgressDialogUtil.hideDialog();
                ToastUtils.showToast(getContext(), "网络错误");
            }

            @Override
            public void onSuccess(int code, String resbody) {
                ProgressDialogUtil.hideDialog();
                if (200 != code) {
                    ToastUtils.showToast(getContext(), "网络错误" + code);
                    return;
                }
                Gson gson = new Gson();
                UpPicInfo upPicInfo = gson.fromJson(resbody, UpPicInfo.class);
                ToastUtils.showToast(getContext(), upPicInfo.getMessage());
                if (upPicInfo.isOk()) {
                    getActivity().finish();
                }
            }
        });
    }
}
