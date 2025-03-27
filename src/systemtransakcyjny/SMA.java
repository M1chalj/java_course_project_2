package systemtransakcyjny;

import java.util.LinkedList;
import java.util.Queue;

public class SMA {
    private final Queue<Integer> kolejkaDanych;
    private final int liczbaDanych;
    private int suma;

    public SMA(int liczbaDanych) {
        this.liczbaDanych = liczbaDanych;
        if (liczbaDanych <= 0) {
            throw new IllegalArgumentException("Liczba danych musi być dodatnia");
        }
        suma = 0;
        kolejkaDanych = new LinkedList<>();
    }

    public void następnaWartość(int wartość) {
        if (kolejkaDanych.size() >= liczbaDanych) {
            suma -= kolejkaDanych.remove();
        }
        suma += wartość;
        kolejkaDanych.add(wartość);
    }

    public double średnia() {
        if (kolejkaDanych.size() < liczbaDanych) {
            throw new UnsupportedOperationException("Średnia jeszcze nie istnieje");
        }
        return (double) suma / liczbaDanych;
    }
}
