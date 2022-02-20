package com.canalbrewing.abcdatacollector.controller;

import java.sql.SQLException;
import java.util.List;

import com.canalbrewing.abcdatacollector.business.UserBusiness;
import com.canalbrewing.abcdatacollector.exception.AbcDataCollectorException;
import com.canalbrewing.abcdatacollector.model.RequestUser;
import com.canalbrewing.abcdatacollector.model.Observed;
import com.canalbrewing.abcdatacollector.model.StatusMessage;
import com.canalbrewing.abcdatacollector.model.User;
import com.canalbrewing.abcdatacollector.security.IAuthenticationFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	UserBusiness userBusiness;

	@Autowired
	private IAuthenticationFacade authenticationFacade;

	@PutMapping(value = "/signOut")
	public ResponseEntity<StatusMessage> signOut()
			throws SQLException, AbcDataCollectorException {
		userBusiness.signOutUser(authenticationFacade.getUserSession());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "User Signed Out"), HttpStatus.OK);
	}

	@PutMapping(value = "/settings")
	public ResponseEntity<StatusMessage> updateSettings(@RequestBody RequestUser requestUser)
			throws SQLException, AbcDataCollectorException {

		User user = new User();
		user.setId(authenticationFacade.getUserId());
		user.setEmail(requestUser.getEmail());
		user.setStartPage(requestUser.getStartPage());
		user.setStatus(User.STATUS_ACTIVE);

		userBusiness.updateUser(user);

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Settings updated"), HttpStatus.OK);
	}

	@PutMapping(value = "/password")
	public ResponseEntity<StatusMessage> updatePassword(@RequestBody RequestUser requestUser)
			throws SQLException, AbcDataCollectorException {

		userBusiness.updatePassword(authenticationFacade.getUserId(), requestUser.getCurrentPassword(),
				requestUser.getPassword());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Password updated"), HttpStatus.OK);
	}

	@PutMapping(value = "/username")
	public ResponseEntity<StatusMessage> updateUsername(@RequestBody RequestUser requestUser)
			throws SQLException, AbcDataCollectorException {

		userBusiness.updateUsername(authenticationFacade.getUserId(), requestUser.getUsername());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Username updated"), HttpStatus.OK);
	}

	@PutMapping("/observed")
	public ResponseEntity<Observed> updateObserved(@RequestBody RequestUser requestUser) throws SQLException {

		Observed observed = userBusiness.updateObserved(authenticationFacade.getUserId(), requestUser.getObservedId(),
				requestUser.getObservedName(), requestUser.getRole(), requestUser.getRelationship());

		return new ResponseEntity<>(observed, HttpStatus.OK);
	}

	@PostMapping("/observed")
	public ResponseEntity<Observed> addObserved(@RequestBody RequestUser requestUser)
			throws SQLException, AbcDataCollectorException {

		Observed observed = userBusiness.addObserved(authenticationFacade.getUserId(), requestUser.getObservedName(),
				requestUser.getRelationship());

		return new ResponseEntity<>(observed, HttpStatus.OK);
	}

	@GetMapping("/observers/{observedId}")
	public ResponseEntity<List<Observed>> getObservedUsers(@PathVariable String observedId) throws SQLException {

		return new ResponseEntity<>(userBusiness.getUsersByObserved(observedId), HttpStatus.OK);
	}

	@PostMapping("/access/{observedId}")
	public ResponseEntity<StatusMessage> grantAccess(@PathVariable String observedId,
			@RequestBody RequestUser requestUser)
			throws SQLException, AbcDataCollectorException {
		userBusiness.grantAccess(observedId, requestUser.getEmail(), requestUser.getRole(),
				requestUser.getRelationship());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Access granted"), HttpStatus.OK);
	}

	@DeleteMapping("/{observedUserId}/access/{observedId}")
	public ResponseEntity<StatusMessage> removeAccess(@PathVariable String observedUserId,
			@PathVariable String observedId)
			throws SQLException {
		userBusiness.deleteObservers(authenticationFacade.getUserId(), observedUserId, observedId);

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Access removed"), HttpStatus.OK);
	}

	@PostMapping("/reassignment/{observedId}")
	public ResponseEntity<StatusMessage> requestReassign(@PathVariable String observedId,
			@RequestBody RequestUser requestUser)
			throws SQLException, AbcDataCollectorException {

		String message = userBusiness.requestReassign(authenticationFacade.getUserId(), observedId,
				requestUser.getEmail());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, message), HttpStatus.OK);
	}

	@DeleteMapping("/remove")
	public ResponseEntity<StatusMessage> removeMe()
			throws SQLException, AbcDataCollectorException {

		userBusiness.deleteUser(authenticationFacade.getUserId());

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "User removed"), HttpStatus.OK);
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