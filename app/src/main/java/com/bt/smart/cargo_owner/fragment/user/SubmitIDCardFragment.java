package com.bt.smart.cargo_owner.fragment.user;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bt.smart.cargo_owner.MyApplication;
import com.bt.smart.cargo_owner.NetConfig;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.activity.homeAct.AuthenticationWebAct;
import com.bt.smart.cargo_owner.messageInfo.AuthInfo;
import com.bt.smart.cargo_owner.messageInfo.UpPicInfo;
import com.bt.smart.cargo_owner.utils.GlideLoaderUtil;
import com.bt.smart.cargo_owner.utils.HttpOkhUtils;
import com.bt.smart.cargo_owner.utils.MyPopChoisePic;
import com.bt.smart.cargo_owner.utils.MyTextUtils;
import com.bt.smart.cargo_owner.utils.ProgressDialogUtil;
import com.bt.smart.cargo_owner.utils.RequestParamsFM;
import com.bt.smart.cargo_owner.utils.ToastUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.Request;

public class SubmitIDCardFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    private ImageView img_back;
    private TextView tv_title;
    private EditText et_name;
    private EditText et_code;
    private LinearLayout line_legal;
    private EditText et_legalName;
    private EditText et_legalIdNo;
    private LinearLayout line_lic;
    private ImageView img_up_lic;
    private RelativeLayout rlt_lic;
    private ImageView img_lic;
    private LinearLayout line_per;
    private ImageView img_up_head;
    private RelativeLayout rlt_headex;
    private ImageView img_head;
    private ImageView img_up_cardZ;
    private RelativeLayout rlt_carZ;
    private ImageView img_cardZ;
    private RelativeLayout rlt_carB;
    private ImageView img_up_cardB;
    private ImageView img_cardB;
    private TextView tv_submit;

    private int SHOT_CODE = 10069;//调用系统相册-选择图片
    private int IMAGE = 10068;//调用系统相册-选择图片
    private String licPhotoPath;
    private String licPhotoUrl;
    private int picWhich;//0是营业执照、1是头像、2是身份证正面、3是身份证背面
    private String headPicPath;//头像本地地址
    private String SFZZPicPath;//身份证正面本地地址
    private String SFZBPicPath;//身份证背面本地地址
    private String headFileUrl;//头像图片网络地址
    private String IDZFileUrl;//身份证正面网络地址
    private String IDBFileUrl;//身份证背面网络地址

    private OrganizeInfoFragment orgFt;
    private PersonalInfoFragment personalFt;

    private int REQUEST_AUTHENTICA_CODE = 10110;//跳转人脸认证协议码
    private int RESULT_AUTHENTICA_CODE = 10111;
    private int RESULT_CHECK_FACE = 1018;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.frame_personal_idcard, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        img_back = mRootView.findViewById(R.id.img_back);
        tv_title = mRootView.findViewById(R.id.tv_title);
        et_name = mRootView.findViewById(R.id.et_name);
        et_code = mRootView.findViewById(R.id.et_code);
        line_legal = mRootView.findViewById(R.id.line_legal);
        et_legalName = mRootView.findViewById(R.id.et_legalName);
        et_legalIdNo = mRootView.findViewById(R.id.et_legalIdNo);
        line_lic = mRootView.findViewById(R.id.line_lic);
        img_up_lic = mRootView.findViewById(R.id.img_up_lic);
        rlt_lic = mRootView.findViewById(R.id.rlt_lic);
        img_lic = mRootView.findViewById(R.id.img_lic);
        line_per = mRootView.findViewById(R.id.line_per);
        img_up_head = mRootView.findViewById(R.id.img_up_head);
        rlt_headex = mRootView.findViewById(R.id.rlt_headex);
        img_head = mRootView.findViewById(R.id.img_head);
        img_up_cardZ = mRootView.findViewById(R.id.img_up_cardZ);
        rlt_carZ = mRootView.findViewById(R.id.rlt_carZ);
        img_cardZ = mRootView.findViewById(R.id.img_cardZ);
        img_up_cardB = mRootView.findViewById(R.id.img_up_cardB);
        rlt_carB = mRootView.findViewById(R.id.rlt_carB);
        img_cardB = mRootView.findViewById(R.id.img_cardB);
        tv_submit = mRootView.findViewById(R.id.tv_submit);
    }

    private void initData() {
        img_back.setVisibility(View.VISIBLE);
        tv_title.setText("货主身份认证");

        if ("2".equals(MyApplication.userType)) {//企业
            line_legal.setVisibility(View.VISIBLE);
        } else {//个人
            line_lic.setVisibility(View.GONE);
            line_per.setVisibility(View.VISIBLE);
        }
        img_back.setOnClickListener(this);
        img_up_lic.setOnClickListener(this);
        img_up_head.setOnClickListener(this);
        img_up_cardZ.setOnClickListener(this);
        img_up_cardB.setOnClickListener(this);
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
                toGetPic(0);
                break;
            case R.id.img_up_head:
                //选择头像
                toGetPic(1);
                break;
            case R.id.img_up_cardZ:
                //选择身份证正面
                toGetPic(2);
                break;
            case R.id.img_up_cardB:
                //选择身份证背面
                toGetPic(3);
                break;
            case R.id.tv_submit:
                //检测是否正确填写
                //然后提交信息
                checkWriteInfo();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //判断人脸认证信息
        if (REQUEST_AUTHENTICA_CODE == requestCode && RESULT_AUTHENTICA_CODE == resultCode) {
            if ("2".equals(MyApplication.userType)) {
                getActivity().setResult(RESULT_CHECK_FACE);
                getActivity().finish();
            } else {
                //提交个人资料
                FragmentTransaction ftt = getFragmentManager().beginTransaction();
                personalFt = new PersonalInfoFragment();
                ftt.add(R.id.frame, personalFt, "personalFt");
                ftt.commit();
            }
        }
        //相册返回，获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            if (0 == picWhich) {
                licPhotoPath = c.getString(columnIndex);
                rlt_lic.setVisibility(View.GONE);
                showImage(img_lic, licPhotoPath);
            } else if (1 == picWhich) {
                headPicPath = c.getString(columnIndex);
                rlt_headex.setVisibility(View.GONE);
                showImage(img_head, headPicPath);
            } else if (2 == picWhich) {
                SFZZPicPath = c.getString(columnIndex);
                rlt_carZ.setVisibility(View.GONE);
                showImage(img_cardZ, SFZZPicPath);
            } else if (3 == picWhich) {
                SFZBPicPath = c.getString(columnIndex);
                rlt_carB.setVisibility(View.GONE);
                showImage(img_cardB, SFZBPicPath);
            }
            c.close();
        }

        //获取拍摄的图片
        if (requestCode == SHOT_CODE && resultCode == Activity.RESULT_OK) {
            if (0 == picWhich) {
                if (null == licPhotoPath || "".equals(licPhotoPath)) {
                    ToastUtils.showToast(getContext(), "未获取到照片");
                    return;
                }
                rlt_lic.setVisibility(View.GONE);
                showImage(img_lic, licPhotoPath);
            } else if (1 == picWhich) {
                if (null == headPicPath || "".equals(headPicPath)) {
                    ToastUtils.showToast(getContext(), "未获取到照片");
                    return;
                }
                rlt_headex.setVisibility(View.GONE);
                showImage(img_head, headPicPath);
            } else if (2 == picWhich) {
                if (null == SFZZPicPath || "".equals(SFZZPicPath)) {
                    ToastUtils.showToast(getContext(), "未获取到照片");
                    return;
                }
                rlt_carZ.setVisibility(View.GONE);
                showImage(img_cardZ, SFZZPicPath);
            } else if (3 == picWhich) {
                if (null == SFZBPicPath || "".equals(SFZBPicPath)) {
                    ToastUtils.showToast(getContext(), "未获取到照片");
                    return;
                }
                rlt_carB.setVisibility(View.GONE);
                showImage(img_cardB, SFZBPicPath);
            } else {
                ToastUtils.showToast(getContext(), "出现未知状况，请重新选择");
            }
        }
    }

    //加载图片
    private void showImage(ImageView img, String imgPath) {
        GlideLoaderUtil.showImageView(getContext(), imgPath, img);
    }

    private void toGetPic(int which) {
        picWhich = which;
        long photoTime = System.currentTimeMillis();
        MyPopChoisePic mPopChoisePic = new MyPopChoisePic(getActivity());
        if (0 == picWhich) {
            licPhotoPath = Environment.getExternalStorageDirectory().getPath() + "/temp" + photoTime + ".jpg";
            mPopChoisePic.showChoose(img_up_head, licPhotoPath);
        } else if (1 == picWhich) {
            headPicPath = Environment.getExternalStorageDirectory().getPath() + "/temp" + photoTime + ".jpg";
            mPopChoisePic.showChoose(img_up_head, headPicPath);
        } else if (2 == picWhich) {
            SFZZPicPath = Environment.getExternalStorageDirectory().getPath() + "/temp" + photoTime + ".jpg";
            mPopChoisePic.showChoose(img_up_head, SFZZPicPath);
        } else if (3 == picWhich) {
            SFZBPicPath = Environment.getExternalStorageDirectory().getPath() + "/temp" + photoTime + ".jpg";
            mPopChoisePic.showChoose(img_up_head, SFZBPicPath);
        }
    }
    
    private void checkWriteInfo() {
        if (MyTextUtils.isEditTextEmpty(et_name, "名称必须和证件上相同")) {
            ToastUtils.showToast(getContext(), "名称不能为空");
            return;
        }

        if (MyTextUtils.isEditTextEmpty(et_code, "请输入证件号")) {
            ToastUtils.showToast(getContext(), "证件号不能为空");
            return;
        }

        //先提交图片，在提交信息
        if ("2".equals(MyApplication.userType)) {
            if (MyTextUtils.isEditTextEmpty(et_legalName, "请输入法人代表名称")) {
                ToastUtils.showToast(getContext(), "法人名称不能为空");
                return;
            }
            if (MyTextUtils.isEditTextEmpty(et_legalIdNo, "请输入法人身份证号")) {
                ToastUtils.showToast(getContext(), "法人身份证件号不能为空");
                return;
            }
            if (null == licPhotoPath || "".equals(licPhotoPath)) {
                ToastUtils.showToast(getContext(), "请选择营业执照");
                return;
            }
            submitLicPic();
        } else {
            if (null == headPicPath || "".equals(headPicPath)) {
                ToastUtils.showToast(getContext(), "请选择头像");
                return;
            }
            if (null == SFZZPicPath || "".equals(SFZZPicPath)) {
                ToastUtils.showToast(getContext(), "请选择身份证正面");
                return;
            }
            if (null == SFZBPicPath || "".equals(SFZBPicPath)) {
                ToastUtils.showToast(getContext(), "请选择身份证背面");
                return;
            }
            submitPerPic();
        }
    }

    private void submitPerPic() {
        subPic(headPicPath, 1);
    }

    private void subPic(String picPhoto, final int upLoadwhich) {
        File file = new File(picPhoto);
        if (!file.exists()) {
            ToastUtils.showToast(getContext(), "相片读取失败");
            return;
        }
        RequestParamsFM headParam = new RequestParamsFM();
        headParam.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("folder", "Auth");
        params.put("kind", 1);
        params.put("type", 1);
        params.setUseJsonStreamer(true);
        ProgressDialogUtil.startShow(getContext(), "正在提交图片" + upLoadwhich + "...");
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
                    if (1 == upLoadwhich) {
                        headFileUrl = upPicInfo.getData();
                        subPic(SFZZPicPath, 2);
                    } else if (2 == upLoadwhich) {
                        IDZFileUrl = upPicInfo.getData();
                        subPic(SFZBPicPath, 3);
                    } else if (3 == upLoadwhich) {
                        IDBFileUrl = upPicInfo.getData();
                        //提交信息
                        submitToService();
                    }
                }
            }
        });
    }

    private void submitLicPic() {
        File file = new File(licPhotoPath);
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
                    licPhotoUrl = upPicInfo.getData();
                    submitToService();
                }
            }
        });
    }

    private void submitToService() {
        String postUrl;
        String paramKey;
        RequestParamsFM headParams = new RequestParamsFM();
        headParams.put("X-AUTH-TOKEN", MyApplication.userToken);
        RequestParamsFM params = new RequestParamsFM();
        params.put("id", MyApplication.userID);
        params.put("name", MyTextUtils.getEditTextContent(et_name));
        if ("2".equals(MyApplication.userType)) {//企业
            postUrl = NetConfig.ORGANIZEAUTH;
            paramKey = "organizeAuth";
            params.put("organCode", MyTextUtils.getEditTextContent(et_code));
            params.put("companyLicence", licPhotoUrl);
            params.put("legalName", MyTextUtils.getEditTextContent(et_legalName));
            params.put("legalIdNo", MyTextUtils.getEditTextContent(et_legalIdNo));
        } else {//货主个人
            postUrl = NetConfig.PERSONAUTH;
            paramKey = "personAuth";
            params.put("idno", MyTextUtils.getEditTextContent(et_code));
            params.put("type", 0);
            params.put("headpic", headFileUrl);
            params.put("front", IDZFileUrl);
            params.put("back", IDBFileUrl);
        }
        ProgressDialogUtil.startShow(getContext(), "正在提交信息...");
        HttpOkhUtils.getInstance().doPostWithHeader(postUrl, headParams, params, new HttpOkhUtils.HttpCallBack() {
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
                AuthInfo authInfo = gson.fromJson(resbody, AuthInfo.class);
                ToastUtils.showToast(getContext(), authInfo.getMessage());
                if (authInfo.isOk()) {
                    //先跳转支付宝人脸认证
                    String webUri = authInfo.getData().toString();
                    if (null == webUri || "".equals(webUri)) {
                        ToastUtils.showToast(getContext(), "未获取到个人认证信息");
                        return;
                    }
                    Intent intent = new Intent(getContext(), AuthenticationWebAct.class);
                    intent.putExtra("url", webUri);
                    getActivity().startActivityForResult(intent, REQUEST_AUTHENTICA_CODE);
//                    getActivity().finish();

//                    if ("2".equals(MyApplication.userType)) {
//                        //提交企业资料
//                        orgFt = new OrganizeInfoFragment();
//                        FragmentTransaction ftt = getFragmentManager().beginTransaction();
//                        ftt.add(R.id.frame, orgFt, "orgFt");
//                        ftt.commit();
//                    } else {
//                        //提交个人资料
//                        personalFt = new PersonalInfoFragment();
//                        FragmentTransaction ftt = getFragmentManager().beginTransaction();
//                        ftt.add(R.id.frame, personalFt, "personalFt");
//                        ftt.commit();
//                    }
                }
            }
        });
    }
}
