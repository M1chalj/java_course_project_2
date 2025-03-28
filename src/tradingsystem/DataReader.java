package tradingsystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.DataFormatException;

public class DataReader {

    private final tradingSystem tradingSystem;
    private final Set<CompanyId> companyIDs;
    private final Wallet wallet;
    private int randomInvestorsNumber;
    private int SMAInvestorsNumber;

    public DataReader() {
        tradingSystem = new tradingSystem();
        SMAInvestorsNumber = 0;
        randomInvestorsNumber = 0;
        wallet = new Wallet(0);
        companyIDs = new HashSet<>();
    }

    public tradingSystem read(String path) throws FileNotFoundException, DataFormatException {
        try (Scanner scanner = new Scanner(new File(path))) {
            int cnt = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.charAt(0) == '#') {
                    continue;
                }

                if (cnt == 0) {
                    readInvestors(line);
                } else if (cnt == 1) {
                    readCompanies(line);
                } else if (cnt == 2) {
                    readWallets(line);
                }

                cnt++;
            }
            if (cnt != 3) {
                throw new DataFormatException("Wrong number of lines with data");
            }

            for (int i = 0; i < randomInvestorsNumber; i++) {
                tradingSystem.addInvestor(new InvestorRandom(tradingSystem, new Wallet(wallet)));
            }
            for (int i = 0; i < SMAInvestorsNumber; i++) {
                tradingSystem.addInvestor(new InvestorSMA(tradingSystem, new Wallet(wallet)));
            }

            return tradingSystem;
        } catch (IndexOutOfBoundsException e) {
            throw new DataFormatException("Invalid line");
        }
    }

    private void readInvestors(String line) throws DataFormatException {
        try (Scanner scanner = new Scanner(line)) {
            while (scanner.hasNext()) {
                String s = scanner.next();
                if (s.length() > 1 || (s.charAt(0) != 'R' && s.charAt(0) != 'S')) {
                    throw new DataFormatException("Invalid investor format");
                } else if (s.charAt(0) == 'R') {
                    randomInvestorsNumber++;
                } else {
                    SMAInvestorsNumber++;
                }
            }
        }
    }

    private void readCompanies(String line) throws DataFormatException {
        try (Scanner scanner = new Scanner(line)) {
            while (scanner.hasNext()) {
                String s = scanner.next();

                int IdLength = s.indexOf(':');
                if (IdLength == -1) {
                    throw new DataFormatException("Invalid Company format");
                }

                CompanyId companyId = new CompanyId(s.substring(0, IdLength));
                int lastPrice = Integer.parseInt(s.substring(IdLength + 1));
                if (companyIDs.contains(companyId) || lastPrice < 1) {
                    throw new DataFormatException("Invalid Company format");
                }
                companyIDs.add(companyId);
                tradingSystem.addCompany(new Company(companyId, lastPrice));
            }
        } catch (NumberFormatException e) {
            throw new DataFormatException("Invalid Company format");
        }
    }

    private void readWallets(String line) throws DataFormatException {
        try (Scanner scanner = new Scanner(line)) {

            wallet.addMoney(Integer.parseInt(scanner.next()));

            while (scanner.hasNext()) {
                String s = scanner.next();

                int IdLength = s.indexOf(':');
                if (IdLength == -1) {
                    throw new DataFormatException("Invalid wallet format");
                }

                CompanyId companyId = new CompanyId(s.substring(0, IdLength));
                int numberOfShares = Integer.parseInt(s.substring(IdLength + 1));
                if (!companyIDs.contains(companyId) || numberOfShares < 1) {
                    throw new DataFormatException("Invalid wallet format");
                }
                wallet.addShares(companyId, numberOfShares);
            }
        } catch (NoSuchElementException | NumberFormatException e) {
            throw new DataFormatException("Invalid wallet format");
        }
    }
}
