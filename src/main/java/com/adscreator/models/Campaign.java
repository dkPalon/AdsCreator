package com.adscreator.models;

import java.time.LocalDateTime;

public record Campaign(String name, String[] productIDs, LocalDateTime startTime, Double bid) { }
