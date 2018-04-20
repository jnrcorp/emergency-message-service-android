package com.jnrcorp.ems.util;

public class VersionUpgradeData {

	private int currentVersionCode;
	private int lastVersionCode;
	private boolean isUpgrade;

	public VersionUpgradeData(int currentVersionCode, int lastVersionCode) {
		super();
		this.currentVersionCode = currentVersionCode;
		this.lastVersionCode = lastVersionCode;
		this.isUpgrade = lastVersionCode < currentVersionCode;
	}

	public int getCurrentVersionCode() {
		return currentVersionCode;
	}

	public int getLastVersionCode() {
		return lastVersionCode;
	}

	public boolean isUpgrade() {
		return isUpgrade;
	}

}
