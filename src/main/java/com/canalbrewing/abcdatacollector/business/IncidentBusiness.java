package com.canalbrewing.abcdatacollector.business;

import java.sql.SQLException;
import java.util.List;

import com.canalbrewing.abcdatacollector.exception.AbcDataCollectorException;
import com.canalbrewing.abcdatacollector.model.Incident;
import com.canalbrewing.abcdatacollector.model.RequestIncident;
import com.canalbrewing.abcdatacollector.model.Intensity;

import org.springframework.stereotype.Component;

@Component
public interface IncidentBusiness {

    List<Intensity> getIntensities() throws SQLException;

    Incident saveIncident(int userId, RequestIncident incidentEntry) throws SQLException;

    Incident updateIncident(int userId, RequestIncident incidentEntry) throws SQLException;

    void deleteIncident(int incidentId) throws SQLException, AbcDataCollectorException;

    void checkEntryPermission(int userId, int observedId) throws SQLException, AbcDataCollectorException;

    void checkAdminPermission(int userId, int observedId) throws SQLException, AbcDataCollectorException;

}