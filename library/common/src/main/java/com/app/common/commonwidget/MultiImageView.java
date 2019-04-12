package com.app.common.commonwidget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import com.app.common.R;
import com.app.common.baseapp.BaseApplication;
import com.app.common.commonutils.DisplayUtil;
import com.app.common.commonutils.GlideApp;
import com.app.common.commonutils.ImageLoaderUtils;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;


/**
 * des:显示1~N张图片的View
 * Created by xsf
 * on 2016.07.11:11
 */
public class MultiImageView extends LinearLayout
{
    public static int MAX_WIDTH = 0;

    // 照片的Url列表
    private List<String> imagesList;

    /** 长度 单位为Pixel **/
    private int pxOneMaxWandH;  // 单张图最大允许宽高
    private int pxMoreWandH = 0;// 多张图的宽高
    private int pxImagePadding = DisplayUtil.dip2px(3);// 图片间的间距

    private int MAX_PER_ROW_COUNT = 3;// 每行显示最大数

    private LayoutParams onePicPara;
    private LayoutParams morePara, moreParaColumnFirst;
    private LayoutParams rowPara;
    private OnItemClickListener mOnItemClickListener;
    private boolean showBigImage=true;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        mOnItemClickListener = onItemClickListener;
    }

    public MultiImageView(Context context)
    {
        super(context);
    }

    public MultiImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setList(List<String> lists, boolean showBigImg) throws IllegalArgumentException
    {
        if (lists == null)
        {
            throw new IllegalArgumentException("imageList is null...");
        }
        imagesList = lists;
        showBigImage=showBigImg;
        if (MAX_WIDTH > 0&&showBigImage)
        {
            pxMoreWandH = (MAX_WIDTH - (int) BaseApplication.getInstance().getResources().getDimension(R.dimen.x48) - pxImagePadding * 2) / 3; //解决右侧图片和内容对不齐问题
            pxOneMaxWandH = MAX_WIDTH * 3 / 5;
            initImageLayoutParams();
        }else {
            pxMoreWandH = (MAX_WIDTH - (int) BaseApplication.getInstance().getResources().getDimension(R.dimen.x216) - pxImagePadding * 2) / 3; //解决右侧图片和内容对不齐问题
            pxOneMaxWandH = (MAX_WIDTH - (int) BaseApplication.getInstance().getResources().getDimension(R.dimen.x216) - pxImagePadding * 2) / 3; //解决右侧图片和内容对不齐问题
            initImageLayoutParams();
        }

        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (MAX_WIDTH == 0)
        {
            int width = measureWidth(widthMeasureSpec);
            if (width > 0)
            {
                MAX_WIDTH = width;
                if (imagesList != null && imagesList.size() > 0)
                {
                    setList(imagesList,showBigImage);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY)
        {
            // We were told how big to be
            result = specSize;
        }
        else
        {
            // Measure the text
            // result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
            // + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST)
            {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private void initImageLayoutParams()
    {
        int wrap = LayoutParams.WRAP_CONTENT;
        int match = LayoutParams.MATCH_PARENT;
        //pxOneMaxWandH
        if(showBigImage){
            onePicPara = new LayoutParams(match, pxOneMaxWandH);
        }else {
            onePicPara = new LayoutParams(pxOneMaxWandH, pxOneMaxWandH);
        }
      

        moreParaColumnFirst = new LayoutParams(pxMoreWandH, pxMoreWandH);
        morePara = new LayoutParams(pxMoreWandH, pxMoreWandH);
        morePara.setMargins(pxImagePadding, 0, 0, 0);

        rowPara = new LayoutParams(match, wrap);
    }

    // 根据imageView的数量初始化不同的View布局,还要为每一个View作点击效果
    private void initView()
    {
        this.setOrientation(VERTICAL);
        this.removeAllViews();
        if (MAX_WIDTH == 0)
        {
            //为了触发onMeasure()来测量MultiImageView的最大宽度，MultiImageView的宽设置为match_parent
            addView(new View(getContext()));
            return;
        }

        if (imagesList == null || imagesList.size() == 0)
        {
            return;
        }

        if (imagesList.size() == 1)
        {
            addView(createImageView(0, false));
        }
        else
        {
            int allCount = imagesList.size();
            if (allCount == 4)
            {
                MAX_PER_ROW_COUNT = 2;
            }
            else
            {
                MAX_PER_ROW_COUNT = 3;
            }
            int rowCount = allCount / MAX_PER_ROW_COUNT + (allCount % MAX_PER_ROW_COUNT > 0 ? 1 : 0);// 行数
            for (int rowCursor = 0; rowCursor < rowCount; rowCursor++)
            {
                LinearLayout rowLayout = new LinearLayout(getContext());
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);

                rowLayout.setLayoutParams(rowPara);
                if (rowCursor != 0)
                {
                    rowLayout.setPadding(0, pxImagePadding, 0, 0);
                }

                int columnCount = allCount % MAX_PER_ROW_COUNT == 0 ? MAX_PER_ROW_COUNT : allCount % MAX_PER_ROW_COUNT;//每行的列数
                if (rowCursor != rowCount - 1)
                {
                    columnCount = MAX_PER_ROW_COUNT;
                }
                addView(rowLayout);

                int rowOffset = rowCursor * MAX_PER_ROW_COUNT;// 行偏移
                for (int columnCursor = 0; columnCursor < columnCount; columnCursor++)
                {
                    int position = columnCursor + rowOffset;
                    rowLayout.addView(createImageView(position, true));
                }
            }
        }
    }

    private ImageView createImageView(int position, final boolean isMultiImage)
    {
        String url = imagesList.get(position);
//        ImageView imageView = new ColorFilterImageView(getContext());
        ImageView imageView = new ImageView(getContext());
        if (isMultiImage)
        {
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageView.setLayoutParams(position % MAX_PER_ROW_COUNT == 0 ? moreParaColumnFirst : morePara);
        }
        else
        {
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageView.setMaxHeight(pxOneMaxWandH);
            imageView.setLayoutParams(onePicPara);
        }
        imageView.setTag(R.string.img_position, position);
        imageView.setId(url.hashCode());
        GlideApp.with(getContext()).load(url).preload();
        GlideApp.with(getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.img_pic_placeholder).error(R.drawable
                .img_pic_placeholder).apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(12))).listener(new RequestListener<Drawable>()
        {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
            {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
            {
                imageView.setOnClickListener(mImageViewOnClickListener);
                return false;
            }
        }).into(imageView);
        return imageView;
    }
    
    // 图片点击事件
    private OnClickListener mImageViewOnClickListener = new OnClickListener()
    {

        @Override
        public void onClick(View view)
        {
            if (mOnItemClickListener != null)
            {
                mOnItemClickListener.onItemClick(view, (Integer) view.getTag(R.string.img_position));
            }
        }
    };

    public interface OnItemClickListener
    {
        public void onItemClick(View view, int position);
    }
}