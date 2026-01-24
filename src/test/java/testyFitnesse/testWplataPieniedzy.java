package testyFitnesse;

import fit.ColumnFixture;

import java.util.HashMap;
import java.util.Map;


public class testWplataPieniedzy extends ColumnFixture {

    public int nrKarty;
    public int pin;
    public int kwota;
    public String banknoty;
    public boolean potwierdzenie;

    private boolean wykonano;
    private boolean wynik;
    private int liczbaTransakcjiPo;
    private int iloscBanknotowPo;
    private boolean czyNowaTransakcjaZPotwierdzeniem;

    public boolean wplataPieniedzy() {
        if (wykonano) return wynik;

        int transPrzed = SetUp.rejestr.dajLiczbeTransakcji();
        int banknotyPrzed = SetUp.sejf.dajIloscBanknotow();

        Map<Integer, Integer> mapaBanknotow = parseBanknoty(this.banknoty);

        SetUp.kontrolerKlienta.wplataPieniedzy(
                nrKarty,
                pin,
                kwota,
                mapaBanknotow,
                potwierdzenie
        );

        liczbaTransakcjiPo = SetUp.rejestr.dajLiczbeTransakcji();
        iloscBanknotowPo = SetUp.sejf.dajIloscBanknotow();

        wynik = (liczbaTransakcjiPo != transPrzed);

        if (wynik) {
            String daneOstatniej = SetUp.rejestr.dajDaneOstatniejTransakcji();
            czyNowaTransakcjaZPotwierdzeniem = (daneOstatniej != null) && daneOstatniej.contains("Klient");
        } else {
            czyNowaTransakcjaZPotwierdzeniem = false;
        }

        wykonano = true;
        return wynik;
    }

    public int dajLiczbeTransakcji() {
        if (!wykonano) wplataPieniedzy();
        return liczbaTransakcjiPo;
    }

    public int dajIloscBanknotow() {
        if (!wykonano) wplataPieniedzy();
        return iloscBanknotowPo;
    }

    public boolean czyNowaTransakcjaZPotwierdzeniem() {
        if (!wykonano) wplataPieniedzy();
        return czyNowaTransakcjaZPotwierdzeniem;
    }


    private static Map<Integer, Integer> parseBanknoty(String txt) {
        Map<Integer, Integer> out = new HashMap<>();
        if (txt == null) return out;

        String s = txt.trim();
        if (s.isEmpty() || s.equals("-")) return out;

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
