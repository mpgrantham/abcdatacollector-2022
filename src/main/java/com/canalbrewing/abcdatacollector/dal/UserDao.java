package com.canalbrewing.abcdatacollector.dal;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.canalbrewing.abcdatacollector.model.Relationship;
import com.canalbrewing.abcdatacollector.model.User;
import com.canalbrewing.abcdatacollector.model.UserSession;
import com.canalbrewing.abcdatacollector.model.Verification;

import org.springframework.stereotype.Component;

@Component
public interface UserDao {

	List<Relationship> getRelationships() throws SQLException;

	User getUserByUsername(String username) throws SQLException;

	User getUserByEmail(String email) throws SQLException;

	User getUserById(int userId) throws SQLException;

	User getUserBySignedInKey(String signedInKey) throws SQLException;

	UserSession getUserSession(String sessionToken) throws SQLException;

	void insertUserSession(User user) throws SQLException;

	void updateUserSession(UserSession session) throws SQLException;

	User insertUser(User user) throws SQLException;

	User updateUser(User user) throws SQLException;

	User updatePassword(User user) throws SQLException;

	User updateUsername(User user) throws SQLException;

	void deleteUser(int userId) throws SQLException;

	void insertVerification(Verification verification) throws SQLException;

	void updateVerification(String verificationKey, Date expirationDt) throws SQLException;

	Verification getVerification(String verificationKey) throws SQLException;
}