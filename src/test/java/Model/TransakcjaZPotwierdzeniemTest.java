package main.java.Model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TransakcjaZPotwierdzeniem - pobranie danych")
@TestMethodOrder(OrderAnnotation.class)
@Tag("encje")
class TransakcjaZPotwierdzeniemTest {

    @Order(1)
    @DisplayName("Powinno dodawać dane klienta i salda")
    @ParameterizedTest
    @CsvSource({
            "1000,1200,Jan Kowalski",
            "500,700,Ala Nowak"
    })
    void pobranieDanychRozszerzone(int saldoPrzed, int saldoPo, String klient) {
        // given
        ITransakcja bazowa = new Transakcja("2025-12-14", "WPLATA", 200, 111111, 0, "Bankomat", "Wplata");
        TransakcjaZPotwierdzeniem transakcja = new TransakcjaZPotwierdzeniem(bazowa, saldoPrzed, saldoPo, klient);

        // when
        String wynik = transakcja.pobranieDanych();

        // then
        assertTrue(wynik.contains("Klient: " + klient));
        assertTrue(wynik.contains("SaldoPrzed: " + saldoPrzed));
        assertTrue(wynik.contains("SaldoPo: " + saldoPo));
        assertEquals(1, wynik.split("Klient:").length - 1);
    }

    @Order(2)
    @DisplayName("Powinno zachowywać podstawową treść transakcji")
    @ParameterizedTest
    @MethodSource("daneBazowe")
    void pobranieDanychZachowujeBazoweDane(Transakcja bazowa, String oczekiwanyPrefix) {
        // given
        TransakcjaZPotwierdzeniem transakcja = new TransakcjaZPotwierdzeniem(bazowa, 1000, 1200, "Jan Kowalski");

        // when
        String wynik = transakcja.pobranieDanych();

        // then
        assertTrue(wynik.startsWith(oczekiwanyPrefix));
    }

    @Order(3)
    @DisplayName("Powinno zwracać niepusty rezultat")
    @Test
    void pobranieDanychNiepuste() {
        // given
        ITransakcja bazowa = new Transakcja("2025-12-14", "WYPLATA", 100, 111111, 0, "Bankomat", "Wyplata");
        TransakcjaZPotwierdzeniem transakcja = new TransakcjaZPotwierdzeniem(bazowa, 900, 800, "Jan Kowalski");

        // when
        String wynik = transakcja.pobranieDanych();

        // then
        assertNotNull(wynik);
        assertTrue(wynik.contains("SaldoPo"));
    }

    static Stream<Arguments> daneBazowe() {
        return Stream.of(
                Arguments.of(new Transakcja("2025-01-10", "WPLATA", 300, 111111, 0, "Bankomat", "Wplata"),
                        "[Data=2025-01-10, Typ=WPLATA, Kwota=300 PLN"),
                Arguments.of(new Transakcja("2025-02-11", "PRZELEW", 150, 222222, 333333, "Jan Nowak", "Za obiad"),
                        "[Data=2025-02-11, Typ=PRZELEW, Kwota=150 PLN")
        );
    }
}
