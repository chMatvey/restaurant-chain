package ru.chudakov.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Error implements Result {
    private final String error;
}
