package ru.yandex.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.model.ClientStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientStatusResponse {
    private String clientId;
    private String accountId;
    private ClientStatus status;
}
