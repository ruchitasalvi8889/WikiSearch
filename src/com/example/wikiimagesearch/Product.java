package com.example.wikiimagesearch;

import java.util.ArrayList;

public class Product {

	int page_id;
	int ns;
	String title;
	int index;
	ArrayList<ThumbNail>thumb_nail;
	public int getPage_id() {
		return page_id;
	}
	public void setPage_id(int page_id) {
		this.page_id = page_id;
	}
	public int getNs() {
		return ns;
	}
	public void setNs(int ns) {
		this.ns = ns;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public ArrayList<ThumbNail> getThumb_nail() {
		return thumb_nail;
	}
	public void setThumb_nail(ArrayList<ThumbNail> thumb_nail) {
		this.thumb_nail = thumb_nail;
	}
}
