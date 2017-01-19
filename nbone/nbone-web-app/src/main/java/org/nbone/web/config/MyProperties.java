package org.nbone.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "my")
@EnableConfigurationProperties
public class MyProperties {

	private String secret;
	private String number;
	private String bignumber;
	private String number10;
	private String numberRange;
	
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getBignumber() {
		return bignumber;
	}
	public void setBignumber(String bignumber) {
		this.bignumber = bignumber;
	}
	public String getNumber10() {
		return number10;
	}
	public void setNumber10(String number10) {
		this.number10 = number10;
	}
	public String getNumberRange() {
		return numberRange;
	}
	public void setNumberRange(String numberRange) {
		this.numberRange = numberRange;
	}

}
