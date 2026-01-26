package testyFitnesse;

import Kontroler.WplataPieniedzy;
import fit.ColumnFixture;

import java.util.HashMap;
import java.util.Map;

public class testWplataPieniedzy extends ColumnFixture {

    public int nrKarty;
    public int pin;
    public int kwota;
    public int nominal;
    public int sztuk;
    public boolean potwierdzenie;

    public boolean wplataPieniedzy() {

        int transPrzed = SetUp.rejestr.dajLiczbeTransakcji();
        int banknotyPrzed = SetUp.sejf.dajIloscBanknotow();

        Map<Integer, Integer> mapaBanknotow = new HashMap<>();
        if (nominal > 0 && sztuk > 0) {
            mapaBanknotow.put(nominal, sztuk);
        }

        new WplataPieniedzy(SetUp.model, nrKarty, pin, kwota, mapaBanknotow, potwierdzenie);

        int transPo = SetUp.rejestr.dajLiczbeTransakcji();
        int banknotyPo = SetUp.sejf.dajIloscBanknotow();

        return (transPo == transPrzed + 1) && (banknotyPo == banknotyPrzed + sztuk);
    }

    public int dajIloscBanknotow() {
        return SetUp.sejf.dajIloscBanknotow();
    }

    public int dajLiczbeTransakcji() {
        return SetUp.rejestr.dajLiczbeTransakcji();
    }
}
