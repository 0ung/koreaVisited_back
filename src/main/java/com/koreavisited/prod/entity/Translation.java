package com.koreavisited.prod.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "translations")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;

    private String namespace;

    @Column(name = "translation_key")
    private String translationKey;

    @Column(name = "language_code")
    private String languageCode;

    @Column(columnDefinition = "TEXT")
    private String value;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

}