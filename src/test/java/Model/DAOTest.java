package Model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DAO - prawdziwa implementacja")
@TestMethodOrder(OrderAnnotation.class)
@Tag("dao")
class DAOTest {

    private static final AtomicInteger AFTER_EACH_COUNTER = new AtomicInteger(0);
    private DAO dao;

    @BeforeAll
    static void setUpBeforeAll() {
        // given
        AFTER_EACH_COUNTER.set(0);
    }

    @BeforeEach
    void setUp() {
        // given
        dao = new DAO();
    }

    @AfterEach
    void tearDown() {
        // then
        AFTER_EACH_COUNTER.incrementAndGet();
    }

    @AfterAll
    static void tearDownAfterAll() {
        // then
        assertTrue(AFTER_EACH_COUNTER.get() >= 1);
    }

    @Order(1)
    @DisplayName("Powinno zwracać stan początkowy DAO")
    @Test
    void stanPoczatkowyDao() {
        // given

        // when
        int stanGotowki = dao.pobranieStanuGotowki();
        String[] logi = dao.pobranieLogow();

        // then
        assertEquals(0, stanGotowki);
        assertNotNull(logi);
        assertEquals(0, logi.length);
    }

    @Order(2)
    @DisplayName("Powinno weryfikować transakcję w banku")
    @ParameterizedTest
    @CsvSource({
            "100,111111,true",
            "0,111111,false",
            "100,0,false"
    })
    void weryfikacjaTransakcjiWBanku(int kwota, int nrKarty, boolean oczekiwane) {
        // given

        // when
        boolean wynik = dao.weryfikacjaTransakcjiWBanku(kwota, nrKarty);

        // then
        assertEquals(oczekiwane, wynik);
    }

    @Order(3)
    @DisplayName("Powinno zapisywać transakcję i zwracać listę")
    @Test
    void zapisTransakcjiIOdczytListy() {
        // given
        RejestrTransakcji rejestr = new RejestrTransakcji(dao);
        int przed = rejestr.pobranieWszystkichTransakcji().length;

        // when
        rejestr.zapisTransakcji("Data=2025-12-14;Typ=WPLATA;Kwota=200;Nadawca=111111;Adresat=0;NazwaAdresata=Bankomat;Tytul=Wplata", false);
        String[] po = rejestr.pobranieWszystkichTransakcji();

        // then
        assertEquals(przed + 1, po.length);
        assertNotNull(po[po.length - 1]);
        assertTrue(po[po.length - 1].contains("Typ=WPLATA"));
    }
}
