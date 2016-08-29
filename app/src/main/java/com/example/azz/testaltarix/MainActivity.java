package com.example.azz.testaltarix;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
