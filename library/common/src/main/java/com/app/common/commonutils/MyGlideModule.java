package com.app.common.commonutils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.app.common.commonwidget.OkHttpUrlLoader;
import com.app.common.commonwidget.ProgressInterceptor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * 用户生成GlideApp，沿用Glide3版本使用方式
 */
@GlideModule
public final class MyGlideModule extends AppGlideModule
{
    @Override
    public void applyOptions(Context context, GlideBuilder builder)
    {

        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).setMemoryCacheScreens(2).build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
        //自定义缓存目录，磁盘缓存给150M 另外一种设置缓存方式
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "GlideImgCache", 150 * 1024 * 1024));
//        //配置图片缓存格式 默认格式为8888
//        builder.setDefaultRequestOptions(RequestOptions.formatOf(DecodeFormat.PREFER_ARGB_8888));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry)
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new ProgressInterceptor())
                .build();

        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
    }

    /**
     * 禁止解析Manifest文件
     * 主要针对V3升级到v4的用户，可以提升初始化速度，避免一些潜在错误
     *
     * @return
     */
    @Override
    public boolean isManifestParsingEnabled()
    {
        return false;
    }

}
