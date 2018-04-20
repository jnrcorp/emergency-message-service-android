package com.jnrcorp.ems.adapter;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.sqllite.model.RuleTime;
import com.jnrcorp.ems.util.RuleTimeUtils;
import com.jnrcorp.ems.util.TimeUtil;

public class ScheduleListAdapter implements ListAdapter {

	private final LayoutInflater layoutInflater;
	private final List<RuleTime> ruleTimes;

	public ScheduleListAdapter(Context context, List<RuleTime> ruleTimes) {
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.ruleTimes = ruleTimes;
	}

	@Override
	public int getCount() {
		return ruleTimes.size();
	}

	@Override
	public RuleTime getItem(int position) {
		return ruleTimes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return ruleTimes.get(position).getTimeId();
	}

	@Override
	public int getItemViewType(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.rule_edit_schedule_row, parent, false);
		}
		RuleTime ruleTime = ruleTimes.get(position);
		TextView scheduleDetails = (TextView) convertView.findViewById(R.id.scheduleDetails);
		TextView dayDetails = (TextView) convertView.findViewById(R.id.dayDetails);
		String startTime = TimeUtil.getTimeForDisplay(ruleTime.getTimeStart());
		String endTime = TimeUtil.getTimeForDisplay(ruleTime.getTimeEnd());
		String ruleTimes = convertView.getResources().getString(R.string.timeFrameHoursDisplay, startTime, endTime);
		scheduleDetails.setText(ruleTimes);
		dayDetails.setText(RuleTimeUtils.getDaysCommaSeparated(convertView.getResources(), ruleTime));
		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int arg0) {
		return true;
	}

}
