package com.komori.camera.colorpicker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ColorPickerActivity extends AppCompatActivity {

    private TextView txtColor;
    private ColorPickView myView;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        iv = (ImageView) findViewById(R.id.iv);
        myView = (ColorPickView) findViewById(R.id.color_picker_view);
        txtColor = (TextView) findViewById(R.id.txt_color);
//        Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("path"), new
//                BitmapFactory.Options());
//        iv.setImageBitmap(bitmap);
        myView.setOnColorChangedListener(new ColorPickView.OnColorChangedListener() {

            @Override
            public void onColorChange(int color) {
                txtColor.setTextColor(color);
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                txtColor.setText((r + "," + g + "," + b));
                Log.i("info", "color:" + color);
            }

        });
    }
}
