package Kontroler;

import Model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;
import java.util.stream.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WplataPieniedzy")
@TestMethodOrder(OrderAnnotation.class)
@Tag("kontroler")
@Tag("mock")
class WplataPieniedzyTest {

    private static final int NR_KARTY = 111111;
    private static final int PIN = 1234;
    private static final int DEKLAROWANA_KWOTA = 1200;

    @Mock
    private IModel model;

    @Order(1)
    @Test
    @DisplayName("Pozytywny scenariusz księgowania wpłaty")
    void ksiegowanieWplatyScenariuszPozytywny() {
        // given
        when(model.logowanieKlient(NR_KARTY, PIN)).thenReturn(true);
        when(model.sprawdzenieMiejscaNaGotowke(anyMap())).thenReturn(true);
        when(model.weryfikacjaTransakcjiWBanku(DEKLAROWANA_KWOTA, NR_KARTY)).thenReturn(true);

        ArgumentCaptor<Integer> kwotaCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> nrKartyCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Boolean> potwierdzenieCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<Map<Integer, Integer>> banknotyCaptor = ArgumentCaptor.forClass(Map.class);

        // when
        WplataPieniedzy kontroler = new WplataPieniedzy(model, NR_KARTY, PIN);

        // then
        verify(model).ksiegowanieWplaty(kwotaCaptor.capture(), nrKartyCaptor.capture(),
                potwierdzenieCaptor.capture(), banknotyCaptor.capture());
        verify(model, times(1)).ksiegowanieWplaty(anyInt(), anyInt(), anyBoolean(), anyMap());

        assertNotNull(kontroler);
        assertEquals(DEKLAROWANA_KWOTA, kwotaCaptor.getValue());
        assertTrue(potwierdzenieCaptor.getValue());
        assertEquals(12, banknotyCaptor.getValue().get(100));
        assertEquals(NR_KARTY, nrKartyCaptor.getValue());
    }

    @Order(2)
    @ParameterizedTest(name = "Brak logowania dla karty {0}")
    @CsvSource({
            "111111,1234",
            "222222,9999"
    })
    @DisplayName("Brak kontynuacji bez logowania")
    void niePowinnoKontynuowacBezLogowania(int nrKarty, int pin) {
        // given
        when(model.logowanieKlient(nrKarty, pin)).thenReturn(false);

        // when
        WplataPieniedzy kontroler = new WplataPieniedzy(model, nrKarty, pin);

        // then
        verify(model).logowanieKlient(nrKarty, pin);
        verifyNoMoreInteractions(model);
        assertNotNull(kontroler);
    }

    @Order(3)
    @ParameterizedTest(name = "{0}")
    @MethodSource("negatywneScenariusze")
    @DisplayName("Brak księgowania w scenariuszach negatywnych")
    void ksiegowanieWplatyScenariuszNegatywny(String opis, boolean miejsce, boolean weryfikacja, int oczekiwaneWeryfikacje) {
        // given
        when(model.logowanieKlient(NR_KARTY, PIN)).thenReturn(true);
        when(model.sprawdzenieMiejscaNaGotowke(anyMap())).thenReturn(miejsce);
        if (miejsce) {
            when(model.weryfikacjaTransakcjiWBanku(DEKLAROWANA_KWOTA, NR_KARTY)).thenReturn(weryfikacja);
        }

        // when
        WplataPieniedzy kontroler = new WplataPieniedzy(model, NR_KARTY, PIN);

        // then
        verify(model).logowanieKlient(NR_KARTY, PIN);
        verify(model, times(1)).sprawdzenieMiejscaNaGotowke(anyMap());
        verify(model, times(oczekiwaneWeryfikacje)).weryfikacjaTransakcjiWBanku(DEKLAROWANA_KWOTA, NR_KARTY);
        verify(model, never()).ksiegowanieWplaty(anyInt(), anyInt(), anyBoolean(), anyMap());
        assertNotNull(kontroler);
    }

    static Stream<Arguments> negatywneScenariusze() {
        return Stream.of(
                Arguments.of("Brak miejsca na gotówkę", false, true, 0),
                Arguments.of("Nieudana weryfikacja w banku", true, false, 1)
        );
    }

    @Order(4)
    @Test
    @DisplayName("Obsługa wyjątku podczas księgowania")
    void wyjatekKsiegowanie() {
        // given
        when(model.logowanieKlient(NR_KARTY, PIN)).thenReturn(true);
        when(model.sprawdzenieMiejscaNaGotowke(anyMap())).thenReturn(true);
        when(model.weryfikacjaTransakcjiWBanku(DEKLAROWANA_KWOTA, NR_KARTY)).thenReturn(true);
        doThrow(new IllegalStateException("Błąd księgowania"))
                .when(model)
                .ksiegowanieWplaty(anyInt(), anyInt(), anyBoolean(), anyMap());

        // when
        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> new WplataPieniedzy(model, NR_KARTY, PIN));

        // then
        assertEquals("Błąd księgowania", thrown.getMessage());
        verify(model).ksiegowanieWplaty(anyInt(), anyInt(), anyBoolean(), anyMap());
    }
}
