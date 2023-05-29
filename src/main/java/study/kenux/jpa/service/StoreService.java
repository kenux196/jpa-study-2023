package study.kenux.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.kenux.jpa.domain.Store;
import study.kenux.jpa.global.exception.StoreNotFoundException;
import study.kenux.jpa.repository.StoreRepository;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public void save(Store store) {
        storeRepository.save(store);
    }

    public Store getStoreByName(String name) {
        return storeRepository.findByName(name)
                .orElseThrow(() -> new StoreNotFoundException("Store not founded : " + name));
    }
}
