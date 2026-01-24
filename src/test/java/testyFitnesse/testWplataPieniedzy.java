package testyFitnesse;

import fit.ColumnFixture;

import java.util.HashMap;
import java.util.Map;

/**
 * FitNesse - test akceptacyjny PU02: "Wpłata pieniędzy".
 *
 * W tabeli FitNesse ustawiamy pola wejściowe (nrKarty, pin, deklarowanaKwota, banknoty, potwierdzenie),
 * a metody z "?" w nagłówkach kolumn zwracają wyniki do asercji.
 */
public class testWplataPieniedzy extends ColumnFixture {

    // ----------- WEJŚCIA (kolumny bez znaku "?") -----------
    public int nrKarty;
    public int pin;
    public int kwota;

    /**
     * Format: "100:12,5
     * 0:2" albo "100=12,50=2" (separatory ','; pary ':' lub '=').
     * Pusty string / '-' oznacza brak banknotów.
     */
    public String banknoty;

    /** Czy klient chce wydrukować potwierdzenie? */
    public boolean potwierdzenie;

    // ----------- PAMIĘĆ WYNIKU DLA JEDNEGO WIERSZA (żeby nie wywołać PU kilka razy) -----------
    private boolean wykonano;
    private boolean wynik;
    private int liczbaTransakcjiPo;
    private int iloscBanknotowPo;
    private boolean czyNowaTransakcjaZPotwierdzeniem;

    /**
     * Metoda wykonywana w tabeli jako "wplataPieniedzy?".
     * Wykonuje PU przez warstwę kontroli i zwraca TRUE, gdy stan encji się zmienił (zaksięgowano wpłatę).
     */
    public boolean wplataPieniedzy() {
        if (wykonano) return wynik;

        int transPrzed = SetUp.rejestr.dajLiczbeTransakcji();
        int banknotyPrzed = SetUp.sejf.dajIloscBanknotow();

        Map<Integer, Integer> mapaBanknotow = parseBanknoty(this.banknoty);

        // Wywołanie PU możliwie "jak najwyżej" – przez KontrolerKlienta
        SetUp.kontrolerKlienta.wplataPieniedzy(
                nrKarty,
                pin,
                kwota,
                mapaBanknotow,
                potwierdzenie
        );

        liczbaTransakcjiPo = SetUp.rejestr.dajLiczbeTransakcji();
        iloscBanknotowPo = SetUp.sejf.dajIloscBanknotow();

        // Sukces = zmieniła się liczba transakcji (zarejestrowano wpłatę).
        wynik = (liczbaTransakcjiPo != transPrzed);

        if (wynik) {
            String daneOstatniej = SetUp.rejestr.dajDaneOstatniejTransakcji();
            // W TransakcjaZPotwierdzeniem pobranieDanych() dodaje fragmenty o kliencie/saldach.
            czyNowaTransakcjaZPotwierdzeniem = (daneOstatniej != null) && daneOstatniej.contains("Klient");
        } else {
            czyNowaTransakcjaZPotwierdzeniem = false;
        }

        wykonano = true;
        return wynik;
    }

    /** Kolumna w tabeli: "dajLiczbeTransakcji?" */
    public int dajLiczbeTransakcji() {
        if (!wykonano) wplataPieniedzy();
        return liczbaTransakcjiPo;
    }

    /** Kolumna w tabeli: "dajIloscBanknotow?" */
    public int dajIloscBanknotow() {
        if (!wykonano) wplataPieniedzy();
        return iloscBanknotowPo;
    }

    /** Kolumna w tabeli: "czyNowaTransakcjaZPotwierdzeniem?" */
    public boolean czyNowaTransakcjaZPotwierdzeniem() {
        if (!wykonano) wplataPieniedzy();
        return czyNowaTransakcjaZPotwierdzeniem;
    }

    // ----------------- PARSOWANIE BANKNOTÓW -----------------

    private static Map<Integer, Integer> parseBanknoty(String txt) {
        Map<Integer, Integer> out = new HashMap<>();
        if (txt == null) return out;

        String s = txt.trim();
        if (s.isEmpty() || s.equals("-")) return out;

        // normalizujemy separator na ':'
        s = s.replace('=', ':');

        String[] pairs = s.split(",");
        for (String p : pairs) {
            String pair = p.trim();
            if (pair.isEmpty()) continue;

            String[] kv = pair.split(":");
            if (kv.length != 2) continue;

            try {
                int nominal = Integer.parseInt(kv[0].trim());
                int szt = Integer.parseInt(kv[1].trim());
                if (nominal > 0 && szt > 0) out.put(nominal, szt);
            } catch (Exception ignored) {
                // błędny format w komórce tabeli = ignorujemy daną parę
            }
        }
        return out;
    }
    @Override
    public void reset() {
        wykonano = false;
        wynik = false;
        liczbaTransakcjiPo = 0;
        iloscBanknotowPo = 0;
        czyNowaTransakcjaZPotwierdzeniem = false;

        new SetUp();
    }

}
