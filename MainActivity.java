package com.example.weatherforecast_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.*;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView cityName;
    Button search;
    TextView show;
    String url;

    class getWeather extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls){
            StringBuilder result=new StringBuilder();
            try{
                URL url=new URL(urls[0]);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream=urlConnection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));

                String line=" ";
                while((line=reader.readLine())!=null){
                    result.append(line).append("\n");
                }
                return result.toString();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try{
                JSONObject jsonObject=new JSONObject(result);
                String weatherInfo=jsonObject.getString("main");
                weatherInfo = weatherInfo.replace("temp","temperature");
                show.setText(weatherInfo);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = findViewById(R.id.cityName);
        search = findViewById(R.id.search);
        show = findViewById(R.id.weather);
        final String[] temp ={" "};

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
                String city = cityName.getText().toString().trim();

                try {
                    if (city != null && !city.isEmpty()) {
                        url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=033f19189a8ab629d8ac47ab098e86ef";
                    } else {
                        Toast.makeText(MainActivity.this, "Please Enter City Name", Toast.LENGTH_SHORT).show();
                        return; // Exit the method if the city name is not provided
                    }

                    getWeather task = new getWeather();
                    temp[0] = task.execute(url).get();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (temp[0] == null) {
                    show.setText("Cannot find weather data.");
                }
            }
        });

    }
}