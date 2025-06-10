package com.koreavisited.prod.service;

import com.koreavisited.prod.entity.Translation;
import com.koreavisited.prod.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TranslationService {
    private final TranslationRepository translationRepository;


    @Cacheable(value = "translations_all", key = "#languageCode")
    public List<Translation> getAllByLanguage(String languageCode) {
        return translationRepository.findByLanguageCode(languageCode);
    }

    @CacheEvict(value = "translations_all", key = "#languageCode")
    public void evictNamespaceCache(String namespace, String languageCode) {
        // 직접 캐시 제거할 때 사용
    }
}
