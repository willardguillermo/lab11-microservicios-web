package com.tecsup.model;

import java.util.Set;

public enum EstadoCita {
    PROGRAMADA, CONFIRMADA, ATENDIDA, CANCELADA;

    // Flujo permitido: PROGRAMADA -> CONFIRMADA -> ATENDIDA, y se puede cancelar antes de ser atendida
    public boolean puedeCambiarA(EstadoCita nuevo) {
        return switch (this) {
            case PROGRAMADA -> Set.of(CONFIRMADA, CANCELADA).contains(nuevo);
            case CONFIRMADA -> Set.of(ATENDIDA, CANCELADA).contains(nuevo);
            case ATENDIDA, CANCELADA -> false;
        };
    }
}
