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
public class AuthenticateController {

	@Autowired
	UserBusiness userBusiness;

	@PostMapping("/authentication")
	public ResponseEntity<User> signIn(@RequestBody RequestUser requestUser)
			throws SQLException, AbcDataCollectorException {
		User user = userBusiness.signInUser(requestUser.getUsername(), requestUser.getPassword(),
				requestUser.isStaySignedIn());

		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PostMapping("/authentication/key")
	public ResponseEntity<User> signInWithKey(@RequestBody RequestUser requestUser)
			throws SQLException, AbcDataCollectorException {
		User user = userBusiness.signInUser(requestUser.getKey());

		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/authentication/usernamePassword")
	public Object sendUsernamePassword(@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "forgotType", required = true) String forgotType) throws AbcDataCollectorException {
		return userBusiness.sendUsernameOrPassword(email, forgotType.toUpperCase());
	}

	@GetMapping("/authentication/resetKey")
	public ResponseEntity<StatusMessage> checkResetKey(@RequestParam(value = "key", required = true) String key)
			throws SQLException, AbcDataCollectorException {
		userBusiness.getUserByResetKey(key);
		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Provide new Password"), HttpStatus.OK);
	}

	@PostMapping("/authentication/password")
	public ResponseEntity<StatusMessage> saveResetPassword(@RequestBody RequestUser requestUser)
			throws SQLException, AbcDataCollectorException {

		userBusiness.resetPassword(requestUser.getKey(), requestUser.getPassword());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Password Reset successful"),
				HttpStatus.OK);
	}

	@GetMapping("/authentication/reassignment")
	public ResponseEntity<Verification> getVerification(@RequestParam(value = "key", required = true) String key)
			throws SQLException, AbcDataCollectorException {
		Verification verification = userBusiness.getReassignVerification(key);

		return new ResponseEntity<>(verification, HttpStatus.OK);
	}

	@PutMapping("/authentication/reassignment")
	public ResponseEntity<StatusMessage> reassignObserved(@RequestBody RequestUser requestUser)
			throws SQLException, AbcDataCollectorException {

		userBusiness.reassignObserved(requestUser.getKey(), requestUser.getRelationship());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Observed reassigned"), HttpStatus.OK);
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