package io.mcondle.weathervane.app;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    private final String LOG_TAG = DetailFragment.class.getSimpleName();
//    private final String APP_HASHTAG = " #" + getActivity().getApplicationContext().getApplicationInfo().packageName;
    private String mForecastString;

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


        MenuItem menuItem = menu.findItem(R.id.action_share);   //retrieve the share menu item
        //get the action provider
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        //add an intent to the ShareActionProvider
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareActionIntent());
        } else {
            Log.d(LOG_TAG, "Share action provider is null");
        }
    }

    private Intent createShareActionIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");

        if (mForecastString == null) {
            Toast warningToast = new Toast(getActivity());
            warningToast.setText("mForecastString is null");
            warningToast.show();
        } else {
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    mForecastString + " #WeatherVane");
        }

        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Log.i(LOG_TAG, "clicked Settings");

            Intent settingsIntent = new Intent(getActivity().getApplicationContext(), SettingsActivity.class);
            startActivity(settingsIntent);
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

        if (extras != null) {

            //populate fields on page
            dayTextView.setText(extras.getCharSequence(ForecastFragment.KEY_DAY));
            dateTextView.setText(extras.getCharSequence(ForecastFragment.KEY_DATE));
            maxTempTextView.setText(extras.getCharSequence(ForecastFragment.KEY_MAX));
            minTempTextView.setText(extras.getCharSequence(ForecastFragment.KEY_MIN));
            weatherTextView.setText(extras.getCharSequence(ForecastFragment.KEY_WEATHER));
            humidityTextView.setText(extras.getCharSequence(ForecastFragment.KEY_HUMIDITY));
            pressureTextView.setText(extras.getCharSequence(ForecastFragment.KEY_PRESSURE));
            windTextView.setText(extras.getCharSequence(ForecastFragment.KEY_WIND));

            // TODO: check if Weather enum is required or not?
            String weatherImg = extras.getString(ForecastFragment.KEY_WEATHER);

            mForecastString = extras.getCharSequence(ForecastFragment.KEY_DAY).toString() + " "
                    + extras.getCharSequence(ForecastFragment.KEY_DATE).toString() + " "
                    + extras.getCharSequence(ForecastFragment.KEY_MAX).toString() + "/"
                    + extras.getCharSequence(ForecastFragment.KEY_MIN).toString();

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
        }

        return rootView;

    }
}
