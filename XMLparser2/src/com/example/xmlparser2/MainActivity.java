package com.example.xmlparser2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//Note: Much of the code for this program was copied directly from 
// http://developer.android.com/training/basics/network-ops/xml.html
public class MainActivity extends Activity {
    public final static String EXTRA_FEED = "com.example.XMLparser.FEED";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void readCNN (View view) {
		String xmlLink = "http://rss.cnn.com/rss/cnn_topstories.xml";

	  	Intent intent = new Intent (this, NetworkActivity.class);
    	intent.putExtra(EXTRA_FEED, xmlLink);

    	startActivity(intent);
    	
	}
	
	public void readNYT (View view) {
		String xmlLink = "http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml";

	  	Intent intent = new Intent (this, NetworkActivity.class);
    	intent.putExtra(EXTRA_FEED, xmlLink);

    	startActivity(intent);
    	
	}
	
	public void readMEMO (View view) {
		String xmlLink = "http://schedule25wb.grinnell.edu/rssfeeds/memo.xml";

	  	Intent intent = new Intent (this, NetworkActivity.class);
    	intent.putExtra(EXTRA_FEED, xmlLink);

    	startActivity(intent);
    	
	}
}
	
