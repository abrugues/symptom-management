package org.coursera.capstone.ui;

import org.coursera.capstone.R;
import org.coursera.capstone.dialog.AboutDialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PatientMainMenuActivity extends Activity {

	private ImageView checkinImgView;
	private ImageView reminderImgView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_patient);
		
		checkinImgView = (ImageView) findViewById(R.id.checkinImgView);
		reminderImgView = (ImageView) findViewById(R.id.reminderImgView);
		
		checkinImgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(PatientMainMenuActivity.this, PatientCheckInActivity.class);
				startActivity(i);
				
			}
		});
		
		reminderImgView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(PatientMainMenuActivity.this, PatientReminderActivity.class);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
				DialogFragment newFragment = new AboutDialog();
				newFragment.show(getFragmentManager(), "aboutDialog");
				return true;
			case R.id.logout:
				SharedPreferences settings = getSharedPreferences(LoginMainActivity.PREFS_NAME, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.clear();
				editor.commit();
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
