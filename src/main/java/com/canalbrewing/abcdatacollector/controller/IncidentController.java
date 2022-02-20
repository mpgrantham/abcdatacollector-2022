package com.canalbrewing.abcdatacollector.controller;

import java.sql.SQLException;

import com.canalbrewing.abcdatacollector.business.IncidentBusiness;
import com.canalbrewing.abcdatacollector.business.UserBusiness;
import com.canalbrewing.abcdatacollector.exception.AbcDataCollectorException;
import com.canalbrewing.abcdatacollector.model.StatusMessage;
import com.canalbrewing.abcdatacollector.model.Incident;
import com.canalbrewing.abcdatacollector.model.RequestIncident;
import com.canalbrewing.abcdatacollector.security.IAuthenticationFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IncidentController {

	@Autowired
	UserBusiness userBusiness;

	@Autowired
	IncidentBusiness incidentBusiness;

	@Autowired
	private IAuthenticationFacade authenticationFacade;

	@PostMapping("/incident")
	public ResponseEntity<Incident> addIncident(@RequestBody RequestIncident requestIncident)
			throws SQLException, AbcDataCollectorException {

		int userId = authenticationFacade.getUserId();

		incidentBusiness.checkEntryPermission(userId, requestIncident.getObservedId());

		Incident incident = incidentBusiness.saveIncident(userId, requestIncident);

		return new ResponseEntity<>(incident, HttpStatus.OK);
	}

	@PutMapping("/incident")
	public ResponseEntity<Incident> updateIncident(@RequestBody RequestIncident requestIncident)
			throws SQLException, AbcDataCollectorException {
		int userId = authenticationFacade.getUserId();

		incidentBusiness.checkEntryPermission(userId, requestIncident.getObservedId());

		Incident incident = incidentBusiness.updateIncident(userId, requestIncident);

		return new ResponseEntity<>(incident, HttpStatus.OK);
	}

	@DeleteMapping("/incident/{incidentId}/observed/{observedId}")
	public ResponseEntity<StatusMessage> deleteIncident(
			@PathVariable Integer observedId,
			@PathVariable Integer incidentId) throws SQLException, AbcDataCollectorException {
		incidentBusiness.checkAdminPermission(authenticationFacade.getUserId(), observedId);

		incidentBusiness.deleteIncident(incidentId);

		return new ResponseEntity<>(new StatusMessage(StatusMessage.SUCCESS, "Incident deleted"), HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<StatusMessage> handleException(Exception ex) {
		return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<StatusMessage> handleSQLException(SQLException ex) {
		return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AbcDataCollectorException.class)
	public ResponseEntity<StatusMessage> handleAbcDataCollectorException(AbcDataCollectorException ex) {
		switch (ex.getExceptionCode()) {
			case AbcDataCollectorException.INVALID_LOGIN:
			case AbcDataCollectorException.UNAUTHORIZED:
				return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
						HttpStatus.UNAUTHORIZED);
			case AbcDataCollectorException.NOT_FOUND:
				return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
						HttpStatus.NOT_FOUND);
			default:
				return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}