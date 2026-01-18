package Model;

import main.java.Model.Transakcja;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Transakcja - pobranie danych")
@TestMethodOrder(OrderAnnotation.class)
@Tag("encje")
class TransakcjaTest {

    @Order(1)
    @DisplayName("Powinno zawierać kluczowe pola dla różnych kwot")
    @ParameterizedTest
    @ValueSource(ints = {100, 250, 999})
    void pobranieDanychZawieraKluczowePola(int kwota) {
        // given
        Transakcja transakcja = new Transakcja("2025-12-14", "WPLATA", kwota, 111111, 222222, "Bankomat", "Wplata");

        // when
        String wynik = transakcja.pobranieDanych();

        // then
        assertNotNull(wynik);
        assertTrue(wynik.contains("Typ=WPLATA"));
        assertTrue(wynik.contains("Kwota=" + kwota + " PLN"));
    }

    @Order(2)
    @DisplayName("Powinno zwrócić dokładny format danych dla pełnych wejść")
    @ParameterizedTest
    @CsvSource(value = {
            "2025-01-01|PRZELEW|500|111111|333333|[Data=2025-01-01, Typ=PRZELEW, Kwota=500 PLN, Konto:111111 -> Konto:333333]",
            "2025-02-10|WYPLATA|200|222222|0|[Data=2025-02-10, Typ=WYPLATA, Kwota=200 PLN, Konto:222222 -> Konto:0]"
    }, delimiter = '|')
    void pobranieDanychDokladnyFormat(String data, String typ, int kwota, int nadawca, int adresat, String oczekiwane) {
        // given
        Transakcja transakcja = new Transakcja(data, typ, kwota, nadawca, adresat, "Adresat", "Tytul");

        // when
        String wynik = transakcja.pobranieDanych();

        // then
        assertEquals(oczekiwane, wynik);
    }

    @Order(3)
    @DisplayName("Powinno zaczynać się od daty i zawierać oba konta")
    @Test
    void pobranieDanychZawieraKonta() {
        // given
        Transakcja transakcja = new Transakcja("2025-12-20", "WPLATA", 300, 123123, 321321, "Adresat", "Tytul");

        // when
        String wynik = transakcja.pobranieDanych();

        // then
        assertTrue(wynik.startsWith("[Data=2025-12-20"));
        assertTrue(wynik.contains("Konto:123123 -> Konto:321321"));
    }
}
