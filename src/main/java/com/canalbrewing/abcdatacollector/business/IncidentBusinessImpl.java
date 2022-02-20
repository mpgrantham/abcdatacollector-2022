package com.canalbrewing.abcdatacollector.business;

import java.sql.SQLException;
import java.util.Optional;
import java.util.List;

import com.canalbrewing.abcdatacollector.dal.IncidentDao;
import com.canalbrewing.abcdatacollector.dal.ObservedDao;
import com.canalbrewing.abcdatacollector.exception.AbcDataCollectorException;
import com.canalbrewing.abcdatacollector.model.Incident;
import com.canalbrewing.abcdatacollector.model.RequestIncident;
import com.canalbrewing.abcdatacollector.model.Intensity;
import com.canalbrewing.abcdatacollector.model.Observed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IncidentBusinessImpl implements IncidentBusiness {

	@Autowired
	private IncidentDao incidentDao;

	@Autowired
	ObservedDao observedDao;

	public List<Intensity> getIntensities() throws SQLException {
		return incidentDao.getIntensities();
	}

	public Incident saveIncident(int userId, RequestIncident incidentEntry) throws SQLException {

		Incident incident = incidentEntry.convertToIncident();

		incident.setUserId(userId);

		if (incident.getId() == 0) {
			incidentDao.insertIncident(incident);
		} else {
			incidentDao.updateIncident(incident);
		}

		return incident;
	}

	public Incident updateIncident(int userId, RequestIncident incidentEntry) throws SQLException {

		Incident incident = incidentEntry.convertToIncident();
		incident.setUserId(userId);

		incidentDao.updateIncident(incident);

		return incident;
	}

	public void deleteIncident(int incidentId) throws SQLException, AbcDataCollectorException {

		incidentDao.deleteIncident(incidentId);
	}

	public void checkEntryPermission(int userId, int observedId) throws SQLException, AbcDataCollectorException {
		List<Observed> observedList = observedDao.getObservedByUser(userId);

		Optional<Observed> found = observedList.stream().filter(o -> o.getId() == observedId
				&& o.isEntryRole()).findAny();
		if (!found.isPresent()) {
			throw new AbcDataCollectorException(AbcDataCollectorException.UNAUTHORIZED,
					"User does not have Entry permission for Observed.");
		}
	}

	public void checkAdminPermission(int userId, int observedId) throws SQLException, AbcDataCollectorException {
		List<Observed> observedList = observedDao.getObservedByUser(userId);

		Optional<Observed> found = observedList.stream().filter(o -> o.getId() == observedId
				&& o.isAdminRole()).findAny();
		if (!found.isPresent()) {
			throw new AbcDataCollectorException(AbcDataCollectorException.UNAUTHORIZED,
					"User does not have Admin permission for Observed.");
		}
	}

}