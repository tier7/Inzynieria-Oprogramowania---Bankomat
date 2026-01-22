package Model;

import Model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Transakcja - pobranie danych")
@TestMethodOrder(OrderAnnotation.class)
@Tag("encje")
class TransakcjaTest {

    @Order(1)
    @DisplayName("Dokładny format danych dla pełnych wejść")
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

}
