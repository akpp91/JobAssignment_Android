package com.example.jobassignment_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jobassignment_android.R;
import com.example.jobassignment_android.utils.RetrofitClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Activity extends AppCompatActivity {

    private EditText passwordEditText;
    private Button loginButton;
    private Spinner parameterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        passwordEditText = findViewById(R.id.parameterValueEditText);
        loginButton = findViewById(R.id.loginButton);
        parameterSpinner = findViewById(R.id.parameterSpinner);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String selectedParameter = parameterSpinner.getSelectedItem().toString();
                final String enteredValue = passwordEditText.getText().toString();

                RetrofitClient.getInstance().getApi().getUserDetail().enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            JsonArray jsonArray = response.body();

                            if (!jsonArray.isJsonNull() && jsonArray.size() > 0) {
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                                    if (jsonObject.has(selectedParameter)) {
                                        String parameterValue = jsonObject.get(selectedParameter).getAsString();

                                        if (enteredValue.equals(parameterValue)) {
                                            // Match found, start the DashBoard activity
                                            Intent intent = new Intent(Login_Activity.this, DashBoard.class);
                                            startActivity(intent);
                                            return; // Exit the loop
                                        }
                                    }
                                }

                                // No match found
                                Toast.makeText(Login_Activity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle the case where the JSON array is empty
                                Toast.makeText(Login_Activity.this, "Empty Response Array", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Login_Activity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Log.e("RetrofitError", "Error: " + t.getMessage());
                        Toast.makeText(Login_Activity.this, " - Something Went Wrong: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
