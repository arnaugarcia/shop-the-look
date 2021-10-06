package com.klai.stl.service.impl;

import static java.util.Objects.isNull;
import static javax.persistence.criteria.JoinType.INNER;

import com.klai.stl.domain.Company;
import com.klai.stl.domain.Company_;
import com.klai.stl.domain.Space;
import com.klai.stl.domain.Space_;
import com.klai.stl.repository.SpaceRepository;
import com.klai.stl.service.UserService;
import com.klai.stl.service.criteria.SpaceCriteria;
import com.klai.stl.service.dto.SpaceDTO;
import com.klai.stl.service.mapper.SpaceMapper;
import java.util.List;
import javax.persistence.criteria.Join;
import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Space} entities in the database.
 * The main input is a {@link SpaceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SpaceDTO} or a {@link Page} of {@link SpaceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SpaceQueryService extends QueryService<Space> {

    private final Logger log = LoggerFactory.getLogger(SpaceQueryService.class);

    private final SpaceRepository spaceRepository;

    private final SpaceMapper spaceMapper;

    private final UserService userService;

    public SpaceQueryService(SpaceRepository spaceRepository, SpaceMapper spaceMapper, UserService userService) {
        this.spaceRepository = spaceRepository;
        this.spaceMapper = spaceMapper;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link SpaceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SpaceDTO> findByCriteria(SpaceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Space> specification = createSpecification(criteria);
        return spaceMapper.toDto(spaceRepository.findAll(specification));
    }

    /**
     * Return a {@link List} of {@link SpaceDTO} which matches the criteria from the database.
     * @return the matching entities.
     * @param spaceCriteria The object which holds all the filters, which the entities should match.
     */
    @Transactional(readOnly = true)
    public List<SpaceDTO> findForCurrentUser(SpaceCriteria spaceCriteria) {
        log.debug("find spaces for current user");
        spaceCriteria.setCompanyReference(userService.getCurrentUserCompanyReference());
        final Specification<Space> specification = createSpecification(spaceCriteria);
        return spaceMapper.toDto(spaceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SpaceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SpaceDTO> findByCriteria(SpaceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Space> specification = createSpecification(criteria);
        return spaceRepository.findAll(specification, page).map(spaceMapper::toDto);
    }

    /**
     * Function to convert {@link SpaceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Space> createSpecification(SpaceCriteria criteria) {
        Specification<Space> specification = Specification.where(null);
        if (criteria != null) {
            if (!isNull(criteria.getKeyword())) {
                throw new NotYetImplementedException();
            }
            if (!isNull(criteria.getCompanyReference())) {
                specification = specification.and(byCompanyReference(criteria.getCompanyReference()));
            }
        }
        return specification;
    }

    private Specification<Space> byCompanyReference(String companyReference) {
        return (root, query, builder) -> {
            final Join<Space, Company> companyJoin = root.join(Space_.company, INNER);
            return builder.equal(companyJoin.get(Company_.reference), companyReference);
        };
    }
}
