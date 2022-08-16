package com.paymybuddy.webapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.webapp.model.Connection;
import com.paymybuddy.webapp.model.keys.ConnectionId;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, ConnectionId> {

	List<Connection> findAllConnectionByUserId(Integer userId);
}
