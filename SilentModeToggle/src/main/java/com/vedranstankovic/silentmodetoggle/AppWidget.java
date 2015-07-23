package com.vedranstankovic.silentmodetoggle;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.RemoteViews;

/**
 * Created by vedran on 17.10.13..
 */
public class AppWidget extends AppWidgetProvider{

    @Override
    public void onReceive(Context ctxt, Intent intent) {

        if (intent.getAction()==null) {
            ctxt.startService(new Intent(ctxt, ToggleService.class));
        } else {
            super.onReceive(ctxt, intent);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, CheckService.class));
    }
    public static class ToggleService extends IntentService {

        public ToggleService() {
            super(ToggleService.class.getName());
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            ComponentName me=new ComponentName(this, AppWidget.class);
            AppWidgetManager mgr=AppWidgetManager.getInstance(this);
            mgr.updateAppWidget(me, buildUpdate(this));
        }

        private RemoteViews buildUpdate(Context context) {
            RemoteViews updateViews=new RemoteViews(context.getPackageName(),R.layout.widget);

            AudioManager audioManager = (AudioManager)context.getSystemService(Activity.AUDIO_SERVICE);
            if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT)
            {
                updateViews.setImageViewResource(R.id.phoneState, R.drawable.phone_on);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            } else {
                updateViews.setImageViewResource(R.id.phoneState, R.drawable.phone_silent);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }

            Intent i=new Intent(this, AppWidget.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, i,0);
            updateViews.setOnClickPendingIntent(R.id.phoneState,pi);

            return updateViews;

        }
    }

    public static class CheckService extends IntentService {

        public CheckService() {
            super(CheckService.class.getName());
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            ComponentName me=new ComponentName(this, AppWidget.class);
            AppWidgetManager mgr=AppWidgetManager.getInstance(this);
            mgr.updateAppWidget(me, buildUpdate(this));
        }

        private RemoteViews buildUpdate(Context context) {
            RemoteViews updateViews=new RemoteViews(context.getPackageName(),R.layout.widget);

            AudioManager audioManager = (AudioManager)context.getSystemService(Activity.AUDIO_SERVICE);
            if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT)
            {
                updateViews.setImageViewResource(R.id.phoneState, R.drawable.phone_silent);
            } else {
                updateViews.setImageViewResource(R.id.phoneState, R.drawable.phone_on);
            }

            Intent i=new Intent(this, AppWidget.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, i,0);
            updateViews.setOnClickPendingIntent(R.id.phoneState,pi);

            return updateViews;

        }
    }

}