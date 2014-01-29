package com.emilsjolander.components.StickyScrollViewItems.samples;

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
	     
         /**
          * Below shows setting the scroll view shadow properties programatically.
          */
         // StickyScrollView scrollView = (StickyScrollView)
         // findViewById(R.id.ScrollView);
         // scrollView.setShadowDrawable(getResources().getDrawable(
         // R.drawable.sticky_shadow_default));
         // scrollView.setShadowHeight(height);
    }
}
