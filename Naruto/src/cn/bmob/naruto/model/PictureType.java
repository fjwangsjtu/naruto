package cn.bmob.naruto.model;

import cn.bmob.v3.BmobObject;

public class PictureType extends BmobObject {

	private String name;
	private String url;
	private int love;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "PictureType [name=" + name + "]";
	}

}
