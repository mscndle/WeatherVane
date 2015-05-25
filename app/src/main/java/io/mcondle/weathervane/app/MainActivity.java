package io.mcondle.weathervane.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }

    @Override
    public void onPause() {
        Log.i(LOG_TAG, "onPause called");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.i(LOG_TAG, "onResume called");
        super.onResume();
    }

    @Override
    public void onStop() {
        Log.i(LOG_TAG, "onStop called");
        super.onStop();
    }

    @Override
    public void onStart() {
        Log.i(LOG_TAG, "onStart called");
        super.onStart();
    }

    @Override
    public void onRestart() {
        Log.i(LOG_TAG, "onRestart called");
        super.onRestart();
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "onDestroy called");
        super.onDestroy();
    }


    // Removed the code with the menu item from MainActivity
    // Adding all menu code to fragment instead

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Log.i(LOG_TAG, "clicked settings (this line in MainActivity)");
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
