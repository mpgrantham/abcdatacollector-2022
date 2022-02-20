package com.canalbrewing.abcdatacollector.dal;

import java.util.List;
import java.sql.SQLException;

import com.canalbrewing.abcdatacollector.model.Incident;
import com.canalbrewing.abcdatacollector.model.Intensity;

import org.springframework.stereotype.Component;

@Component
public interface IncidentDao {

	List<Intensity> getIntensities() throws SQLException;

	void insertIncident(Incident incident) throws SQLException;

	void updateIncident(Incident incident) throws SQLException;

	void deleteIncident(int incidentId) throws SQLException;

}