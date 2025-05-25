package ru.yandex.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "DataSourceErrorLog")
public class DataSourceErrorLog {
    @Id
    private Long id;
    private String textStackTrace;
    private String message;
    private String signature;
}
