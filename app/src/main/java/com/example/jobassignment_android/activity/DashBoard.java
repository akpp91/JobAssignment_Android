package com.example.jobassignment_android.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobassignment_android.R;
import com.example.jobassignment_android.adapter.LibraryItemAdapter;
import com.example.jobassignment_android.entity.LibraryItem;
import com.example.jobassignment_android.utils.RetrofitClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DashBoard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LibraryItemAdapter adapter;
    private List<LibraryItem> libraryItemList;
    private DownloadManager downloadManager;
    private long downloadId;
    private static final int REQUEST_CODE_PICK_DIRECTORY = 123;
    private DownloadManager.Request pendingDownloadRequest;
    private Uri pendingDownloadContentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        recyclerView = findViewById(R.id.recyclerViewDashboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        libraryItemList = new ArrayList<>();
        adapter = new LibraryItemAdapter(this, libraryItemList);
        recyclerView.setAdapter(adapter);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        Button downloadButton = findViewById(R.id.downloadButton);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload();
            }


        });
        createNotificationChannel();

        registerReceiver(downloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        fetchLibraryData();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("download_channel", "Download Manager", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Downloads in progress");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void fetchLibraryData() {
        RetrofitClient.getInstance().getApi().getData().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    parseAndAddToAdapter(response.body());
                } else {

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });
    }

    private void parseAndAddToAdapter(JsonArray jsonArray) {
        libraryItemList.clear();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            int id = jsonObject.get("ID").getAsInt();
            String name = jsonObject.get("Name").getAsString();
            String registerNameEng = jsonObject.get("RegisterNameENG").getAsString();
            int count = jsonObject.get("cnt").getAsInt();
            LibraryItem libraryItem = new LibraryItem(id, name, registerNameEng, count);
            libraryItemList.add(libraryItem);
        }

        adapter.notifyDataSetChanged();
    }
    private void startDownload() {
        String fileUrl = "https://www.elibrarycloud.com/docs/marathi/brochure.pdf";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "yourfile.pdf");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        } else {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        }
        downloadId = downloadManager.enqueue(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_DIRECTORY && resultCode == Activity.RESULT_OK && data != null) {
            Uri treeUri = data.getData();
            pendingDownloadRequest.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "yourfile.pdf");
            downloadId = downloadManager.enqueue(pendingDownloadRequest);
            Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show();
        }
    }


    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (id == downloadId) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Cursor cursor = downloadManager.query(query);
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                    String downloadedFilePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadCompleteReceiver);
    }
}
