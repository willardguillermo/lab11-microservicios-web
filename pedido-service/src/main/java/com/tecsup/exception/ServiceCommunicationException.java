package com.tecsup.exception;

// Se lanza cuando otro microservicio no responde o devuelve un error inesperado
public class ServiceCommunicationException extends RuntimeException {

    public ServiceCommunicationException(String mensaje) {
        super(mensaje);
    }
}
