package Kontroler;

import Model.IModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Kontroler - PobranieDanychOTransakcjach (mock)")
@TestMethodOrder(OrderAnnotation.class)
@Tag("kontroler")
@Tag("mock")
class PobranieDanychOTransakcjachTest {

    private static final int NR_KARTY_ADMIN = 999999;
    private static final int PIN_ADMIN = 4321;
    private static final int NR_KARTY_FILTROWANIE = 100000;
    private static final int PIN_FILTROWANIE = 1111;
    private static final int NR_KARTY_EKSPORT = 555555;
    private static final int PIN_EKSPORT = 2222;

    @Mock
    private IModel model;

    @InjectMocks
    private PobranieDanychOTransakcjach kontroler;

    private static final AtomicInteger AFTER_EACH_COUNTER = new AtomicInteger(0);

    @BeforeAll
    @DisplayName("Przygotowanie liczników przed wszystkimi testami")
    static void setUpBeforeAll() {
        // given
        AFTER_EACH_COUNTER.set(0);
    }

    @AfterEach
    @DisplayName("Sprzątanie po pojedynczym teście")
    void tearDown() {
        // then
        AFTER_EACH_COUNTER.incrementAndGet();
    }

    @AfterAll
    @DisplayName("Weryfikacja sprzątania po wszystkich testach")
    static void tearDownAfterAll() {
        // then
        assertTrue(AFTER_EACH_COUNTER.get() >= 1);
    }

    @Order(1)
    @Test
    @DisplayName("Powinno pobierać transakcje po zalogowaniu pracownika")
    void powinnoPobieracTransakcjePoZalogowaniu() {
        // given
        String[] rekordy = {
                "Data=2025-01-02, Typ=WPLATA, Kwota=100",
                "Data=2025-06-15, Typ=WPLATA, Kwota=300"
        };
        when(model.logowaniePracownik(NR_KARTY_ADMIN, PIN_ADMIN)).thenReturn(true);
        when(model.pobranieWszystkichTransakcji()).thenReturn(rekordy);

        // when
        PobranieDanychOTransakcjach kontroler = new PobranieDanychOTransakcjach(model, NR_KARTY_ADMIN, PIN_ADMIN);
        String[] wynik = kontroler.pobranieListyOperacji();

        // then
        verify(model).logowaniePracownik(NR_KARTY_ADMIN, PIN_ADMIN);
        verify(model, atLeastOnce()).pobranieWszystkichTransakcji();

        assertNotNull(kontroler);
        assertEquals(2, wynik.length);
        assertArrayEquals(rekordy, wynik);
    }

    @Order(2)
    @Test
    @DisplayName("Nie powinno pobierać transakcji bez logowania pracownika")
    void niePowinnoPobieracTransakcjiBezLogowania() {
        // given
        when(model.logowaniePracownik(NR_KARTY_ADMIN, PIN_ADMIN)).thenReturn(false);

        // when
        PobranieDanychOTransakcjach kontroler = new PobranieDanychOTransakcjach(model, NR_KARTY_ADMIN, PIN_ADMIN);

        // then
        verify(model).logowaniePracownik(NR_KARTY_ADMIN, PIN_ADMIN);
        verify(model, never()).pobranieWszystkichTransakcji();
        assertNotNull(kontroler);
    }

    @Order(3)
    @ParameterizedTest(name = "Logowanie: {0}")
    @CsvSource({
            "true",
            "false"
    })
    @DisplayName("Powinno respektować wynik logowania pracownika")
    void powinnoRespektowacLogowaniePracownika(boolean czyZalogowano) {
        // given
        when(model.logowaniePracownik(NR_KARTY_FILTROWANIE, PIN_FILTROWANIE)).thenReturn(czyZalogowano);
        if (czyZalogowano) {
            when(model.pobranieWszystkichTransakcji()).thenReturn(new String[0]);
        }

        // when
        PobranieDanychOTransakcjach kontroler = new PobranieDanychOTransakcjach(
                model, NR_KARTY_FILTROWANIE, PIN_FILTROWANIE);

        // then
        verify(model).logowaniePracownik(NR_KARTY_FILTROWANIE, PIN_FILTROWANIE);
        if (czyZalogowano) {
            verify(model, atLeastOnce()).pobranieWszystkichTransakcji();
        } else {
            verify(model, never()).pobranieWszystkichTransakcji();
        }
        assertNotNull(kontroler);
    }

    @Order(4)
    @ParameterizedTest(name = "Opcja eksportu: {0}")
    @ValueSource(ints = {1, 2, 3})
    @DisplayName("Powinno obsłużyć różne opcje eksportu")
    void powinnoObsluzycRozneOpcjeEksportu(int opcja) {
        // given
        when(model.logowaniePracownik(NR_KARTY_EKSPORT, PIN_EKSPORT)).thenReturn(false);
        PobranieDanychOTransakcjach kontroler = new PobranieDanychOTransakcjach(model, NR_KARTY_EKSPORT, PIN_EKSPORT);

        // when
        assertDoesNotThrow(() -> kontroler.eksportDanych(opcja));

        // then
        verify(model).logowaniePracownik(NR_KARTY_EKSPORT, PIN_EKSPORT);
        assertNotNull(kontroler);
    }

    @Order(5)
    @Test
    @DisplayName("Powinno propagować wyjątek z logowania pracownika")
    void powinnoPropagowacWyjatekZLogowaniaPracownika() {
        // given
        when(model.logowaniePracownik(NR_KARTY_ADMIN, PIN_ADMIN))
                .thenThrow(new RuntimeException("Błąd logowania"));

        // when
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> new PobranieDanychOTransakcjach(model, NR_KARTY_ADMIN, PIN_ADMIN));

        // then
        assertEquals("Błąd logowania", thrown.getMessage());
        verify(model).logowaniePracownik(NR_KARTY_ADMIN, PIN_ADMIN);
    }

    @Order(6)
    @Test
    @DisplayName("Powinno tworzyć kontroler przez InjectMocks")
    void powinnoTworzycKontrolerPrzezInjectMocks() {
        // when
        assertNotNull(kontroler);

        // then
        verify(model).logowaniePracownik(0, 0);
    }
}
