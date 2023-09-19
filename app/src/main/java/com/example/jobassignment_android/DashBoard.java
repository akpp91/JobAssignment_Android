package com.example.jobassignment_android;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.List;

public class DashBoard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LibraryItemAdapter adapter;
    private List<LibraryItem> libraryItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        recyclerView = findViewById(R.id.recyclerViewDashboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        libraryItemList = new ArrayList<>();
        adapter = new LibraryItemAdapter(this, libraryItemList);
        recyclerView.setAdapter(adapter);

        // Fetch JSON data from the API
        fetchLibraryData();
    }

    private void fetchLibraryData() {
        RetrofitClient.getInstance().getApi().getData().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Parse the JSON data and add it to the adapter
                    parseAndAddToAdapter(response.body());
                } else {
                    // Handle API call failure
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                // Handle API call failure
            }
        });
    }

    private void parseAndAddToAdapter(JsonArray jsonArray) {
        // Clear existing data
        libraryItemList.clear();

        // Iterate through JSON array and add items to the list
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            int id = jsonObject.get("ID").getAsInt();
            String name = jsonObject.get("Name").getAsString();
            String registerNameEng = jsonObject.get("RegisterNameENG").getAsString();
            int count = jsonObject.get("cnt").getAsInt();

            LibraryItem libraryItem = new LibraryItem(id, name, registerNameEng, count);
            libraryItemList.add(libraryItem);
        }

        // Notify adapter that data has changed
        adapter.notifyDataSetChanged();
    }
}
