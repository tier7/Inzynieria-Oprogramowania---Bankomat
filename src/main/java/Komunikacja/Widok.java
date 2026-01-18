package main.java.Komunikacja;

public class Widok {
    public static void wyswietlanie(String klasa, String operacja, boolean czyDziala, String tresc) {
        String status = czyDziala ? "OK" : "BŁĄD";
        System.out.println(status + " " + klasa + "." + operacja + "(): " + tresc);
    }
}

