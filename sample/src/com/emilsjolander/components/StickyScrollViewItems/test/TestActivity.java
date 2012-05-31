package com.emilsjolander.components.StickyScrollViewItems.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.emilsjolander.components.StickyScrollViewItems.R;

/**
 * 
 * @author Emil Sj�lander - sjolander.emil@gmail.com
 *
 */
public class TestActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.mybutton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "hej", Toast.LENGTH_SHORT).show();
			}
		});
    }
}