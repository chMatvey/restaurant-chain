package ru.chudakov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.chudakov.domain.Chef;
import ru.chudakov.repository.ChefRepository;

import java.util.List;

@Service
public class ChefServiceImpl implements ChefService {
    private ChefRepository repository;

    @Autowired
    public ChefServiceImpl(ChefRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Chef> getAllChef() {
        return null;
    }
}
