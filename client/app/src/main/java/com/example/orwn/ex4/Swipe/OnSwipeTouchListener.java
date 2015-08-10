package com.example.orwn.ex4.Swipe;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/*/**
 * This class represents MenuFragment
 *
 * @author Or Weinstein Inspierd by Hemi the undiputed teacher
 */
public class OnSwipeTouchListener implements OnTouchListener
{
    // private gestureDetector
    private final GestureDetector gestureDetector; // Use to detect gestures

    /**
     *  Constructs OnSwipeTouchListener with a context
     * @param context the context whom the swipe detector detect swiping
     */
    public OnSwipeTouchListener(Context context) {
        gestureDetector = new GestureDetector(context,new GestureListener());
    }

    /**
     * Called onSwipeLeft created to overwrite
     */
    public void onSwipeLeft() {

    }
    /**
     * Called onSwipeRight created to overwrite
     */
    public void onSwipeRight(){}

    /**
     * Called onSwipeUp created to overwrite
     */
    public void onSwipeUp(){}

    /**
     * Called onSwipeDown created to overwrite
     */
    private void onSwipeDown() {
    }

    /**
     * Calculate if swipe event did occur and calls the right function
     * by calling onTouchEvent
     *
     * @param v the View which it occurred at
     * @param evet the data about the MotionEcent
     * @return true if motion did occur Otherwise false
     */
    public boolean onTouch(View v,MotionEvent evet)
    {
        Log.i("onSwipeTouchListener","click");
        return gestureDetector.onTouchEvent(evet);
    }

        /**
         *  Iner class that represent gestureListner
         */
        private final class GestureListener extends SimpleOnGestureListener {


            // The min distance that swipe has to make in order to be called swiping
            private static final int SWIPE_DISTANCE_THRESHOLD = 100;

            // The min velocity that swipe has to have in order to be called swipe
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;


            /**
             * Has to be override does nothing
             * @param e motionEcent
             * @return true
             */
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }


            /**
             *
             *
             *
             * @param e1 MotionEvent
             * @param e2 MotionEvent
             * @param velocityX the velocity of x
             * @param velocityY the velocity of y
             * @return true of swipe occurred otherwise false
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX(); // The distance in X axis
                float distanceY = e2.getY() - e1.getY(); // The distance in y axis

                // Check if swipe has been done and define it kind
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();

                    if (distanceY > 0)
                        onSwipeUp();
                    else
                        onSwipeDown();
                    return true;
                }
                else
                    Log.i("onSwipeTouchListener","REAL click");


                return false;
            }}



}
