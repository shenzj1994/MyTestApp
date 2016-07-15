package com.zhongjieshen.mytestapp;

import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;



public class MainActivity extends AppCompatActivity {
    TextView uvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uvText=(TextView)findViewById(R.id.uvIndex);


        loadAd();
        new retrieveUVIndex().execute();
    }

    public void getUVIndex(View view){
        new retrieveUVIndex().execute();
    }

    protected void loadAd() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3485573207705717~1922889185");

        AdView mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
//                .addTestDevice("26876D708590843C")  // An example device ID
                .build();

        mAdView.loadAd(adRequest);
    }

    class retrieveUVIndex extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = null;
                url = new URL("http://api.owm.io/air/1.0/uvi/current?lat=43&lon=-80");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(String response) {
            try {
                JSONObject object=(JSONObject) new JSONTokener(response).nextValue();
                double uvindex=object.getDouble("value");

                uvText.setText(Double.toString(uvindex));
                Toast.makeText(getApplicationContext(),"UV Index Updated",Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}


