package com.koreavisited.prod.repository;

import com.koreavisited.prod.entity.Translation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TranslationRepository extends JpaRepository<Translation, Long> {
    List<Translation> findByLanguageCode(String languageCode);

    List<Translation> findByNamespaceAndLanguageCode(String namespace, String languageCode);

    Optional<Translation> findByNamespaceAndTranslationKeyAndLanguageCode(String namespace, String key, String languageCode);
}
