package com.jnrcorp.ems.rule.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.jnrcorp.ems.model.MessageContext;
import com.jnrcorp.ems.rule.validator.RuleFilterValidator;
import com.jnrcorp.ems.rule.validator.RuleFilterValidatorContains;
import com.jnrcorp.ems.rule.validator.RuleFilterValidatorEndsWith;
import com.jnrcorp.ems.rule.validator.RuleFilterValidatorEquals;
import com.jnrcorp.ems.rule.validator.RuleFilterValidatorNumberRange;
import com.jnrcorp.ems.rule.validator.RuleFilterValidatorOfLength;
import com.jnrcorp.ems.rule.validator.RuleFilterValidatorStartsWith;
import com.jnrcorp.ems.sqllite.GlobalsDAO.GlobalNames;
import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;
import com.jnrcorp.ems.sqllite.model.RuleFilter;
import com.jnrcorp.ems.sqllite.model.RuleType;
import com.jnrcorp.ems.sqllite.service.RuleService;
import com.jnrcorp.ems.util.RuleTimeUtil;

public class EMSRuleFactory {


	private RuleService ruleService;
	private MessageContext messageContext;
	private static Map<RuleType, RuleFilterValidator> ruleValidatorsByRuleType = new HashMap<RuleType, RuleFilterValidator>();

	static {
		ruleValidatorsByRuleType.put(RuleFilterValidatorContains.getRuleType(), new RuleFilterValidatorContains());
		ruleValidatorsByRuleType.put(RuleFilterValidatorStartsWith.getRuleType(), new RuleFilterValidatorStartsWith());
		ruleValidatorsByRuleType.put(RuleFilterValidatorEndsWith.getRuleType(), new RuleFilterValidatorEndsWith());
		ruleValidatorsByRuleType.put(RuleFilterValidatorEquals.getRuleType(), new RuleFilterValidatorEquals());
		ruleValidatorsByRuleType.put(RuleFilterValidatorOfLength.getRuleType(), new RuleFilterValidatorOfLength());
		ruleValidatorsByRuleType.put(RuleFilterValidatorNumberRange.getRuleType(), new RuleFilterValidatorNumberRange());
	}

	public EMSRuleFactory(Context context, MessageContext messageContext) {
		this.ruleService = new RuleService(context);
		this.messageContext = messageContext;
	}

	public EmergencyMessageServiceRule execute() {
		Boolean emsEnabled = ruleService.getGlobalBoolean(GlobalNames.GLOBAL_INTERCEPTION_ACTIVE);
		EmergencyMessageServiceRule emsRule = null;
		if (emsEnabled != null && emsEnabled.booleanValue()) {
			emsRule = processRule();
		}
		ruleService.close();
		return emsRule;
	}

	private EmergencyMessageServiceRule processRule() {
		List<EmergencyMessageServiceRule> emsRules = ruleService.loadAllRules();
		for (EmergencyMessageServiceRule emsRule : emsRules) {
			if (emsRule.isEnabled()) {
				String fromPhoneNumber = messageContext.getSenderPhoneNumber();
				for (RuleFilter ruleFilter : emsRule.getRuleFilters()) {
					RuleFilterValidator ruleExecutor = ruleValidatorsByRuleType.get(ruleFilter.getRuleType());
					if (RuleTimeUtil.fitsSchedule(emsRule) && ruleExecutor.isRuleAppropriate(fromPhoneNumber, ruleFilter)) {
						return emsRule;
					}
				}
			}
		}
		return null;
	}

	public static RuleFilterValidator getRuleValidator(RuleType ruleType) {
		return ruleValidatorsByRuleType.get(ruleType);
	}

}
