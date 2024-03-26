package com.cursos.api.springsecuritycourse.exception;

import com.cursos.api.springsecuritycourse.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handlerGenericException(HttpServletRequest request, Exception exception){

        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getLocalizedMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setMessage("Error interno en el servidor, vuelve a intentarlo");
        apiError.setTimestamp(LocalDateTime.now() );


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handlerAccessDeniedException(HttpServletRequest request,
                                                          AccessDeniedException exception){

        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getLocalizedMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setMessage("Acceso denegado. No tienes los permisos necesarios para acceder a esta función. " +
                "Por favor, contacta al administrador si crees que esto es un error.");
        apiError.setTimestamp(LocalDateTime.now() );


        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handlerMethodArgumentNotValidException(HttpServletRequest request,
                                                                    MethodArgumentNotValidException exception){

        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getLocalizedMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setMessage("Error en la petición enviada");

        List<String> errorMessages = exception.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        String errorMessageString = String.valueOf(errorMessages);
        if (!errorMessageString.isEmpty()) {
            LOGGER.info(errorMessageString);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

}
