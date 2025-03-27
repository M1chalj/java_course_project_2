package main;

import tradingsystem.DataReader;
import tradingsystem.Wallet;
import tradingsystem.tradingSystem;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.zip.DataFormatException;

public class Main {
    public static void main(String[] args) {
        try {
            DataReader dataReader = new DataReader();
            tradingSystem tradingSystem = dataReader.read(args[0]);

            tradingSystem.symuluj(Integer.parseInt(args[1]));

            List<Wallet> wallets = tradingSystem.portfele();
            for (Wallet wallet : wallets) {
                System.out.println(wallet);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error - invalid number");
        } catch (FileNotFoundException | DataFormatException e) {
            System.out.println("Error - invalid data. " + e.getMessage());
        }
    }
}
