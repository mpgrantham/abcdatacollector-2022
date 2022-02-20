package com.canalbrewing.abcdatacollector.business;

import java.sql.SQLException;
import java.util.List;

import com.canalbrewing.abcdatacollector.model.Relationship;
import com.canalbrewing.abcdatacollector.exception.AbcDataCollectorException;
import com.canalbrewing.abcdatacollector.model.Observed;
import com.canalbrewing.abcdatacollector.model.User;
import com.canalbrewing.abcdatacollector.model.UserSession;
import com.canalbrewing.abcdatacollector.model.Verification;

import org.springframework.stereotype.Component;

@Component
public interface UserBusiness {

	List<Relationship> getRelationships() throws SQLException;

	UserSession getUserSession(String sessionToken) throws SQLException, AbcDataCollectorException;

	User signInUser(String username, String password, boolean staySignedIn)
			throws SQLException, AbcDataCollectorException;

	void signOutUser(UserSession session) throws SQLException, AbcDataCollectorException;

	User signInUser(String signedInKey) throws SQLException, AbcDataCollectorException;

	User register(User user) throws SQLException, AbcDataCollectorException;

	User confirmRegister(String verificationKey) throws SQLException, AbcDataCollectorException;

	User saveUser(User user) throws SQLException, AbcDataCollectorException;

	User updateUser(User user) throws SQLException, AbcDataCollectorException;

	User updatePassword(int userId, String currentPassword, String password)
			throws SQLException, AbcDataCollectorException;

	User updateUsername(int userId, String userName) throws SQLException, AbcDataCollectorException;

	String sendUsernameOrPassword(String email, String forgottenItem) throws AbcDataCollectorException;

	User getUserByResetKey(String resetKey) throws SQLException, AbcDataCollectorException;

	User resetPassword(String resetKey, String password) throws SQLException, AbcDataCollectorException;

	Observed addObserved(int userId, String observedNm, String relationshipId)
			throws SQLException, AbcDataCollectorException;

	Observed updateObserved(int userId, String observedId, String observedNm, String role, String relationshipId)
			throws SQLException;

	Observed grantAccess(String obsId, String email, String role, String relationshipId)
			throws SQLException, AbcDataCollectorException;

	List<Observed> getUsersByObserved(String observedId) throws SQLException;

	Observed updateObservers(String userId, String observedId, String role, String relationshipId) throws SQLException;

	Observed deleteObservers(int userId, String observedUserId, String observedId) throws SQLException;

	String requestReassign(int userId, String observedId, String email)
			throws SQLException, AbcDataCollectorException;

	Observed reassignObserved(String verificationKey, String relationshipId)
			throws SQLException, AbcDataCollectorException;

	void deleteUser(int userId) throws SQLException, AbcDataCollectorException;

	Verification getVerification(String verificationKey) throws SQLException;

	Verification getReassignVerification(String verificationKey) throws SQLException, AbcDataCollectorException;

}