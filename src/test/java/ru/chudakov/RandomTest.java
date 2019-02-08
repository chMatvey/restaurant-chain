package ru.chudakov;

import org.junit.Test;

import java.util.Random;

public class RandomTest {
    @Test
    public void generateIndex() {
        Random random = new Random();
        System.out.println(random.nextInt(6));
    }
}
