package com.ecom.ecommerceapp.adapter;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ecom.ecommerceapp.R;
import com.ecom.ecommerceapp.model.BannerList;

import java.util.List;



public class MyPagerAdapter extends PagerAdapter {


    Context mContext;
    LayoutInflater mLayoutInflater;
    List<BannerList> list;
    ImageView imageView;

    public MyPagerAdapter(Context mainActivity, List<BannerList> list) {

        mContext = mainActivity;
        this.list = list;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ConstraintLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BannerList l = list.get(position);
        View itemView = mLayoutInflater.inflate(R.layout.banner_view_layout, container, false);

        imageView = itemView.findViewById(R.id.image_banner);
        Glide.with(mContext).load(l.getImgUrl()).apply(options).into(imageView);
        container.addView(itemView);
        return itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
    RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.sliderdemo)
            .error(R.drawable.sliderdemo)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);

}
