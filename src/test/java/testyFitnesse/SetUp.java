package testyFitnesse;

import Kontroler.KontrolerKlienta;
import Model.DAO;
import Model.IDAO;
import Model.IModel;
import Model.Model;
import Model.RejestrTransakcji;
import Model.Sejf;
import fit.Fixture;

import java.util.HashMap;
import java.util.Map;

/**
 * FitNesse - SetUp.
 *
 * SetUp inicjalizuje obiekty systemu (łańcuch zależności) i udostępnia je jako pola statyczne,
 * aby klasy testów (ColumnFixture) mogły:
 *  - wywołać operację realizacji PU w warstwie kontroli,
 *  - sprawdzić stan warstwy encji (np. sejf/rejestr) przed i po wykonaniu PU.
 */
public class SetUp extends Fixture {

    // Warstwa danych / encji
    public static IDAO dao;
    public static Sejf sejf;
    public static RejestrTransakcji rejestr;

    // Warstwa modelu
    public static IModel model;

    // Warstwa kontroli (wywołujemy PU możliwie "jak najwyżej")
    public static KontrolerKlienta kontrolerKlienta;

    public SetUp() {
        // Stan początkowy sejfu – łącznie 160 banknotów
        Map<Integer, Integer> startoweBanknoty = new HashMap<>();
        startoweBanknoty.put(100, 50);
        startoweBanknoty.put(50, 100);
        startoweBanknoty.put(20, 10);

        dao = new DAO();
        sejf = new Sejf(startoweBanknoty);
        rejestr = new RejestrTransakcji(dao);
        model = new Model(dao, rejestr, sejf);
        kontrolerKlienta = new KontrolerKlienta(model);
    }
}
