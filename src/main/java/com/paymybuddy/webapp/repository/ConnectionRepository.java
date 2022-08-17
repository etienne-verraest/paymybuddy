package com.paymybuddy.webapp.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paymybuddy.webapp.model.Connection;
import com.paymybuddy.webapp.model.compositekeys.ConnectionId;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, ConnectionId> {

	List<Connection> findAllConnectionByUserId(Integer userId);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM connection c WHERE c.id_user = :userId AND c.id_buddy = :buddyId", nativeQuery = true)
	void deleteBuddyFromId(@Param("userId") Integer userId, @Param("buddyId") Integer buddyId);
}
