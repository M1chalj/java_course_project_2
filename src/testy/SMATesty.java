package testy;

import org.junit.jupiter.api.Test;
import tradingsystem.SMA;

import static org.junit.jupiter.api.Assertions.*;

public class SMATesty {

    @Test
    public void poprawneUżycie() {
        SMA średnia = new SMA(5);
        for (int i = 0; i < 10; i++) {
            średnia.nextValue(i);
        }
        assertEquals(średnia.average(), 7);
        średnia.nextValue(10);
        assertEquals(średnia.average(), 8);
    }

    @Test
    public void niepoprawneUżycie() {
        SMA średnia = new SMA(100);
        assertThrows(UnsupportedOperationException.class, średnia::average);
        for (int i = 0; i < 99; i++) {
            średnia.nextValue(5);
        }
        assertThrows(UnsupportedOperationException.class, średnia::average);
        średnia.nextValue(5);
        assertEquals(średnia.average(), 5);
    }

    @Test
    public void niepoprawneTworzenie() {
        assertThrows(IllegalArgumentException.class, () -> new SMA(-5));
        assertThrows(IllegalArgumentException.class, () -> new SMA(0));
    }
}
