package ru.chudakov.service;

import ru.chudakov.domain.Chef;

import java.util.List;

public interface ChefService {
    public List<Chef> getAllChef();

    public Chef addChef(Chef chef);

    public void deleteChef(Chef chef);

    public Chef findChefById(int id);
}
