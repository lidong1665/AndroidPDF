/**
 * Copyright 2014 Joan Zapata
 *
 * This file is part of Android-pdfview.
 *
 * Android-pdfview is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Android-pdfview is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Android-pdfview.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.lidong.pdf.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.lidong.pdf.api.ApiManager;
import com.lidong.pdf.listener.OnFileListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class FileUtils {

    private FileUtils() {
        // Prevents instantiation
    }

    public static File fileFromAsset(Context context, String assetName) throws IOException {
        File outFile = new File(context.getCacheDir(), assetName + "-pdfview.pdf");
        copy(context.getAssets().open(assetName), outFile);
        return outFile;
    }

    /**
     * http://file.chmsp.com.cn/colligate/file/000000224821.pdf
     * @param fileUrl
     */
    public static void  fileFromLocalStorage(String fileUrl, final String fileName, final OnFileListener listener)throws IOException{
        final String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/pdf/";
        final File file = new File(SDPath, fileName);
        if (file.exists()){//文件已经存在，直接获取本地文件打开，否则从网络现在文件，文件下载成功之后再打开
              listener.setFile(file);

        }else {
            ApiManager.downloadPicFromNet(fileUrl).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ResponseBody>() {
                        @Override
                        public void call(ResponseBody responseBody) {
                            InputStream is = null;
                            byte[] buf = new byte[2048];
                            int len = 0;
                            FileOutputStream fos = null;
                            try {
                                is = responseBody.byteStream();
                                long total = responseBody.contentLength();
                                File file1 = new File(SDPath);
                                if (!file1.exists()){
                                    file1.mkdirs();
                                }
                                File fileN = new File(SDPath, fileName);
                                if (!fileN.exists()){
                                    boolean mkdir = fileN.createNewFile();
                                    Log.d("mkdir", "call: "+mkdir);
                                }
                                fos = new FileOutputStream(fileN);
                                long sum = 0;
                                while ((len = is.read(buf)) != -1) {
                                    fos.write(buf, 0, len);
                                    sum += len;
                                    int progress = (int) (sum * 1.0f / total * 100);
                                    Log.d("h_bl", "progress=" + progress);
                                }
                                fos.flush();
                                Log.d("h_bl", "文件下载成功");
                                listener.setFile(fileN);
                            } catch (Exception e) {
                                Log.d("h_bl", "文件下载失败");
                            } finally {
                                try {
                                    if (is != null)
                                        is.close();
                                } catch (IOException e) {
                                }
                                try {
                                    if (fos != null)
                                        fos.close();
                                } catch (IOException e) {
                                }
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.d("h_bl", "文件下载失败");
                        }
                    });
        }
    }



    public static void copy(InputStream inputStream, File output) throws IOException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(output);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
    }
}
