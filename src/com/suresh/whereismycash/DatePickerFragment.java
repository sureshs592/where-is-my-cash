package com.suresh.whereismycash;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements OnDateSetListener{
	
	public DatePickerFragment() {
		//Required
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		long dateMillis = (Long) getActivity().findViewById(R.id.tvChosenDate).getTag();
        Calendar c = Calendar.getInstance(); c.setTimeInMillis(dateMillis);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		CreateActivity activity = (CreateActivity) getActivity();
		activity.setDate(true, year, monthOfYear, dayOfMonth);
	}
}
