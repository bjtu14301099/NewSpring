package test;

import java.util.ArrayList;
import java.util.List;

import bean.Property;

public class Bean{
	private String id;
	private String classname;
	private List<Property> properties = new ArrayList<>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Bean [id=" + id + ", classname=" + classname + ", properties=" + properties + "]";
	}
	public String getClassName() {
		return classname;
	}
	public void setClassName(String classname) {
		this.classname = classname;
	}
	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
}
