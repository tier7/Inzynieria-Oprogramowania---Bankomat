package Model;

import Model.Transakcja;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
