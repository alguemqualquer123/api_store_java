package com.example.api.dtos;

import java.math.BigDecimal;

public class ProductValueUpdateRequest {
    public String id;
    public BigDecimal value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
