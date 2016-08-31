package com.example.azamat.testaltarix;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private String names[] = {
            "HTML",
            "CSS",
            "Java Script",
            "Wordpress"
    };

    private String metras[] = {
            "The Powerful Hypter Text Markup Language 5",
            "Cascading Style Sheets",
            "Code with Java Script",
            "Manage your content with Wordpress"
    };


    private Integer imageid[] = {
            R.drawable.icon,
            R.drawable.icon,
            R.drawable.icon,
            R.drawable.icon
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomList customList = new CustomList(this, names, metras, imageid);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(customList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),"You Clicked "+names[i],Toast.LENGTH_SHORT).show();
                GPSLocation gps = new GPSLocation(MainActivity.this);
                if(gps.canGetLocation()){

                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // if (id == R.id.action_settings) {
        //     return true;
        //  }

        return super.onOptionsItemSelected(item);
    }

}
