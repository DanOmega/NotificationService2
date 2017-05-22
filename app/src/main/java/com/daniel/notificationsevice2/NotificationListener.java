package com.daniel.notificationsevice2;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import static com.daniel.notificationsevice2.MainActivity.killService;

/**
 * Created by Daniel on 5/13/2017.
 */

public class NotificationListener extends NotificationListenerService implements TextToSpeech.OnInitListener
{

    private TextToSpeech engine;
    private boolean isEngineInitialized;
    private PackageManager pm;
    private AudioManager audioManager;
    private long previousPostDate;




    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        pm = getApplicationContext().getPackageManager();
        engine = new TextToSpeech(this, this);

        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        logger( "Service being Created");

        pm = getApplicationContext().getPackageManager();
        engine = new TextToSpeech(this, this);

        logger( "Service Created");
    }

    @Override
    public void onDestroy()
    {
        engine.shutdown();
        logger("onDestroy");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn)
    {
        logger("Notification Posted");
        tellNotification(sbn);
    }

    public void tellNotification(StatusBarNotification n)
    {
        if( ! isEngineInitialized ) return;

        if( killService )
        {
            //onDestroy();
            return;
        }

        if ( isDuplicateNotification( n ) )
        {
           return;
        }

        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        if(audioManager == null ) return;

        String appName = "UNKNOWN";

        try
        {
            appName = pm.getApplicationLabel(pm.getApplicationInfo(n.getPackageName(), PackageManager.GET_META_DATA)).toString();
        }
        catch( PackageManager.NameNotFoundException e)
        {
            logger( e.getMessage() );
        }

        if( ! okayToSpeak(appName))
        {
            return;
        }

        Bundle params = new Bundle();
        int currentVolume =  audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        int max = audioManager.getStreamMaxVolume( AudioManager.STREAM_NOTIFICATION );
        Float volume = new Float( (float) currentVolume / (float) max);
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, volume );

        engine.speak(appName, TextToSpeech.QUEUE_ADD, params, null);

    }

    private boolean okayToSpeak(String app)
    {
        logger(app);

        switch(app)
        {
            case "Amazon Music":
            case "Tinder":
            case "Android System":
            case "System UI":
            case "Google App":
            case "Maps":
            case "Sleep":
            case "Clock":
            case "Phone":
                return false;
        }

        if( app.contains("Cupid")) return false;

        if( audioManager != null)
        {
            switch ( audioManager.getRingerMode() )
            {
                case AudioManager.RINGER_MODE_SILENT:
                case AudioManager.RINGER_MODE_VIBRATE:
                    return false;
            }
        }
        return true;
    }

    private void logger(String log)
    {
        Log.i("NotificationService" , log);
    }

    private boolean isDuplicateNotification( StatusBarNotification n )
    {

        logger( n.getNotification().when + " WHEN");

        if( n.getNotification().when  == 0 )
        {
            return true;
        }

        if ( previousPostDate == n.getNotification().when )
        {
            return true;
        }

        previousPostDate = n.getNotification().when;


        return false;
    }

    @Override
    public void onInit(int i)
    {

        if( i == TextToSpeech.SUCCESS )
        {
            isEngineInitialized = true;
        }
        else
        {
            isEngineInitialized = false;
        }

    }

}




