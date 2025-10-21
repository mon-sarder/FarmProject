package com.example.queueinv;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ItemDTO(@NotBlank String name, @Min(0) int qty) {}
public record AddOrIncDTO(@NotBlank String name, @Min(1) int amount) {}
public record SetQtyDTO(@NotBlank String name, @Min(0) int qty) {}

public record CustomerDTO(@NotBlank String name, String note) {}
