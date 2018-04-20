package com.jnrcorp.ems.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.action.AboutAction;
import com.jnrcorp.ems.action.HelpAction;
import com.jnrcorp.ems.action.UserAction;
import com.jnrcorp.ems.action.VersionUpgradeAction;
import com.jnrcorp.ems.adapter.RuleListAdapter;
import com.jnrcorp.ems.constant.Constants;
import com.jnrcorp.ems.sqllite.GlobalsDAO.GlobalNames;
import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;
import com.jnrcorp.ems.sqllite.service.RuleService;
import com.jnrcorp.ems.util.VersionUpgradeData;
import com.jnrcorp.ems.util.VersionUpgradeUtil;

public class AlertManagementActivity extends ListActivity implements OnClickListener {

	private RuleService ruleService;

	@SuppressLint("UseSparseArrays")
	private Map<Integer, UserAction> menuActions = new HashMap<Integer, UserAction>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		ruleService = new RuleService(this);
		loadRulesForDisplay();
		loadGlobalsForDisplay();
		Button addRule = (Button) findViewById(R.id.add_rule);
		addRule.setOnClickListener(this);
		Switch interceptionEnabled = (Switch) findViewById(R.id.interception_enabled);
		interceptionEnabled.setOnClickListener(this);
		getOverflowMenu();
		fillMenuOptionActions();
		showVersionUpgradeMessage();
	}

	private void showVersionUpgradeMessage() {
		VersionUpgradeData versionUpgradeData = VersionUpgradeUtil.isVersionUpgrade(this);
		if(versionUpgradeData.isUpgrade()) {
			VersionUpgradeAction versionUpgradeAction = new VersionUpgradeAction(this);
			versionUpgradeAction.executeAction();
		}
	}

	private void getOverflowMenu() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		UserAction menuAction = menuActions.get(item.getItemId());
		if (menuAction != null) {
			menuAction.executeAction();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	private void fillMenuOptionActions() {
		menuActions.put(R.id.menu_help, new HelpAction(this));
		menuActions.put(R.id.menu_about, new AboutAction(this));
		menuActions.put(R.id.menu_recent_changes, new VersionUpgradeAction(this));
	}

	private void loadRulesForDisplay() {
		final List<EmergencyMessageServiceRule> emsRules = ruleService.loadAllRules();
		if (emsRules.size() > 0) {
			displayRules(emsRules);
		} else {
			displayNoRulesMessage();
		}
	}

	private void displayNoRulesMessage() {
		List<String> noRulesMessages = new ArrayList<String>();
		noRulesMessages.add(getResources().getString(R.string.noRulesToDisplay));
		noRulesMessages.add(getResources().getString(R.string.noRulesToDisplay2));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noRulesMessages);
		setListAdapter(adapter);
	}

	private void displayRules(final List<EmergencyMessageServiceRule> emsRules) {
		RuleListAdapter ruleListAdapter = new RuleListAdapter(this, emsRules);
		setListAdapter(ruleListAdapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				EmergencyMessageServiceRule emsRule = emsRules.get(position);
				goToRuleEditActivity(emsRule.getRuleId());
			}

		});
	}

	private void loadGlobalsForDisplay() {
		Switch interceptionEnabled = (Switch) findViewById(R.id.interception_enabled);
		boolean emsEnabled = ruleService.getGlobalBoolean(GlobalNames.GLOBAL_INTERCEPTION_ACTIVE);
		interceptionEnabled.setChecked(emsEnabled);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.add_rule:
				goToRuleEditActivity(null);
				break;
			case R.id.interception_enabled:
				toggleInterceptionEnable();
				break;
		}
	}

	private void toggleInterceptionEnable() {
		Switch interceptionEnabled = (Switch) findViewById(R.id.interception_enabled);
		boolean emsEnabled = interceptionEnabled.isChecked();
		ruleService.updateGlobal(GlobalNames.GLOBAL_INTERCEPTION_ACTIVE, Boolean.toString(emsEnabled));
	}

	private void goToRuleEditActivity(Long ruleId) {
		Intent i = new Intent();
		i.setClassName(Constants.APP_PACKAGE, Constants.ADD_RULE_ACTIVITY);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (ruleId != null) {
			i.putExtra(Constants.RULE_ID, ruleId);
		}
		startActivity(i);
	}

	@Override
	protected void onResume() {
		ruleService = new RuleService(this);
		loadRulesForDisplay();
		super.onResume();
	}

	@Override
	protected void onPause() {
		ruleService.close();
		super.onPause();
	}

}
