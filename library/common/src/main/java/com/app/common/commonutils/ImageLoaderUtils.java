package com.app.common.commonutils;

import android.content.Context;
import android.widget.ImageView;
import com.app.common.R;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Description : 图片加载工具类 使用glide框架封装
 */
public class ImageLoaderUtils {

    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        GlideApp.with(context).load(url).placeholder(placeholder).error(error).transition(withCrossFade()).into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        GlideApp.with(context).load(url).preload();
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().placeholder(R.drawable.img_pic_placeholder).error(R.drawable
                .img_pic_placeholder).into(imageView);
    }

    public static void display(Context context, ImageView imageView, File url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.ic_photo_default).error(R.mipmap.ic_photo_default)
                .into(imageView);
    }

    public static void displaySmallPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        GlideApp.with(context).load(url).preload();
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.ic_photo_default).error(R.mipmap.ic_photo_default)
                .thumbnail(0.5f).into(imageView);
    }

    public static void displayBigPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        GlideApp.with(context).asBitmap().load(url).format(DecodeFormat.PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap
                .ic_photo_default).error(R.mipmap.ic_photo_default).into(imageView);
    }

    public static void display(Context context, ImageView imageView, int url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        GlideApp.with(context).load(url).preload();
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().placeholder(R.mipmap.ic_photo_default).error(R.mipmap
                .ic_photo_default).into(imageView);
    }


    public static void displayLvIcon(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(imageView);
    }

    public static void displayCircle(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        } else {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        if (isFinished(context)) {
            return;
        }
        GlideApp.with(context).load(url).preload();
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.img_avatar_placeholder).error(R.drawable
                .img_avatar_placeholder).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
    }

    /**
     * 加载圆角图片
     */
    public static void displayRoundedCorners(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        } else {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        if (isFinished(context)) {
            return;
        }
        GlideApp.with(context).load(url).preload();
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.img_pic_placeholder).error(R.drawable
                .img_pic_placeholder).apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(12))).into(imageView);
    }

    public static boolean isFinished(Context mContext) {
        if (null == mContext) {
            return true;
        }
        return false;
    }

    /**
     * 加载图片设置模糊度
     */
    public static void displayBlurImage(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        if (isFinished(context)) {
            return;
        }
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).apply(RequestOptions.bitmapTransform(new GlideBlurformation(context))).into
                (imageView);
    }

    /**
     * 加载圆形图片
     */
    public static void displayCircle(Context context, ImageView imageView, String url, int placeHolder, int errId) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        } else {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        if (isFinished(context)) {
            return;
        }
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(placeHolder).error(errId).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
    }

    public static void displayChatMap(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        } else {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        if (isFinished(context)) {
            return;
        }
        GlideApp.with(context).load(url).preload();
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ico_invalid_error).error(R.drawable
                .ico_invalid_error).apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(12))).into(imageView);
    }


}
