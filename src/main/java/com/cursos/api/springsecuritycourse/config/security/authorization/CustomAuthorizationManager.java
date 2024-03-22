package com.cursos.api.springsecuritycourse.config.security.authorization;


import com.cursos.api.springsecuritycourse.persistence.entity.security.Operation;
import com.cursos.api.springsecuritycourse.persistence.repository.security.OperationRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthorizationManager.class);
    private final OperationRepository operacionRepository;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication,
                                       RequestAuthorizationContext requestContext) {
        HttpServletRequest request = requestContext.getRequest();

        String url = extractUrl(request);
        String httpMethod = request.getMethod();

        boolean isPublic = isPublic(url, httpMethod);


        return new AuthorizationDecision(isPublic);
    }

    private boolean isPublic(String url, String httpMethod) {
        List<Operation> publicAccessEndpoints = operacionRepository
                .findByPublicAccess();

        boolean isPublic = publicAccessEndpoints.stream().anyMatch(operation -> {
            String basePath = operation.getModule().getBasePath();

            Pattern pattern = Pattern.compile(basePath.concat(operation.getPath()));
            Matcher matcher = pattern.matcher(url);
            return matcher.matches() && operation.getHttpMethod().equals(httpMethod);
        });
        LOGGER.info("IS PUBLIC: {}",isPublic);

        return isPublic;
    }

    private String extractUrl(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String url = request.getRequestURI();
        url = url.replace(contextPath, "");
        LOGGER.info(url);
        return url;
    }
}
