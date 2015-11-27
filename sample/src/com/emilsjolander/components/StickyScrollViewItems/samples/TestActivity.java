package com.emilsjolander.components.StickyScrollViewItems.samples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.emilsjolander.components.StickyScrollViewItems.StickyScrollView;

/**
 * @author Emil Sj�lander - sjolander.emil@gmail.com
 */
public class TestActivity extends Activity implements StickyScrollView.OnStickyScrollViewListener {

    private static final String TAG = "StickyScrollView";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.my_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hello World", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Below sets the listener on start sticking view and stop sticking current view
         */
        StickyScrollView scrollView = (StickyScrollView) findViewById(R.id.scroll_view);
        scrollView.setOnStickyScrollViewListener(this);

        /**
         * Below shows setting the scroll view shadow properties programmatically.
         */
        //scrollView.setShadowDrawable(getResources().getDrawable(R.drawable.sticky_shadow_default));
        //scrollView.setShadowHeight(height);
    }

    @Override
    public void startStickingView(View v) {
        Log.e(TAG, "Start sticking view");
    }

    @Override
    public void stopStickingCurrentView(View v) {
        Log.e(TAG, "Stop sticking current view");
    }
}
