package com.klai.stl.service;

import com.klai.stl.domain.*; // for static metamodels
import com.klai.stl.domain.Space;
import com.klai.stl.repository.SpaceRepository;
import com.klai.stl.service.criteria.SpaceCriteria;
import com.klai.stl.service.dto.SpaceDTO;
import com.klai.stl.service.mapper.SpaceMapper;
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

    public SpaceQueryService(SpaceRepository spaceRepository, SpaceMapper spaceMapper) {
        this.spaceRepository = spaceRepository;
        this.spaceMapper = spaceMapper;
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
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SpaceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Space> specification = createSpecification(criteria);
        return spaceRepository.count(specification);
    }

    /**
     * Function to convert {@link SpaceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Space> createSpecification(SpaceCriteria criteria) {
        Specification<Space> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Space_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Space_.name));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Space_.active));
            }
            if (criteria.getReference() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReference(), Space_.reference));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Space_.description));
            }
            if (criteria.getMaxPhotos() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxPhotos(), Space_.maxPhotos));
            }
            if (criteria.getVisible() != null) {
                specification = specification.and(buildSpecification(criteria.getVisible(), Space_.visible));
            }
            if (criteria.getPhotoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPhotoId(), root -> root.join(Space_.photos, JoinType.LEFT).get(Photo_.id))
                    );
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompanyId(), root -> root.join(Space_.company, JoinType.LEFT).get(Company_.id))
                    );
            }
        }
        return specification;
    }
}
