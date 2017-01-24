package org.coursera.capstone.connector;

import java.util.List;

import org.coursera.capstone.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MedicationListAdapter extends BaseAdapter {

	private List<String> medications;
	private Context context;
	
	public MedicationListAdapter(Context context, List<String> medications) {
		this.context = context;
		this.medications = medications;
	}
	
	public void addMedication(String medication) {
		medications.add(medication);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return medications.size();
	}

	@Override
	public Object getItem(int pod) {
		return null;
	}

	@Override
	public long getItemId(int id) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_medication, parent, false);
		}
		
		String medication = medications.get(position);
		TextView medicationTxtView = (TextView) convertView.findViewById(R.id.itemViewMedication);
		medicationTxtView.setText(medication);
		
		return convertView;
	}

}
