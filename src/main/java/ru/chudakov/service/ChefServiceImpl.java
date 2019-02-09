package ru.chudakov.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.chudakov.domain.Chef;
import ru.chudakov.repository.ChefRepository;

import java.util.List;

@Service
public class ChefServiceImpl implements ChefService {
    private final ChefRepository repository;

    @Autowired
    public ChefServiceImpl(ChefRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Chef> getAllChef() {
        return Lists.newArrayList(repository.findAll());
    }

    @Override
    public Chef addChef(Chef chef) {
        return repository.save(chef);
    }

    @Override
    public void deleteChef(Chef chef) {
        repository.delete(chef);
    }

    @Override
    public Chef updateChef(Chef chef) {
        return repository.save(chef);
    }

    @Override
    public Chef findChefById(int id) {
        return repository.findById(id).orElse(null);
    }
}
