package com.canalbrewing.abcdatacollector.business;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.canalbrewing.abcdatacollector.dal.ObservedDao;
import com.canalbrewing.abcdatacollector.exception.AbcDataCollectorException;
import com.canalbrewing.abcdatacollector.model.Abc;
import com.canalbrewing.abcdatacollector.model.Incident;
import com.canalbrewing.abcdatacollector.model.Observed;
import com.canalbrewing.abcdatacollector.model.RequestAbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObservedBusinessImpl implements ObservedBusiness {

	private static final String NA = "N/A";

	@Autowired
	ObservedDao observedDao;

	public List<Observed> getObservedByUser(int userId) throws SQLException {
		return observedDao.getObservedByUser(userId);
	}

	public Observed getObserved(int userId, String observedId) throws SQLException, AbcDataCollectorException {

		List<Observed> observedList = observedDao.getObservedByUser(userId);

		int id = Integer.parseInt(observedId);

		Optional<Observed> optional = observedList.stream().filter(o -> o.getId() == id).findFirst();
		if (optional.isEmpty()) {
			throw new AbcDataCollectorException(AbcDataCollectorException.NOT_FOUND, "Observed not found for User");
		}

		Observed observed = optional.get();

		observed.getLocations().add(new Abc(0, NA));

		List<Abc> abcs = observedDao.getObservedABCs(id);
		for (Abc abc : abcs) {
			observed.addValue(abc);
		}

		return observed;
	}

	public List<Incident> getIncidentsByObserved(String observedId, String incidentStartDt) throws SQLException {
		return observedDao.getIncidentsByObserved(Integer.parseInt(observedId), incidentStartDt);
	}

	public Incident getIncidentById(String observedId, String incidentId) throws SQLException {
		return observedDao.getIncidentById(Integer.parseInt(observedId), Integer.parseInt(incidentId));
	}

	public Abc addObservedABC(String observedId, RequestAbc requestAbc) throws SQLException {
		Abc abc = new Abc();
		abc.setTypeCd(requestAbc.getTypeCd());
		abc.setTypeValue(requestAbc.getTypeValue());

		if (Abc.LOCATION.equals(requestAbc.getTypeCd())) {
			return observedDao.insertObservedLocation(Integer.parseInt(observedId), abc);
		} else {
			return observedDao.insertObservedABC(Integer.parseInt(observedId), abc);
		}
	}

	public Abc updateObservedABC(String observedId, RequestAbc requestAbc) throws SQLException {
		Abc abc = new Abc();
		abc.setValueId(requestAbc.getValueId());
		abc.setTypeCd(requestAbc.getTypeCd());
		abc.setTypeValue(requestAbc.getTypeValue());
		abc.setActiveFl(requestAbc.getActiveFl());

		if (Abc.LOCATION.equals(requestAbc.getTypeCd())) {
			return observedDao.updateObservedLocation(abc);
		} else {
			return observedDao.updateObservedABC(abc);
		}
	}

	public Observed deleteObserved(String observedId) throws SQLException {

		int id = Integer.parseInt(observedId);

		Observed observed = observedDao.getObservedById(id);

		observedDao.deleteObserved(observed);

		return observed;
	}

}