package com.example.adopet.Utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.adopet.R;

public class ImageLoader {
    private static Context context;
    private static volatile ImageLoader instance;

    private ImageLoader(Context context) {
        this.context = context;
    }

    public static ImageLoader getInstance() {
        return instance;
    }

    public static ImageLoader init(Context context){
        if (instance == null){
            synchronized (ImageLoader.class){
                if (instance == null){
                    instance = new ImageLoader(context);
                }
            }
        }
        return getInstance();
    }

    public void load (String source, ImageView imageView){
        Glide
                .with(context)
                .load(source)
                .placeholder(R.drawable.unavailable_photo)
                .centerInside()
                .into(imageView);
    }
    public void loadPet (String source, ImageView imageView){
        Glide
                .with(context)
                .load(source)
                .thumbnail(Glide.with(context).load(R.drawable.tenor))
                .centerInside()
                .into(imageView);
    }
    public void load (int source, ImageView imageView){
        Glide
                .with(context)
                .load(source)
                .placeholder(R.drawable.unavailable_photo)
                .centerInside()
                .into(imageView);
    }
    public void loadFave (int source, ImageView imageView){
        Glide
                .with(context)
                .load(source)
                .placeholder(R.drawable.empty_heart)
                .centerInside()
                .into(imageView);
    }
    public void loadUser (String source, ImageView imageView){
        Glide
                .with(context)
                .load(source)
                .placeholder(R.drawable.user_img)
                .centerInside()
                .into(imageView);
    }

    public void load (Drawable source, ImageView imageView){
        Glide
                .with(context)
                .load(source)
                .placeholder(R.drawable.unavailable_photo)
                .centerInside()
                .into(imageView);
    }

}
