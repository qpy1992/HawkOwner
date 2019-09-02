package com.bt.smart.cargo_owner.util.UpApkDataFile;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import java.io.File;

/**
 * @创建者 AndyYan
 * @创建时间 2018/10/29 15:37
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class DownloadAppUtil {
    private static final String TAG = "DownloadAppUtil";

    public static long downloadUpdateApkId = -1;//下载更新Apk 下载任务对应的Id
    public static String downloadUpdateApkFilePath;//下载更新Apk 文件路径

    public static DownloadManager downloadManager;

    /**
     * 下载app完成后自动安装
     *
     * @param context
     * @param url
     * @param fileName
     * @param notificationTitle
     */
    public static void downloadWithAutoInstall(Context context, String url, String fileName, String notificationTitle) {
        if (TextUtils.isEmpty(url)) {
            Log.e(TAG, "url为空!!!!!");
            return;
        }
        try {
            Uri uri = Uri.parse(url);
            Log.i(TAG, String.valueOf(uri));
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            //在通知栏中显示
            request.setTitle(notificationTitle);
            request.setVisibleInDownloadsUi(true);

            String filePath = null;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//SD卡是否正常挂载
                filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            } else {
                Log.i(TAG, "没有SD卡" + "filePath:" + context.getFilesDir().getAbsolutePath());
                //filePath=context.getFilesDir().getAbsolutePath();
                return;
            }
            downloadUpdateApkFilePath = filePath + File.separator + fileName;
            // 若存在，则删除
            deleteFile(downloadUpdateApkFilePath);
            Uri fileUri = Uri.parse("file://" + downloadUpdateApkFilePath);
            request.setDestinationUri(fileUri);
            downloadUpdateApkId = downloadManager.enqueue(request);
            //注册广播接收者，监听下载状态
            context.registerReceiver(receiver,
                    new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //广播监听下载的各个状态
    private static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus(context);
        }
    };

    //检查下载状态
    private static void checkStatus(Context context) {
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        query.setFilterById(downloadUpdateApkId);
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    //下载完成安装APK
                    installAPK(context);
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        c.close();
    }


    private static boolean deleteFile(String fileStr) {
        File file = new File(fileStr);
        return file.delete();
    }

//    private static void installAPK(Context context) {
//        //获取下载文件的Uri
//        Uri downloadFileUri = downloadManager.getUriForDownloadedFile(downloadUpdateApkId);
//        if (downloadFileUri != null) {
//            Intent intent= new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            context.startActivity(intent);
//            context.unregisterReceiver(receiver);
//        }
//    }

    private static void installAPK(Context context) {

        Uri apkUri = FileProvider.getUriForFile(context, "com.bt.smart.cargo_owner.fileprovider", new File("file://" +downloadUpdateApkFilePath));//在AndroidManifest中的android:authorities值

        Intent install =new Intent(Intent.ACTION_VIEW);

        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        install.setDataAndType(apkUri, "application/vnd.android.package-archive");

        context.startActivity(install);

    }
}
