package com.example.delivery.extra;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 *  AppCompat Image View with Image Loading Ability
 *
 */
public class GTImageView extends AppCompatImageView {

    private Context context;


    public GTImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * Load image with url
     * @param url : Image url
     */
    public void loadImageFromUrl(String url) {

        Glide.with(context)
                .load(url)
                .into(this);
    }

    /**
     * Load image from url with setting up place holder
     * @param placeHolder placeholder image id
     * @param url         image url
     */
    public void loadImageFromUrl(int placeHolder, String url) {

        RequestOptions requestOptions = new RequestOptions().placeholder(placeHolder);

        if (placeHolder != 0){
            Glide.with(context)
                    .load(url)
                    .apply(requestOptions)
                    .into(this);

        }else {
            loadImageFromUrl(url);
        }
    }


    /**
     * Load image from url with setting up place holder
     * @param url             image url
     * @param requestOptions  Glide request options
     */
    public void loadImageFromUrl(String url, RequestOptions requestOptions) {

        if (null != requestOptions){
            Glide.with(context)
                    .load(url)
                    .apply(requestOptions)
                    .into(this);

        }else {
            loadImageFromUrl(url);
        }

    }

}
