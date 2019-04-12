package com.example.wwy.mvpwwy.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.app.common.commonutils.ImageLoaderUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wwy.mvpwwy.R;
import com.example.wwy.mvpwwy.bean.GankEntity;

import java.util.List;

public class GankAdapter extends BaseQuickAdapter<GankEntity, BaseViewHolder> {
    public GankAdapter(@Nullable List<GankEntity> data) {
        super(R.layout.item_gank, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GankEntity item) {
        ImageLoaderUtils.display(mContext, (ImageView) helper.getView(R.id.iv_gank), item.getUrl());
    }
}
