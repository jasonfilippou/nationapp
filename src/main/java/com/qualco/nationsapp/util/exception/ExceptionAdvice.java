package com.qualco.nationsapp.util.exception;

import jakarta.validation.ConstraintViolationException;

import org.hibernate.HibernateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * {@link RestControllerAdvice} for all our custom exceptions, and some non-custom ones, too.
 *
 * @author jason
 */
@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * Handler for all exceptions that should return an HTTP Status Code of {@link
     * HttpStatus#BAD_REQUEST}.
     *
     * @param exc The {@link Exception} thrown by our application.
     * @return A {@link ResponseEntity} with the exception's message as the body and {@link
     *     HttpStatus#BAD_REQUEST} as the status code.
     */
    @ResponseBody
    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        MethodArgumentNotValidException.class,
        MethodArgumentTypeMismatchException.class,
        ConstraintViolationException.class,
        BadDateFormatException.class,
        HibernateException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionMessageContainer> badRequestStatusMessage(Exception exc) {
        return new ResponseEntity<>(
                new ExceptionMessageContainer(exc.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler for all exceptions that should return an HTTP Status Code of {@link
     * HttpStatus#UNAUTHORIZED}.
     *
     * @param exc The {@link Exception} thrown by our application.
     * @return A {@link ResponseEntity} with the exception's message as the body and {@link
     *     HttpStatus#UNAUTHORIZED} as the status code.
     */
    @ResponseBody
    @ExceptionHandler({
//        BadCredentialsException.class,
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ExceptionMessageContainer> unauthorizedStatusMessage(Exception exc) {
        return new ResponseEntity<>(
                new ExceptionMessageContainer(exc.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handler for all exceptions that should return an HTTP Status code of {@link
     * HttpStatus#CONFLICT}
     *
     * @param exc he {@link Exception} thrown by our application.
     * @return A {@link ResponseEntity} with the exception's message as the body and {@link
     *     HttpStatus#CONFLICT} as the status code.
     */
    @ResponseBody
    @ExceptionHandler({EmailAlreadyInDatabaseException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    private ResponseEntity<ExceptionMessageContainer> conflictStatusMessage(Exception exc) {
        return new ResponseEntity<>(
                new ExceptionMessageContainer(exc.getMessage()), HttpStatus.CONFLICT);
    }

    /**
     * Handler for all exceptions that should return an HTTP Status Code of {@link
     * HttpStatus#NOT_FOUND}.
     *
     * @param exc The {@link Exception} thrown by our application.
     * @return A {@link ResponseEntity} with the exception's message as the body and {@link
     *     HttpStatus#NOT_FOUND} as the status code.
     */
    @ResponseBody
    @ExceptionHandler({/*UsernameNotFoundException.class,*/ CountryNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionMessageContainer> notFoundStatusMessage(Exception exc) {
        return new ResponseEntity<>(
                new ExceptionMessageContainer(exc.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler({DataAccessLayerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionMessageContainer> internalServerErrorMessage(Exception exc) {
        return new ResponseEntity<>(
                new ExceptionMessageContainer(exc.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
