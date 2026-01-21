package Model;

import Model.IDAO;
import Model.RejestrTransakcji;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("RejestrTransakcji - zapis i pobranie danych")
@TestMethodOrder(OrderAnnotation.class)
@Tag("transakcje")
class RejestrTransakcjiTest {

    private static final AtomicInteger AFTER_EACH_COUNTER = new AtomicInteger(0);

    @BeforeAll
    static void setUpBeforeAll() {
        // given
        AFTER_EACH_COUNTER.set(0);
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
    @DisplayName("Powinno zwrócić wszystkie domyślne transakcje")
    @Test
    void pobranieWszystkichTransakcjiDomyslne() {
        // given
        TestDAO dao = new TestDAO();
        RejestrTransakcji rejestr = new RejestrTransakcji(dao);

        // when
        String[] transakcje = rejestr.pobranieWszystkichTransakcji();

        // then
        assertNotNull(transakcje);
        assertEquals(4, transakcje.length);
        assertTrue(transakcje[0].contains("WPLATA"));
    }

    @Order(2)
    @DisplayName("Powinno zapisywać transakcje z opcjonalnym potwierdzeniem")
    @ParameterizedTest
    @CsvSource({
            "Data=2025-12-14;Typ=WPLATA;Kwota=200;Nadawca=111111;Adresat=0;NazwaAdresata=Bankomat;Tytul=Wplata,true,SaldoPrzed",
            "Data=2025-12-14;Typ=WYPLATA;Kwota=100;Nadawca=111111;Adresat=0;NazwaAdresata=Bankomat;Tytul=Wyplata,false,Typ=WYPLATA"
    })
    void zapisTransakcjiZPotwierdzeniem(String dane, boolean potwierdzenie, String oczekiwanyFragment) {
        // given
        TestDAO dao = new TestDAO();
        RejestrTransakcji rejestr = new RejestrTransakcji(dao);
        int przed = rejestr.pobranieWszystkichTransakcji().length;

        // when
        rejestr.zapisTransakcji(dane, potwierdzenie);
        String[] po = rejestr.pobranieWszystkichTransakcji();

        // then
        assertEquals(przed + 1, po.length);
        assertNotNull(dao.lastZaksiegowanie);
        assertTrue(dao.lastZaksiegowanie.contains(oczekiwanyFragment));
    }

    @Order(3)
    @DisplayName("Powinno odrzucać błędne dane transakcji")
    @ParameterizedTest
    @ValueSource(strings = {
            "Data=2025-12-14;Typ=WPLATA;Kwota=0;Nadawca=111111",
            "Data=2025-12-14;Typ=WPLATA;Kwota=-5;Nadawca=111111",
            "Data=2025-12-14;Typ=WPLATA;Kwota=100;Nadawca=0"
    })
    void zapisTransakcjiBledneDane(String dane) {
        // given
        TestDAO dao = new TestDAO();
        RejestrTransakcji rejestr = new RejestrTransakcji(dao);

        // when
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> rejestr.zapisTransakcji(dane, false));

        // then
        assertTrue(ex.getMessage().contains("Błędne dane transakcji"));
    }

    static class TestDAO implements IDAO {

        private String lastZaksiegowanie;
        private final List<String> logi = new ArrayList<>();

        @Override
        public boolean uwierzytelnienieKlienta(int nrKarty, int pin) {
            return false;
        }

        @Override
        public boolean uwierzytelnieniePracownika(int nrKarty, int pin) {
            return false;
        }

        @Override
        public int pobranieStanuGotowki() {
            return 0;
        }

        @Override
        public void aktualizacjaStanuGotowki(int kwota) {
        }

        @Override
        public boolean zaksiegowanieOperacji(String dane) {
            this.lastZaksiegowanie = dane;
            return true;
        }

        @Override
        public String[] pobranieLogow() {
            return logi.toArray(new String[0]);
        }

        @Override
        public void zapisanieLogow(String informacje) {
            logi.add(informacje);
        }

        @Override
        public boolean weryfikacjaTransakcjiWBanku(int kwota, int nrKarty) {
            return kwota > 0 && nrKarty > 0;
        }
    }
}
