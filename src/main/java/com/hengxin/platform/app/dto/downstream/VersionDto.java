package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/**
 * VersionDto.
 *
 */
public class VersionDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String androidAppVersion;

	private String androidAppDownloadUrl;

	private String iosAppVersion;

	private String iosAppDownloadUrl;

	public String getAndroidAppVersion() {
		return androidAppVersion;
	}

	public void setAndroidAppVersion(String androidAppVersion) {
		this.androidAppVersion = androidAppVersion;
	}

	public String getAndroidAppDownloadUrl() {
		return androidAppDownloadUrl;
	}

	public void setAndroidAppDownloadUrl(String androidAppDownloadUrl) {
		this.androidAppDownloadUrl = androidAppDownloadUrl;
	}

	public String getIosAppVersion() {
		return iosAppVersion;
	}

	public void setIosAppVersion(String iosAppVersion) {
		this.iosAppVersion = iosAppVersion;
	}

	public String getIosAppDownloadUrl() {
		return iosAppDownloadUrl;
	}

	public void setIosAppDownloadUrl(String iosAppDownloadUrl) {
		this.iosAppDownloadUrl = iosAppDownloadUrl;
	}

}
