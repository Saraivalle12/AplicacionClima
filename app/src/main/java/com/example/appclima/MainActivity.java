package com.example.appclima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    ImageView icon;
    TextView temperature, summary, time, locationText, coordenadas;
    ProgressBar progressBar;
    Toolbar toolbar;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private static final int ACCESS_FINE_LOCATION_PERMISSION = 430;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        mResultReceiver = new AddressResultReceiver(null);
        icon = findViewById(R.id.icon);
        temperature = findViewById(R.id.temperature);
        summary = findViewById(R.id.summary);
        progressBar = findViewById(R.id.progress);
        time = findViewById(R.id.time);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        locationText = findViewById(R.id.location);
        coordenadas = findViewById(R.id.coordenadas);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add:
                        Dialog dialog;
                        dialog = new Dialog(MainActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); /*Transparencia*/
                        dialog.setContentView(R.layout.custom_layout);
                        final EditText elatitud = (EditText) dialog.findViewById(R.id.latitud);
                        final EditText elongitud = (EditText) dialog.findViewById(R.id.longitud);
                        Button mLogin = (Button) dialog.findViewById(R.id.btnLogin);
                        mLogin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                locationText.setVisibility(View.GONE);
                                new FetchWeather(Double.parseDouble(elatitud.getText().toString()), Double.parseDouble(elongitud.getText().toString())).execute();
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        break;

                    case R.id.refresh:
                        checkLocationPermission();
                        break;
                }
                return false;
            }
        });
        checkLocationPermission();
    }








    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showPermissionExplanationDialog();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void checkLocationPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showPermissionExplanationDialog();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION_PERMISSION);
            }
        } else {
            getLocation();
        }
    }

    private void showPermissionExplanationDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("WeatherApp requires permission to access your location")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkLocationPermission();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.finishAffinity(MainActivity.this);
                    }
                })
                .show();
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        progressBar.setVisibility(View.VISIBLE);
        locationText.setVisibility(View.VISIBLE);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        progressBar.setVisibility(View.GONE);
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            mLastLocation = location;
                            // Start service and update UI to reflect new location
                            startIntentService();
                            locationText.setText(String.format(Locale.getDefault(), "%s, %s", location.getLatitude(), location.getLongitude()));
                            new FetchWeather(location.getLatitude(), location.getLongitude()).execute();
                        } else {
                            locationText.setText("Failed to get location.");
                        }
                    }
                });
    }

    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    class FetchWeather extends AsyncTask<Void, Void, String> {
        private static final String WEATHER_URL = "https://api.darksky.net/forecast/6b908d36e73b53299adcd9957dd194a9/%s,%s?units=si&exclude=minutely,hourly,daily,alerts,flags";
        private double latitude;
        private double longitude;

        FetchWeather(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(String.format(Locale.getDefault(), WEATHER_URL, latitude, longitude));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                Log.d("MainActivity", "Opening " + WEATHER_URL);

                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200) {
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        return stringBuilder.toString();
                    } finally {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            Log.d("MainActivity", "Response: \n" + response);
            progressBar.setVisibility(View.GONE);
            if (response != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject currently = jsonResponse.getJSONObject("currently");

                    switch (currently.getString("icon")) {
                        case "clear-day":
                            icon.setImageResource(R.drawable.ic_clear_day);
                            break;

                        case "clear-night":
                            icon.setImageResource(R.drawable.ic_clear_night);
                            break;

                        case "rain":
                            icon.setImageResource(R.drawable.ic_rain);
                            break;

                        case "snow":
                            icon.setImageResource(R.drawable.ic_snow);
                            break;

                        case "sleet":
                            icon.setImageResource(R.drawable.ic_sleet);
                            break;

                        case "wind":
                            icon.setImageResource(R.drawable.ic_wind);
                            break;

                        case "fog":
                            icon.setImageResource(R.drawable.ic_fog);
                            break;

                        case "cloudy":
                            icon.setImageResource(R.drawable.ic_cloudy);
                            break;

                        case "partly-cloudy-day":
                            icon.setImageResource(R.drawable.ic_cloudy_day);
                            break;

                        case "partly-cloudy-night":
                            icon.setImageResource(R.drawable.ic_cloudy_night);
                            break;

                        case "hail":
                            icon.setImageResource(R.drawable.ic_hail);
                            break;

                        case "thunderstorm":
                            icon.setImageResource(R.drawable.ic_thunderstorm);
                            break;

                        case "tornado":
                            icon.setImageResource(R.drawable.ic_tornado);
                            break;
                    }

                    temperature.setText(currently.getDouble("temperature") + " Â°C");
                    summary.setText(currently.getString("summary"));

                    long jsonTime = currently.getLong("time");
                    String stringTime = new SimpleDateFormat("EE, dd , MM, yyyy 'at' HH:mm", Locale.getDefault()).format(new Date(jsonTime * 1000));

                    time.setText(stringTime);
                    coordenadas.setText("lat: "+latitude+" , long: "+longitude);

                } catch (JSONException e) {
                    Log.e("MainActivity", e.getMessage(), e);
                }
            } else {
                temperature.setText("Error");
                summary.setText("Error");
                time.setText("Error");
            }
        }
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultData == null) {
                return;
            }
            // Display the address string
            // or an error message sent from the intent service.
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    locationText.setText(resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY));
                }
            });
        }
    }
  }
