package com.example.wwy.mvpwwy.mvp;

import com.app.common.basemvp.BaseModel;
import com.app.common.basemvp.BasePresenter;
import com.app.common.basemvp.BaseView;
import com.example.wwy.mvpwwy.bean.GankEntity;
import io.reactivex.Flowable;

import java.util.List;

public interface MainContract {

    interface Model extends BaseModel {
        Flowable<List<GankEntity>> getGankEntity(String type, int count, int pageIndex);

    }

    interface View extends BaseView {
        void showGankEntityDetails(List<GankEntity> gankEntity);

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void loadGankEntityData(String type, int count, int pageIndex);
    }
}
