package com.adscreator.models;

import java.time.LocalDateTime;
import java.util.List;

public record Campaign(String name, List<Product> products, LocalDateTime startTime) { }
