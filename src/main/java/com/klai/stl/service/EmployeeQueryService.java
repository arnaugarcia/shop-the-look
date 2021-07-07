package com.klai.stl.service;

import com.klai.stl.domain.*;
import com.klai.stl.repository.UserRepository;
import com.klai.stl.service.criteria.EmployeeCriteria;
import com.klai.stl.service.dto.UserDTO;
import com.klai.stl.service.exception.EmployeeNotFound;
import com.klai.stl.service.mapper.UserMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
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

    private final UserMapper userMapper;

    public EmployeeQueryService(UserRepository userRepository, UserService userService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Return a {@link Page} of {@link UserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> findByCriteria(EmployeeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final User currentUser = findCurrentUser();
        final Specification<User> specification = createSpecification(criteria, currentUser);
        return userRepository.findAll(specification, page).map(userMapper::userToUserDTO);
    }

    /**
     * Function to convert {@link EmployeeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<User> createSpecification(EmployeeCriteria criteria, User currentUser) {
        Specification<User> specification = Specification.where(null);

        if (!currentUser.isAdmin()) {
            StringFilter companyFilter = new StringFilter();
            companyFilter.setEquals(currentUser.getCompany().getReference());
            specification =
                specification.and(
                    buildSpecification(companyFilter, root -> root.join(User_.company, JoinType.LEFT).get(Company_.reference))
                );
        } else {
            StringFilter companyFilter = new StringFilter();
            companyFilter.setEquals(criteria.getCompany());
            specification =
                specification.and(
                    buildSpecification(companyFilter, root -> root.join(User_.company, JoinType.LEFT).get(Company_.reference))
                );
        }
        if (criteria != null) {
            if (criteria.getName() != null) {
                specification =
                    specification
                        .or(buildStringSpecification(criteria.getName(), User_.firstName))
                        .or(buildStringSpecification(criteria.getName(), User_.lastName));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogin(), User_.login));
            }
        }
        return specification;
    }

    private User findCurrentUser() {
        return userService.getUserWithAuthorities().orElseThrow(EmployeeNotFound::new);
    }
}
