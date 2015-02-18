package com.yjkim.dugout;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.yjkim.util.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by jehyeok on 2/18/15.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> imageNames;
    private LayoutInflater inflater;
    private ImageLoader mImageLoader = VolleySingleton.getInstance().getImageLoader();

    public ViewPagerAdapter(Context context, ArrayList<String> imageNames) {
        this.context = context;
        this.imageNames = imageNames;
    }


    @Override
    public int getCount() {
        return imageNames.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_viewpager, container, false);

        NetworkImageView image = (NetworkImageView) itemView.findViewById(R.id.image);
        String imageUrl = MyApplication.host + "/assets/" + imageNames.get(position);
        image.setImageUrl(imageUrl, mImageLoader);

        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }
}
