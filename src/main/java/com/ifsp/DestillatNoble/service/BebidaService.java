package com.ifsp.DestillatNoble.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifsp.DestillatNoble.dao.BebidaDao;
import com.ifsp.DestillatNoble.model.Bebida;

@Service
public class BebidaService {
   private final BebidaDao bebidaDao;

    public BebidaService(BebidaDao bebidaDao) {
        this.bebidaDao = bebidaDao;
    }

}
