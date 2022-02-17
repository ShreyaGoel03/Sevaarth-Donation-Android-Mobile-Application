package com.example.sevaarth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context)
    {
        this.context=context;
    }

    public int[] slideImages={
            R.drawable.about_head,
            R.drawable.func_head,
            R.drawable.stake_head
    };

    public int[] slideDescriptionImages = {
            R.drawable.about_des,
            R.drawable.func_des,
            R.drawable.stake_des
    };

    @Override
    public int getCount() {
        return slideImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }


    //used to add those slide effects
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.imageAboutView);
        ImageView slideDesImageView = (ImageView) view.findViewById(R.id.desAboutView);

        slideImageView.setImageResource(slideImages[position]);
        slideDesImageView.setImageResource(slideDescriptionImages[position]);

        container.addView(view);

        return view;
    }

    // to remove the view as soon as it gets done
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
         container.removeView((RelativeLayout)object);
    }
}