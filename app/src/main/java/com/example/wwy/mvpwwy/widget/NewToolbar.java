package com.example.wwy.mvpwwy.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.wwy.mvpwwy.R;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import java.util.concurrent.TimeUnit;


public class NewToolbar extends Toolbar {
    @BindView(R.id.view_back)
    View view_back;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.view_right)
    View view_right;
    @BindView(R.id.toolbar_right_button)
    ImageView toolbar_right_button;
    @BindView(R.id.toolbar_left_button)
    ImageView toolbar_left_button;
    private String title_text;
    private int right_img;

    public NewToolbar(Context context) {
        super(context);
        init(null);
    }

    public NewToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NewToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_my_toolbar, this, true);
        ButterKnife.bind(this, view);

        //很重要
        setContentInsetsRelative(0, 0);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.NewToolbar, 0, 0);
        title_text = a.getString(R.styleable.NewToolbar_titleText);
        right_img = a.getResourceId(R.styleable.NewToolbar_rightImg, 0);
        if (!TextUtils.isEmpty(title_text)) {
            toolbar_title.setText(title_text);
        }
        if (right_img != 0) {
            toolbar_right_button.setBackgroundResource(right_img);
        }
        a.recycle();
    }

    public void hideRightImage() {
        if (toolbar_right_button != null) {
            toolbar_right_button.setVisibility(View.GONE);
            view_right.setVisibility(GONE);
        }
    }

    public void hideLeftImage() {
        if (toolbar_left_button != null) {
            toolbar_left_button.setVisibility(View.GONE);
            view_back.setVisibility(GONE);
        }
    }

    public void setTitle(String titleStr) {
        if (toolbar_title != null) {
            toolbar_title.setText(titleStr);
        }
    }

    public void setShowTitle(boolean b) {
        if (toolbar_title != null) {
            toolbar_title.setVisibility(b ? VISIBLE : GONE);
        }
    }

    public void setTitleByResourceId(int rid) {
        if (toolbar_title != null) {
            toolbar_title.setText(rid);
        }
    }

    public void setLeftOnClickListener(OnClickListener onClickListener) {
        view_back.setOnClickListener(onClickListener);
    }

    public Observable<Object> leftClick() {
        return RxView.clicks(view_back).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Object> leftClick(int milliseconds) {
        return RxView.clicks(view_back).throttleFirst(milliseconds, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Object> rightClick() {
        return RxView.clicks(view_right).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Object> rightClick(int milliseconds) {
        return RxView.clicks(view_right).throttleFirst(milliseconds, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Object> setTitleOnClickListener() {
        return RxView.clicks(toolbar_title).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Object> setTitleOnClickListener(int milliseconds) {
        return RxView.clicks(toolbar_title).throttleFirst(milliseconds, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread());
    }
}
