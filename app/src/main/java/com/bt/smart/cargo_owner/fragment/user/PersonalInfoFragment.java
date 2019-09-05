package com.bt.smart.cargo_owner.fragment.user;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bt.smart.cargo_owner.MyApplication;
import com.bt.smart.cargo_owner.NetConfig;
import com.bt.smart.cargo_owner.R;
import com.bt.smart.cargo_owner.messageInfo.UpPicInfo;
import com.bt.smart.cargo_owner.utils.GlideLoaderUtil;
import com.bt.smart.cargo_owner.utils.HttpOkhUtils;
import com.bt.smart.cargo_owner.utils.MyPopChoisePic;
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

public class PersonalInfoFragment extends Fragment implements View.OnClickListener {
    private View mRootView;
    private ImageView img_back;
    private TextView tv_title;
    private ImageView img_up_head;//选择头像
    private ImageView img_head;//头像
    private ImageView img_up_cardZ;//选择身份证正面
    private ImageView img_cardZ;
    private ImageView img_up_cardB;//选择身份证背面
    private ImageView img_cardB;
    private TextView tv_submit;

    private int SHOT_CODE = 10069;//调用系统相册-选择图片
    private int IMAGE = 10068;//调用系统相册-选择图片
    private int picWhich;//0是头像、1是身份证正面、2是身份证背面
    private String headPicPath;//头像本地地址
    private String SFZZPicPath;//身份证正面本地地址
    private String SFZBPicPath;//身份证背面本地地址

    private String headFileUrl;//头像图片网络地址
    private String IDZFileUrl;//身份证正面网络地址
    private String IDBFileUrl;//身份证背面网络地址

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.frame_personal_info, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        img_back = mRootView.findViewById(R.id.img_back_a);
        tv_title = mRootView.findViewById(R.id.tv_title);
        img_up_head = mRootView.findViewById(R.id.img_up_head);
        img_head = mRootView.findViewById(R.id.img_head);
        img_up_cardZ = mRootView.findViewById(R.id.img_up_cardZ);
        img_cardZ = mRootView.findViewById(R.id.img_cardZ);
        img_up_cardB = mRootView.findViewById(R.id.img_up_cardB);
        img_cardB = mRootView.findViewById(R.id.img_cardB);
        tv_submit = mRootView.findViewById(R.id.tv_submit);
    }

    private void initData() {
        img_back.setVisibility(View.VISIBLE);
        tv_title.setText("信息认证");
        img_back.setOnClickListener(this);
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
            case R.id.img_up_head:
                //上传人脸头像,人脸活体检测(暂无)
                toGetPic(0, headPicPath);
                break;
            case R.id.img_up_cardZ:
                //选择身份证正面
                toGetPic(1, SFZZPicPath);
                break;
            case R.id.img_up_cardB:
                //选择身份证背面
                toGetPic(2, SFZBPicPath);
                break;
            case R.id.tv_submit:
                //下一步，填写车辆信息
                toWriteCarInfo();
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
            if (0 == picWhich) {
                headPicPath = c.getString(columnIndex);
                showImage(img_head, headPicPath);
            } else if (1 == picWhich) {
                SFZZPicPath = c.getString(columnIndex);
                showImage(img_cardZ, SFZZPicPath);
            } else if (2 == picWhich) {
                SFZBPicPath = c.getString(columnIndex);
                showImage(img_cardB, SFZBPicPath);
            }
            c.close();
        }

        //获取拍摄的图片
        if (requestCode == SHOT_CODE && resultCode == Activity.RESULT_OK) {
            if (0 == picWhich) {
                if (null == headPicPath || "".equals(headPicPath)) {
                    ToastUtils.showToast(getContext(), "未获取到照片");
                    return;
                }
                showImage(img_head, headPicPath);
            } else if (1 == picWhich) {
                if (null == SFZZPicPath || "".equals(SFZZPicPath)) {
                    ToastUtils.showToast(getContext(), "未获取到照片");
                    return;
                }
                showImage(img_cardZ, SFZZPicPath);
            } else if (2 == picWhich) {
                if (null == SFZBPicPath || "".equals(SFZBPicPath)) {
                    ToastUtils.showToast(getContext(), "未获取到照片");
                    return;
                }
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

    private void sendPicList() {//
        if (null == headFileUrl || "".equals(headFileUrl)) {
            subPic(headPicPath, 0);
        }
        if (null == IDZFileUrl || "".equals(IDZFileUrl)) {
            subPic(SFZZPicPath, 1);
        }
        if (null == IDBFileUrl || "".equals(IDBFileUrl)) {
            subPic(SFZBPicPath, 2);
        }
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
                    if (0 == upLoadwhich) {
                        headFileUrl = upPicInfo.getData();
                    } else if (1 == upLoadwhich) {
                        IDZFileUrl = upPicInfo.getData();
                    } else if (2 == upLoadwhich) {
                        IDBFileUrl = upPicInfo.getData();
                        //提交信息
                        subFilePath();
                    }
                }
            }
        });
    }

    private void subFilePath() {
//        RequestParamsFM headParams = new RequestParamsFM();
//        headParams.put("X-AUTH-TOKEN", MyApplication.userToken);
//        RequestParamsFM params = new RequestParamsFM();
//        params.put("", "");
//        params.setUseJsonStreamer(true);
//        ProgressDialogUtil.startShow(getContext(), "正在提交信息...");
//        HttpOkhUtils.getInstance().doPostWithHeader(NetConfig.DOUPDATELOGISTICS, headParams, params, new HttpOkhUtils.HttpCallBack() {
//            @Override
//            public void onError(Request request, IOException e) {
//                ProgressDialogUtil.hideDialog();
//                ToastUtils.showToast(getContext(), "网络错误");
//            }
//
//            @Override
//            public void onSuccess(int code, String resbody) {
//                ProgressDialogUtil.hideDialog();
//                if (200 != code) {
//                    ToastUtils.showToast(getContext(), "网络错误");
//                    return;
//                }
//                Gson gson = new Gson();
//
//            }
//        });
    }

    private void toGetPic(int which, String cameraPath) {
        picWhich = which;
        cameraPath = Environment.getExternalStorageDirectory().getPath();//获取SD卡路径
        long photoTime = System.currentTimeMillis();
        cameraPath = cameraPath + "/temp" + photoTime + ".jpg";//指定路径
        MyPopChoisePic mPopChoisePic = new MyPopChoisePic(getActivity());
        mPopChoisePic.showChoose(img_up_head, cameraPath);
    }

    private void toWriteCarInfo() {
        if (null == headPicPath || "".equals(headPicPath)) {
            ToastUtils.showToast(getContext(), "头像图片不能为空");
            return;
        }
        if (null == SFZZPicPath || "".equals(SFZZPicPath)) {
            ToastUtils.showToast(getContext(), "身份证正面图片不能为空");
            return;
        }
        if (null == SFZBPicPath || "".equals(SFZBPicPath)) {
            ToastUtils.showToast(getContext(), "身份证背面图片不能为空");
            return;
        }
        //提交三张图片,在提交信息
        sendPicList();
    }

    public void changePicPath(String pciPath) {
        if (0 == picWhich) {
            headPicPath = pciPath;
        } else if (1 == picWhich) {
            SFZZPicPath = pciPath;
        } else if (2 == picWhich) {
            SFZBPicPath = pciPath;
        }
    }
}
