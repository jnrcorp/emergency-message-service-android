package com.jnrcorp.ems.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.activity.AddRuleActivity;
import com.jnrcorp.ems.sqllite.model.RuleFilter;
import com.jnrcorp.ems.sqllite.model.RuleType;
import com.jnrcorp.ems.util.AlertDialogUtil;
import com.jnrcorp.ems.util.StringUtil;

public class FilterEmailDialogService implements FilterDialogService, OnClickListener {

	public static final int CONTACT_PICKER_EMAIL_RESULT_CODE = 2001;

	private final AddRuleActivity activity;
	private final RuleFilter ruleFilter;
	private final RuleFilter ruleFilterBeforeSave;

	private AlertDialog alertDialog;

	public FilterEmailDialogService(AddRuleActivity addRuleActivity, RuleFilter ruleFilter) {
		this.activity = addRuleActivity;
		this.ruleFilter = ruleFilter;
		this.ruleFilterBeforeSave = new RuleFilter(ruleFilter);
	}

	@Override
	public void setFilterData0(String filterData0) {
		ruleFilterBeforeSave.setFilterData0(filterData0);
	}

	@Override
	public void setFilterData1(String filterData1) {
		ruleFilterBeforeSave.setFilterData1(filterData1);
	}

	@Override
	public void editFilter() {
		final View editFilterView = loadEditFilterView();

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		if (ruleFilterBeforeSave.getFilterId() > 0) {
			builder.setTitle(R.string.edit_filter);
		} else {
			builder.setTitle(R.string.add_filter);
		}
		builder.setView(editFilterView);
		builder.setPositiveButton(activity.getString(R.string.save), null);
		builder.setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		alertDialog = builder.create();
		alertDialog.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				Button saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				saveButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						saveFormToBean(editFilterView);
						if (filterValid(editFilterView)) {
							ruleFilter.setFilterData0(ruleFilterBeforeSave.getFilterData0());
							ruleFilter.setFilterData1(ruleFilterBeforeSave.getFilterData1());
							ruleFilter.setFilterType(ruleFilterBeforeSave.getFilterType());
							ruleFilter.setRuleType(RuleType.EQUALS);
							activity.addRuleFilter(ruleFilter);
							alertDialog.dismiss();
						}
					}

				});
			}

		});
		alertDialog.show();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.select_contact:
				askForContactEmail(CONTACT_PICKER_EMAIL_RESULT_CODE);
				break;
		}
	}

	private void askForContactEmail(int requestCode) {
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
		contactPickerIntent.setType(Email.CONTENT_TYPE);
	    activity.startActivityForResult(contactPickerIntent, requestCode);
	    alertDialog.dismiss();
	}

	private View loadEditFilterView() {
		LayoutInflater inflater = activity.getLayoutInflater();
		ScrollView scrollView = new ScrollView(activity.getApplicationContext());
		View editFilterView = inflater.inflate(R.layout.filter_email_edit_dialog, scrollView);
		setupForm(editFilterView);
		populateForm(editFilterView);
		return editFilterView;
	}

	private void setupForm(final View editFilterView) {
		ImageButton selectContact = findViewById(R.id.select_contact, editFilterView, ImageButton.class);
		selectContact.setOnClickListener(this);
	}

	private void populateForm(View editFilterView) {
		EditText emailAddress = findViewById(R.id.email_address, editFilterView, EditText.class);
		emailAddress.setText(ruleFilterBeforeSave.getFilterData0());
	}

	private void saveFormToBean(final View editFilterView) {
		EditText emailAddress = findViewById(R.id.email_address, editFilterView, EditText.class);
		ruleFilterBeforeSave.setFilterData0(emailAddress.getText().toString());
	}

	private boolean filterValid(View editFilterView) {
		boolean success = true;
		if (StringUtil.isEmpty(ruleFilterBeforeSave.getFilterData0())	) {
			AlertDialogUtil.createOkAlertDialog(activity, activity.getString(R.string.emailRequired)).show();
			EditText emailAddress = findViewById(R.id.email_address, editFilterView, EditText.class);
			emailAddress.requestFocus();
			success = false;
		}
		return success;
	}

	protected <T> T findViewById(int id, View view, Class<T> classToCast) {
		return classToCast.cast(view.findViewById(id));
	}

}
