package Model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DAO")
@TestMethodOrder(OrderAnnotation.class)
@Tag("dao")
class DAOTest {

    private DAO dao;

    @BeforeEach
    void setUp() {
        // given
        dao = new DAO();
    }


    @Order(1)
    @DisplayName("Stan początkowy DAO")
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
    @DisplayName("Weryfikacja transakcji w banku")
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
    @DisplayName("Zapis transakcji i zwrot listy")
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
