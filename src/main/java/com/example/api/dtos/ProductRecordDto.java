package com.example.api.dtos;

import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;

@SuppressWarnings("deprecation")
public record ProductRecordDto(@NotNull String name, @NotNull BigDecimal value, @NotNull String category) {


}
