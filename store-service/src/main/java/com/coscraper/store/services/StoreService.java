package com.coscraper.store.services;

import com.coscraper.store.models.store.Store;
import com.coscraper.store.models.store.StoreAddRequest;
import com.coscraper.store.models.store.StoreUpdateRequest;
import com.coscraper.store.repositories.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public record StoreService(StoreRepository storeRepository) {

    public Optional<Store> findStoreById(UUID storeId) {
        Optional<Store> store = storeRepository.findById(storeId);
        return store;
    }

    public List<Store> findAllStores() {
        return storeRepository.findAll();
    }

    public void addNewStore(StoreAddRequest request) {
        Store store = Store.builder()
                .name(request.name())
                .baseUrl(request.baseUrl())
                .description(request.description().get())
                .imageUrl(request.imageUrl().get())
                .language(request.language())
                .age(0)
                .build();

        storeRepository.save(store);
    }

    public void removeStoreById(UUID storeId) {
        storeRepository.deleteById(storeId);

        // TODO: Delete products related to the store.
    }

    public void updateStore(Store store, StoreUpdateRequest storeUpdateRequest) {
        store.setBaseUrl(storeUpdateRequest.baseUrl());
        store.setImageUrl(storeUpdateRequest.imageUrl());

        storeRepository.save(store);
    }
}
