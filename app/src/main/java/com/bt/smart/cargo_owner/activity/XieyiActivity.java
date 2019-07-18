package com.bt.smart.cargo_owner.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bt.smart.cargo_owner.MyApplication;
import com.bt.smart.cargo_owner.NetConfig;
import com.bt.smart.cargo_owner.R;

public class XieyiActivity extends AppCompatActivity {

    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xieyi);
        wv = findViewById(R.id.wv);
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        Log.i("协议网络地址",NetConfig.IMG_HEAD+ MyApplication.companyContract);
        wv.loadUrl("file:///android_asset/mypdf.html?pdfpath="+ NetConfig.IMG_HEAD+ MyApplication.companyContract);
    }
}
