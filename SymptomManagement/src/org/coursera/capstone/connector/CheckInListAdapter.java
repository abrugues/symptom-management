package org.coursera.capstone.connector;

import java.util.ArrayList;
import java.util.List;

import org.coursera.capstone.DoctorCheckInActivity;
import org.coursera.capstone.DoctorCheckInListActivity;
import org.coursera.capstone.R;
import org.coursera.capstone.model.CheckIn;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CheckInListAdapter extends BaseAdapter {

	public static final String CHECKIN = "check-in";
	public static final String PATIENT_NAME = "patientName";
	
	private List<CheckIn> checkins;
	private Context context;
	
	public CheckInListAdapter(Context context) {
		this.context = context;
		this.checkins = new ArrayList<CheckIn>();
	}
	
	public void loadCheckins(List<CheckIn> checkins) {
		this.checkins = checkins;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return checkins.size();
	}

	@Override
	public Object getItem(int position) {
		return checkins.get(position);
	}

	@Override
	public long getItemId(int id) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_checkin_filled, parent, false);
		}
		
		final CheckIn checkin = checkins.get(position);
		TextView checkinTxtView = (TextView) convertView.findViewById(R.id.itemViewCheckin);
		checkinTxtView.setText(checkin.getStringTimeStamp());
		
		convertView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				String patientName = ((DoctorCheckInListActivity) context).getPatientName();
				Intent i = new Intent(context, DoctorCheckInActivity.class);
				i.putExtra(CHECKIN, checkin);
				i.putExtra(PATIENT_NAME, patientName);
				context.startActivity(i);
			}
		});
		
		return convertView;
	}

}
