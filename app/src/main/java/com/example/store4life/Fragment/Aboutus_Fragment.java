package com.example.store4life.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.store4life.R;

public class Aboutus_Fragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about,container,false);
        ImageView iv = (ImageView)view.findViewById(R.id.logo);
        int color = Color.parseColor("#FF1493");
        iv.setColorFilter(color);
        return view;
    }
}
