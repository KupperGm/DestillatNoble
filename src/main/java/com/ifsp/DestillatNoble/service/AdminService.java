package com.ifsp.DestillatNoble.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifsp.DestillatNoble.dao.AdminDao;
import com.ifsp.DestillatNoble.model.Admin;

import jakarta.transaction.Transactional;

@Service
public class AdminService {
    @Autowired
    private AdminDao adminDao;

    public List<Admin> listarAtivos() {
        return adminDao.findByAtivoTrue();
    }
    public List<Admin> listarInativos(){
        return adminDao.findByAtivoFalse();
    }
    public Admin buscarPorMatricula(String matricula){
        return adminDao.findById(matricula).orElse(null);
    }
    public void salvar(Admin admin) {
        adminDao.save(admin);
    }
    @Transactional
    public void desativar (String matricula){
       Admin admin = buscarPorMatricula(matricula);
       if (admin != null) {
        admin.setAtivo(false);
        admin.setDataDesativacao(java.time.LocalDateTime.now());
        adminDao.save(admin);
       }
    }



}
