package com.klai.stl.service.impl;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.klai.stl.domain.Company;
import com.klai.stl.repository.CompanyRepository;
import com.klai.stl.service.TokenService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    public static final int TOKEN_SIZE = 25;
    private final CompanyRepository companyRepository;

    public TokenServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public String generateToken() {
        String randomToken = randomAlphanumeric(TOKEN_SIZE);
        final Optional<Company> company = companyRepository.findByToken(randomToken);
        if (company.isPresent()) {
            return this.generateToken();
        }
        return randomToken;
    }
}
