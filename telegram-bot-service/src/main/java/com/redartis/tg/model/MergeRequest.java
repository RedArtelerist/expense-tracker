package com.redartis.tg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "merge_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MergeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "message_id", nullable = false)
    private Integer messageId;

    @Column(name = "completed")
    private Boolean completed;

    @Column(name = "date", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime date;
}
