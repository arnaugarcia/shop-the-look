package com.klai.repository;

import com.klai.domain.SpaceTemplate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SpaceTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpaceTemplateRepository extends JpaRepository<SpaceTemplate, Long> {}
