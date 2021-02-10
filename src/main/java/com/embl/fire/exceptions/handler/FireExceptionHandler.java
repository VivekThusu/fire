package com.embl.fire.exceptions.handler;

import com.embl.fire.exceptions.EntityNotFoundException;
import com.embl.fire.logging.ILogger;
import com.embl.fire.logging.ILoggerFactory;
import com.embl.fire.model.ErrorResponse;
import com.embl.fire.model.FieldError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class FireExceptionHandler extends ResponseEntityExceptionHandler {

    private static final ILogger logger = ILoggerFactory.getLogger(FireExceptionHandler.class);
    private static final String BAD_REQUEST_EXCEPTION = "Bad Request Exception : Request not valid";
    public static final String HTTP_REQUEST_METHOD_NOT_SUPPORTED = "Http request method not supported : ";
    public static final String NOT_FOUND_EXCEPTION = "Not found Exception : Resource not found";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("{} : parameter value is missing/incorrect", ex.getParameter());

        List<FieldError> fieldErrors = getFieldErrors(ex.getBindingResult());

        ErrorResponse errorResponse = new ErrorResponse()
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.toString())
                .message(BAD_REQUEST_EXCEPTION + getExceptionClauseClassName(ex.getCause()))
                .detail(request.getDescription(false) + " : " + ex.getLocalizedMessage())
                .fieldErrors(fieldErrors);

        return new ResponseEntity<>(errorResponse, status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(NOT_FOUND_EXCEPTION + ex);
        ErrorResponse errorResponse = new ErrorResponse()
                .code(status.value())
                .status(status.name())
                .message(NOT_FOUND_EXCEPTION + getExceptionClauseClassName(ex.getCause()))
                .detail(request.getDescription(false) + " : " + ex.getLocalizedMessage());

        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        logger.error(HTTP_REQUEST_METHOD_NOT_SUPPORTED + ex);
        ErrorResponse errorResponse = new ErrorResponse()
                .code(status.value())
                .status(status.name())
                .message(HTTP_REQUEST_METHOD_NOT_SUPPORTED)
                .detail(request.getDescription(false) + " : " + ex.getLocalizedMessage());

        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Http Media type not supported : " + ex);
        ErrorResponse errorResponse = new ErrorResponse()
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.toString())
                .message("Http Media type not supported" + getExceptionClauseClassName(ex.getCause()))
                .detail(request.getDescription(false) + " : " + ex.getLocalizedMessage());

        List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
        if(!CollectionUtils.isEmpty(mediaTypes)) {
            headers.setAccept(mediaTypes);
        }

        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        logger.error("Entity not found : " + ex);
        ErrorResponse errorResponse = new ErrorResponse()
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND.toString())
                .message(ex.getMessage())
                .detail(request.getDescription(false) + " : " +
                        NOT_FOUND_EXCEPTION + getExceptionClauseClassName(ex.getCause()));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        logger.error("Generic exception : " + ex);
        ErrorResponse error = new ErrorResponse()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .message("Generic FIRE exception")
                .detail(request.getDescription(false) + " : " + ex.getLocalizedMessage());

        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    private static List<FieldError> getFieldErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().map(FireExceptionHandler::createFieldError)
                .collect(Collectors.toList());
    }

    private static FieldError createFieldError(org.springframework.validation.FieldError fieldError) {
        return new FieldError()
                .field(fieldError.getField())
                .code(fieldError.getCode())
                .rejectedValue(fieldError.getRejectedValue())
                .defaultMessage(fieldError.getDefaultMessage());
    }

    private String getExceptionClauseClassName(Throwable cause) {
        return Objects.nonNull(cause) ? ". " + cause.getClass().getSimpleName()
                : StringUtils.EMPTY;
    }
}
