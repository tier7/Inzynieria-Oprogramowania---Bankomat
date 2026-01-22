package Model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RejestrTransakcji - zapis i pobranie danych")
@TestMethodOrder(OrderAnnotation.class)
@Tag("transakcje")
class RejestrTransakcjiTest {

    @Order(1)
    @DisplayName("Wszystkie domyślne transakcje")
    @Test
    void pobranieWszystkichTransakcjiDomyslne() {
        // given
        DAO dao = new DAO();
        RejestrTransakcji rejestr = new RejestrTransakcji(dao);

        // when
        String[] transakcje = rejestr.pobranieWszystkichTransakcji();

        // then
        assertNotNull(transakcje);
        assertEquals(4, transakcje.length);
        assertTrue(transakcje[0].contains("WPLATA"));
    }

    @Order(2)
    @DisplayName("Zapis transakcji z opcjonalnym potwierdzeniem")
    @ParameterizedTest
    @CsvSource({
            "Data=2025-12-14;Typ=WPLATA;Kwota=200;Nadawca=111111;Adresat=0;NazwaAdresata=Bankomat;Tytul=Wplata,true,SaldoPrzed",
            "Data=2025-12-14;Typ=WYPLATA;Kwota=100;Nadawca=111111;Adresat=0;NazwaAdresata=Bankomat;Tytul=Wyplata,false,Typ=WYPLATA"
    })
    void zapisTransakcjiZPotwierdzeniem(String dane, boolean potwierdzenie, String oczekiwanyFragment) {
        // given
        DAO dao = new DAO();
        RejestrTransakcji rejestr = new RejestrTransakcji(dao);
        int przed = rejestr.pobranieWszystkichTransakcji().length;

        // when
        rejestr.zapisTransakcji(dane, potwierdzenie);
        String[] po = rejestr.pobranieWszystkichTransakcji();

        // then
        assertEquals(przed + 1, po.length);
        assertNotNull(po[po.length - 1]);
        assertTrue(po[po.length - 1].contains(oczekiwanyFragment));
    }

    @Order(3)
    @DisplayName("Odrzucenie błędnych danych transakcji")
    @ParameterizedTest
    @ValueSource(strings = {
            "Data=2025-12-14;Typ=WPLATA;Kwota=0;Nadawca=111111",
            "Data=2025-12-14;Typ=WPLATA;Kwota=-5;Nadawca=111111",
            "Data=2025-12-14;Typ=WPLATA;Kwota=100;Nadawca=0"
    })
    void zapisTransakcjiBledneDane(String dane) {
        // given
        DAO dao = new DAO();
        RejestrTransakcji rejestr = new RejestrTransakcji(dao);

        // when
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> rejestr.zapisTransakcji(dane, false));

        // then
        assertTrue(ex.getMessage().contains("Błędne dane transakcji"));
    }
}
