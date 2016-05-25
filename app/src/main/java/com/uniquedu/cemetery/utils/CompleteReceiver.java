package com.uniquedu.cemetery.utils;

/**
 * Created by ZhongHang on 2016/4/12.
 */

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class CompleteReceiver extends BroadcastReceiver {

    private DownloadManager downloadManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            Toast.makeText(context, "下载完成了....", Toast.LENGTH_LONG).show();
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);                                                                                      //TODO 判断这个id与之前的id是否相等，如果相等说明是之前的那个要下载的文件
            Query query = new Query();
            query.setFilterById(id);
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = downloadManager.query(query);
            int columnCount = cursor.getColumnCount();
            String path = null;                                                                                                                                       //TODO 这里把所有的列都打印一下，有什么需求，就怎么处理,文件的本地路径就是path
            if (cursor.moveToFirst()) {
                int fileNameIdx =
                        cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                int fileUriIdx =
                        cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);

                String fileName = cursor.getString(fileNameIdx);
                String fileUri = cursor.getString(fileUriIdx);

                // TODO Do something with the file.
                Log.d("MyDownlaod", fileName + " : " + fileUri + "     " + Environment.getDownloadCacheDirectory());
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setDataAndType(Uri.fromFile(new File(fileName)),
                        "application/vnd.android.package-archive");
                context.startActivity(i);
                android.os.Process.killProcess(android.os.Process.myPid());

            }
            cursor.close();


//            Log.d("CompleteReceiver",path);//content://downloads/my_downloads/31
        } else if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            Toast.makeText(context, "下载未成功", Toast.LENGTH_LONG).show();
        }
    }
}

