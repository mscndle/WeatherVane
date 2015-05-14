package io.mcondle.weathervane.app;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import io.mcondle.weathervane.app.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mscndle
 * @Description
 */
public class ForecastFragment extends Fragment {

    private final String LOG_TAG = ForecastFragment.class.getSimpleName();

    private ArrayAdapter<String> mForecastAdapter;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
//            String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=10010&mode=json&units=metric&cnt=7";
            Log.i(LOG_TAG, "clicked Refresh");
            FetchWeatherTask weatherTask = new FetchWeatherTask();

            String postCode = "10010";
            String forecastJsonStr = null;
            String jsonResponse = weatherTask.execute(postCode).toString();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        List<String> forecastList = new ArrayList<>();
        forecastList.add("Today - Sunny - 88/63");
        forecastList.add("Tomorrow - Foggy- 70/46");
        forecastList.add("Weds - Cloudy - 72/63");
        forecastList.add("Thurs - Rainy - 64/51");
        forecastList.add("Fri - Foggy - 70/46");
        forecastList.add("Sat - Sunny - 76/68");

        mForecastAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                forecastList);

        ListView mforecastListView = (ListView) rootView.findViewById(
                R.id.listview_forecast);
        mforecastListView.setAdapter(mForecastAdapter);
        mforecastListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getActivity().getApplicationContext();
                CharSequence text = parent.getAdapter().getItem(position).toString();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        return rootView;
    }



    // creating progress type as Integer for now
    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        // TODO - Change the entire fetch String[] weather data logic
        // TODO - Fix the bug with incorrect max/min values

        private String getReadableDateString(long time) {
            //API returns a unix timestamp which must be converted to milliseconds to get a valid date
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(time);
        }

        private String formatHighLows(double high, double low) {
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);

            return roundedHigh + "/" + roundedLow;
        }

        private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";
            final String OWM_DESCRIPTION = "main";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.

            // Since this data is also sent in-order and the first day is always the
            // current day, we're going to take advantage of that to get a nice
            // normalized UTC date for all of our weather.

            Time dayTime = new Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new Time();

            String[] resultStrs = new String[numDays];
            for(int i = 0; i < weatherArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String day;
                String description;
                String highAndLow;

                // Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                long dateTime;
                // Cheating to convert this to UTC time, which is what we want anyhow
                dateTime = dayTime.setJulianDay(julianStartDay+i);
                day = getReadableDateString(dateTime);

                // description is in a child array called "weather", which is 1 element long.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);

                highAndLow = formatHighLows(high, low);
                resultStrs[i] = day + " - " + description + " - " + highAndLow;
            }

            for (String s : resultStrs) {
                Log.v(LOG_TAG, "Forecast entry: " + s);
            }
            return resultStrs;

        }

        @Override
        protected String[] doInBackground(String... params) {  //postcode is input param
            // calling api.openweather to get 7 day forecast
            HttpURLConnection urlConnection = null;
            BufferedReader br = null;

            // will contain the raw JSON response
            String forecastJsonString = null;
            int numDays = 7;

            try {
                Uri.Builder uriBuilder = new Uri.Builder();

                uriBuilder.scheme("http")
                        .authority("api.openweathermap.org")
                        .appendPath("data")
                        .appendPath("2.5")
                        .appendPath("forecast")
                        .appendPath("daily")
                        .appendQueryParameter("q", params[0])
                        .appendQueryParameter("mode", "json")
                        .appendQueryParameter("unit", "metrics")
                        .appendQueryParameter("cnt", "7");

                URL url = new URL(uriBuilder.build().toString());
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                br = new BufferedReader(new InputStreamReader(inputStream));

                if (inputStream == null) {
                    return null;
                }
                StringBuffer buffer = new StringBuffer();

                String line;
                while ((line = br.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                forecastJsonString = buffer.toString();

                Log.v(LOG_TAG, "ForecastWeather JSON: " + forecastJsonString);

            } catch (IOException ioe) {
                Log.e(LOG_TAG, "Error", ioe);
            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException ioe) {
                        Log.e(LOG_TAG, "Error closing stream", ioe);
                    }
                }
            }

            try {
                return getWeatherDataFromJson(forecastJsonString, numDays);
            } catch (JSONException jse) {
                Log.e(LOG_TAG, jse.getMessage(), jse);
            }

            return null;
        }

        @Override
        public void onPostExecute(String[] result) {
            if (result != null) {
                mForecastAdapter.clear();
                for (String weatherData : result) {
                    mForecastAdapter.add(weatherData);
                }
            }
        }
    }
}