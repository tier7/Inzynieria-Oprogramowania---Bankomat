package Kontroler;

import Model.IModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
}
