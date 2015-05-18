package io.mcondle.weathervane.app;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    private final String LOG_TAG = DetailFragment.class.getSimpleName();


    public DetailFragment()  {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Log.i(LOG_TAG, "clicked Settings");

            Intent intent = new Intent(getActivity().getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //initialize all the text fields
        TextView dayTextView = (TextView) rootView.findViewById(R.id.dayTextView);
        TextView dateTextView = (TextView) rootView.findViewById(R.id.dateTextView);

        TextView maxTempTextView = (TextView) rootView.findViewById(R.id.maxTempTextView);
        TextView minTempTextView = (TextView) rootView.findViewById(R.id.minTempTextView);

        ImageView weatherImageView = (ImageView) rootView.findViewById(R.id.weatherImageView);
        TextView weatherTextView = (TextView) rootView.findViewById(R.id.weatherTextView);

        TextView humidityTextView = (TextView) rootView.findViewById(R.id.humidityTextView);
        TextView pressureTextView = (TextView) rootView.findViewById(R.id.pressureTextView);
        TextView windTextView = (TextView) rootView.findViewById(R.id.windTextView);

        Bundle extras = getActivity().getIntent().getExtras();

        //populate fields on page
        dayTextView.setText(extras.getCharSequence(ForecastFragment.KEY_DAY));
        dateTextView.setText(extras.getCharSequence(ForecastFragment.KEY_DATE));
        maxTempTextView.setText(extras.getCharSequence(ForecastFragment.KEY_MAX));
        minTempTextView.setText(extras.getCharSequence(ForecastFragment.KEY_MIN));
        weatherTextView.setText(extras.getCharSequence(ForecastFragment.KEY_WEATHER));
        humidityTextView.setText(extras.getCharSequence(ForecastFragment.KEY_HUMIDITY));
        pressureTextView.setText(extras.getCharSequence(ForecastFragment.KEY_PRESSURE));
        windTextView.setText(extras.getCharSequence(ForecastFragment.KEY_WIND));

//        TODO: check if Weather enum is required or not?
        String weatherImg = extras.getString(ForecastFragment.KEY_WEATHER);

        switch (weatherImg.toUpperCase()) {

            case "CLEAR":
                weatherImageView.setImageResource(R.drawable.art_clear);
                break;

            case "CLOUDS":
                weatherImageView.setImageResource(R.drawable.art_clouds);
                break;

            case "CLOUDY":
                weatherImageView.setImageResource(R.drawable.art_clouds);
                break;

            case "FOG":
                weatherImageView.setImageResource(R.drawable.art_fog);
                break;

            case "RAIN":
                weatherImageView.setImageResource(R.drawable.art_rain);
                break;

            case "SNOW":
                weatherImageView.setImageResource(R.drawable.art_snow);
                break;

            case "STORM":
                weatherImageView.setImageResource(R.drawable.art_storm);
                break;

            default:
                break;
        }


        return rootView;

    }
}
