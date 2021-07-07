package com.klai.stl.service;

import static javax.persistence.criteria.JoinType.LEFT;

import com.klai.stl.domain.*;
import com.klai.stl.repository.UserRepository;
import com.klai.stl.service.criteria.EmployeeCriteria;
import com.klai.stl.service.dto.EmployeeDTO;
import com.klai.stl.service.exception.EmployeeNotFound;
import java.util.List;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.StringFilter;

/**
 * Service for executing complex queries for {@link User} entities in the database.
 * The main input is a {@link com.klai.stl.service.criteria.EmployeeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link com.klai.stl.service.dto.UserDTO} or a {@link Page} of {@link com.klai.stl.service.dto.UserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeQueryService extends QueryService<User> {

    private final Logger log = LoggerFactory.getLogger(EmployeeQueryService.class);

    private final UserRepository userRepository;

    private final UserService userService;

    public EmployeeQueryService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Return a {@link Page} of {@link EmployeeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findByCriteria(EmployeeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final User currentUser = findCurrentUser();
        final Specification<User> specification = createSpecification(criteria, currentUser);
        return userRepository.findAll(specification, page).map(EmployeeDTO::new);
    }

    /**
     * Function to convert {@link EmployeeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<User> createSpecification(EmployeeCriteria criteria, User currentUser) {
        Specification<User> specification = Specification.where(null);

        if (!currentUser.isAdmin()) {
            specification = specification.and(byCompanyReference(currentUser.getCompany().getReference()));
        } else {
            specification = specification.and(byCompanyReference(criteria.getCompany()));
        }

        if (criteria != null) {
            if (criteria.getName() != null) {
                specification = specification.and(nameLike(criteria.getName()));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(loginLike(criteria.getLogin()));
            }
        }

        return specification;
    }

    private Specification<User> byCompanyReference(String companyReference) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<User, Company> companyJoin = root.join(User_.company, LEFT);
            return criteriaQuery.where(criteriaBuilder.equal(companyJoin.get(Company_.reference), companyReference)).getRestriction();
        };
    }

    private User findCurrentUser() {
        return userService.getUserWithAuthorities().orElseThrow(EmployeeNotFound::new);
    }

    private Specification<User> nameLike(String name) {
        return (root, query, builder) ->
            builder.like(builder.concat(root.get(User_.firstName), builder.concat(" ", root.get(User_.lastName))), "%" + name + "%");
    }

    private Specification<User> loginLike(String login) {
        return (root, query, builder) -> builder.like(root.get(User_.login), login);
    }
}
