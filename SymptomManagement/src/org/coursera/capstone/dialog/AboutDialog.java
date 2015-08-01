package org.coursera.capstone.dialog;

import org.coursera.capstone.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

public class AboutDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.app_name);
		builder.setIcon(R.drawable.ic_launcher);
		
		TextView aboutTxtView = new TextView(getActivity());
		aboutTxtView.setAutoLinkMask(Linkify.WEB_URLS);
		aboutTxtView.setText(R.string.about_text);
		builder.setView(aboutTxtView);
		
		builder.setPositiveButton(R.string.ok, null);
		
		return builder.create();
	}
	
}
