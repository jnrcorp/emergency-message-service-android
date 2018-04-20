package com.jnrcorp.ems.sqllite.model;

import java.util.ArrayList;
import java.util.List;

import com.jnrcorp.ems.R;

public enum FilterType {

	PHONE(1, R.string.filter_phone_label, R.string.filter_phone_description),
	EMAIL(2, R.string.filter_email_label, R.string.filter_email_description);

	private int id;
	private int label;
	private int description;

	private FilterType(int id, int label, int description) {
		this.id = id;
		this.label = label;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public int getLabel() {
		return label;
	}

	public int getDescription() {
		return description;
	}

	public static FilterType getById(int id) {
		for (FilterType filterType : FilterType.values()) {
			if (filterType.getId() == id) {
				return filterType;
			}
		}
		return null;
	}

	public static List<String> getFilterTypesForAdapter() {
		List<String> filterTypes = new ArrayList<String>();
		for (FilterType filterType : FilterType.values()) {
			filterTypes.add(filterType.toString());
		}
		return filterTypes;
	}

	public static List<FilterType> getForFilterTypeAdapter() {
		List<FilterType> filterTypes = new ArrayList<FilterType>();
		for (FilterType filterType : FilterType.values()) {
			filterTypes.add(filterType);
		}
		return filterTypes;
	}

}
