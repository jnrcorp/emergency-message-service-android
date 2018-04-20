package com.jnrcorp.ems.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.activity.AddRuleActivity;
import com.jnrcorp.ems.model.ValidationResult;
import com.jnrcorp.ems.rule.validator.RuleTimeValidator;
import com.jnrcorp.ems.sqllite.model.Day;
import com.jnrcorp.ems.sqllite.model.RuleTime;
import com.jnrcorp.ems.sqllite.model.RuleTimeDay;
import com.jnrcorp.ems.util.AlertDialogUtil;
import com.jnrcorp.ems.util.StringUtil;
import com.jnrcorp.ems.util.TimePickerUtil;

public class ScheduleDialogService {

	private final AddRuleActivity activity;
	private final RuleTime ruleTime;
	private final RuleTime ruleTimeBeforeSave;

	private static final Map<Day, Integer> dayIds = new HashMap<Day, Integer>();

	static {
		dayIds.put(Day.SUNDAY, R.id.toggleSunday);
		dayIds.put(Day.MONDAY, R.id.toggleMonday);
		dayIds.put(Day.TUESDAY, R.id.toggleTuesday);
		dayIds.put(Day.WEDNESDAY, R.id.toggleWednesday);
		dayIds.put(Day.THURSDAY, R.id.toggleThursday);
		dayIds.put(Day.FRIDAY, R.id.toggleFriday);
		dayIds.put(Day.SATURDAY, R.id.toggleSaturday);
	}

	public ScheduleDialogService(AddRuleActivity activity, RuleTime ruleTime) {
		this.activity = activity;
		this.ruleTime = ruleTime;
		this.ruleTimeBeforeSave = new RuleTime();
	}

	public void editSchedule() {

		final View editScheduleView = loadEditScheduleView();

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		if (ruleTime.getTimeId() > 0) {
			builder.setTitle(R.string.edit_schedule);
		} else {
			builder.setTitle(R.string.add_schedule);
		}
		builder.setView(editScheduleView);
		builder.setPositiveButton(activity.getString(R.string.save), null);
		builder.setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
		final AlertDialog d = builder.create();
		d.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				Button saveButton = d.getButton(AlertDialog.BUTTON_POSITIVE);
				saveButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						saveFormToBean(editScheduleView);
						if (ruleTimeValid()) {
							ruleTime.setTimeStart(ruleTimeBeforeSave.getTimeStart());
							ruleTime.setTimeEnd(ruleTimeBeforeSave.getTimeEnd());
							ruleTime.setRuleTimeDays(ruleTimeBeforeSave.getRuleTimeDays());
							activity.addRuleTime(ruleTime);
							d.dismiss();
						}
					}

				});
			}

		});
		d.show();

	}

	private View loadEditScheduleView() {
		LayoutInflater inflater = activity.getLayoutInflater();
		ScrollView scrollView = new ScrollView(activity.getApplicationContext());
		View editScheduleView = inflater.inflate(R.layout.schedule_edit_dialog, scrollView);
		populateForm(editScheduleView);
		return editScheduleView;
	}

	private void populateForm(final View editScheduleView) {
		final TimePicker timeStart = findViewById(R.id.schedule_time_start, editScheduleView, TimePicker.class);
		final TimePicker timeEnd = findViewById(R.id.schedule_time_end, editScheduleView, TimePicker.class);

		TimePickerUtil.setTime(timeStart, ruleTime.getTimeStart());
		TimePickerUtil.setTime(timeEnd, ruleTime.getTimeEnd());

		if (ruleTime.getRuleTimeDays() != null) {
			for (RuleTimeDay ruleTimeDay : ruleTime.getRuleTimeDays()) {
				Integer dayId = dayIds.get(ruleTimeDay.getDay());
				final ToggleButton day = findViewById(dayId, editScheduleView, ToggleButton.class);
				day.setChecked(true);
			}
		}
	}

	private boolean ruleTimeValid() {
		RuleTimeValidator ruleTimeValidator = new RuleTimeValidator(activity, ruleTimeBeforeSave);
		ValidationResult validationResult = ruleTimeValidator.validate();
		if (!validationResult.isValid()) {
			StringBuilder outputMessage = new StringBuilder();
			for (String vaiidationMessage : validationResult.getErrorMessages()) {
				if (StringUtil.isNotEmpty(outputMessage.toString())) {
					outputMessage.append("\n\n");
				}
				outputMessage.append(vaiidationMessage);
			}
			AlertDialogUtil.createOkAlertDialog(activity, activity.getString(R.string.ruleTimeValidationError), outputMessage).show();
		}
		return validationResult.isValid();
	}

	private void saveFormToBean(final View editScheduleView) {
		final TimePicker timeStart = findViewById(R.id.schedule_time_start, editScheduleView, TimePicker.class);
		final TimePicker timeEnd = findViewById(R.id.schedule_time_end, editScheduleView, TimePicker.class);

		ruleTimeBeforeSave.setTimeStart(TimePickerUtil.getTime(timeStart));
		ruleTimeBeforeSave.setTimeEnd(TimePickerUtil.getTime(timeEnd));

		List<RuleTimeDay> ruleTimeDays = new ArrayList<RuleTimeDay>();
		for (Map.Entry<Day, Integer> dayId : dayIds.entrySet()) {
			final ToggleButton day = findViewById(dayId.getValue(), editScheduleView, ToggleButton.class);
			if (day.isChecked()) {
				RuleTimeDay ruleTimeDay = new RuleTimeDay();
				ruleTimeDay.setDay(dayId.getKey());
				ruleTimeDays.add(ruleTimeDay);
			}
		}
		Collections.sort(ruleTimeDays);
		ruleTimeBeforeSave.setRuleTimeDays(ruleTimeDays);
	}

	protected <T> T findViewById(int id, View view, Class<T> classToCast) {
		return classToCast.cast(view.findViewById(id));
	}

}
