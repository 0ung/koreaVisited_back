package com.koreavisited.prod.controller;

import com.koreavisited.prod.entity.Translation;
import com.koreavisited.prod.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locale")
@RequiredArgsConstructor
public class TranslationController {
    private final TranslationService translationService;

    @GetMapping("/{locale}")
    public ResponseEntity<Map<String, Object>> getTranslations(@PathVariable String locale) {
        List<Translation> translations = translationService.getAllByLanguage(locale);

        // 배열을 객체로 변환
        Map<String, Object> result = new HashMap<>();

        for (Translation translation : translations) {
            String namespace = translation.getNamespace();
            String key = translation.getTranslationKey();
            String value = translation.getValue();

            if (!result.containsKey(namespace)) {
                result.put(namespace, new HashMap<String, Object>());
            }

            ((Map<String, Object>) result.get(namespace)).put(key, value);
        }

        System.out.println(result);

        return ResponseEntity.ok(result);
    }

}