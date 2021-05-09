package com.android.a59facedetectionusinggooglevision;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button btnProses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageview);
        btnProses = findViewById(R.id.btnProsess);

        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.scott);
        imageView.setImageBitmap(bitmap);

        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap1);
        canvas.drawBitmap(bitmap,0,0,null);

        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setMode(FaceDetector.FAST_MODE)
                        .build();

                if (!faceDetector.isOperational()){
                    Toast.makeText(MainActivity.this, "Face Detector could not be set up on your device", Toast.LENGTH_SHORT).show();
                    return;
                }

                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<Face> sparseArray = faceDetector.detect(frame);
                for (int i = 0 ; i<sparseArray.size();i++){
                    Face face = sparseArray.valueAt(i);
                    float x1 = face.getPosition().x;
                    float y1 = face.getPosition().y;
                    float x2 = x1+face.getWidth();
                    float y2 = y1+face.getHeight();
                    RectF rectF = new RectF(x1,y1,x2,y2);
                    canvas.drawRoundRect(rectF,2,2,paint);
                }
                imageView.setImageDrawable(new BitmapDrawable(getResources(),bitmap1));
            }
        });
    }
}