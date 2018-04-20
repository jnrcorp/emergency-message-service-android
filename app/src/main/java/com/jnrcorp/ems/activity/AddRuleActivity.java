package com.jnrcorp.ems.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.adapter.AlertTypeSpinnerAdapter;
import com.jnrcorp.ems.adapter.RuleFilterListAdapter;
import com.jnrcorp.ems.adapter.ScheduleListAdapter;
import com.jnrcorp.ems.adapter.VibrateTypeSpinnerAdapter;
import com.jnrcorp.ems.constant.Constants;
import com.jnrcorp.ems.dialog.FilterActionService;
import com.jnrcorp.ems.dialog.FilterDialogService;
import com.jnrcorp.ems.dialog.FilterEmailDialogService;
import com.jnrcorp.ems.dialog.FilterPhoneDialogService;
import com.jnrcorp.ems.dialog.ScheduleActionService;
import com.jnrcorp.ems.dialog.ScheduleDialogService;
import com.jnrcorp.ems.sqllite.model.AlertType;
import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;
import com.jnrcorp.ems.sqllite.model.FilterType;
import com.jnrcorp.ems.sqllite.model.RuleFilter;
import com.jnrcorp.ems.sqllite.model.RuleTime;
import com.jnrcorp.ems.sqllite.model.VibrateType;
import com.jnrcorp.ems.sqllite.service.RuleService;
import com.jnrcorp.ems.util.AlertDialogUtil;
import com.jnrcorp.ems.util.ContactUtil;
import com.jnrcorp.ems.util.HighlightOnTouchListener;
import com.jnrcorp.ems.util.RingtoneUtil;

public class AddRuleActivity extends Activity implements OnClickListener {

	private static final int RINGTONE_PICKER_RESULT_CODE = 5;

	private RuleService ruleService;
	private FilterDialogService filterDialogService;
	private EmergencyMessageServiceRule emsRule;
	private List<RuleFilter> ruleFiltersToDelete = new ArrayList<RuleFilter>();
	private List<RuleTime> ruleTimesToDelete = new ArrayList<RuleTime>();
	private Uri selectedNotificationRingtoneURI = null;
	private String selectedNotificationRingtoneTitle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rule_edit);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		ruleService = new RuleService(this);
		Bundle extras = getIntent().getExtras();
		Long emsRuleId = null;
		if (extras != null) {
			emsRuleId = extras.getLong(Constants.RULE_ID);
		}
		if (emsRuleId != null) {
			emsRule = ruleService.loadRule(emsRuleId);
		} else {
			emsRule = new EmergencyMessageServiceRule();
		}
		setupForm();
		setupActionButtons();
		loadRuleToForm();
//		Log.i(Constants.LOG_TAG, SmsUtil.getSmsDetails(this).toString());
	}

	private void setupForm() {
		Spinner alertTypeSelector = (Spinner) findViewById(R.id.alertTypeSelector);
		alertTypeSelector.setAdapter(new AlertTypeSpinnerAdapter(this));
		alertTypeSelector.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				AlertType alertType = AlertType.getById((int) id);
				applyAlertTypeLayout(alertType);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});
		final Activity activity = this;
		TextView ringTone = (TextView) findViewById(R.id.ringTone);
		ringTone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, selectedNotificationRingtoneURI);
				activity.startActivityForResult(intent, RINGTONE_PICKER_RESULT_CODE);
			}

		});
		ringTone.setOnTouchListener(new HighlightOnTouchListener());
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int ruleVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		setRingtoneData(null);
		SeekBar volume = (SeekBar) findViewById(R.id.volume);
		volume.setMax(0);
		volume.setMax(ruleVolume);
		volume.setProgress(ruleVolume);
		Spinner vibrateTypeSelector = (Spinner) findViewById(R.id.vibrate);
		vibrateTypeSelector.setAdapter(new VibrateTypeSpinnerAdapter(this));
		Switch enabled = (Switch) findViewById(R.id.enabled);
		enabled.setChecked(true);
		final CheckBox turnScreenOn = (CheckBox) findViewById(R.id.turnScreenOn);
		final ImageView screenIcon = (ImageView) findViewById(R.id.screenIcon);
		turnScreenOn.setChecked(true);
		screenIcon.setImageResource(R.drawable.screen_on);
		turnScreenOn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (turnScreenOn.isChecked()) {
					screenIcon.setImageResource(R.drawable.screen_on);
				} else {
					screenIcon.setImageResource(R.drawable.screen_off);
				}
			}
		});
		LinearLayout flashLayout = (LinearLayout) findViewById(R.id.flashSelection);
		boolean hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		if (!hasCameraFlash) {
			flashLayout.setVisibility(LinearLayout.GONE);
		}
		Button addDayTimeSchedule = (Button) findViewById(R.id.addDayTimeSchedule);
		addDayTimeSchedule.setOnClickListener(this);
		final CheckBox turnFlashOn = (CheckBox) findViewById(R.id.turnFlashOn);
		final ImageView flashIcon = (ImageView) findViewById(R.id.flashIcon);
		flashIcon.setImageResource(R.drawable.flash_off);
		turnFlashOn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (turnFlashOn.isChecked()) {
					flashIcon.setImageResource(R.drawable.flash_on);
				} else {
					flashIcon.setImageResource(R.drawable.flash_off);
				}
			}
		});
		Button addPhone = (Button) findViewById(R.id.add_phone);
		addPhone.setOnClickListener(this);
		Button addEmail = (Button) findViewById(R.id.add_email);
		addEmail.setOnClickListener(this);
		displayScheduleRules();
	}

	private void setupActionButtons() {
		Button cancel = (Button) findViewById(R.id.rule_cancel);
		Button delete = (Button) findViewById(R.id.rule_delete);
		Button save = (Button) findViewById(R.id.rule_save);
		if (emsRule.getRuleId() <= 0) {
			delete.setVisibility(Button.GONE);
		}
		cancel.setOnClickListener(this);
		delete.setOnClickListener(this);
		save.setOnClickListener(this);
	}

	private void loadRuleToForm() {
		Log.i(Constants.LOG_TAG, "EMS Rule Id: " + emsRule.getRuleId());
		if (emsRule.getRuleId() <= 0) {
			return;
		}
		Spinner alertTypeSelector = (Spinner) findViewById(R.id.alertTypeSelector);
		SeekBar volume = (SeekBar) findViewById(R.id.volume);
		Spinner vibrateTypeSelector = (Spinner) findViewById(R.id.vibrate);
		CheckBox turnScreenOn = (CheckBox) findViewById(R.id.turnScreenOn);
		ImageView screenIcon = (ImageView) findViewById(R.id.screenIcon);
		CheckBox turnFlashOn = (CheckBox) findViewById(R.id.turnFlashOn);
		ImageView flashIcon = (ImageView) findViewById(R.id.flashIcon);
		Switch enabled = (Switch) findViewById(R.id.enabled);
		if (emsRule.getAlertType() != null) {
			alertTypeSelector.setSelection(emsRule.getAlertType().getId()-1);
		}
		Uri ringtoneUri = RingtoneUtil.getRingtoneUri(emsRule.getRingtone());
		setRingtoneData(ringtoneUri);
		volume.setProgress(emsRule.getVolume());
		if (emsRule.getVibrateType() != null) {
			vibrateTypeSelector.setSelection(emsRule.getVibrateType().getId()-1);
		}
		turnScreenOn.setChecked(emsRule.isTurnScreenOn());
		if (emsRule.isTurnScreenOn()) {
			screenIcon.setImageResource(R.drawable.screen_on);
		} else {
			screenIcon.setImageResource(R.drawable.screen_off);
		}
		turnFlashOn.setChecked(emsRule.isFlashLightOn());
		if (emsRule.isFlashLightOn()) {
			flashIcon.setImageResource(R.drawable.flash_on);
		} else {
			flashIcon.setImageResource(R.drawable.flash_off);
		}
		enabled.setChecked(emsRule.isEnabled());
		displayCurrentFilters();
		displayCurrentRules();
	}

	private void setRingtoneData(Uri ringToneUri) {
		selectedNotificationRingtoneURI = ringToneUri;
		selectedNotificationRingtoneTitle = RingtoneUtil.getRingtoneTitle(this, ringToneUri);
		updateSelectedRingToneDisplay();
	}

	private void updateSelectedRingToneDisplay() {
		String ringToneDisplayText = getResources().getString(R.string.ringToneHint);
		if (selectedNotificationRingtoneTitle != null) {
			ringToneDisplayText += "\n\n\t\t" + selectedNotificationRingtoneTitle;
		}
		TextView ringTone = (TextView) findViewById(R.id.ringTone);
		ringTone.setText(ringToneDisplayText);
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == RINGTONE_PICKER_RESULT_CODE) {
				Uri ringToneUri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
				setRingtoneData(ringToneUri);
			} else if (requestCode == FilterPhoneDialogService.CONTACT_PICKER_PHONE_RESULT_CODE) {
				Uri contactUri = intent.getData();
				String number = ContactUtil.getPhoneNumberById(this, contactUri);
				filterDialogService.setFilterData0(number);
				filterDialogService.editFilter();
			} else if (requestCode == FilterPhoneDialogService.CONTACT_PICKER_PHONE_END_RESULT_CODE) {
				Uri contactUri = intent.getData();
				String number = ContactUtil.getPhoneNumberById(this, contactUri);
				filterDialogService.setFilterData1(number);
				filterDialogService.editFilter();
			} else if (requestCode == FilterEmailDialogService.CONTACT_PICKER_EMAIL_RESULT_CODE) {
				Uri contactUri = intent.getData();
				String number = ContactUtil.getEmailById(this, contactUri);
				filterDialogService.setFilterData0(number);
				filterDialogService.editFilter();
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.rule_cancel:
				cancelRule();
				break;
			case R.id.rule_save:
				saveRule();
				break;
			case R.id.rule_delete:
				deleteRule();
				break;
			case R.id.addDayTimeSchedule:
				addDayTimeSchedule(new RuleTime());
				break;
			case R.id.add_phone:
				addRuleFilterPhoneNumber();
				break;
			case R.id.add_email:
				addRuleFilterEmail();
				break;
		}
	}

	private void addRuleFilterEmail() {
		filterDialogService = new FilterEmailDialogService(this, new RuleFilter(FilterType.EMAIL));
		filterDialogService.editFilter();
	}

	private void editRuleFilterEmail(RuleFilter ruleFilter) {
		if (ruleFilter != null) {
			FilterActionService filterActionService = new FilterActionService(this, ruleFilter);
			filterActionService.showActionMenu();
		}
	}

	private void addRuleFilterPhoneNumber() {
		filterDialogService = new FilterPhoneDialogService(this, new RuleFilter(FilterType.PHONE));
		filterDialogService.editFilter();
	}

	private void editRuleFilterPhoneNumber(RuleFilter ruleFilter) {
		if (ruleFilter != null) {
			FilterActionService filterActionService = new FilterActionService(this, ruleFilter);
			filterActionService.showActionMenu();
		}
	}

	public void setFilterDialogService(FilterDialogService filterDialogService) {
		this.filterDialogService = filterDialogService;
	}

	private void addDayTimeSchedule(RuleTime ruleTime) {
		ScheduleDialogService scheduleDialogService = new ScheduleDialogService(this, ruleTime);
		scheduleDialogService.editSchedule();
	}

	private void editDayTimeSchedule(RuleTime ruleTime) {
		if (ruleTime != null) {
			ScheduleActionService scheduleActionService = new ScheduleActionService(this, ruleTime);
			scheduleActionService.showActionMenu();
		}
	}

	private void displayFilterRules() {
		LinearLayout filterList = (LinearLayout) findViewById(R.id.ruleFilters);
		filterList.removeAllViews();
		RuleFilterListAdapter ruleFilterListAdapter = new RuleFilterListAdapter(this, emsRule.getRuleFilters());
		for (int i=0; i<ruleFilterListAdapter.getCount(); i++) {
			final RuleFilter ruleFilter = ruleFilterListAdapter.getItem(i);
			View viewToAdd = ruleFilterListAdapter.getView(i, null, filterList);
			filterList.addView(viewToAdd);
			viewToAdd.setOnTouchListener(new HighlightOnTouchListener());
			viewToAdd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (ruleFilter.getFilterType() == FilterType.EMAIL) {
						editRuleFilterEmail(ruleFilter);
					} else {
						editRuleFilterPhoneNumber(ruleFilter);
					}
				}
			});
		}
	}

	private void displayCurrentFilters() {
		displayFilterRules();
		displayEmptyCollectionMessage(R.id.currentFilters, emsRule.getRuleFilters());
	}

	private void displayEmptyCollectionMessage(int resourceId, Collection<?> collection) {
		TextView emptyMessageDisplay = (TextView) findViewById(resourceId);
		if (collection == null || collection.isEmpty()) {
			emptyMessageDisplay.setVisibility(TextView.VISIBLE);
		} else {
			emptyMessageDisplay.setVisibility(TextView.GONE);
		}
	}

	private void displayScheduleRules() {
		LinearLayout scheduleList = (LinearLayout) findViewById(R.id.scheduleList);
		scheduleList.removeAllViews();
		ScheduleListAdapter scheduleListAdapter = new ScheduleListAdapter(this, emsRule.getRuleTimes());
		for (int i=0; i<scheduleListAdapter.getCount(); i++) {
			final RuleTime ruleTime = scheduleListAdapter.getItem(i);
			View viewToAdd = scheduleListAdapter.getView(i, null, scheduleList);
			scheduleList.addView(viewToAdd);
			viewToAdd.setOnTouchListener(new HighlightOnTouchListener());
			viewToAdd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					editDayTimeSchedule(ruleTime);
				}

			});
		}
	}

	private void displayCurrentRules() {
		displayScheduleRules();
		displayEmptyCollectionMessage(R.id.currentRules, emsRule.getRuleTimes());
	}

	public void removeRuleFilter(RuleFilter ruleFilter) {
		emsRule.getRuleFilters().remove(ruleFilter);
		if (ruleFilter.getFilterId() > 0) {
			ruleFiltersToDelete.add(ruleFilter);
		}
		displayCurrentFilters();
	}

	public void addRuleFilter(RuleFilter ruleFilter) {
		boolean found = false;
		for (RuleFilter inList : emsRule.getRuleFilters()) {
			if (inList == ruleFilter) {
				found = true;
			}
		}
		if (!found) {
			emsRule.getRuleFilters().add(ruleFilter);
		}
		displayCurrentFilters();
	}

	public void removeRuleTime(RuleTime ruleTime) {
		emsRule.getRuleTimes().remove(ruleTime);
		if (ruleTime.getTimeId() > 0) {
			ruleTimesToDelete.add(ruleTime);
		}
		displayCurrentRules();
	}

	public void addRuleTime(RuleTime ruleTime) {
		boolean found = false;
		for (RuleTime inList : emsRule.getRuleTimes()) {
			if (inList == ruleTime) {
				found = true;
			}
		}
		if (!found) {
			emsRule.getRuleTimes().add(ruleTime);
		}
		displayCurrentRules();
	}

	private void deleteRule() {
		ruleService.deleteRule(emsRule);
		finishActivity();
	}

	private void cancelRule() {
		finishActivity();
	}

	private void saveRule() {
		populateRuleFromForm();
		if (isRuleValid()) {
			ruleService.saveRule(emsRule, ruleFiltersToDelete, ruleTimesToDelete);
			finishActivity();
		}
	}

	private boolean isRuleValid() {
		boolean hasRuleFilters = !emsRule.getRuleFilters().isEmpty();
		if (!hasRuleFilters) {
			String outputMessage = getString(R.string.filtersRequired);
			AlertDialogUtil.createOkAlertDialog(this, getString(R.string.ruleValidationError), outputMessage).show();
		}
		return hasRuleFilters;
	}

	private void finishActivity() {
		ruleService.close();
		finish();
	}

	private void populateRuleFromForm() {
		Spinner alertTypeSelector = (Spinner) findViewById(R.id.alertTypeSelector);
		SeekBar volume = (SeekBar) findViewById(R.id.volume);
		Spinner vibrateTypeSelector = (Spinner) findViewById(R.id.vibrate);
		CheckBox turnScreenOn = (CheckBox) findViewById(R.id.turnScreenOn);
		CheckBox turnFlashOn = (CheckBox) findViewById(R.id.turnFlashOn);
		Switch enabled = (Switch) findViewById(R.id.enabled);
		AlertType alertType = (AlertType) alertTypeSelector.getSelectedItem();
		emsRule.setAlertType(alertType);
		if (selectedNotificationRingtoneURI != null && !selectedNotificationRingtoneURI.equals("")) {
			emsRule.setRingtone(selectedNotificationRingtoneURI.toString());
		}
		emsRule.setVolume(volume.getProgress());
		VibrateType vibrateType = (VibrateType) vibrateTypeSelector.getSelectedItem();
		emsRule.setVibrateType(vibrateType);
		emsRule.setTurnScreenOn(turnScreenOn.isChecked());
		emsRule.setFlashLightOn(turnFlashOn.isChecked());
		emsRule.setEnabled(enabled.isChecked());
	}

	private void applyAlertTypeLayout(AlertType alertType) {
		TextView ringTone = (TextView) findViewById(R.id.ringTone);
		if (!alertType.isPlaysNotification() && alertType.isReadsAloud()) {
			ringTone.setVisibility(TextView.GONE);
		} else {
			ringTone.setVisibility(TextView.VISIBLE);
		}
	}

}
