package com.klai.stl.web.rest.errors;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class ForbiddenException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final String errorKey;

    public ForbiddenException(String defaultMessage, String entityName, String errorKey) {
        this(ErrorConstants.FORBIDDEN, defaultMessage, entityName, errorKey);
    }

    public ForbiddenException(URI type, String defaultMessage, String entityName, String errorKey) {
        super(type, defaultMessage, Status.FORBIDDEN, null, null, null, getAlertParameters(entityName, errorKey));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        parameters.put("params", entityName);
        return parameters;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
