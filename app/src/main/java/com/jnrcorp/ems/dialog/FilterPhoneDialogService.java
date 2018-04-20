package com.jnrcorp.ems.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.activity.AddRuleActivity;
import com.jnrcorp.ems.adapter.RuleTypeSpinnerAdapter;
import com.jnrcorp.ems.model.ValidationResult;
import com.jnrcorp.ems.rule.factory.EMSRuleFactory;
import com.jnrcorp.ems.rule.validator.RuleFilterValidator;
import com.jnrcorp.ems.sqllite.model.RuleFilter;
import com.jnrcorp.ems.sqllite.model.RuleType;
import com.jnrcorp.ems.util.AlertDialogUtil;
import com.jnrcorp.ems.util.StringUtil;

public class FilterPhoneDialogService implements FilterDialogService, OnClickListener {

	public static final int CONTACT_PICKER_PHONE_RESULT_CODE = 1001;
	public static final int CONTACT_PICKER_PHONE_END_RESULT_CODE = 1002;

	private final AddRuleActivity activity;
	private final RuleFilter ruleFilter;
	private final RuleFilter ruleFilterBeforeSave;

	private AlertDialog alertDialog;

	public FilterPhoneDialogService(AddRuleActivity addRuleActivity, RuleFilter ruleFilter) {
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
							ruleFilter.setRuleType(ruleFilterBeforeSave.getRuleType());
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
				askForContactPhoneNumber(CONTACT_PICKER_PHONE_RESULT_CODE);
				break;
			case R.id.select_end_contact:
				askForContactPhoneNumber(CONTACT_PICKER_PHONE_END_RESULT_CODE);
				break;
		}
	}

	private void askForContactPhoneNumber(int requestCode) {
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
		contactPickerIntent.setType(Phone.CONTENT_TYPE);
	    activity.startActivityForResult(contactPickerIntent, requestCode);
	    alertDialog.dismiss();
	}

	private View loadEditFilterView() {
		LayoutInflater inflater = activity.getLayoutInflater();
		ScrollView scrollView = new ScrollView(activity.getApplicationContext());
		View editFilterView = inflater.inflate(R.layout.filter_phone_edit_dialog, scrollView);
		setupForm(editFilterView);
		populateForm(editFilterView);
		return editFilterView;
	}

	private void setupForm(final View editFilterView) {
		Spinner ruleTypeSelector = findViewById(R.id.ruleTypeSelector, editFilterView, Spinner.class);
		ruleTypeSelector.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View parentView, int position, long id) {
				RuleType ruleType = RuleType.getById((int) id);
				applyRuleTypeLayout(editFilterView, ruleType);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		ruleTypeSelector.setAdapter(new RuleTypeSpinnerAdapter(activity));
		ImageButton selectContact = findViewById(R.id.select_contact, editFilterView, ImageButton.class);
		selectContact.setOnClickListener(this);
		ImageButton selectEndContact = findViewById(R.id.select_end_contact, editFilterView, ImageButton.class);
		selectEndContact.setOnClickListener(this);
	}

	private void populateForm(View editFilterView) {
		EditText telephoneStart = findViewById(R.id.telephoneStart, editFilterView, EditText.class);
		EditText telephoneEnd = findViewById(R.id.telephoneEnd, editFilterView, EditText.class);
		telephoneStart.setText(ruleFilterBeforeSave.getFilterData0());
		telephoneEnd.setText(ruleFilterBeforeSave.getFilterData1());
		Spinner ruleTypeSelector = findViewById(R.id.ruleTypeSelector, editFilterView, Spinner.class);
		if (ruleFilterBeforeSave.getRuleType() != null) {
			applyRuleTypeLayout(editFilterView, ruleFilterBeforeSave.getRuleType());
			for (int i=0; i<ruleTypeSelector.getCount(); i++) {
				RuleType ruleType = (RuleType) ruleTypeSelector.getItemAtPosition(i);
				if (ruleType == ruleFilterBeforeSave.getRuleType()) {
					ruleTypeSelector.setSelection(i);
				}
			}
		}
	}

	private void applyRuleTypeLayout(View editFilterView, RuleType ruleType) {
		EditText telephoneStart = findViewById(R.id.telephoneStart, editFilterView, EditText.class);
		RelativeLayout telephoneEndLayout = findViewById(R.id.telephoneEndLayout, editFilterView, RelativeLayout.class);
		if (ruleType == RuleType.RANGE) {
			telephoneStart.setHint(R.string.phoneNumberHintRangeStart);
			telephoneEndLayout.setVisibility(EditText.VISIBLE);
		} else {
			telephoneStart.setHint(R.string.phoneNumberHint);
			telephoneEndLayout.setVisibility(EditText.GONE);
		}
	}

	private void saveFormToBean(final View editFilterView) {
		Spinner ruleTypeSelector = findViewById(R.id.ruleTypeSelector, editFilterView, Spinner.class);
		EditText telephoneStart = findViewById(R.id.telephoneStart, editFilterView, EditText.class);
		EditText telephoneEnd = findViewById(R.id.telephoneEnd, editFilterView, EditText.class);
		ruleFilterBeforeSave.setRuleType((RuleType) ruleTypeSelector.getSelectedItem());
		ruleFilterBeforeSave.setFilterData0(telephoneStart.getText().toString());
		ruleFilterBeforeSave.setFilterData1(telephoneEnd.getText().toString());
	}

	private boolean filterValid(View editFilterView) {
		RuleFilterValidator ruleValidator = EMSRuleFactory.getRuleValidator(ruleFilterBeforeSave.getRuleType());
		ValidationResult validationResult = ruleValidator.inputValid(activity, ruleFilterBeforeSave.getFilterData0(), ruleFilterBeforeSave.getFilterData1());
		if (!validationResult.isValid()) {
			StringBuilder outputMessage = new StringBuilder();
			for (String vaiidationMessage : validationResult.getErrorMessages()) {
				if (StringUtil.isNotEmpty(outputMessage.toString())) {
					outputMessage.append("\n\n");
				}
				outputMessage.append(vaiidationMessage);
			}
			AlertDialogUtil.createOkAlertDialog(activity, activity.getString(R.string.ruleValidationError), outputMessage).show();
			EditText telephoneStart = findViewById(R.id.telephoneStart, editFilterView, EditText.class);
			telephoneStart.requestFocus();
		}
		return validationResult.isValid();
	}

	protected <T> T findViewById(int id, View view, Class<T> classToCast) {
		return classToCast.cast(view.findViewById(id));
	}

}
