package com.jnrcorp.ems.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.media.AudioManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;
import com.jnrcorp.ems.sqllite.model.RuleFilter;
import com.jnrcorp.ems.sqllite.model.RuleTime;
import com.jnrcorp.ems.sqllite.model.RuleType;
import com.jnrcorp.ems.sqllite.model.VibrateType;
import com.jnrcorp.ems.util.RingtoneUtil;
import com.jnrcorp.ems.util.RuleTimeUtils;
import com.jnrcorp.ems.util.TimeUtil;

public class RuleListAdapter implements ListAdapter {

	private final Context context;
	private final LayoutInflater layoutInflater;
	private final List<EmergencyMessageServiceRule> emsRules;

	private final int iconWidth;
	private final int iconHeight;

	public RuleListAdapter(Context context, List<EmergencyMessageServiceRule> emsRules) {
		this.context = context;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.emsRules = emsRules;
		iconWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, context.getResources().getDisplayMetrics());
		iconHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, context.getResources().getDisplayMetrics());
	}

	@Override
	public int getCount() {
		return emsRules.size();
	}

	@Override
	public Object getItem(int position) {
		return emsRules.get(position);
	}

	@Override
	public long getItemId(int position) {
		return emsRules.get(position).getRuleId();
	}

	@Override
	public int getItemViewType(int arg0) {
		return 0;
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
		return emsRules.isEmpty();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int arg0) {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.rule_list_adapter_row, parent, false);
		}
		Resources resources = context.getResources();
		EmergencyMessageServiceRule emsRule = emsRules.get(position);
		setRuleDetailsText(convertView, resources, emsRule);
		setAudibleAlertType(convertView, emsRule);
		setRingtoneText(convertView, emsRule);
		setVolumeSeekBar(convertView, resources, emsRule);
		setRuleScheduleIconAndText(convertView, emsRule);
		setVibrateIcon(convertView, resources, emsRule);
		setScreenOnIcon(convertView, resources, emsRule);
		setCameraFlashOnIcon(convertView, resources, emsRule);
		return convertView;
	}

	private void setRuleScheduleIconAndText(View convertView, EmergencyMessageServiceRule emsRule) {
		TextView ruleScheduleDisplay = (TextView) convertView.findViewById(R.id.ruleSchedule);
		ImageView scheduleImage = (ImageView) convertView.findViewById(R.id.scheduleImage);
		ruleScheduleDisplay.setText(getRuleScheduleText(convertView, emsRule));
		if (!emsRule.getRuleTimes().isEmpty()) {
			scheduleImage.setVisibility(ImageView.GONE);
		} else {
			scheduleImage.getLayoutParams().height = iconHeight;
			scheduleImage.getLayoutParams().width = iconWidth;
			ruleScheduleDisplay.setVisibility(TextView.GONE);
		}
	}

	private void setVolumeSeekBar(View convertView, Resources resources, EmergencyMessageServiceRule emsRule) {
		ProgressBar volumeBar = (ProgressBar) convertView.findViewById(R.id.volumeBar);
		volumeBar.setProgress(emsRule.getVolume());
		AudioManager audioManager = (AudioManager) convertView.getContext().getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		volumeBar.setMax(maxVolume);
	}

	private void setRingtoneText(View convertView, EmergencyMessageServiceRule emsRule) {
		TextView ringTone = (TextView) convertView.findViewById(R.id.ringTone);
		if (emsRule.getAlertType().isPlaysNotification()) {
			String ringtoneTitle = RingtoneUtil.getRingtoneTitle(context, emsRule.getRingtone());
			ringTone.setText(ringtoneTitle);
			ringTone.setVisibility(TextView.VISIBLE);
		} else {
			ringTone.setVisibility(TextView.GONE);
		}
	}

	private void setAudibleAlertType(View convertView, EmergencyMessageServiceRule emsRule) {
		TextView alertType = (TextView) convertView.findViewById(R.id.alertType);
		String alertTypeLabel = convertView.getResources().getString(emsRule.getAlertType().getLabelResource());
		alertType.setText(alertTypeLabel);
	}

	private void setRuleDetailsText(View convertView, Resources resources, EmergencyMessageServiceRule emsRule) {
		RelativeLayout ruleListRow = (RelativeLayout) convertView.findViewById(R.id.ruleListRow);
		TextView ruleDetails = (TextView) convertView.findViewById(R.id.ruleDetails);
		StringBuilder ruleDetailsAll = new StringBuilder();
		for (RuleFilter ruleFilter : emsRule.getRuleFilters()) {
			String rulePhoneData;
			if (ruleFilter.getRuleType() == RuleType.RANGE) {
				rulePhoneData = resources.getString(R.string.rangeRuleDescription, ruleFilter.getFilterData0(), ruleFilter.getFilterData1());
			} else {
				rulePhoneData = ruleFilter.getFilterData0();
			}
			String ruleLabel = resources.getString(ruleFilter.getRuleType().getLabelResource());
			if (ruleDetailsAll.length() > 0) {
				ruleDetailsAll.append("\n");
			}
			ruleDetailsAll.append(resources.getString(R.string.ruleDetails, ruleLabel, rulePhoneData));
		}
		if (!emsRule.isEnabled()) {
			ruleListRow.setAlpha(0.4f);
			ruleDetailsAll.append(" (Disabled)");
		}
		ruleDetails.setText(ruleDetailsAll.toString());
	}

	private void setCameraFlashOnIcon(View convertView, Resources resources, EmergencyMessageServiceRule emsRule) {
		ImageView cameraFlashOn = (ImageView) convertView.findViewById(R.id.cameraFlashOn);
		cameraFlashOn.getLayoutParams().height = iconHeight;
		cameraFlashOn.getLayoutParams().width = iconWidth;
		if (emsRule.isFlashLightOn()) {
			cameraFlashOn.setContentDescription(resources.getString(R.string.flashLightOn));
			cameraFlashOn.setImageResource(R.drawable.flash_on);
		} else {
			cameraFlashOn.setContentDescription(resources.getString(R.string.flashLightOff));
			cameraFlashOn.setImageResource(R.drawable.flash_off);
		}
	}

	private void setScreenOnIcon(View convertView, Resources resources, EmergencyMessageServiceRule emsRule) {
		ImageView turnScreenOn = (ImageView) convertView.findViewById(R.id.turnScreenOn);
		turnScreenOn.getLayoutParams().height = iconHeight;
		turnScreenOn.getLayoutParams().width = iconWidth;
		if (emsRule.isTurnScreenOn()) {
			turnScreenOn.setContentDescription(resources.getString(R.string.screenWillTurnOn));
			turnScreenOn.setImageResource(R.drawable.screen_on);
		} else {
			turnScreenOn.setContentDescription(resources.getString(R.string.screenWillStayOff));
			turnScreenOn.setImageResource(R.drawable.screen_off);
		}
	}

	private void setVibrateIcon(View convertView, Resources resources, EmergencyMessageServiceRule emsRule) {
		ImageView vibrate = (ImageView) convertView.findViewById(R.id.vibrate);
		String vibrateLabel = resources.getString(emsRule.getVibrateType().getLabelResource());
		vibrate.setContentDescription(resources.getString(R.string.vibrateType, vibrateLabel));
		vibrate.getLayoutParams().height = iconHeight;
		vibrate.getLayoutParams().width = iconWidth;
		if (emsRule.getVibrateType() == VibrateType.NO_VIBRATE) {
			vibrate.setImageResource(R.drawable.vibrate_off);
		} else if (emsRule.getVibrateType() == VibrateType.VIBRATE_CONTINUOUS) {
			vibrate.setImageResource(R.drawable.vibrate_continuous);
		} else if (emsRule.getVibrateType() == VibrateType.VIBRATE_ONCE) {
			vibrate.setImageResource(R.drawable.vibrate_once);
		}
	}

	private String getRuleScheduleText(View convertView, EmergencyMessageServiceRule emsRule) {
		StringBuilder ruleSchedule = new StringBuilder();
		for (RuleTime ruleTime : emsRule.getRuleTimes()) {
			String startTime = TimeUtil.getTimeForDisplay(ruleTime.getTimeStart());
			String endTime = TimeUtil.getTimeForDisplay(ruleTime.getTimeEnd());
			String ruleTimes = convertView.getResources().getString(R.string.timeFrameHoursDisplay, startTime, endTime);
			if (ruleSchedule.length() != 0) {
				ruleSchedule.append("\n");
			}
			ruleSchedule.append(ruleTimes);
			String daysCommaSeparated = RuleTimeUtils.getDaysCommaSeparated(context.getResources(), ruleTime);
			String days = convertView.getResources().getString(R.string.timeFrameHoursDaysDisplay, daysCommaSeparated);
			ruleSchedule.append(" ");
			ruleSchedule.append(days);
		}
		return ruleSchedule.toString();
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub

	}

}
