package com.example.project;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class CustomProgressDialog extends Dialog {
    public CustomProgressDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_landing);

        ImageView earth, stars;

        earth = findViewById(R.id.earth);
        stars = findViewById(R.id.stars);

        Glide.with(context).load(R.drawable.earth).into(earth);
        Glide.with(context).load(R.drawable.stars).into(stars);
    }
}
