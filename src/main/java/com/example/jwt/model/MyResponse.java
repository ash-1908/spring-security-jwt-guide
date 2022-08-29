package com.example.jwt.model;

public class MyResponse {
	private String jwtToken;

	public MyResponse () {

	}

	public MyResponse (String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String getJwtToken () {
		return jwtToken;
	}

	public void setJwtToken (String jwtToken) {
		this.jwtToken = jwtToken;
	}
}
