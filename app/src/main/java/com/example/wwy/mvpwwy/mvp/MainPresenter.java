package com.example.wwy.mvpwwy.mvp;

import com.app.common.baserx.RxSubscriber;
import com.example.wwy.mvpwwy.bean.GankEntity;

import java.util.List;

public class MainPresenter extends MainContract.Presenter {
    @Override
    void loadGankEntityData(String type, int count, int pageIndex) {
        mRxManage.addSubscribe(mModel.getGankEntity(type,count,pageIndex).subscribeWith(new RxSubscriber<List<GankEntity>>(mContext) {
            @Override
            protected void _onNext(List<GankEntity> gankEntities) {
                mView.showGankEntityDetails(gankEntities);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
