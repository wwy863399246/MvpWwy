package com.example.wwy.mvpwwy.app;

import com.app.common.basebean.BaseResponse;
import com.example.wwy.mvpwwy.bean.GankEntity;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

import java.util.List;

public interface ApiService {
   
    //http://gank.io/api/data/Android/10/1
    @Headers("Cache-Control: public, max-age=120")
    @GET("data/{type}/{count}/{pageIndex}")
    Flowable<BaseResponse<List<GankEntity>>> getCommonDateNew(@Path("type") String type,
                                                              @Path("count") int count,
                                                              @Path("pageIndex") int pageIndex);
    //这里填写全部路径就会覆盖掉Build得BaseUrl
    @Headers("Cache-Control: public, max-age=3600")
    @GET("day/history")
    Flowable<BaseResponse<List<String>>> getGankHistoryDate();
                                                          
}
