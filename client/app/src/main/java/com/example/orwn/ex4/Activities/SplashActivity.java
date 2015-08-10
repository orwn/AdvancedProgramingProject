package com.example.orwn.ex4.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.orwn.ex4.MapsActivity;
import com.example.orwn.ex4.R;

import org.w3c.dom.Text;

/**
 * This class represents a splash activity
 *
 * @author Or Weinstein
 *
 */
public class SplashActivity extends ActionBarActivity {


    // Private paramters
    ProgressBar pb;
    int i;
    Intent intent;
    int counter;
    TextView txt;
    String[] names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Hiding the title bar
        getSupportActionBar().hide();


        setContentView(R.layout.activity_splash);


        // Gets references to the views in this activity
        txt = (TextView) findViewById(R.id.splash_text);
        names = this.getResources().getStringArray(R.array.splash_names);
        pb = (ProgressBar) findViewById(R.id.splash_progressBar);

        // Creating a thread for prograssing the prograss bar
        Thread tLoader = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    // Loading the prorgress bar for 5 seconds
                    while (i <= 100) {

                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                pb.setProgress(i);
                            }
                        });
                        // Sleeping
                        Thread.sleep(50);
                        i++;


                    }
                    // Sleeping additionally 3 seconds
                    Thread.sleep(3000);

                    // Starting a new activity
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            intent = new Intent(SplashActivity.this, MapsActivity.class);
                            startActivity(intent);
                        }
                    });

                } catch (Exception ex) {}
            }
        });

        // Creating a thread that changes the text label each second for 5 seconds
        Thread textSwiper = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {

                    for (counter = 0; counter < 5; counter++) {

                        // Setting the text in the main thread
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                txt.setText(names[counter]);
                            }
                        });
                        // Sleeping for 1 second
                        Thread.sleep(1000);

                    }
                }
                catch (Exception ex){}
            }
        });

        // Starting both of the side threads
        tLoader.start();
        textSwiper.start();


    }


}
