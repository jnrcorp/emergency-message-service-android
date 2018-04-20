package com.jnrcorp.ems.adapter;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.sqllite.model.RuleType;

public class RuleTypeSpinnerAdapter implements SpinnerAdapter {

	private LayoutInflater layoutInflater;
	private List<RuleType> ruleTypes = RuleType.getForRuleTypeAdapter();

	public RuleTypeSpinnerAdapter(Context context) {
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.custom_adapter_row, parent, false);
		}
		RuleType ruleType = ruleTypes.get(position);
		TextView label = (TextView) convertView.findViewById(R.id.label);
		String ruleTypePlural = convertView.getResources().getString(ruleType.getLabelPluralResource());
		label.setText(ruleTypePlural);
		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.custom_adapter_drop_down_row, parent, false);
		}
		RuleType ruleType = ruleTypes.get(position);
		TextView label = (TextView) convertView.findViewById(R.id.label);
		TextView description = (TextView) convertView.findViewById(R.id.description);
		String ruleTypePlural = convertView.getResources().getString(ruleType.getLabelPluralResource());
		label.setText(ruleTypePlural);
		String ruleTypeDetail = convertView.getResources().getString(ruleType.getDescriptionResource());
		description.setText(ruleTypeDetail);
		return convertView;
	}

	@Override
	public RuleType getItem(int position) {
		return ruleTypes.get(position);
	}

	@Override
	public int getCount() {
		return ruleTypes.size();
	}

	@Override
	public long getItemId(int position) {
		return ruleTypes.get(position).getId();
	}

	@Override
	public int getItemViewType(int arg0) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 0;
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

}
