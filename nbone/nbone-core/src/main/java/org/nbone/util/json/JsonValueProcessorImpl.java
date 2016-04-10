package org.nbone.util.json;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonValueProcessorImpl implements  JsonValueProcessor{


	public JsonValueProcessorImpl() {
		format = "yyyy-MM-dd HH:mm:ss";
	}

	public JsonValueProcessorImpl(String format) {
		this.format = "yyyy-MM-dd HH:mm:ss";
		this.format = format;
	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		String obj[] = new String[0];
		if (value instanceof Date[]) {
			SimpleDateFormat sf = new SimpleDateFormat(format);
			Date dates[] = (Date[]) (Date[]) value;
			obj = new String[dates.length];
			for (int i = 0; i < dates.length; i++)
				obj[i] = sf.format(dates[i]);

		}
		if (value instanceof Timestamp[]) {
			SimpleDateFormat sf = new SimpleDateFormat(format);
			Timestamp dates[] = (Timestamp[]) (Timestamp[]) value;
			obj = new String[dates.length];
			for (int i = 0; i < dates.length; i++)
				obj[i] = sf.format(dates[i]);

		}
		if (value instanceof java.sql.Date[]) {
			SimpleDateFormat sf = new SimpleDateFormat(format);
			java.sql.Date dates[] = (java.sql.Date[]) (java.sql.Date[]) value;
			obj = new String[dates.length];
			for (int i = 0; i < dates.length; i++)
				obj[i] = sf.format(dates[i]);

		}
		return obj;
	}

	public Object processObjectValue(String key, Object value,JsonConfig jsonConfig) {
		if (value == null)
			return "";
		if (value instanceof Timestamp) {
			String str = (new SimpleDateFormat(format)).format((Timestamp) value);
			return str;
		}
		if (value instanceof Date) {
			String str = (new SimpleDateFormat(format)).format((Date) value);
			return str;
		}
		if (value instanceof java.sql.Date) {
			String str = (new SimpleDateFormat(format)).format((java.sql.Date) value);
			return str;
		} else {
			return String.valueOf(value);
		}
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	private String format;



}
