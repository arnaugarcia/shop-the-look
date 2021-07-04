package com.klai.stl.web.rest;

import com.klai.stl.IntegrationTest;
import com.klai.stl.domain.Company;
import com.klai.stl.domain.GoogleFeedProduct;
import com.klai.stl.domain.enumeration.GoogleFeedAgeGroup;
import com.klai.stl.domain.enumeration.GoogleFeedProductAvailability;
import com.klai.stl.domain.enumeration.GoogleFeedProductCondition;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import javax.persistence.EntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * Integration tests for the {@link GoogleFeedProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GoogleFeedProductResourceIT {

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_ADDITIONAL_IMAGE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_IMAGE_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_LINK = "BBBBBBBBBB";

    private static final GoogleFeedProductAvailability DEFAULT_AVAILABILITY = GoogleFeedProductAvailability.IN_STOCK;
    private static final GoogleFeedProductAvailability UPDATED_AVAILABILITY = GoogleFeedProductAvailability.OUT_OF_STOCK;

    private static final ZonedDateTime DEFAULT_AVAILABILITY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_AVAILABILITY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_AVAILABILITY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_PRICE = "AAAAAAAAAA";
    private static final String UPDATED_PRICE = "BBBBBBBBBB";

    private static final String DEFAULT_SALE_PRICE = "AAAAAAAAAA";
    private static final String UPDATED_SALE_PRICE = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final GoogleFeedProductCondition DEFAULT_CONDITION = GoogleFeedProductCondition.NEW;
    private static final GoogleFeedProductCondition UPDATED_CONDITION = GoogleFeedProductCondition.REFURBISHED;

    private static final Boolean DEFAULT_ADULT = false;
    private static final Boolean UPDATED_ADULT = true;

    private static final GoogleFeedAgeGroup DEFAULT_AGE_GROUP = GoogleFeedAgeGroup.NEWBORN;
    private static final GoogleFeedAgeGroup UPDATED_AGE_GROUP = GoogleFeedAgeGroup.INFANT;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GoogleFeedProduct createEntity(EntityManager em) {
        GoogleFeedProduct googleFeedProduct = new GoogleFeedProduct()
            .sku(DEFAULT_SKU)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .link(DEFAULT_LINK)
            .imageLink(DEFAULT_IMAGE_LINK)
            .additionalImageLink(DEFAULT_ADDITIONAL_IMAGE_LINK)
            .mobileLink(DEFAULT_MOBILE_LINK)
            .availability(DEFAULT_AVAILABILITY)
            .availabilityDate(DEFAULT_AVAILABILITY_DATE)
            .price(DEFAULT_PRICE)
            .salePrice(DEFAULT_SALE_PRICE)
            .brand(DEFAULT_BRAND)
            .condition(DEFAULT_CONDITION)
            .adult(DEFAULT_ADULT)
            .ageGroup(DEFAULT_AGE_GROUP);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        googleFeedProduct.setCompany(company);
        return googleFeedProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GoogleFeedProduct createUpdatedEntity(EntityManager em) {
        GoogleFeedProduct googleFeedProduct = new GoogleFeedProduct()
            .sku(UPDATED_SKU)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .link(UPDATED_LINK)
            .imageLink(UPDATED_IMAGE_LINK)
            .additionalImageLink(UPDATED_ADDITIONAL_IMAGE_LINK)
            .mobileLink(UPDATED_MOBILE_LINK)
            .availability(UPDATED_AVAILABILITY)
            .availabilityDate(UPDATED_AVAILABILITY_DATE)
            .price(UPDATED_PRICE)
            .salePrice(UPDATED_SALE_PRICE)
            .brand(UPDATED_BRAND)
            .condition(UPDATED_CONDITION)
            .adult(UPDATED_ADULT)
            .ageGroup(UPDATED_AGE_GROUP);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        googleFeedProduct.setCompany(company);
        return googleFeedProduct;
    }
}
