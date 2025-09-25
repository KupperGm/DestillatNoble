package com.ifsp.DestillatNoble.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ifsp.DestillatNoble.model.FormaPagamento;

public interface FormaPagamentoDao extends JpaRepository<FormaPagamento, Long> {
    
}
