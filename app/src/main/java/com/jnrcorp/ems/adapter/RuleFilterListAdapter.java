package com.jnrcorp.ems.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.sqllite.model.RuleFilter;
import com.jnrcorp.ems.sqllite.model.RuleType;

public class RuleFilterListAdapter implements ListAdapter {

	private final Context context;
	private final LayoutInflater layoutInflater;
	private final List<RuleFilter> ruleFilters;

	public RuleFilterListAdapter(Context context, List<RuleFilter> ruleFilters) {
		this.context = context;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.ruleFilters = ruleFilters;
	}

	@Override
	public int getCount() {
		return ruleFilters.size();
	}

	@Override
	public RuleFilter getItem(int position) {
		return ruleFilters.get(position);
	}

	@Override
	public long getItemId(int position) {
		return ruleFilters.get(position).getFilterId();
	}

	@Override
	public int getItemViewType(int position) {
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
		return ruleFilters.isEmpty();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.rule_list_filter_adapter_row, parent, false);
		}
		Resources resources = context.getResources();
		RuleFilter ruleFilter = ruleFilters.get(position);
		setFilterDetailsText(convertView, resources, ruleFilter);
		return convertView;
	}

	private void setFilterDetailsText(View convertView, Resources resources, RuleFilter ruleFilter) {
		TextView filterDetailsDisplay = (TextView) convertView.findViewById(R.id.filterDetails);
		filterDetailsDisplay.setText(getFilterDetailsText(resources, ruleFilter));
	}

	private CharSequence getFilterDetailsText(Resources resources, RuleFilter ruleFilter) {
		String rulePhoneData;
		if (ruleFilter.getRuleType() == RuleType.RANGE) {
			rulePhoneData = resources.getString(R.string.rangeRuleDescription, ruleFilter.getFilterData0(), ruleFilter.getFilterData1());
		} else {
			rulePhoneData = ruleFilter.getFilterData0();
		}
		String display;
		if (ruleFilter.getRuleType() != null) {
			String ruleLabel = resources.getString(ruleFilter.getRuleType().getLabelResource());
			display = resources.getString(R.string.ruleDetails, ruleLabel, rulePhoneData);
		} else {
			display = rulePhoneData;
		}
		return display;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

}
