package com.app.NAMESPACE.model;

import com.app.NAMESPACE.base.BaseModel;

public class Config extends BaseModel{
	
	// model columns
	public final static String COL_NAME = "name";
	public final static String COL_VALUE = "value";
	
	private String name;
	private String value;
	
	public Config (String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName () {
		return this.name;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public String getValue () {
		return this.value;
	}
	
	public void setValue (String value) {
		this.value = value;
	}
}