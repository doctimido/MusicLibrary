package com.waves.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.waves.R;
import com.waves.library.scan.FileScanService;

public class HomePage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);

		final Button btnFullScan = (Button) findViewById(R.id.btnFullScan);
		btnFullScan.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						SongList.class);
				intent.putExtra(SongList.START_SCAN, true);
				startActivity(intent);
			}
		});

		final Button btnBackgroundScan = (Button) findViewById(R.id.btnBackgroundScan);
		btnBackgroundScan.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//TODO class to select default directory
				Intent intent = new Intent(getString(
						FileScanService.FULL_SCAN_UNBOUND,
						getString(R.string.scheme_top_dir)
								+ "/storage/extSdCard/Music"));
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}

}
