package com.utils;

import java.util.List;

import android.R.bool;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

public class WifiAdmin
{
	private WifiManager mWifiManager;

	public WifiManager getmWifiManager()
	{
		return mWifiManager;
	}

	private WifiInfo mWifiInfo;
	private List<ScanResult> mWifiList;
	private List<WifiConfiguration> mWifiConfiguration;
	WifiLock mWifiLock;

	public WifiAdmin(Context context)
	{
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
		mWifiConfiguration = mWifiManager.getConfiguredNetworks();
	}

	public void openWifi()
	{
		if (!mWifiManager.isWifiEnabled())
		{
			mWifiManager.setWifiEnabled(true);
		}
	}

	public void closeWifi()
	{
		if (mWifiManager.isWifiEnabled())
		{
			mWifiManager.setWifiEnabled(false);
		}
	}

	public int checkState()
	{
		return mWifiManager.getWifiState();
	}

	public void acquireWifiLock()
	{
		mWifiLock.acquire();
	}

	public void creatWifiLock()
	{
		mWifiLock = mWifiManager.createWifiLock("Test");
	}

	public void releaseWifiLock()
	{
		mWifiLock.release();
	}

	public List<WifiConfiguration> getConfiguration()
	{
		return mWifiConfiguration;
	}

	public void connectConfiguration(int index)
	{
		if (index > mWifiConfiguration.size())
		{
			return;
		}
		mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);
	}

	public void startScan()
	{
		mWifiManager.startScan();
	}

	public List<ScanResult> getWifiList()
	{
		mWifiList = mWifiManager.getScanResults();
		return mWifiList;
	}

	public StringBuilder lookUpScan()
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < mWifiList.size(); i++)
		{
			stringBuilder.append("Index_" + new Integer(i + 1).toString() + ":");
			stringBuilder.append((mWifiList.get(i)).toString());
			stringBuilder.append("/n");
		}
		return stringBuilder;
	}

	public String getMacAddress()
	{
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	public String getBSSID()
	{
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	public int getIPAddress()
	{
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	public int getNetworkId()
	{
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	public String getWifiInfo()
	{
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}

	public void addNetwork(WifiConfiguration wcg)
	{
		int wcgID = mWifiManager.addNetwork(wcg);
		boolean b = mWifiManager.enableNetwork(wcgID, true);
	}

	public void disconnectWifi(int netId)
	{
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}

	public String getSSID()
	{
		return mWifiInfo.getSSID();
	}

	public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type)
	{
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";
		config.priority = 1000;
		if (Type == 1) // WIFICIPHER_NOPASS
		{
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		}
		if (Type == 2) // WIFICIPHER_WEP
		{
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 3) // WIFICIPHER_WPA
		{
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}
		return config;

	}

	public WifiConfiguration IsExsits(String SSID)
	{
		List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs)
		{
			if (existingConfig.SSID.equals("\"" + SSID + "\""))
			{
				return existingConfig;
			}
		}
		return null;

	}

	public ScanResult IsConnect(String SSID)
	{
		List<ScanResult> connectingConfigs = mWifiManager.getScanResults();
		for (ScanResult connectingConfig : connectingConfigs)
		{
			if (connectingConfig.SSID.equals("\"" + SSID + "\""))
			{
				return connectingConfig;
			}
		}
		return null;

	}

	public boolean isWificonnect(String SSID)
	{
		if (this.getSSID().equals(SSID))
			return true;
		else
			return false;

	}

}
