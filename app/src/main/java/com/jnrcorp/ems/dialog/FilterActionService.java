package com.jnrcorp.ems.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.activity.AddRuleActivity;
import com.jnrcorp.ems.sqllite.model.FilterType;
import com.jnrcorp.ems.sqllite.model.RuleFilter;

public class FilterActionService {

	private AddRuleActivity activity;
	private RuleFilter ruleFilter;

	public FilterActionService(AddRuleActivity activity, RuleFilter ruleFilter) {
		super();
		this.activity = activity;
		this.ruleFilter = ruleFilter;
	}

	public void showActionMenu() {

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(activity.getString(R.string.choose_action));
		CharSequence[] fillUpActions = getFilterActions();
		builder.setItems(fillUpActions, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// The 'which' argument contains the index position of the selected item
				if (which == 0) {
					FilterDialogService filterDialogService;
					if (ruleFilter.getFilterType() == FilterType.PHONE) {
						filterDialogService = new FilterPhoneDialogService(activity, ruleFilter);
					} else {
						filterDialogService = new FilterEmailDialogService(activity, ruleFilter);
					}
					activity.setFilterDialogService(filterDialogService);
					filterDialogService.editFilter();
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
		builder.setTitle(R.string.confirm_delete_rule_filter_title);
		builder.setMessage(getConfirmDeleteRuleFilterMessage());
		builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.removeRuleFilter(ruleFilter);
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

	private CharSequence getConfirmDeleteRuleFilterMessage() {
		String filterData0 = ruleFilter.getFilterData0();
		String filterData1 = ruleFilter.getFilterData1();
		String ruleType = activity.getString(ruleFilter.getRuleType().getLabelResource());
		String filterType = activity.getString(ruleFilter.getFilterType().getLabel());
		return activity.getString(R.string.confirm_delete_rule_filter, filterType, ruleType, filterData0, filterData1);
	}

	private CharSequence[] getFilterActions() {
		List<String> filterActions = new ArrayList<String>();
		filterActions.add(getEditRuleFilter());
		filterActions.add(getDeleteRuleFilter());
		return filterActions.toArray(new String[filterActions.size()]);
	}

	private String getDeleteRuleFilter() {
		return activity.getString(R.string.filter_actions_delete);
	}

	private String getEditRuleFilter() {
		return activity.getString(R.string.filter_actions_edit);
	}

}
