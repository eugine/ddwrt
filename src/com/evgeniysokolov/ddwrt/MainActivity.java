package com.evgeniysokolov.ddwrt;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	private String executionResult;
	
	// Need handler for callbacks to the UI thread
	final Handler handler = new Handler();

	// Create runnable for posting
	final Runnable updateResults = new Runnable() {
		public void run() {
			// updateResultsInUi();
			System.out.println("== ExecutionResults: \n" + executionResult + "\n ================ ");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void sendCommand(View view) {
		Thread t = new Thread() {
			public void run() {
				executionResult = null;
				try {
					executionResult = DDWrtController.executeRemoteCommandTmp();
				} catch (Exception e) {
					executionResult = e.getMessage();
				} finally {
					handler.post(updateResults);
				}
			}
		};
		t.start();
	}
}
