package com.jnrcorp.ems.sqllite.service;

import java.util.List;

import android.content.Context;

import com.jnrcorp.ems.sqllite.EMSDataSource;
import com.jnrcorp.ems.sqllite.GlobalsDAO.GlobalNames;
import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;
import com.jnrcorp.ems.sqllite.model.RuleFilter;
import com.jnrcorp.ems.sqllite.model.RuleTime;
import com.jnrcorp.ems.sqllite.model.RuleTimeDay;

public class RuleService {

	private EMSDataSource emsDataSource;

	public RuleService(Context context) {
		super();
		this.emsDataSource = new EMSDataSource(context);
		this.emsDataSource.open();
	}

	public void close() {
		emsDataSource.close();
	}

	public List<EmergencyMessageServiceRule> loadAllRules() {
		List<EmergencyMessageServiceRule> emsRules = emsDataSource.getRulesDAO().getAllEmergencyMessageServiceRules();
		for (EmergencyMessageServiceRule emsRule : emsRules) {
			loadRuleFilters(emsRule);
			loadRuleTimes(emsRule);
		}
		return emsRules;
	}

	private void loadRuleFilters(EmergencyMessageServiceRule emsRule) {
		List<RuleFilter> ruleFilters = emsDataSource.getRuleFiltersDAO().getAllRuleFilters(emsRule.getRuleId());
		emsRule.setRuleFilters(ruleFilters);
	}

	private void loadRuleTimes(EmergencyMessageServiceRule emsRule) {
		List<RuleTime> ruleTimes = emsDataSource.getRuleTimesDAO().getAllRuleTimes(emsRule.getRuleId());
		for (RuleTime ruleTime : ruleTimes) {
			List<RuleTimeDay> ruleTimeDays = emsDataSource.getRuleTimeDaysDAO().getAllRuleTimeDays(ruleTime.getTimeId());
			ruleTime.setRuleTimeDays(ruleTimeDays);
		}
		emsRule.setRuleTimes(ruleTimes);
	}

	public EmergencyMessageServiceRule loadRule(long emsRuleId) {
		EmergencyMessageServiceRule emsRule = emsDataSource.getRulesDAO().getEmergencyMessageServiceRule(emsRuleId);
		loadRuleFilters(emsRule);
		loadRuleTimes(emsRule);
		return emsRule;
	}

	public void saveRule(EmergencyMessageServiceRule emsRule, List<RuleFilter> ruleFiltersToRemove, List<RuleTime> ruleTimesToRemove) {
		EmergencyMessageServiceRule emsRuleSaved = emsDataSource.getRulesDAO().createRule(emsRule);
		emsRuleSaved.setRuleFilters(emsRule.getRuleFilters());
		saveRuleFilters(emsRuleSaved, ruleFiltersToRemove);
		emsRuleSaved.setRuleTimes(emsRule.getRuleTimes());
		saveRuleTimes(emsRuleSaved, ruleTimesToRemove);
	}

	private void saveRuleFilters(EmergencyMessageServiceRule emsRule, List<RuleFilter> ruleFiltersToRemove) {
		if (emsRule.getRuleFilters() != null) {
			for(RuleFilter ruleFilter : emsRule.getRuleFilters()) {
				ruleFilter.setRuleId(emsRule.getRuleId());
				emsDataSource.getRuleFiltersDAO().createRuleFilter(ruleFilter);
			}
		}
		for (RuleFilter ruleFilterToRemove : ruleFiltersToRemove) {
			deleteRuleFilter(ruleFilterToRemove);
		}
	}

	private void deleteRuleFilter(RuleFilter ruleFilterToRemove) {
		emsDataSource.getRuleFiltersDAO().deleteRuleFilter(ruleFilterToRemove);
	}

	private void saveRuleTimes(EmergencyMessageServiceRule emsRule, List<RuleTime> ruleTimesToRemove) {
		if (emsRule.getRuleTimes() != null) {
			for (RuleTime ruleTime : emsRule.getRuleTimes()) {
				ruleTime.setRuleId(emsRule.getRuleId());
				RuleTime ruleTimeSaved = emsDataSource.getRuleTimesDAO().createRuleTime(ruleTime);
				ruleTimeSaved.setRuleTimeDays(ruleTime.getRuleTimeDays());
				saveRuleTimeDays(ruleTimeSaved);
			}
		}
		for (RuleTime ruleTimeToRemove : ruleTimesToRemove) {
			deleteRuleTime(ruleTimeToRemove);
		}
	}

	private void saveRuleTimeDays(RuleTime ruleTime) {
		deleteRuleTimeDays(ruleTime);
		if (ruleTime.getRuleTimeDays() != null) {
			for (RuleTimeDay ruleTimeDay : ruleTime.getRuleTimeDays()) {
				ruleTimeDay.setTimeId(ruleTime.getTimeId());
				emsDataSource.getRuleTimeDaysDAO().createRuleTimeDay(ruleTimeDay);
			}
		}
	}

	public void deleteRule(EmergencyMessageServiceRule emsRule) {
		emsDataSource.getRulesDAO().deleteRule(emsRule);
		deleteRuleTimes(emsRule);
	}

	private void deleteRuleTimes(EmergencyMessageServiceRule emsRule) {
		if (emsRule.getRuleTimes() != null) {
			for (RuleTime ruleTime : emsRule.getRuleTimes()) {
				emsDataSource.getRuleTimesDAO().deleteRuleTime(ruleTime);
				deleteRuleTimeDays(ruleTime);
			}
		}
	}

	private void deleteRuleTimeDays(RuleTime ruleTime) {
		emsDataSource.getRuleTimeDaysDAO().deleteRuleTimeDays(ruleTime);
	}

	private void deleteRuleTime(RuleTime ruleTime) {
		deleteRuleTimeDays(ruleTime);
		emsDataSource.getRuleTimesDAO().deleteRuleTime(ruleTime);
	}

	public boolean getGlobalBoolean(GlobalNames globalName) {
		return emsDataSource.getGlobalsDAO().getGlobalBoolean(globalName);
	}

	public void updateGlobal(GlobalNames globalName, String toStore) {
		emsDataSource.getGlobalsDAO().updateGlobal(globalName, toStore);
	}

}
