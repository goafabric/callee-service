package org.goafabric.calleeservice.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record Callee(
        @Nullable String id,
        @NotNull String message
) {}
