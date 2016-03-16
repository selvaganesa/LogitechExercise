package com.logitech.exercise.database;

import java.io.Serializable;

public class Device implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4016377815790892649L;
	private String name;
	private String model;
	private String type;
	
	public Device(){
		
	}
	/**
	 * 
	 * @param name
	 * @param model
	 * @param type
	 */
	public Device(String name, String model, String type) {
		this.name = name;
		this.model = model;
		this.type = type;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModel() {
		return model;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
