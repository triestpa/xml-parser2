package com.example.xmlparser2;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.xmlparser2.StackOverflowXmlParser.Entry;

//Note: Much of the code for this program was copied directly from 
//http://developer.android.com/training/basics/network-ops/xml.html
public class NetworkActivity extends Activity {

	public static final String WIFI = "Wi-Fi";
	public static final String ANY = "Any";

	// Whether the display should be refreshed.
	public static boolean refreshDisplay = true;
	public static String sPref = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_network);

		Intent intent = getIntent();
		String link = intent.getStringExtra(MainActivity.EXTRA_FEED);

		new DownloadXmlTask().execute(link);

	}

	// Implementation of AsyncTask used to download XML feed
	private class DownloadXmlTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			try {
				return loadXmlFromNetwork(urls[0]);
			} catch (IOException e) {
				e.printStackTrace();
				return "IO error!";
			} catch (XmlPullParserException e) {
				e.printStackTrace();
				return "Xml error!";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			setContentView(R.layout.activity_network);
			// Displays the HTML string in the UI via a WebView
			WebView myWebView = (WebView) findViewById(R.id.webview);
			myWebView.loadData(result, "text/html", null);
		}
	}

	// Uploads XML from stackoverflow.com, parses it, and combines it with
	// HTML markup. Returns HTML string.
	@SuppressLint("SimpleDateFormat")
	private String loadXmlFromNetwork(String urlString)
			throws XmlPullParserException, IOException {
		InputStream stream = null;
		// Instantiate the parser
		StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();
		List<Entry> entries = null;

		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");

		/*
		 * If I were to spend more time on the app I could add a settings menu
		 * for toggling the summary 
 			SharedPreferences sharedPrefs =
		  	PreferenceManager .getDefaultSharedPreferences(this); boolean pref =
		  	sharedPrefs.getBoolean("summaryPref", false);
		 */

		StringBuilder htmlString = new StringBuilder();
		htmlString.append("<h3>"
				+ getResources().getString(R.string.page_title) + "</h3>");
		htmlString.append("<em>" + getResources().getString(R.string.updated)
				+ " " + formatter.format(rightNow.getTime()) + "</em>");

		try {
			stream = downloadUrl(urlString);
			entries = stackOverflowXmlParser.parse(stream);
			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} finally {
			if (stream != null) {
				stream.close();
			}
		}

		// Store retrieve each entry and format it as an html string
		for (Entry entry : entries) {
			htmlString.append("<p><a href='");
			htmlString.append(entry.link);
			htmlString.append("'>" + entry.title + "</a></p>");
			// uncomment if a summary setting is added
			// if (pref) {
			htmlString.append(entry.summary);
			// }
		}
		return htmlString.toString();
	}

	// Given a string representation of a URL, sets up a connection and gets
	// an input stream.
	private InputStream downloadUrl(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		// Starts the query
		conn.connect();
		return conn.getInputStream();
	}

}
