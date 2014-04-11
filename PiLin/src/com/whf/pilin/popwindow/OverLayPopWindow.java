package com.whf.pilin.popwindow;

import java.util.Calendar;
import java.util.Date;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.whf.pilin.R;
import com.whf.pilin.constVar.CustomConst;
import com.whf.pilin.utils.TypeConverter;

public class OverLayPopWindow extends PopupWindow implements
		OnDateChangedListener, OnTimeChangedListener {

	private String selectTime;

	private DatePicker dp_start;

	private TimePicker tp_start;

	public boolean timeMarker;
	
	public OverLayPopWindow(Context context,final Handler handler) {
		super(context);
		
		View view = LayoutInflater.from(context).inflate(
				R.layout.pop_time_select_layout, null);
		
		Button btn = (Button) view.findViewById(R.id.btn_selctok);

		Button btn_cancel = (Button) view.findViewById(R.id.btn_selctcancel);

		dp_start = (DatePicker) view.findViewById(R.id.dp_start);
		dp_start.setCalendarViewShown(false);
		tp_start = (TimePicker) view.findViewById(R.id.tp_start);

		Calendar c = Calendar.getInstance();

		dp_start.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH), this);

		tp_start.setOnTimeChangedListener(this);

		selectTime = TypeConverter
				.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");

		tp_start.setIs24HourView(true);
		tp_start.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
		tp_start.setCurrentMinute(c.get(Calendar.MINUTE));

		this.setContentView(view);
		
		this.setWidth(LayoutParams.MATCH_PARENT);
		
		this.setHeight(LayoutParams.WRAP_CONTENT);
		
		this.setFocusable(true);

		this.setOutsideTouchable(true);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Message msg = new Message();

				msg.what = CustomConst.Handler_PopDateSelectWindow;

				Bundle bundle = new Bundle();

				bundle.putBoolean("timeMarker", timeMarker);

				bundle.putString("selectTime", selectTime);

				msg.setData(bundle);
				
				handler.sendMessage(msg);

				dismiss();

			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});

	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		if (view.getId() == R.id.tp_start) {

			int year = dp_start.getYear();

			String month = TypeConverter.returnXXMonthDayHourMinte(dp_start
					.getMonth() + 1);

			String day = TypeConverter.returnXXMonthDayHourMinte(dp_start
					.getDayOfMonth());

			String hour = TypeConverter.returnXXMonthDayHourMinte(hourOfDay);

			String min = TypeConverter.returnXXMonthDayHourMinte(minute);

			selectTime = year + "-" + month + "-" + day + " " + hour + ":"
					+ min + ":" + "00";

		}
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		if (view.getId() == R.id.dp_start) {

			String month = TypeConverter
					.returnXXMonthDayHourMinte(monthOfYear + 1);

			String day = TypeConverter.returnXXMonthDayHourMinte(dayOfMonth);

			String hour = TypeConverter.returnXXMonthDayHourMinte(tp_start
					.getCurrentHour());

			String min = TypeConverter.returnXXMonthDayHourMinte(tp_start
					.getCurrentMinute());

			selectTime = year + "-" + month + "-" + day + " " + hour + ":"
					+ min + ":" + "00";

		}
	}
	
	
}
