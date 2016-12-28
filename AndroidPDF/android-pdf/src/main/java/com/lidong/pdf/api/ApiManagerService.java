package com.lidong.pdf.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface ApiManagerService {

    /**
     * //下载文件
     * @param fileUrl 文件的url
     * @return
     */
    @GET
    Observable<ResponseBody> downloadPicFromNet(@Url String fileUrl);
  
}  