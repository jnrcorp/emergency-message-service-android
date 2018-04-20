package com.jnrcorp.ems.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.activity.AddRuleActivity;
import com.jnrcorp.ems.sqllite.model.RuleTime;
import com.jnrcorp.ems.util.RuleTimeUtils;
import com.jnrcorp.ems.util.TimeUtil;

public class ScheduleActionService {

	private AddRuleActivity activity;
	private RuleTime ruleTime;

	public ScheduleActionService(AddRuleActivity activity, RuleTime ruleTime) {
		super();
		this.activity = activity;
		this.ruleTime = ruleTime;
	}

	public void showActionMenu() {

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(activity.getString(R.string.choose_action));
		CharSequence[] fillUpActions = getScheduleActions();
		builder.setItems(fillUpActions, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// The 'which' argument contains the index position of the selected item
				if (which == 0) {
					ScheduleDialogService scheduleDialogService = new ScheduleDialogService(activity, ruleTime);
					scheduleDialogService.editSchedule();
				} else if (which == 1) {
					confirmDelete();
				}
				dialog.dismiss();
			}

		});
		builder.show();
	}

	private void confirmDelete() {

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle(R.string.confirm_delete_rule_time_title);
		builder.setMessage(getConfirmDeleteRuleTimeMessage());
		builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.removeRuleTime(ruleTime);
			}

		});
		builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// nothing to do
			}

		});
		builder.show();
	}

	private CharSequence getConfirmDeleteRuleTimeMessage() {
		String timeStart = TimeUtil.getTimeForDisplay(ruleTime.getTimeStart());
		String timeEnd = TimeUtil.getTimeForDisplay(ruleTime.getTimeEnd());
		String daysCommaSeparated = RuleTimeUtils.getDaysCommaSeparated(activity.getResources(), ruleTime);
		return activity.getString(R.string.confirm_delete_rule_time, timeStart, timeEnd, daysCommaSeparated);
	}

	private CharSequence[] getScheduleActions() {
		List<String> scheduleActions = new ArrayList<String>();
		scheduleActions.add(getEditRuleTime());
		scheduleActions.add(getDeleteRuleTime());
		return scheduleActions.toArray(new String[scheduleActions.size()]);
	}

	private String getDeleteRuleTime() {
		return activity.getString(R.string.schedule_actions_delete);
	}

	private String getEditRuleTime() {
		return activity.getString(R.string.schedule_actions_edit);
	}

}
