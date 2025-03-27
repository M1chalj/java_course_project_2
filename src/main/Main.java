package main;

import systemtransakcyjny.Czytnik;
import systemtransakcyjny.Portfel;
import systemtransakcyjny.SystemTransakcyjny;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.zip.DataFormatException;

public class Main {
    public static void main(String[] args) {
        try {
            Czytnik czytnikDanych = new Czytnik();
            SystemTransakcyjny systemTransakcyjny = czytnikDanych.wczytaj(args[0]);

            systemTransakcyjny.symuluj(Integer.parseInt(args[1]));

            List<Portfel> portfele = systemTransakcyjny.portfele();
            for (Portfel portfel : portfele) {
                System.out.println(portfel);
            }
        } catch (NumberFormatException e) {
            System.out.println("Błąd - niepoprawna liczba");
        } catch (FileNotFoundException | DataFormatException e) {
            System.out.println("Błąd - niepoprawne dane. " + e.getMessage());
        }
    }
}
