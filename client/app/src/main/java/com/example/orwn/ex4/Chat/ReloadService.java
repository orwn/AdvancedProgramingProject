package com.example.orwn.ex4.Chat;

import android.app.IntentService;
import android.content.Intent;


public class ReloadService extends IntentService
{
    // Setting Done service
    public static final String DONE = "com.biu.ap2.BAZINGA.Services.ReloadService.DONE";

    public ReloadService() {
        super(ReloadService.class.getName());
    }

    public ReloadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            // Refreshing time for 5 seconds
            Thread.sleep(5000,0);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Set the broad coast
        Intent i = new Intent(DONE);

        // Broadcast to the reset of the app
        this.sendBroadcast(i);
    }
}
