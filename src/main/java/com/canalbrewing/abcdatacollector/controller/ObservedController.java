package com.canalbrewing.abcdatacollector.controller;

import java.sql.SQLException;
import java.util.List;

import com.canalbrewing.abcdatacollector.business.ObservedBusiness;
import com.canalbrewing.abcdatacollector.business.UserBusiness;
import com.canalbrewing.abcdatacollector.exception.AbcDataCollectorException;
import com.canalbrewing.abcdatacollector.model.Abc;
import com.canalbrewing.abcdatacollector.model.Incident;
import com.canalbrewing.abcdatacollector.model.Observed;
import com.canalbrewing.abcdatacollector.model.RequestAbc;
import com.canalbrewing.abcdatacollector.model.StatusMessage;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ObservedController {

	@Autowired
	UserBusiness userBusiness;

	@Autowired
	ObservedBusiness observedBusiness;

	@Autowired
	private IAuthenticationFacade authenticationFacade;

	@GetMapping("/observed")
	public ResponseEntity<List<Observed>> getUserObserved()
			throws SQLException {
		List<Observed> observedList = observedBusiness.getObservedByUser(authenticationFacade.getUserId());

		return new ResponseEntity<>(observedList, HttpStatus.OK);
	}

	@GetMapping("/observed/{observedId}")
	public ResponseEntity<Observed> getObserved(@PathVariable String observedId)
			throws SQLException, AbcDataCollectorException {
		Observed observed = observedBusiness.getObserved(authenticationFacade.getUserId(), observedId);

		return new ResponseEntity<>(observed, HttpStatus.OK);
	}

	@GetMapping("/observed/{observedId}/incidents")
	public ResponseEntity<List<Incident>> getObservedIncidents(@PathVariable String observedId,
			@RequestParam(value = "start", required = false) String incidentStartDt) throws SQLException {

		return new ResponseEntity<>(observedBusiness.getIncidentsByObserved(observedId, incidentStartDt),
				HttpStatus.OK);
	}

	@GetMapping("/observed/{observedId}/incidents/{incidentId}")
	public ResponseEntity<Incident> getObservedIncident(@PathVariable String observedId,
			@PathVariable String incidentId)
			throws SQLException {

		return new ResponseEntity<>(observedBusiness.getIncidentById(observedId, incidentId), HttpStatus.OK);
	}

	@PostMapping("/observed/{observedId}/abc")
	public ResponseEntity<Abc> addObservedABC(@PathVariable String observedId, @RequestBody RequestAbc requestAbc)
			throws SQLException {

		return new ResponseEntity<>(observedBusiness.addObservedABC(observedId, requestAbc), HttpStatus.OK);
	}

	@PutMapping("/observed/{observedId}/abc")
	public ResponseEntity<Abc> updateObservedABC(@PathVariable String observedId, @RequestBody RequestAbc requestAbc)
			throws SQLException {

		return new ResponseEntity<>(observedBusiness.updateObservedABC(observedId, requestAbc), HttpStatus.OK);
	}

	@DeleteMapping("/observed/{observedId}")
	public ResponseEntity<Observed> deleteObserved(@PathVariable String observedId) throws SQLException {

		Observed observed = observedBusiness.deleteObserved(observedId);

		return new ResponseEntity<>(observed, HttpStatus.OK);
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

		if (ex.getExceptionCode() == AbcDataCollectorException.NOT_FOUND) {
			return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(new StatusMessage(StatusMessage.ERROR, ex.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

}