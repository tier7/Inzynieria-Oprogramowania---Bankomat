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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    @DisplayName("Powinno pobierać transakcje po zalogowaniu pracownika")
    void powinnoPobieracTransakcjePoZalogowaniu() {
        // given
        String[] rekordy = {
                "Data=2025-01-02, Typ=WPLATA, Kwota=100",
                "Data=2025-06-15, Typ=WPLATA, Kwota=300"
        };
        when(model.logowaniePracownik(999999, 4321)).thenReturn(true);
        when(model.pobranieWszystkichTransakcji()).thenReturn(rekordy);

        // when
        PobranieDanychOTransakcjach sut = new PobranieDanychOTransakcjach(model, 999999, 4321);
        String[] wynik = sut.pobranieListyOperacji();

        // then
        verify(model).logowaniePracownik(999999, 4321);
        verify(model, atLeastOnce()).pobranieWszystkichTransakcji();

        assertNotNull(sut);
        assertEquals(2, wynik.length);
        assertArrayEquals(rekordy, wynik);
    }

    @Order(2)
    @Test
    @DisplayName("Nie powinno pobierać transakcji bez logowania pracownika")
    void niePowinnoPobieracTransakcjiBezLogowania() {
        // given
        when(model.logowaniePracownik(999999, 4321)).thenReturn(false);

        // when
        PobranieDanychOTransakcjach sut = new PobranieDanychOTransakcjach(model, 999999, 4321);

        // then
        verify(model).logowaniePracownik(999999, 4321);
        verify(model, never()).pobranieWszystkichTransakcji();
        assertNotNull(sut);
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
        when(model.logowaniePracownik(100000, 1111)).thenReturn(czyZalogowano);
        when(model.pobranieWszystkichTransakcji()).thenReturn(new String[0]);

        // when
        PobranieDanychOTransakcjach sut = new PobranieDanychOTransakcjach(model, 100000, 1111);

        // then
        verify(model).logowaniePracownik(100000, 1111);
        if (czyZalogowano) {
            verify(model, atLeastOnce()).pobranieWszystkichTransakcji();
        } else {
            verify(model, never()).pobranieWszystkichTransakcji();
        }
        assertNotNull(sut);
    }

    @Order(4)
    @ParameterizedTest(name = "Opcja eksportu: {0}")
    @ValueSource(ints = {1, 2, 3})
    @DisplayName("Powinno obsłużyć różne opcje eksportu")
    void powinnoObsluzycRozneOpcjeEksportu(int opcja) {
        // given
        when(model.logowaniePracownik(555555, 2222)).thenReturn(false);
        PobranieDanychOTransakcjach sut = new PobranieDanychOTransakcjach(model, 555555, 2222);

        // when
        assertDoesNotThrow(() -> sut.eksportDanych(opcja));

        // then
        verify(model).logowaniePracownik(555555, 2222);
        assertNotNull(sut);
    }
}
