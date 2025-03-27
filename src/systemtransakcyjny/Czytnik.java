package systemtransakcyjny;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.DataFormatException;

public class Czytnik {

    private final SystemTransakcyjny systemTransakcyjny;
    private final Set<IdSpółki> idSpółek;
    private final Portfel portfel;
    private int liczbaInwestorówRandom;
    private int liczbaInwestorówSMA;

    public Czytnik() {
        systemTransakcyjny = new SystemTransakcyjny();
        liczbaInwestorówSMA = 0;
        liczbaInwestorówRandom = 0;
        portfel = new Portfel(0);
        idSpółek = new HashSet<>();
    }

    public SystemTransakcyjny wczytaj(String ścieżka) throws FileNotFoundException, DataFormatException {
        try (Scanner scanner = new Scanner(new File(ścieżka))) {
            int licznik = 0;
            while (scanner.hasNextLine()) {
                String linia = scanner.nextLine();
                if (linia.charAt(0) == '#') {
                    continue;
                }

                if (licznik == 0) {
                    wczytajInwestorów(linia);
                } else if (licznik == 1) {
                    wczytajSpółki(linia);
                } else if (licznik == 2) {
                    wczytajPortfel(linia);
                }

                licznik++;
            }
            if (licznik != 3) {
                throw new DataFormatException("Niepoprawna liczba linii z danymi");
            }

            for (int i = 0; i < liczbaInwestorówRandom; i++) {
                systemTransakcyjny.dodajInwestora(new InwestorRandom(systemTransakcyjny, new Portfel(portfel)));
            }
            for (int i = 0; i < liczbaInwestorówSMA; i++) {
                systemTransakcyjny.dodajInwestora(new InwestorSMA(systemTransakcyjny, new Portfel(portfel)));
            }

            return systemTransakcyjny;
        } catch (IndexOutOfBoundsException e) {
            throw new DataFormatException("Niepoprawna linia");
        }
    }

    private void wczytajInwestorów(String linia) throws DataFormatException {
        try (Scanner scanner = new Scanner(linia)) {
            while (scanner.hasNext()) {
                String s = scanner.next();
                if (s.length() > 1 || (s.charAt(0) != 'R' && s.charAt(0) != 'S')) {
                    throw new DataFormatException("Niepoprawny format inwestorów");
                } else if (s.charAt(0) == 'R') {
                    liczbaInwestorówRandom++;
                } else {
                    liczbaInwestorówSMA++;
                }
            }
        }
    }

    private void wczytajSpółki(String linia) throws DataFormatException {
        try (Scanner scanner = new Scanner(linia)) {
            while (scanner.hasNext()) {
                String s = scanner.next();

                int długośćId = s.indexOf(':');
                if (długośćId == -1) {
                    throw new DataFormatException("Niepoprawny format spółek");
                }

                IdSpółki idSpółki = new IdSpółki(s.substring(0, długośćId));
                int ostatniaCena = Integer.parseInt(s.substring(długośćId + 1));
                if (idSpółek.contains(idSpółki) || ostatniaCena < 1) {
                    throw new DataFormatException("Niepoprawny format spółek");
                }
                idSpółek.add(idSpółki);
                systemTransakcyjny.dodajSpółkę(new Spółka(idSpółki, ostatniaCena));
            }
        } catch (NumberFormatException e) {
            throw new DataFormatException("Niepoprawny format spółek");
        }
    }

    private void wczytajPortfel(String linia) throws DataFormatException {
        try (Scanner scanner = new Scanner(linia)) {

            portfel.dodajGotówkę(Integer.parseInt(scanner.next()));

            while (scanner.hasNext()) {
                String s = scanner.next();

                int długośćId = s.indexOf(':');
                if (długośćId == -1) {
                    throw new DataFormatException("Niepoprawny format portfela");
                }

                IdSpółki idSpółki = new IdSpółki(s.substring(0, długośćId));
                int liczbaAkcji = Integer.parseInt(s.substring(długośćId + 1));
                if (!idSpółek.contains(idSpółki) || liczbaAkcji < 1) {
                    throw new DataFormatException("Niepoprawny format portfela");
                }
                portfel.dodajAkcje(idSpółki, liczbaAkcji);
            }
        } catch (NoSuchElementException | NumberFormatException e) {
            throw new DataFormatException("Niepoprawny format portfela");
        }
    }
}
