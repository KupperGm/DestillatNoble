package com.ifsp.DestillatNoble.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifsp.DestillatNoble.model.Usuario;

@Repository
public interface UsuarioDao extends JpaRepository<Usuario, String>{
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCpf(String cpf);
}
