package com.example.qr_3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenerateQR extends AppCompatActivity {

    private ImageView qrImg;
    private EditText infoEdit;
    private Button generateBtn, saveBtn;
    private TextView text;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        ImageView qrImg = findViewById(R.id.idIVQrcode);
        EditText infoEdit = findViewById(R.id.info_edit);
        Button generateBtn = findViewById(R.id.generate_btn);
        Button saveBtn = findViewById(R.id.save_btn);
        TextView text = findViewById(R.id.textView);

        saveBtn.setEnabled(false);

        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(infoEdit.getText().toString())) {
                    Toast.makeText(GenerateQR.this, "Input your info.", Toast.LENGTH_LONG).show();
                } else {
                    text.setVisibility(TextView.GONE);
                    MultiFormatWriter writer = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = writer.encode(infoEdit.getText().toString(), BarcodeFormat.QR_CODE, qrImg.getWidth(), qrImg.getHeight());

                        BarcodeEncoder encoder = new BarcodeEncoder();
                        Bitmap bitmap = encoder.createBitmap(bitMatrix);

                        qrImg.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Toast.makeText(GenerateQR.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                saveBtn.setEnabled(true);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bitmap bitmap = ((BitmapDrawable) qrImg.getDrawable()).getBitmap();

                    File path = new File(getExternalFilesDir(null), "QR Generator" + File.separator + "Images");
                    // getApplicationContext().getFilesDir()

                    if(!path.exists()){
                        path.mkdirs();
                    }

                    File outFile = new File(path, infoEdit.getText().toString() + "QR.jpeg");
                    FileOutputStream outputStream = new FileOutputStream(outFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();
                    Toast.makeText(GenerateQR.this, "Generated QR code successfully saved", Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    Toast.makeText(GenerateQR.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(GenerateQR.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}