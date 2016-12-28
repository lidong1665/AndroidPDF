package com.lidong.pdf.api;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class ApiManager {


    private static final String BASE_URL ="http://file.chmsp.com.cn/" ;
    private static final Retrofit sRetrofit = new Retrofit .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    private static final ApiManagerService apiManager = sRetrofit.create(ApiManagerService.class);

    /**
     * 下载文件
     * @param fileUrl
     * @return
     */
    public static Observable<ResponseBody> downloadPicFromNet(String fileUrl) {
        return apiManager.downloadPicFromNet(fileUrl);
    }

}