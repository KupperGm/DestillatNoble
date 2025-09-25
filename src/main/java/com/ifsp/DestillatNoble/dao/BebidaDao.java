package com.ifsp.DestillatNoble.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifsp.DestillatNoble.model.Bebida;

public interface BebidaDao extends JpaRepository<Bebida, Long> {
}

