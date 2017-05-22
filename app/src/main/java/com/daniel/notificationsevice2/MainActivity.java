package com.daniel.notificationsevice2;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    Intent myService;
    public static boolean killService = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myService = new Intent(this, NotificationListener.class);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();

        if( id == R.id.action_settings )
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void stopMyService(View view)
    {
        if( myService == null )
            return ;

        logger( "stopMyService");

        killService = true;

        Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT ).show();

    }

    public void startMyService(View view)
    {
        if( myService == null )
            return ;

        logger("in startMyService");

        killService = false;

        startService( myService );

        logger("end startMyService");

        Toast.makeText(this, "Servicing started", Toast.LENGTH_SHORT ).show();

    }

    public void logger(String log)
    {
        Log.i("NotificationService" , log);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
