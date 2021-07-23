package com.klai.stl.repository;

import com.klai.stl.domain.Preferences;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Preferences entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PreferencesRepository extends JpaRepository<Preferences, Long> {
    Optional<Preferences> findByCompanyReference(String reference);
}
