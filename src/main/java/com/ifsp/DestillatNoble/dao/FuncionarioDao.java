package com.ifsp.DestillatNoble.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifsp.DestillatNoble.model.Funcionario;

@Repository
public interface FuncionarioDao  extends JpaRepository<Funcionario, String>{
    List<Funcionario> findByAtivoTrue();
    List<Funcionario> findByAtivoFalse();
    Optional<Funcionario> findByEmail(String email);
     Optional<Funcionario> findByCpf(String cpf);
}
