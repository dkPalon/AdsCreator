package com.adscreator.models;

import java.time.LocalDate;

public record Campaign(String name, String[] productIDs, LocalDate startTime, Double bid) { }
