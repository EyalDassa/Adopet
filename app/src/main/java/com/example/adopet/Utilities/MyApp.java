package com.example.adopet.Utilities;

import android.app.Application;

import com.example.adopet.Models.User;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DbManager.init();
        ImageLoader.init(this);
        User.init();
    }
}
