package Model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Model - logika biznesowa gotówki")
@TestMethodOrder(OrderAnnotation.class)
@Tag("gotowka")
class ModelTest {

    private static final AtomicInteger AFTER_EACH_COUNTER = new AtomicInteger(0);
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

    @BeforeAll
    static void setUpBeforeAll() {
        // given
        AFTER_EACH_COUNTER.set(0);
    }

    @AfterAll
    static void tearDownAfterAll() {
        // then
        assertTrue(AFTER_EACH_COUNTER.get() >= 1);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        // then
        AFTER_EACH_COUNTER.incrementAndGet();
    }

    @Order(1)
    @DisplayName("Powinno sprawdzać miejsce na gotówkę w sejfie")
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
    @DisplayName("Powinno delegować weryfikację transakcji do DAO")
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
    @DisplayName("Powinno księgować wpłatę i aktualizować stan")
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
        assertTrue(po[po.length - 1].contains("Nadawca=111111"));
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
