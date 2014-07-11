package cn.bmob.naruto.model;

import java.util.ArrayList;
import java.util.List;

public class PictureData {

	private PictureType pictureType = null;
	private ArrayList<PictureItem> pictureItems;
	private int showPosition = 0;
	private int loadPageNumber = 0;

	public PictureData(PictureType type) {
		pictureType = type;
		pictureItems = new ArrayList<PictureItem>();
	}

	public PictureType getPictureType() {
		return pictureType;
	}

	public void setPictureType(PictureType pictureType) {
		this.pictureType = pictureType;
	}

	public void addPictureItems(List<PictureItem> data) {
		if (data != null) {
			for (PictureItem item : data)
				pictureItems.add(item);
		}
	}

	public ArrayList<PictureItem> getPictureItems() {
		return pictureItems;
	}

	public void setPictureItems(ArrayList<PictureItem> pictureItems) {
		this.pictureItems = pictureItems;
	}

	public int getShowPosition() {
		return showPosition;
	}

	public void setShowPosition(int showPosition) {
		this.showPosition = showPosition;
	}

	public int getLoadPageNumber() {
		return loadPageNumber;
	}

	public void setLoadPageNumber(int loadPageNumber) {
		this.loadPageNumber = loadPageNumber;
	}

}
