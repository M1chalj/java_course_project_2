package systemtransakcyjny;

import main.Losowanie;

public enum TypZlecenia {
    KUPNO,
    SPRZEDAŻ;

    public static TypZlecenia losowy() {
        return values()[Losowanie.losuj(0, values().length - 1)];
    }
}