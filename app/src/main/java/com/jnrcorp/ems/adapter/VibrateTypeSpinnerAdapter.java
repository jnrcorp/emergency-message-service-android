package com.jnrcorp.ems.adapter;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.sqllite.model.VibrateType;

public class VibrateTypeSpinnerAdapter implements SpinnerAdapter {

	private LayoutInflater layoutInflater;
	private List<VibrateType> vibrateTypes = VibrateType.getForVibrateTypeAdapter();

	public VibrateTypeSpinnerAdapter(Context context) {
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.custom_adapter_row, parent, false);
		}
		VibrateType vibrateType = vibrateTypes.get(position);
		setIcon(convertView, vibrateType);
		setLabel(convertView, vibrateType);
		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.custom_adapter_drop_down_row, parent, false);
		}
		VibrateType vibrateType = vibrateTypes.get(position);
		setIcon(convertView, vibrateType);
		setLabel(convertView, vibrateType);
		setDescription(convertView, vibrateType);
		return convertView;
	}

	private void setDescription(View convertView, VibrateType vibrateType) {
		TextView description = (TextView) convertView.findViewById(R.id.description);
		String vibrateDetail = convertView.getResources().getString(vibrateType.getDescriptionResource());
		description.setText(vibrateDetail);
	}

	private void setLabel(View convertView, VibrateType vibrateType) {
		TextView label = (TextView) convertView.findViewById(R.id.label);
		String vibrateLabel = convertView.getResources().getString(vibrateType.getLabelResource());
		label.setText(vibrateLabel);
	}

	private void setIcon(View convertView, VibrateType vibrateType) {
		ImageView icon = (ImageView) convertView.findViewById(R.id.imageIcon);
		icon.setImageResource(vibrateType.getIcon());
		icon.setVisibility(ImageView.VISIBLE);
	}

	@Override
	public VibrateType getItem(int position) {
		return vibrateTypes.get(position);
	}

	@Override
	public int getCount() {
		return vibrateTypes.size();
	}

	@Override
	public long getItemId(int position) {
		return vibrateTypes.get(position).getId();
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
