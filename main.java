package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class main extends AppCompatActivity {

    private static String TAG = "PHP Result";

    private ArrayList<cardlist> mArrayList;
    private CardAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private String mJsonString;

    Context mContext;

    CustomProgressDialog progressDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mRecyclerView = findViewById(R.id.recylerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mArrayList = new ArrayList<>();

        mAdapter = new CardAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        GetData task = new GetData();
            task.execute("https://exampleproject.com/getcard.php");

    }

    private class GetData extends AsyncTask<String, Void, String> {

        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDlg = new CustomProgressDialog(main.this);
            progressDlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            progressDlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            progressDlg.show();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                mJsonString = result;
                showResult();
            } else {
                Intent intent = new Intent(main.this, ConnectionError.class);
                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                //outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){
        try {
            JSONArray jsonarray = new JSONArray(mJsonString);
            for(int i=0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                String productName = jsonobject.getString("productName");
                String productSupplier = jsonobject.getString("productSupplier");
                String productStock = jsonobject.getString("productStock");

                cardlist cardlist = new cardlist();

                cardlist.setProductName(productName);
                cardlist.setProductSupplier(productSupplier);
                cardlist.setProductStock(Float.parseFloat(productStock));

                mArrayList.add(cardlist);
                mAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

}
