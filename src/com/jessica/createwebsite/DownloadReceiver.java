package com.jessica.createwebsite;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;
import com.artifex.mupdfdemo.MuPDFActivity;

import static com.jessica.createwebsite.StartActivity.startViewDocument;

/**
* Created with IntelliJ IDEA.
* User: ruslan
* Date: 25.03.13
* Time: 22:30
* To change this template use File | Settings | File Templates.
*/
public class DownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //To change body of implemented methods use File | Settings | File Templates.
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            Bundle bundle = intent.getExtras();

            if (bundle != null){
                long downloadedId = bundle.getLong("extra_download_id");
                DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadedId);
                Cursor cursor = dm.query(query);
                if (cursor != null){
                    cursor.moveToFirst();
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL){
                        String filename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
//                        String value = convertMediaUriToPath(context, Uri.parse(filename));
                        if (filename != null){
                            startViewDocument(context, filename);
                        }
                    } else {
                        Toast.makeText(context, R.string.download_error, Toast.LENGTH_LONG).show();
                    }
                    cursor.close();
                }

            }


        }
    }

    public static String convertMediaUriToPath(Context context, Uri uri) {
        String [] proj={MediaStore.Files.FileColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj,  null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

}
