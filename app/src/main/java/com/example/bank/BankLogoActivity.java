package com.example.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BankLogoActivity extends AppCompatActivity {

    private static final String TAG = "BankLogoActivity";
    private EditText etImgName;
    private Button btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_logo);
        initViews();

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imgName = etImgName.getText().toString();

                switch (imgName) {
                    case "piggy":
                        downloadFile("https://image.pngaaa.com/402/62402-middle.png");
                        break;
                    case "money":
                        downloadFile("https://st2.depositphotos.com/5266903/8131/i/450/depositphotos_81311248-stock-photo-bank-icon.jpg");
                        break;
                    default:
                        downloadFile(imgName);
                        break;
                }

                etImgName.setText("");
            }
        });
    }

    public void downloadFile(String url) {
        DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);

    }

    private void initViews() {
        etImgName = findViewById(R.id.etImgName);
        btnDownload = findViewById(R.id.btnDownload);
    }
}