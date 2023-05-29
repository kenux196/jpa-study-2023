package study.kenux.jpa.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import study.kenux.jpa.domain.Store;
import study.kenux.jpa.global.exception.StoreNotFoundException;
import study.kenux.jpa.repository.StoreRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;


    @Test
    @DisplayName("스토어 이름으로 조회 - 정상인 경우 스토어 객체 반환")
    void getStoreByName_success() {
        // given
        given(storeRepository.findByName(any())).willReturn(Optional.of(mock(Store.class)));

        // when
        final Store store = storeService.getStoreByName(any());

        // then
        assertThat(store).isNotNull();
    }

    @Test
    @DisplayName("스토어 이름으로 조회 - 실패인 경우 예외(StoreNotFound) 발생")
    void getStoreByName_Failed() {
        // given
        given(storeRepository.findByName(any())).willReturn(Optional.empty());

        // when
        final Throwable throwable = catchThrowable(() -> storeService.getStoreByName(any()));

        // then
        assertThat(throwable).isInstanceOf(StoreNotFoundException.class);
    }
}
