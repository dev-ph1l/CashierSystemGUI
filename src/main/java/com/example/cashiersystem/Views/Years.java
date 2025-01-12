package com.example.cashiersystem.Views;

public enum Years {
    YEAR24(24),
    YEAR25(25);

    private final int value;

    Years(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
