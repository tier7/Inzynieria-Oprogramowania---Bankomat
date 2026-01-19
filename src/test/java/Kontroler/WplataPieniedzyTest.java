package Kontroler;

import Model.IModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Kontroler - WplataPieniedzy (mock)")
@TestMethodOrder(OrderAnnotation.class)
@Tag("kontroler")
@Tag("mock")
class WplataPieniedzyTest {

    @Mock
    private IModel model;

    private static final AtomicInteger AFTER_EACH_COUNTER = new AtomicInteger(0);

    @BeforeAll
    static void setUpBeforeAll() {
        // given
        AFTER_EACH_COUNTER.set(0);
    }

    @AfterEach
    void tearDown() {
        // then
        AFTER_EACH_COUNTER.incrementAndGet();
    }

    @AfterAll
    static void tearDownAfterAll() {
        // then
        assertTrue(AFTER_EACH_COUNTER.get() >= 1);
    }

    @Order(1)
    @Test
    @DisplayName("Powinno księgować wpłatę w scenariuszu pozytywnym")
    void powinnoKsiegowacWplateHappyPath() {
        // given
        when(model.logowanieKlient(111111, 1234)).thenReturn(true);
        when(model.sprawdzenieMiejscaNaGotowke(anyMap())).thenReturn(true);
        when(model.weryfikacjaTransakcjiWBanku(1200, 111111)).thenReturn(true);

        ArgumentCaptor<Integer> kwotaCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> nrKartyCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Boolean> potwierdzenieCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<Map<Integer, Integer>> banknotyCaptor = ArgumentCaptor.forClass(Map.class);

        // when
        WplataPieniedzy sut = new WplataPieniedzy(model, 111111, 1234);

        // then
        verify(model).ksiegowanieWplaty(kwotaCaptor.capture(), nrKartyCaptor.capture(),
                potwierdzenieCaptor.capture(), banknotyCaptor.capture());
        verify(model, times(1)).ksiegowanieWplaty(anyInt(), anyInt(), anyBoolean(), anyMap());

        assertNotNull(sut);
        assertEquals(1200, kwotaCaptor.getValue());
        assertTrue(potwierdzenieCaptor.getValue());
        assertEquals(12, banknotyCaptor.getValue().get(100));
        assertEquals(111111, nrKartyCaptor.getValue());
    }

    @Order(2)
    @ParameterizedTest(name = "Brak logowania dla karty {0}")
    @CsvSource({
            "111111,1234",
            "222222,9999"
    })
    @DisplayName("Nie powinno kontynuować bez logowania klienta")
    void niePowinnoKontynuowacBezLogowania(int nrKarty, int pin) {
        // given
        when(model.logowanieKlient(nrKarty, pin)).thenReturn(false);

        // when
        WplataPieniedzy sut = new WplataPieniedzy(model, nrKarty, pin);

        // then
        verify(model).logowanieKlient(nrKarty, pin);
        verifyNoMoreInteractions(model);
        assertNotNull(sut);
    }

    @Order(3)
    @ParameterizedTest(name = "{0}")
    @MethodSource("negatywneScenariusze")
    @DisplayName("Nie powinno księgować w scenariuszach negatywnych")
    void niePowinnoKsiegowacWNegatywnychScenariuszach(String opis, boolean miejsce, boolean weryfikacja,
                                                     int oczekiwaneWeryfikacje) {
        // given
        when(model.logowanieKlient(111111, 1234)).thenReturn(true);
        when(model.sprawdzenieMiejscaNaGotowke(anyMap())).thenReturn(miejsce);
        when(model.weryfikacjaTransakcjiWBanku(1200, 111111)).thenReturn(weryfikacja);

        // when
        WplataPieniedzy sut = new WplataPieniedzy(model, 111111, 1234);

        // then
        verify(model).logowanieKlient(111111, 1234);
        verify(model, times(1)).sprawdzenieMiejscaNaGotowke(anyMap());
        verify(model, times(oczekiwaneWeryfikacje)).weryfikacjaTransakcjiWBanku(1200, 111111);
        verify(model, never()).ksiegowanieWplaty(anyInt(), anyInt(), anyBoolean(), anyMap());
        assertNotNull(sut);
    }

    static Stream<Arguments> negatywneScenariusze() {
        return Stream.of(
                Arguments.of("Brak miejsca na gotówkę", false, true, 0),
                Arguments.of("Nieudana weryfikacja w banku", true, false, 1)
        );
    }
}
