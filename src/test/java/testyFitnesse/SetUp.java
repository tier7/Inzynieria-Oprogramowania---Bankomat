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

public class SetUp extends Fixture {

    public static IDAO dao;
    public static Sejf sejf;
    public static RejestrTransakcji rejestr;

    public static IModel model;

    public static KontrolerKlienta kontrolerKlienta;

    public SetUp() {
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
