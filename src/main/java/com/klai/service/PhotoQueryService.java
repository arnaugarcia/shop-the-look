package com.klai.service;

import com.klai.domain.*; // for static metamodels
import com.klai.domain.Photo;
import com.klai.repository.PhotoRepository;
import com.klai.service.criteria.PhotoCriteria;
import com.klai.service.dto.PhotoDTO;
import com.klai.service.mapper.PhotoMapper;
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
 * Service for executing complex queries for {@link Photo} entities in the database.
 * The main input is a {@link PhotoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PhotoDTO} or a {@link Page} of {@link PhotoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PhotoQueryService extends QueryService<Photo> {

    private final Logger log = LoggerFactory.getLogger(PhotoQueryService.class);

    private final PhotoRepository photoRepository;

    private final PhotoMapper photoMapper;

    public PhotoQueryService(PhotoRepository photoRepository, PhotoMapper photoMapper) {
        this.photoRepository = photoRepository;
        this.photoMapper = photoMapper;
    }

    /**
     * Return a {@link List} of {@link PhotoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PhotoDTO> findByCriteria(PhotoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Photo> specification = createSpecification(criteria);
        return photoMapper.toDto(photoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PhotoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PhotoDTO> findByCriteria(PhotoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Photo> specification = createSpecification(criteria);
        return photoRepository.findAll(specification, page).map(photoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PhotoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Photo> specification = createSpecification(criteria);
        return photoRepository.count(specification);
    }

    /**
     * Function to convert {@link PhotoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Photo> createSpecification(PhotoCriteria criteria) {
        Specification<Photo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Photo_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Photo_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Photo_.description));
            }
            if (criteria.getLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLink(), Photo_.link));
            }
            if (criteria.getOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrder(), Photo_.order));
            }
            if (criteria.getHeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeight(), Photo_.height));
            }
            if (criteria.getWidth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWidth(), Photo_.width));
            }
            if (criteria.getOrientation() != null) {
                specification = specification.and(buildSpecification(criteria.getOrientation(), Photo_.orientation));
            }
            if (criteria.getDemo() != null) {
                specification = specification.and(buildSpecification(criteria.getDemo(), Photo_.demo));
            }
            if (criteria.getCoordinateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCoordinateId(),
                            root -> root.join(Photo_.coordinates, JoinType.LEFT).get(Coordinate_.id)
                        )
                    );
            }
            if (criteria.getSpaceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSpaceId(), root -> root.join(Photo_.space, JoinType.LEFT).get(Space_.id))
                    );
            }
            if (criteria.getSpaceTemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSpaceTemplateId(),
                            root -> root.join(Photo_.spaceTemplate, JoinType.LEFT).get(SpaceTemplate_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
