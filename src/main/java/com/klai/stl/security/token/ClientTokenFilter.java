package com.klai.stl.security.token;

import static org.springframework.util.StringUtils.hasText;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.filter.GenericFilterBean;

public class ClientTokenFilter extends GenericFilterBean {

    public static final String TOKEN_HEADER = "STL-Token";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = extractToken(httpServletRequest);
        /*if (hasText(token)) {
            Authentication authentication = new AnonymousAuthenticationToken("key", new User(), null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }*/
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String extractToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(TOKEN_HEADER);
        return hasText(token) ? token : null;
    }
}
