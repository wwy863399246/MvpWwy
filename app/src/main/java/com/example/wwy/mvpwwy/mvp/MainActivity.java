package com.example.wwy.mvpwwy.mvp;

import android.support.v4.app.NavUtils;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import com.app.common.baseview.BaseMvpActivity;
import com.app.common.commonwidget.LoadingView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.wwy.mvpwwy.R;
import com.example.wwy.mvpwwy.adapter.GankAdapter;
import com.example.wwy.mvpwwy.app.ApiConstants;
import com.example.wwy.mvpwwy.bean.GankEntity;
import com.example.wwy.mvpwwy.utils.BaseUtils;
import com.example.wwy.mvpwwy.utils.SpacesItemDecoration;
import com.example.wwy.mvpwwy.widget.NewToolbar;
import me.dkzwm.widget.srl.RefreshingListenerAdapter;
import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.extra.header.ClassicHeader;

import java.util.List;

public class MainActivity extends BaseMvpActivity<MainPresenter, MainModel> implements MainContract.View, BaseQuickAdapter
        .RequestLoadMoreListener, LoadingView.onReloadListener {

    @BindView(R.id.my_toolbar)
    NewToolbar my_toolbar;
    @BindView(R.id.my_recycleview)
    RecyclerView my_recycleview;
    @BindView(R.id.my_refreshlayout)
    SmoothRefreshLayout my_refreshlayout;
    private int count = 20;
    private int pageIndex = 1;
    private GankAdapter gankAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        my_toolbar.hideLeftImage();
        gankAdapter = new GankAdapter(null);
        //设置layoutManager
        my_recycleview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        my_recycleview.setHasFixedSize(true);
        my_recycleview.setAdapter(gankAdapter);
        my_recycleview.addItemDecoration(new SpacesItemDecoration(16));
        gankAdapter.setOnLoadMoreListener(this, my_recycleview);
        //禁止刷新控件上拉加载
        my_refreshlayout.setDisableLoadMore(true);
        my_refreshlayout.setHeaderView(new ClassicHeader(this));
        //下拉刷新
        my_refreshlayout.setOnRefreshListener(new RefreshingListenerAdapter() {
            @Override
            public void onRefreshing() {
                pageIndex = 1;
                loadData();
            }
        });
        mLoadingView.setOnReloadListener(this);
        loadData();
    }

    @Override
    public void showLoading(String title) {
        gankAdapter.setEmptyView(mLoadingView);
        mLoadingView.setLoadingTip(LoadingView.LoadStatus.loading);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(my_toolbar).init();
    }

    @Override
    public void stopLoading() {
        mLoadingView.setLoadingTip(LoadingView.LoadStatus.finish);
    }

    @Override
    public void showErrorTip(String msg) {
        //设置网络错误布局
        mLoadingView.setLoadingTip(LoadingView.LoadStatus.error);
        if (my_refreshlayout.isRefreshing()) {
            my_refreshlayout.refreshComplete();
        } else {
            gankAdapter.loadMoreFail();
        }
    }

    //接口请求成功回调返回
    @Override
    public void showGankEntityDetails(List<GankEntity> gankEntity) {
        if (1 == pageIndex) {
            if (BaseUtils.isList(gankEntity)) {
                my_refreshlayout.refreshComplete();
                gankAdapter.setNewData(gankEntity);
                gankAdapter.disableLoadMoreIfNotFullPage();
            } else {
                //设置空布局
            }
        } else {
            gankAdapter.loadMoreComplete();
            if (BaseUtils.isList(gankEntity)) {
                gankAdapter.addData(gankEntity);
            }
            if (null == gankEntity || gankEntity.size() < count) {
                gankAdapter.loadMoreEnd(true);
            }
        }
    }


    @Override
    public void onLoadMoreRequested() {
        pageIndex++;
        loadData();

    }

    private void loadData() {
        if (gankAdapter.getData().size() <= 0) {
            showLoading(getString(R.string.please_later));
        }
        //接口请求
        mPresenter.loadGankEntityData(ApiConstants.FlagWelFare, count, pageIndex);
    }

    //失败重试
    @Override
    public void reload() {
        loadData();
    }
}
