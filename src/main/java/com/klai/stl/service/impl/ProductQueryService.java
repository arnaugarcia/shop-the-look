package com.klai.stl.service.impl;

import static com.klai.stl.security.ApiSecurityUtils.isCurrentUserAdmin;
import static javax.persistence.criteria.JoinType.INNER;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.klai.stl.domain.*;
import com.klai.stl.repository.ProductRepository;
import com.klai.stl.service.UserService;
import com.klai.stl.service.criteria.ProductCriteria;
import com.klai.stl.service.dto.ProductDTO;
import com.klai.stl.service.mapper.ProductMapper;
import java.util.List;
import javax.persistence.criteria.Join;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Product} entities in the database.
 * The main input is a {@link ProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductDTO} or a {@link Page} of {@link ProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private final Logger log = LoggerFactory.getLogger(ProductQueryService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final UserService userService;

    public ProductQueryService(ProductRepository productRepository, ProductMapper productMapper, UserService userService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link ProductDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findByCriteria(ProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productMapper.toDto(productRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByCriteria(ProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.findAll(specification, page).map(productMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Product> createSpecification(ProductCriteria criteria) {
        Specification<Product> specification = Specification.where(null);

        if (criteria != null) {
            if (isNotEmpty(criteria.getKeyword())) {
                specification = specification.and(findByNameOrDescriptionOrReferenceLike(criteria.getKeyword()));
            }
            if (!isCurrentUserAdmin()) {
                User currentUser = userService.getCurrentUser();
                specification = specification.and(findByCompanyReference(currentUser.getCompany().getReference()));
            }
        }
        return specification;
    }

    private Specification<Product> findByNameOrDescriptionOrReferenceLike(final String keyword) {
        return (root, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.or(
                criteriaBuilder.like(root.get(Product_.reference), '%' + keyword + '%'),
                criteriaBuilder.like(root.get(Product_.name), '%' + keyword + '%'),
                criteriaBuilder.like(root.get(Product_.sku), '%' + keyword + '%')
            );
    }

    private Specification<Product> findByCompanyReference(String companyReference) {
        return (root, query, builder) -> {
            final Join<Product, Company> companyJoin = root.join(Product_.company, INNER);
            return builder.equal(companyJoin.get(Company_.reference), companyReference);
        };
    }
}
