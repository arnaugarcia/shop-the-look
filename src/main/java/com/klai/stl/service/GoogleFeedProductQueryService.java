package com.klai.stl.service;

import com.klai.stl.domain.*; // for static metamodels
import com.klai.stl.domain.GoogleFeedProduct;
import com.klai.stl.repository.GoogleFeedProductRepository;
import com.klai.stl.service.criteria.GoogleFeedProductCriteria;
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
 * Service for executing complex queries for {@link GoogleFeedProduct} entities in the database.
 * The main input is a {@link GoogleFeedProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GoogleFeedProduct} or a {@link Page} of {@link GoogleFeedProduct} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GoogleFeedProductQueryService extends QueryService<GoogleFeedProduct> {

    private final Logger log = LoggerFactory.getLogger(GoogleFeedProductQueryService.class);

    private final GoogleFeedProductRepository googleFeedProductRepository;

    public GoogleFeedProductQueryService(GoogleFeedProductRepository googleFeedProductRepository) {
        this.googleFeedProductRepository = googleFeedProductRepository;
    }

    /**
     * Return a {@link List} of {@link GoogleFeedProduct} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GoogleFeedProduct> findByCriteria(GoogleFeedProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GoogleFeedProduct> specification = createSpecification(criteria);
        return googleFeedProductRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GoogleFeedProduct} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GoogleFeedProduct> findByCriteria(GoogleFeedProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GoogleFeedProduct> specification = createSpecification(criteria);
        return googleFeedProductRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GoogleFeedProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GoogleFeedProduct> specification = createSpecification(criteria);
        return googleFeedProductRepository.count(specification);
    }

    /**
     * Function to convert {@link GoogleFeedProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GoogleFeedProduct> createSpecification(GoogleFeedProductCriteria criteria) {
        Specification<GoogleFeedProduct> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GoogleFeedProduct_.id));
            }
            if (criteria.getSku() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSku(), GoogleFeedProduct_.sku));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), GoogleFeedProduct_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), GoogleFeedProduct_.description));
            }
            if (criteria.getLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLink(), GoogleFeedProduct_.link));
            }
            if (criteria.getImageLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageLink(), GoogleFeedProduct_.imageLink));
            }
            if (criteria.getAdditionalImageLink() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAdditionalImageLink(), GoogleFeedProduct_.additionalImageLink));
            }
            if (criteria.getMobileLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobileLink(), GoogleFeedProduct_.mobileLink));
            }
            if (criteria.getAvailability() != null) {
                specification = specification.and(buildSpecification(criteria.getAvailability(), GoogleFeedProduct_.availability));
            }
            if (criteria.getAvailabilityDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAvailabilityDate(), GoogleFeedProduct_.availabilityDate));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrice(), GoogleFeedProduct_.price));
            }
            if (criteria.getSalePrice() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSalePrice(), GoogleFeedProduct_.salePrice));
            }
            if (criteria.getBrand() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBrand(), GoogleFeedProduct_.brand));
            }
            if (criteria.getCondition() != null) {
                specification = specification.and(buildSpecification(criteria.getCondition(), GoogleFeedProduct_.condition));
            }
            if (criteria.getAdult() != null) {
                specification = specification.and(buildSpecification(criteria.getAdult(), GoogleFeedProduct_.adult));
            }
            if (criteria.getAgeGroup() != null) {
                specification = specification.and(buildSpecification(criteria.getAgeGroup(), GoogleFeedProduct_.ageGroup));
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompanyId(),
                            root -> root.join(GoogleFeedProduct_.company, JoinType.LEFT).get(Company_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
