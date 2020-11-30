package ro.kuberam.libs.java.pdf.CSS2Java;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.helger.css.propertyvalue.CSSSimpleValueWithUnit;

public class CSS2JavaModel {
	public CSSSimpleValueWithUnit fontSize;
	private HashMap<String, Object> properties = new HashMap<String, Object>();

	public CSS2JavaModel() {
		setProperty("color", new Color(0, 0, 0, 1));
	}

	public void setProperty(String propertyName, Object propertyValue) {
		properties.put(propertyName, propertyValue);
	}

	public Object getProperty(String propertyName) {
		return properties.get(propertyName);
	}

	public HashMap<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			sb.append("property name = " + entry.getKey() + ", property value = " + entry.getValue() + "; ");
		}

		return sb.toString();
	}

}
