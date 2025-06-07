package ru.yandex.service;

import ru.yandex.model.dto.TransactionAcceptDto;

public interface TransactionAnalysisService {
    public void analyze(TransactionAcceptDto dto);
}
