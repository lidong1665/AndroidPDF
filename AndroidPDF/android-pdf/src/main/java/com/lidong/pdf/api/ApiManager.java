package com.lidong.pdf.api;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class ApiManager {


    private static final String BASE_URL ="http://file.chmsp.com.cn/" ;
    private static final Retrofit sRetrofit = new Retrofit .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    private static final ApiManagerService apiManager = sRetrofit.create(ApiManagerService.class);

    public static Observable<ResponseBody> downloadPicFromNet(String fileUrl) {
        return apiManager.downloadPicFromNet(fileUrl);
    }

}