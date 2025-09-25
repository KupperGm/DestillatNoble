package com.ifsp.DestillatNoble.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ifsp.DestillatNoble.dao.UsuarioDao;
import com.ifsp.DestillatNoble.model.Usuario;

@Service
public class UserService {

    private UsuarioDao usuarioDao;

    // Buscar usuário pelo email (usado no login)
    public Usuario findByEmail(String email) {
        return usuarioDao.findByEmail(email).orElse(null);
    }

    // Listar todos usuários (se precisar no admin)
    public List<Usuario> findAll() {
        return usuarioDao.findAll();
    }

    // Buscar por ID
    public Usuario findById(String cpf) {
        return usuarioDao.findByCpf(cpf).orElse(null);
    }

}
