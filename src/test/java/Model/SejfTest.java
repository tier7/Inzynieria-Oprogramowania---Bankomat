package Model;

import Model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.stream.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Sejf - obsługa banknotów")
@TestMethodOrder(OrderAnnotation.class)
@Tag("gotowka")
class SejfTest {

    private Map<Integer, Integer> banknoty;
    private Sejf sejf;

    @BeforeEach
    void setUp() {
        banknoty = new HashMap<>();
        banknoty.put(100, 10);
        banknoty.put(50, 5);
        sejf = new Sejf(banknoty);
    }

    @AfterEach
    void tearDown() {
        // then
        banknoty.clear();
        sejf = null;
    }

    @Order(1)
    @DisplayName("Sprawdzenie dostępnego miejsca w sejfie")
    @ParameterizedTest
    @CsvSource({
            "10,true",
            "3000,false"
    })
    void czyMiejsceWSejfie(int noweBanknoty, boolean oczekiwane) {
        // given

        // when
        boolean wynik = sejf.czyMiejsce(noweBanknoty);

        // then
        assertEquals(oczekiwane, wynik);
    }

    @Order(2)
    @DisplayName("Weryfikacja dostępności nominałów")
    @ParameterizedTest
    @MethodSource("kwotyINominaly")
    void czySaNominalyDlaKwoty(int kwota, boolean oczekiwane) {
        // given

        // when
        boolean wynik = sejf.czySaNominaly(kwota);

        // then
        assertEquals(oczekiwane, wynik);
    }

    @Order(3)
    @DisplayName("Aktualizacja stanu sejfu po dodaniu banknotów")
    @Test
    void aktualizacjaStanuDodajeBanknoty() {
        // given
        Map<Integer, Integer> nowe = new HashMap<>();
        nowe.put(100, 2);
        nowe.put(20, 5);

        // when
        sejf.aktualizacjaStanu(nowe);

        // then
        assertEquals(12, banknoty.get(100));
        assertEquals(5, banknoty.get(20));
        assertTrue(sejf.czyMiejsce(1));
    }

    @Order(4)
    @DisplayName("Ignorowanie braku nowych banknotów")
    @Test
    void aktualizacjaStanuPustaMapa() {
        // given
        Map<Integer, Integer> nowe = new HashMap<>();

        // when
        sejf.aktualizacjaStanu(nowe);

        // then
        assertEquals(10, banknoty.get(100));
        assertFalse(sejf.czySaNominaly(130));
    }

    static Stream<Arguments> kwotyINominaly() {
        return Stream.of(
                Arguments.of(150, true),
                Arguments.of(200, true),
                Arguments.of(130, false),
                Arguments.of(0, false)
        );
    }
}
