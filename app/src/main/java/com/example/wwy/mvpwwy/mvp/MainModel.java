package com.example.wwy.mvpwwy.mvp;


import com.app.common.baserx.RxUtil;
import com.example.wwy.mvpwwy.app.Api;
import com.example.wwy.mvpwwy.app.HostType;
import com.example.wwy.mvpwwy.bean.GankEntity;
import io.reactivex.Flowable;

import java.util.List;

public class MainModel implements MainContract.Model {

    //RxUtil.<List<GankEntity>>handleBaseResult() 根据(BaseResponse)做过统一处理 如果其他接口数据返回格式不统一用RxUtil.rxSchedulerHelper 做线程转换即可
    @Override
    public Flowable<List<GankEntity>> getGankEntity(String type, int count, int pageIndex) {
        return Api.getDefault(HostType.BASE_URL).getCommonDateNew(type, count, pageIndex).compose(RxUtil.<List<GankEntity>>handleBaseResult());
    }
}
