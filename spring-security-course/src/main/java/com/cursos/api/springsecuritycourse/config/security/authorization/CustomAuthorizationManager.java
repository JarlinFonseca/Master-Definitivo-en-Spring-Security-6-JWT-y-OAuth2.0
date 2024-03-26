package com.cursos.api.springsecuritycourse.config.security.authorization;


import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.persistence.entity.security.GrantedPermission;
import com.cursos.api.springsecuritycourse.persistence.entity.security.Operation;
import com.cursos.api.springsecuritycourse.persistence.entity.security.User;
import com.cursos.api.springsecuritycourse.persistence.repository.security.OperationRepository;
import com.cursos.api.springsecuritycourse.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthorizationManager.class);
    private final OperationRepository operacionRepository;
    private final UserService userService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication,
                                       RequestAuthorizationContext requestContext) {
        HttpServletRequest request = requestContext.getRequest();

        String url = extractUrl(request);
        String httpMethod = request.getMethod();

        boolean isPublic = isPublic(url, httpMethod);

        if(isPublic){
            return new AuthorizationDecision(true);
        }


        boolean isGranted = isGranted(url, httpMethod, authentication.get());

        return new AuthorizationDecision(isGranted);
    }

    private boolean isGranted(String url, String httpMethod, Authentication authentication) {
        if(!(authentication instanceof UsernamePasswordAuthenticationToken)){
           throw new AuthenticationCredentialsNotFoundException("User not logged in");
        }

        List<Operation> operations = obtainedOperations(authentication);

        boolean isGranted = operations.stream().anyMatch(getOperationPredicate(url, httpMethod));
        LOGGER.info("IS GRANTED: {}",isGranted);
        return isGranted;
    }

    private static Predicate<Operation> getOperationPredicate(String url, String httpMethod) {
        return operation -> {
            String basePath = operation.getModule().getBasePath();

            Pattern pattern = Pattern.compile(basePath.concat(operation.getPath()));
            Matcher matcher = pattern.matcher(url);
            return matcher.matches() && operation.getHttpMethod().equals(httpMethod);
        };
    }

    private List<Operation> obtainedOperations(Authentication authentication) {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
        String username = (String) authToken.getPrincipal();
        User user = userService.findOneByUsername(username)
                .orElseThrow(()-> new ObjectNotFoundException("User not found: "+username));

        return user.getRole().getPermissions().stream()
                .map(GrantedPermission::getOperation)
                .toList();
    }

    private boolean isPublic(String url, String httpMethod) {
        List<Operation> publicAccessEndpoints = operacionRepository
                .findByPublicAccess();

        boolean isPublic = publicAccessEndpoints.stream().anyMatch(getOperationPredicate(url, httpMethod));
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
