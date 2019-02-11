package ru.chudakov;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomTest {
    @Test
    public void generateIndex() {
        Random random = new Random();
        System.out.println(random.nextInt(6));
    }

    @Test
    public void mapTest() {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        updateMap(map);
        System.out.println(map);
    }

    private void updateMap(Map<String, Integer> map) {
        map.put("1", 5);
    }

    @Test
    public void modTest() {
        System.out.println(9 % 3);
    }
}
