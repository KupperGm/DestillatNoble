package com.ifsp.DestillatNoble.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifsp.DestillatNoble.dao.FuncionarioDao;
import com.ifsp.DestillatNoble.model.Funcionario;

import jakarta.transaction.Transactional;

@Service
public class FuncionarioService {
    @Autowired
    private FuncionarioDao funcDao;

    public List<Funcionario> listarAtivos() {
        return funcDao.findByAtivoTrue();
    }
    public List<Funcionario> listarInativos(){
        return funcDao.findByAtivoFalse();
    }
    public Funcionario buscarPorMatricula(String matricula){
        return funcDao.findById(matricula).orElse(null);
    }
      public void salvar(Funcionario funcionario) {
        funcDao.save(funcionario);
    }
    @Transactional
    public void desativar (String matricula){
       Funcionario funcionario = buscarPorMatricula(matricula);
       if (funcionario != null) {
        funcionario.setAtivo(false);
        funcionario.setDataDesativacao(LocalDateTime.now());

        funcDao.save(funcionario);
        
       }
    }

}
