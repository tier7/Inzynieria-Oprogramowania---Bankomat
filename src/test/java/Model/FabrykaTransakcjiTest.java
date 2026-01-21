package Model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("FabrykaTransakcji: tworzenie transakcji")
@TestMethodOrder(OrderAnnotation.class)
@Tag("transakcje")
class FabrykaTransakcjiTest {

    private final FabrykaTransakcji fabryka = new FabrykaTransakcji();

    @Order(1)
    @DisplayName("Powinno tworzyć transakcję dla poprawnych danych")
    @ParameterizedTest
    @CsvSource({
            "Data=2025-12-14;Typ=WPLATA;Kwota=200;Nadawca=111111;Adresat=0;NazwaAdresata=Bankomat;Tytul=Wplata,200",
            "Data=2025-12-15;Typ=PRZELEW;Kwota=500;Nadawca=222222;Adresat=333333;NazwaAdresata=Jan;Tytul=Za_obiad,500"
    })
    void utworzenieTransakcjiPoprawneDane(String dane, int kwota) {
        // given

        // when
        ITransakcja transakcja = fabryka.utworzenieTransakcji(dane);
        String opis = transakcja.pobranieDanych();

        // then
        assertNotNull(transakcja);
        assertTrue(opis.contains("Kwota=" + kwota + " PLN"));
    }

    @Order(2)
    @DisplayName("Powinno rzucać wyjątek dla błędnych danych")
    @ParameterizedTest
    @ValueSource(strings = {
            "Data=2025-12-14;Typ=WPLATA;Kwota=0;Nadawca=111111",
            "Data=2025-12-14;Typ=WPLATA;Kwota=-10;Nadawca=111111",
            "Data=2025-12-14;Typ=WPLATA;Kwota=200;Nadawca=0"
    })
    void utworzenieTransakcjiBledneDane(String dane) {
        // given

        // when
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> fabryka.utworzenieTransakcji(dane));

        // then
        assertTrue(ex.getMessage().contains("Błędne dane transakcji"));
    }

    @Order(3)
    @DisplayName("Powinno zwracać transakcję z poprawnym formatem danych")
    @Test
    void utworzenieTransakcjiFormat() {
        // given
        String dane = "Data=2025-12-16;Typ=WYPLATA;Kwota=300;Nadawca=444444;Adresat=0;NazwaAdresata=Bankomat;Tytul=Wyplata";

        // when
        ITransakcja transakcja = fabryka.utworzenieTransakcji(dane);
        String opis = transakcja.pobranieDanych();

        // then
        assertEquals("[Data=2025-12-16, Typ=WYPLATA, Kwota=300 PLN, Konto:444444 -> Konto:0]", opis);
    }
}
