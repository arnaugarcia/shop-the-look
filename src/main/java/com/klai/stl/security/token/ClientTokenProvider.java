package com.klai.stl.security.token;

import com.klai.stl.domain.Company;
import com.klai.stl.service.CompanyService;
import java.util.ArrayList;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class ClientTokenProvider {

    private final CompanyService companyService;

    public ClientTokenProvider(CompanyService companyService) {
        this.companyService = companyService;
    }

    public Authentication getAuthentication(String token) {
        final Company company = companyService.findByToken(token).orElseThrow(InvalidTokenException::new);
        User principal = new User(company.getReference(), "", new ArrayList<>());
        return new UsernamePasswordAuthenticationToken(principal, token, new ArrayList<>());
    }
}
