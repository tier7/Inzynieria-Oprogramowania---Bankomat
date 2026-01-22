package Model;

import org.junit.jupiter.api.BeforeEach;
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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Model")
@TestMethodOrder(OrderAnnotation.class)
@Tag("gotowka")
class ModelTest {

    private DAO dao;
    private RejestrTransakcji rejestr;
    private Map<Integer, Integer> banknoty;
    private Sejf sejf;
    private Model model;

    @BeforeEach
    void setUp() {
        dao = new DAO();
        banknoty = new HashMap<>();
        banknoty.put(100, 10);
        banknoty.put(50, 5);
        sejf = new Sejf(banknoty);
        rejestr = new RejestrTransakcji(dao);
        model = new Model(dao, rejestr, sejf);
    }

    @Order(1)
    @DisplayName("Sprawdzenie miejsca na gotówkę w sejfie")
    @ParameterizedTest
    @MethodSource("mapyBanknotow")
    void sprawdzenieMiejscaNaGotowke(Map<Integer, Integer> nowe, boolean oczekiwane) {
        // given

        // when
        boolean wynik = model.sprawdzenieMiejscaNaGotowke(nowe);

        // then
        assertEquals(oczekiwane, wynik);
    }

    @Order(2)
    @DisplayName("Delegowanie weryfikacji transakcji do DAO")
    @ParameterizedTest
    @CsvSource({
            "100,111111,true",
            "0,111111,false",
            "100,0,false"
    })
    void weryfikacjaTransakcjiWBanku(int kwota, int nrKarty, boolean oczekiwane) {
        // given

        // when
        boolean wynik = model.weryfikacjaTransakcjiWBanku(kwota, nrKarty);

        // then
        assertEquals(oczekiwane, wynik);
    }

    @Order(3)
    @DisplayName("Księgowanie wpłaty i aktualizacja stanu")
    @Test
    void ksiegowanieWplatyAktualizujeStan() {
        // given
        int przed = rejestr.pobranieWszystkichTransakcji().length;
        Map<Integer, Integer> nowe = new HashMap<>();
        nowe.put(100, 2);

        // when
        model.ksiegowanieWplaty(200, 111111, false, nowe);
        String[] po = rejestr.pobranieWszystkichTransakcji();

        // then
        assertEquals(przed + 1, po.length);
        assertEquals(12, banknoty.get(100));
        assertTrue(po[po.length - 1].contains("Kwota=200"));
        assertTrue(po[po.length - 1].contains("Konto:111111"));
    }

    static Stream<Arguments> mapyBanknotow() {
        Map<Integer, Integer> male = new HashMap<>();
        male.put(100, 1);

        Map<Integer, Integer> duze = new HashMap<>();
        duze.put(100, 4000);

        return Stream.of(
                Arguments.of(male, true),
                Arguments.of(duze, false),
                Arguments.of(null, true)
        );
    }
}
