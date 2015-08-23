package com;

import java.util.HashSet;
import java.util.Set;

public class ItemSet {

	private Set<String> items = new HashSet<String>();
	private int supportCount;
	
	public ItemSet() {}
	
	public ItemSet(Set<String> items) {
		this.items = items;
	}
	
	public Set<String> getItems() {
		return items;
	}
	public void setItems(Set<String> items) {
		this.items = items;
	}
	public int getSupportCount() {
		return supportCount;
	}
	public void setSupportCount(int supportCount) {
		this.supportCount = supportCount;
	}
	

}
