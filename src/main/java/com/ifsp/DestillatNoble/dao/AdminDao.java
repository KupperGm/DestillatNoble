package com.ifsp.DestillatNoble.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifsp.DestillatNoble.model.Admin;

@Repository
public interface AdminDao extends JpaRepository<Admin, String>{
     List<Admin> findByAtivoTrue();
    List<Admin> findByAtivoFalse();
    Optional<Admin> findByEmail(String email);
     Optional<Admin> findByCpf(String cpf);
}
