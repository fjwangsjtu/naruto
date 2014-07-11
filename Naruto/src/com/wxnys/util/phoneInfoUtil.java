package com.wxnys.util;

import java.net.InetAddress;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class phoneInfoUtil {

	public static String getAppId() {

		return "00000";
	}

	// client ip
	public static String getHostIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr
						.hasMoreElements();) {
					InetAddress inetAddress = ipAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
		} catch (Exception e) {
		}
		return null;
	}

	public static int getVersionCode(Context aContext,String packagename) {
		int versionCode = 0;
		try {
			PackageInfo packageInfo = aContext.getPackageManager()
					.getPackageInfo(packagename, 0);
			versionCode = packageInfo.versionCode;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	public static String getVersionName(Context aContext,String packagename) {
		String versionName = null;
		try {
			PackageInfo packageInfo = aContext.getPackageManager()
					.getPackageInfo(packagename, 0);
			versionName = packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	public static boolean isNetWorkActive(Context aContext) {
		ConnectivityManager cm = (ConnectivityManager) aContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = cm.getActiveNetworkInfo();
		if (networkinfo != null && networkinfo.isConnected()
				&& networkinfo.isAvailable()) {
			int type = networkinfo.getType();
			if (type == ConnectivityManager.TYPE_MOBILE
					|| type == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;

	}
}
