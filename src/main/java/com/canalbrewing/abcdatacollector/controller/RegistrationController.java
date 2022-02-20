package com.canalbrewing.abcdatacollector.controller;

import java.sql.SQLException;

import com.canalbrewing.abcdatacollector.business.UserBusiness;
import com.canalbrewing.abcdatacollector.exception.AbcDataCollectorException;
import com.canalbrewing.abcdatacollector.model.RequestUser;
import com.canalbrewing.abcdatacollector.model.StatusMessage;
import com.canalbrewing.abcdatacollector.model.User;
import com.canalbrewing.abcdatacollector.model.Verification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

	@Autowired
	UserBusiness userBusiness;

	@PostMapping("/registration")
	public ResponseEntity<StatusMessage> register(@RequestBody RequestUser requestUser)
			throws SQLException, AbcDataCollectorException {
		User user = requestUser.convertToUser();

		userBusiness.register(user);

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS,
				"Confirmation email sent. Check your email for the confirm registration link."), HttpStatus.OK);
	}

	@GetMapping("/registration")
	public ResponseEntity<Verification> getRegisterVerification(
			@RequestParam(value = "key", required = true) String key) throws SQLException {
		Verification verification = userBusiness.getVerification(key);

		return new ResponseEntity<>(verification, HttpStatus.OK);
	}

	@PutMapping("/registration")
	public ResponseEntity<StatusMessage> confirmRegister(@RequestBody RequestUser requestUser)
			throws SQLException, AbcDataCollectorException {

		userBusiness.confirmRegister(requestUser.getKey());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Confirmation successful."),
				HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<StatusMessage> handleException(Exception ex) {
		return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<StatusMessage> handleSQLException(SQLException ex) {
		ex.printStackTrace();
		return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AbcDataCollectorException.class)
	public ResponseEntity<StatusMessage> handleAbcDataCollectorException(AbcDataCollectorException ex) {
		switch (ex.getExceptionCode()) {
			case AbcDataCollectorException.INVALID_LOGIN:
				return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
						HttpStatus.UNAUTHORIZED);
			case AbcDataCollectorException.NOT_FOUND:
				return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
						HttpStatus.NOT_FOUND);
			case AbcDataCollectorException.DUPLICATE:
				return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()), HttpStatus.FOUND);
			default:
				return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}