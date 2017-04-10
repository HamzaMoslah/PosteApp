package com.example.moslah_hamza.apprentissagefouille1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button b;
    String url = "http://192.168.43.173/";
    Spinner spinner;
    Map<Integer, String> services = new HashMap<Integer, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonArrayRequest jsonRequest = new JsonArrayRequest
                (url + "get_services.php", new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(0);
                                services.put(jsonObject.getInt("id"), jsonObject.getString("label"));
                            }
                            Log.d("service ", services.get(1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(MainActivity.this).add(jsonRequest);  // adding the request to the Volley request queue

//        if (services.size() == 0) {
//            services.put(1, "Post-finance");
//            services.put(2, "Postassurance");
//            services.put(3, "Courrier");
//            services.put(4, "Rapid-poste");
//            services.put(5, "Poste-colis");
//        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.service_arrays, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        b = (Button) findViewById(R.id.tickbut);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonArrayRequest jsonRequest1 = new JsonArrayRequest
                        (url + "add_ticket.php", new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                // the response is already constructed as a JSONObject!
                                try {
                                    JSONObject jsonObject = response.getJSONObject(0);
                                    int success = jsonObject.getInt("success");
                                    String message = jsonObject.getString("message");

                                    if (success == 0) {
                                        Toast.makeText(getApplicationContext(), "Désolé, vous devez essayez plus tard", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Le numéro de votre ticket est : " + message, Toast.LENGTH_LONG).show();
                                        // Log.d("message : ",message);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });

                Volley.newRequestQueue(MainActivity.this).add(jsonRequest1);  // adding the request to the Volley request queue
            }
        });
    }
}