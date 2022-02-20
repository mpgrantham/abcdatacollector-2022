package com.canalbrewing.abcdatacollector.business;

import javax.mail.PasswordAuthentication;

public class AbcDataCollectorAuthenticator extends javax.mail.Authenticator {
	private PasswordAuthentication authentication;

	public AbcDataCollectorAuthenticator(String username, String password) {
		authentication = new PasswordAuthentication(username, password);
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return authentication;
	}
}