package Model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FabrykaTransakcji: tworzenie transakcji")
@TestMethodOrder(OrderAnnotation.class)
@Tag("transakcje")
class FabrykaTransakcjiTest {

    private final FabrykaTransakcji fabryka = new FabrykaTransakcji();

    @Order(1)
    @DisplayName("Tworzenie transakcji dla poprawnych danych")
    @ParameterizedTest
    @CsvSource(value = {
            "Data=2025-12-14;Typ=WPLATA;Kwota=200;Nadawca=111111;Adresat=0;NazwaAdresata=Bankomat;Tytul=Wplata" +
                    "|[Data=2025-12-14, Typ=WPLATA, Kwota=200 PLN, Konto:111111 -> Konto:0]",
            "Data=2025-12-15;Typ=PRZELEW;Kwota=500;Nadawca=222222;Adresat=333333;NazwaAdresata=Jan;Tytul=Za_obiad" +
                    "|[Data=2025-12-15, Typ=PRZELEW, Kwota=500 PLN, Konto:222222 -> Konto:333333]"
    }, delimiter = '|')
    void utworzenieTransakcjiPoprawneDane(String dane, String oczekiwane) {
        // given

        // when
        ITransakcja transakcja = fabryka.utworzenieTransakcji(dane);
        String opis = transakcja.pobranieDanych();

        // then
        assertNotNull(transakcja);
        assertEquals(oczekiwane, opis);
    }

    @Order(2)
    @DisplayName("Wyjątek dla błędnych danych")
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

}
