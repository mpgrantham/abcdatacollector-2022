package com.canalbrewing.abcdatacollector.controller;

import java.sql.SQLException;
import java.util.List;

import com.canalbrewing.abcdatacollector.business.IncidentBusiness;
import com.canalbrewing.abcdatacollector.business.UserBusiness;
import com.canalbrewing.abcdatacollector.model.Intensity;
import com.canalbrewing.abcdatacollector.model.Relationship;
import com.canalbrewing.abcdatacollector.model.StatusMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("lookup")
public class LookupController {

	@Autowired
	UserBusiness userBusiness;

	@Autowired
	IncidentBusiness incidentBusiness;

	@GetMapping("/relationships")
	public ResponseEntity<List<Relationship>> getRelationships() throws SQLException {
		return new ResponseEntity<>(userBusiness.getRelationships(), HttpStatus.OK);
	}

	@GetMapping("/intensities")
	public ResponseEntity<List<Intensity>> getIntensities() throws SQLException {
		List<Intensity> intensities = incidentBusiness.getIntensities();

		return new ResponseEntity<>(intensities, HttpStatus.OK);
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

}