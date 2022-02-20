package com.canalbrewing.abcdatacollector.business;

import java.sql.SQLException;
import java.util.List;

import com.canalbrewing.abcdatacollector.exception.AbcDataCollectorException;
import com.canalbrewing.abcdatacollector.model.Abc;
import com.canalbrewing.abcdatacollector.model.Incident;
import com.canalbrewing.abcdatacollector.model.Observed;
import com.canalbrewing.abcdatacollector.model.RequestAbc;

import org.springframework.stereotype.Component;

@Component
public interface ObservedBusiness {

	List<Observed> getObservedByUser(int userId) throws SQLException;

	Observed getObserved(int userId, String observedId) throws SQLException, AbcDataCollectorException;

	List<Incident> getIncidentsByObserved(String observedId, String incidentStartDt) throws SQLException;

	Incident getIncidentById(String observedId, String incidentId) throws SQLException;

	Abc addObservedABC(String observedId, RequestAbc requestAbc) throws SQLException;

	Abc updateObservedABC(String observedId, RequestAbc requestAbc) throws SQLException;

	Observed deleteObserved(String observedId) throws SQLException;
}