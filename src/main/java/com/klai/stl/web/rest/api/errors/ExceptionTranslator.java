package com.klai.stl.web.rest.api.errors;

import static com.klai.stl.web.rest.api.errors.ErrorConstants.*;

import com.klai.stl.service.exception.UsernameAlreadyUsedException;
import com.klai.stl.service.webhook.stripe.exception.StripeInvalidWebhookSecret;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.*;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import org.zalando.problem.violations.ConstraintViolationProblem;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.web.util.HeaderUtil;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807).
 */
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling, SecurityAdviceTrait {

    private static final String FIELD_ERRORS_KEY = "fieldErrors";
    private static final String MESSAGE_KEY = "message";
    private static final String PATH_KEY = "path";
    private static final String VIOLATIONS_KEY = "violations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Environment env;

    public ExceptionTranslator(Environment env) {
        this.env = env;
    }

    /**
     * Post-process the Problem payload to add the message key for the front-end if needed.
     */
    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null) {
            return null;
        }
        Problem problem = entity.getBody();
        if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
            return entity;
        }

        HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
        String requestUri = nativeRequest != null ? nativeRequest.getRequestURI() : StringUtils.EMPTY;
        ProblemBuilder builder = Problem
            .builder()
            .withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? DEFAULT_TYPE : problem.getType())
            .withStatus(problem.getStatus())
            .withTitle(problem.getTitle())
            .with(PATH_KEY, requestUri);

        if (problem instanceof ConstraintViolationProblem) {
            builder
                .with(VIOLATIONS_KEY, ((ConstraintViolationProblem) problem).getViolations())
                .with(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION);
        } else {
            builder.withCause(((DefaultProblem) problem).getCause()).withDetail(problem.getDetail()).withInstance(problem.getInstance());
            problem.getParameters().forEach(builder::with);
            if (!problem.getParameters().containsKey(MESSAGE_KEY) && problem.getStatus() != null) {
                builder.with(MESSAGE_KEY, "error.http." + problem.getStatus().getStatusCode());
            }
        }
        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result
            .getFieldErrors()
            .stream()
            .map(
                f ->
                    new FieldErrorVM(
                        f.getObjectName().replaceFirst("DTO$", ""),
                        f.getField(),
                        StringUtils.isNotBlank(f.getDefaultMessage()) ? f.getDefaultMessage() : f.getCode()
                    )
            )
            .collect(Collectors.toList());

        Problem problem = Problem
            .builder()
            .withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
            .withTitle("Method argument not valid")
            .withStatus(defaultConstraintViolationStatus())
            .with(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION)
            .with(FIELD_ERRORS_KEY, fieldErrors)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleEmailAlreadyUsedException(
        com.klai.stl.service.exception.EmailAlreadyUsedException ex,
        NativeWebRequest request
    ) {
        EmailAlreadyUsedException problem = new EmailAlreadyUsedException();
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleCompanyNotFoundException(
        com.klai.stl.service.exception.CompanyNotFound ex,
        NativeWebRequest request
    ) {
        NotFoundException problem = new NotFoundException(NOT_FOUND, "Company not found", "company", "companynotfound");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleSpaceNotFoundException(
        com.klai.stl.service.space.exception.SpaceNotFound ex,
        NativeWebRequest request
    ) {
        NotFoundException problem = new NotFoundException(NOT_FOUND, "Space not found", "space", "spacenotfound");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleSpaceClientNotFoundException(
        com.klai.stl.service.client.exception.SpaceNotFound ex,
        NativeWebRequest request
    ) {
        NotFoundException problem = new NotFoundException(NOT_FOUND, ex.getMessage(), "space", "spacenotfound");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleUserNotFoundException(com.klai.stl.service.exception.UserNotFound ex, NativeWebRequest request) {
        NotFoundException problem = new NotFoundException(NOT_FOUND, "User not found", "user", "usernotfound");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleCoordinateNotFoundException(
        com.klai.stl.service.exception.CoordinateNotFound ex,
        NativeWebRequest request
    ) {
        NotFoundException problem = new NotFoundException(NOT_FOUND, ex.getMessage(), "coordinate", "coordinatenotfound");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleCompanyNotFoundException(
        com.klai.stl.service.exception.ProductNotFound ex,
        NativeWebRequest request
    ) {
        NotFoundException problem = new NotFoundException(NOT_FOUND, "Product not found", "product", "productnotfound");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadOwnerException(com.klai.stl.service.exception.BadOwnerException ex, NativeWebRequest request) {
        ForbiddenException problem = new ForbiddenException(FORBIDDEN, "You aren't the owner of this", "user", "forbidden");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleWebhookSecretError(StripeInvalidWebhookSecret ex, NativeWebRequest request) {
        ForbiddenException problem = new ForbiddenException(FORBIDDEN, ex.getMessage(), "webhook", "forbidden");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadOwnerException(
        com.klai.stl.service.exception.CompanyReferenceNotFound ex,
        NativeWebRequest request
    ) {
        BadRequestAlertException problem = new BadRequestAlertException(
            BAD_REQUEST,
            "Company reference not found",
            "company",
            "referencenotfound"
        );
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleInvalidTokenException(
        com.klai.stl.service.client.exception.InvalidTokenException ex,
        NativeWebRequest request
    ) {
        BadRequestAlertException problem = new BadRequestAlertException(BAD_REQUEST, ex.getMessage(), "token", "invalidtoken");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleSubscriptionException(
        com.klai.stl.service.exception.SubscriptionPlanNotFound ex,
        NativeWebRequest request
    ) {
        NotFoundException problem = new NotFoundException(NOT_FOUND, ex.getMessage(), "subscription", "subscriptionnotfound");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handlePhotoReadExceptionException(
        com.klai.stl.service.exception.PhotoReadException ex,
        NativeWebRequest request
    ) {
        BadRequestAlertException problem = new BadRequestAlertException(BAD_REQUEST, ex.getMessage(), "photo", "photoinvalid");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handlePhotoCleanException(
        com.klai.stl.service.exception.PhotoCleanException ex,
        NativeWebRequest request
    ) {
        InternalServerErrorException problem = new InternalServerErrorException(INTERNAL_SERVER, ex.getMessage(), "photo", "photoclean");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadOwnerException(com.klai.stl.service.exception.NoRemainingImports ex, NativeWebRequest request) {
        TooManyRequestsException problem = new TooManyRequestsException(
            TOO_MANY_REQUESTS,
            "You've exceed your plan",
            "feed",
            "noremainingimports"
        );
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleURLFeedException(
        com.klai.stl.service.exception.URLParseFeedException ex,
        NativeWebRequest request
    ) {
        BadRequestAlertException problem = new BadRequestAlertException(BAD_REQUEST, ex.getMessage(), "feed", "urlnotvalid");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleEmployeeNotFoundException(
        com.klai.stl.service.exception.EmployeeNotFound ex,
        NativeWebRequest request
    ) {
        NotFoundException problem = new NotFoundException(NOT_FOUND, "Employee not found", "employee", "employeenotfound");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handlePhotoNotFoundException(com.klai.stl.service.exception.PhotoNotFound ex, NativeWebRequest request) {
        NotFoundException problem = new NotFoundException(NOT_FOUND, ex.getMessage(), "photo", "photonotfound");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleCompanyNotAssociatedException(
        com.klai.stl.service.exception.CompanyNotAssociated ex,
        NativeWebRequest request
    ) {
        BadRequestAlertException problem = new BadRequestAlertException(
            DEFAULT_TYPE,
            "No company was found for user " + ex.getLogin(),
            "employee",
            "employeenotasociated"
        );
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleYourselfDeletionException(
        com.klai.stl.service.exception.OwnDeletionException ex,
        NativeWebRequest request
    ) {
        BadRequestAlertException problem = new BadRequestAlertException(
            DEFAULT_TYPE,
            "Cannot remove yourself from a company",
            "employee",
            "employeenotasociated"
        );
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleNifAlreadyUsedException(
        com.klai.stl.service.exception.NIFAlreadyRegistered ex,
        NativeWebRequest request
    ) {
        NIFAlreadyUsedException problem = new NIFAlreadyUsedException();
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleUsernameAlreadyUsedException(UsernameAlreadyUsedException ex, NativeWebRequest request) {
        LoginAlreadyUsedException problem = new LoginAlreadyUsedException();
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handlePreferencesNotFoundException(
        com.klai.stl.service.exception.PreferencesNotFoundException ex,
        NativeWebRequest request
    ) {
        PreferencesNotFound problem = new PreferencesNotFound();
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handlePreferencesNotFoundException(
        javax.validation.UnexpectedTypeException ex,
        NativeWebRequest request
    ) {
        BadRequestAlertException problem = new BadRequestAlertException(BAD_REQUEST, ex.getMessage(), "field", "requesterror");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleInvalidPasswordException(
        com.klai.stl.service.exception.InvalidPasswordException ex,
        NativeWebRequest request
    ) {
        return create(new InvalidPasswordException(), request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestAlertException ex, NativeWebRequest request) {
        return create(
            ex,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, ex.getEntityName(), ex.getErrorKey(), ex.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleConcurrencyFailure(ConcurrencyFailureException ex, NativeWebRequest request) {
        Problem problem = Problem.builder().withStatus(Status.CONFLICT).with(MESSAGE_KEY, ErrorConstants.ERR_CONCURRENCY_FAILURE).build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleStripeCheckoutException(
        com.klai.stl.service.exception.StripeCheckoutException ex,
        NativeWebRequest request
    ) {
        BadGatewayAlertException problem = new BadGatewayAlertException(BAD_GATEWAY, ex.getMessage(), "stripe", "checkouterror");
        return create(
            problem,
            request,
            HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(), problem.getErrorKey(), problem.getMessage())
        );
    }

    @Override
    public ProblemBuilder prepare(final Throwable throwable, final StatusType status, final URI type) {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());

        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            if (throwable instanceof HttpMessageConversionException) {
                return Problem
                    .builder()
                    .withType(type)
                    .withTitle(status.getReasonPhrase())
                    .withStatus(status)
                    .withDetail("Unable to convert http message")
                    .withCause(
                        Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null)
                    );
            }
            if (throwable instanceof DataAccessException) {
                return Problem
                    .builder()
                    .withType(type)
                    .withTitle(status.getReasonPhrase())
                    .withStatus(status)
                    .withDetail("Failure during data access")
                    .withCause(
                        Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null)
                    );
            }
            if (containsPackageName(throwable.getMessage())) {
                return Problem
                    .builder()
                    .withType(type)
                    .withTitle(status.getReasonPhrase())
                    .withStatus(status)
                    .withDetail("Unexpected runtime exception")
                    .withCause(
                        Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null)
                    );
            }
        }

        return Problem
            .builder()
            .withType(type)
            .withTitle(status.getReasonPhrase())
            .withStatus(status)
            .withDetail(throwable.getMessage())
            .withCause(
                Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null)
            );
    }

    private boolean containsPackageName(String message) {
        // This list is for sure not complete
        return StringUtils.containsAny(message, "org.", "java.", "net.", "javax.", "com.", "io.", "de.", "com.klai.stl");
    }
}
