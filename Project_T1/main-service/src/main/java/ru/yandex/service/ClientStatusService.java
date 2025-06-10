package ru.yandex.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.yandex.model.dto.ClientStatusResponse;

@Service
@RequiredArgsConstructor
public class ClientStatusService {

    private final WebClient analysisServiceWebClient;

    public Mono<ClientStatusResponse> getClientStatus(String clientId, String accountId) {
        return analysisServiceWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/client-status")
                        .queryParam("clientId", clientId)
                        .queryParam("accountId", accountId)
                        .build())
                .retrieve()
                .bodyToMono(ClientStatusResponse.class);
    }
}
