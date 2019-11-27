import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Main {

    static HashMap<Long,OHLC> EURUSD_M1;
    static HashMap<Long,OHLC> USDJPY_M1;
    static HashMap<Long,OHLC> EURJPY_M1;

    static HashMap<Long,OHLC> EURUSD_H1;
    static HashMap<Long,OHLC> USDJPY_H1;
    static HashMap<Long,OHLC> EURJPY_H1;

    static HashMap<Long,OHLC> EURUSD_H2;
    static HashMap<Long,OHLC> USDJPY_H2;
    static HashMap<Long,OHLC> EURJPY_H2;

    static HashMap<Long,OHLC> EURUSD_H4;
    static HashMap<Long,OHLC> USDJPY_H4;
    static HashMap<Long,OHLC> EURJPY_H4;

    static HashMap<Long,OHLC> EURUSD_H8;
    static HashMap<Long,OHLC> USDJPY_H8;
    static HashMap<Long,OHLC> EURJPY_H8;

    static HashMap<Long,OHLC> EURUSD_H12;
    static HashMap<Long,OHLC> USDJPY_H12;
    static HashMap<Long,OHLC> EURJPY_H12;

    static HashMap<Long,OHLC> EURUSD_D1;
    static HashMap<Long,OHLC> USDJPY_D1;
    static HashMap<Long,OHLC> EURJPY_D1;


    static ArrayList<OHLC> EURUSDM1;
    static ArrayList<OHLC> USDJPYM1;
    static ArrayList<OHLC> EURJPYM1;

    static ArrayList<OHLC> EURUSDH1;
    static ArrayList<OHLC> USDJPYH1;
    static ArrayList<OHLC> EURJPYH1;

    static ArrayList<OHLC> EURUSDH2;
    static ArrayList<OHLC> USDJPYH2;
    static ArrayList<OHLC> EURJPYH2;

    static ArrayList<OHLC> EURUSDH4;
    static ArrayList<OHLC> USDJPYH4;
    static ArrayList<OHLC> EURJPYH4;

    static ArrayList<OHLC> EURUSDH8;
    static ArrayList<OHLC> USDJPYH8;
    static ArrayList<OHLC> EURJPYH8;

    static ArrayList<OHLC> EURUSDH12;
    static ArrayList<OHLC> USDJPYH12;
    static ArrayList<OHLC> EURJPYH12;


    static ArrayList<OHLC> EURUSDD1;
    static ArrayList<OHLC> USDJPYD1;
    static ArrayList<OHLC> EURJPYD1;

    static ArrayList<Float> generateMA(ArrayList<OHLC> A, int k) {
        ArrayList<Float> ma = new ArrayList<>();

        for(int i = 0; i < A.size(); i++) {
            if(i < k-1) {
                ma.add(-1.0f);
            } else {
                float res = 0.0f;

                for(int j = i-k+1; j <= i; j++) {
                    res += ((A.get(j).getH() + A.get(j).getL()) / 2.0);
                }

                ma.add(res / ((float) k));
            }

            assert(i == ma.size()-1);
        }

        return ma;
    }


    static HashMap<Long, Float> generateMA1(ArrayList<OHLC> A, int k) {
        HashMap<Long, Float> ma = new HashMap<>();

        for(int i = 0; i < A.size(); i++) {
            if(i < k-1) {
                ma.put(A.get(i).getTimestamp(), -1.0f);
            } else {
                float res = 0.0f;

                for(int j = i-k+1; j <= i; j++) {
                    res += ((A.get(j).getH() + A.get(j).getL()) / 2.0);
                }

                ma.put(A.get(i).getTimestamp(), res / ((float) k));
            }

            assert(i == ma.size()-1);
        }

        return ma;
    }

    static SimResult tradeSim(float tp, float sl, ArrayList<OHLC> candleset1, ArrayList<OHLC> candleset2, HashMap<Long, OHLC> candlemap, int numberOfMinutes, boolean reverse, float maxloss, float spread) {
        boolean trade = false;
        float profit = 0f;
        float loss = 0f;
        int numberOfTrades = 0;

        boolean tradeLong = false;
        float tradePrice = 0.0f;

        int numberOfTakeprofits = 0;
        int numberOfStoplosses = 0;
        int longtrades = 0, shorttrades = 0;
        float mloss = 0f, mprofit = 0f;
        int counter = 0;

        OHLC currentBig = null;

        boolean greenCandle = false;

        for(OHLC ohlc : candleset1) {
            if(counter % candleset2.get(0).getGranularity() == 0) {
                if(candlemap.containsKey(ohlc.getTimestamp())) {
                    currentBig = candlemap.get(ohlc.getTimestamp());

                    if(currentBig.getC() == currentBig.getO()) { // Empty big candle
                        counter++;
                        continue;
                    } else if(currentBig.getC() > currentBig.getO()) {
                        greenCandle = true;
                    } else {
                        greenCandle = false;
                    }

                    if(!trade) {
                        trade = true;
                        numberOfTrades++;

                        if(!reverse) {

                            if (greenCandle) {
                                tradeLong = true;
                                longtrades++;
                                tradePrice = currentBig.getC() - spread;
                            } else {
                                tradeLong = false;
                                shorttrades++;
                                tradePrice = currentBig.getC() + spread;
                            }
                        } else {
                            if (!greenCandle) {
                                tradeLong = true;
                                longtrades++;
                                tradePrice = currentBig.getC() - spread;
                            } else {
                                tradeLong = false;
                                shorttrades++;
                                tradePrice = currentBig.getC() + spread;
                            }
                        }

                        counter++;
                        continue;
                    }
                } else {
                    System.out.println("ERROR: missing candle");
                    System.exit(1);
                }
            }

            if(trade) {
                if(tradeLong && (ohlc.getH() >= tradePrice + tp)) {
                    trade = false;
                    profit += tp;
                    numberOfTakeprofits++;
                    counter++;
                    continue;
                }

                if(tradeLong && (ohlc.getL() <= tradePrice + spread - sl)) {
                    trade = false;
                    loss += sl;
                    numberOfStoplosses++;
                    counter++;
                    continue;
                }

                if(!tradeLong && (ohlc.getL() <= tradePrice - tp)) {
                    trade = false;
                    profit += tp;
                    numberOfTakeprofits++;
                    counter++;
                    continue;
                }

                if(!tradeLong && (ohlc.getH() >= tradePrice - spread + sl)) {
                    trade = false;
                    loss += sl;
                    numberOfStoplosses++;
                    counter++;
                    continue;
                }
            }

            counter++;
        }

        SimResult result = new SimResult(profit-loss, profit, loss, mloss, mprofit, numberOfStoplosses, numberOfTakeprofits, longtrades, shorttrades, tp, sl, reverse);

        return result;
    }

    static void readData2(String granularity, String instrument, String datadir) {
        String line = null;

        // /home/nick/pool/fxdata
        // /home/seit/data
        String fileName1 = datadir + "/" + instrument + "/" + instrument + "-2008-2018-" + granularity + ".csv";

        try {
            FileReader fileReader = new FileReader(fileName1);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);

                StringBuilder sb = new StringBuilder("");
                sb.append(line.charAt(0));
                sb.append(line.charAt(1));
                int day = Integer.parseInt(sb.toString());

                sb = new StringBuilder("");
                sb.append(line.charAt(3));
                sb.append(line.charAt(4));
                int month = Integer.parseInt(sb.toString());

                sb = new StringBuilder();
                sb.append(line.charAt(6));
                sb.append(line.charAt(7));
                sb.append(line.charAt(8));
                sb.append(line.charAt(9));
                int year = Integer.parseInt(sb.toString());

                sb = new StringBuilder("");
                sb.append(line.charAt(11));
                sb.append(line.charAt(12));
                int hour = Integer.parseInt(sb.toString());

                sb = new StringBuilder("");
                sb.append(line.charAt(14));
                sb.append(line.charAt(15));
                int minute = Integer.parseInt(sb.toString());

                LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute);

                sb = new StringBuilder("");
                int i = 24;
                for(; i < line.length(); i++) {
                    if(line.charAt(i) == ',') break;
                    sb.append(line.charAt(i));
                }

                float o = Float.parseFloat(sb.toString());

                sb = new StringBuilder("");
                for(i++; i < line.length(); i++) {
                    if(line.charAt(i) == ',') break;
                    sb.append(line.charAt(i));
                }

                float h = Float.parseFloat(sb.toString());

                sb = new StringBuilder("");
                for(i++; i < line.length(); i++) {
                    if(line.charAt(i) == ',') break;
                    sb.append(line.charAt(i));
                }

                float l = Float.parseFloat(sb.toString());

                sb = new StringBuilder("");
                for(i++; i < line.length(); i++) {
                    if(line.charAt(i) == ',') break;
                    sb.append(line.charAt(i));
                }

                float c = Float.parseFloat(sb.toString());

                OHLC ohlc = new OHLC(60, dateTime, o, h, l, c);

                //System.out.println(ohlc);

                switch(instrument) {
                    case "eurusd":
                        if(granularity.equals("m1")) {
                            ohlc.setGranularity(1);
                            EURUSDM1.add(ohlc);
                            EURUSD_M1.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("h1")) {
                            ohlc.setGranularity(60);
                            EURUSDH1.add(ohlc);
                            EURUSD_H1.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("h2")) {
                            ohlc.setGranularity(120);
                            EURUSDH2.add(ohlc);
                            EURUSD_H2.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("h4")) {
                            ohlc.setGranularity(240);
                            EURUSDH4.add(ohlc);
                            EURUSD_H4.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("h8")) {
                            ohlc.setGranularity(480);
                            EURUSDH8.add(ohlc);
                            EURUSD_H8.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("h12")) {
                            ohlc.setGranularity(720);
                            EURUSDH12.add(ohlc);
                            EURUSD_H12.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("d1")){
                            ohlc.setGranularity(1440);
                            EURUSDD1.add(ohlc);
                            EURUSD_D1.put(ohlc.getTimestamp(), ohlc);
                        } else {
                            System.out.println("ERROR: unknown granularity");
                            System.exit(1);
                        }
                        break;

                    case "usdjpy":
                        if(granularity.equals("m1")) {
                            ohlc.setGranularity(1);
                            USDJPYM1.add(ohlc);
                            USDJPY_M1.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("h1")) {
                            ohlc.setGranularity(60);
                            USDJPYH1.add(ohlc);
                            USDJPY_H1.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("h2")) {
                            ohlc.setGranularity(120);
                            USDJPYH2.add(ohlc);
                            USDJPY_H2.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("h4")) {
                            ohlc.setGranularity(240);
                            USDJPYH4.add(ohlc);
                            USDJPY_H4.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("h8")) {
                            ohlc.setGranularity(480);
                            USDJPYH8.add(ohlc);
                            USDJPY_H8.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("h12")) {
                            ohlc.setGranularity(720);
                            USDJPYH12.add(ohlc);
                            USDJPY_H12.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("d1")){
                            ohlc.setGranularity(1440);
                            USDJPYD1.add(ohlc);
                            USDJPY_D1.put(ohlc.getTimestamp(), ohlc);
                        } else {
                            System.out.println("ERROR: unknown granularity");
                            System.exit(1);
                        }
                        break;
                    case "eurjpy":
                        if(granularity.equals("m1")) {
                            ohlc.setGranularity(1);
                            EURJPYM1.add(ohlc);
                            EURJPY_M1.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("h1")) {
                            ohlc.setGranularity(60);
                            EURJPYH1.add(ohlc);
                            EURJPY_H1.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("h2")) {
                            ohlc.setGranularity(120);
                            EURJPYH2.add(ohlc);
                            EURJPY_H2.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("h4")) {
                            ohlc.setGranularity(240);
                            EURJPYH4.add(ohlc);
                            EURJPY_H4.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("h8")) {
                            ohlc.setGranularity(480);
                            EURJPYH8.add(ohlc);
                            EURJPY_H8.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("h12")) {
                            ohlc.setGranularity(720);
                            EURJPYH12.add(ohlc);
                            EURJPY_H12.put(ohlc.getTimestamp(), ohlc);
                        } else if(granularity.equals("d1")){
                            ohlc.setGranularity(1440);
                            EURJPYD1.add(ohlc);
                            EURJPY_D1.put(ohlc.getTimestamp(), ohlc);
                        } else {
                            System.out.println("ERROR: unknown granularity");
                            System.exit(1);
                        }
                        break;
                    default:
                        System.out.println("ERROR: Unknown instrument.");
                        System.exit(1);
                        break;
                }
            }

            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName1 + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName1 + "'");
        }
    }

    public static void runSim(String description, ArrayList<OHLC> c1, ArrayList<OHLC> c2, HashMap<Long, OHLC> h2, boolean reversed) {
        System.out.println("Simulating " + description + "...");
        float bestBalance = -Float.MAX_VALUE;
        SimResult bestRes = null;

        for(float tp = 0.0003f; tp < 0.0050; tp += 0.0001f) {
            for(float sl = 0.0003f; sl < 0.0050; sl += 0.0001f) {
                SimResult res = tradeSim(tp, sl, c1, c2, h2, 60, reversed, 0.100f, 0.0002f);
                if (res.getBalance() > bestBalance) {
                    bestBalance = res.getBalance();
                    bestRes = res;
                }
            }
        }

        System.out.println("" + description + " " + bestRes.getBalance() + " " + (bestRes.getTotalProfit()/bestRes.getTotalLoss()) + " " + bestRes.getTakeProfit() + " " + bestRes.getStopLoss() + " " + bestRes.isReverse() + " " + (bestRes.getNumberOfShortTrades() + bestRes.getNumberOfLongTrades()));
        System.out.flush();
    }

    public static void runSim1(String description, ArrayList<OHLC> c1, ArrayList<OHLC> c2, HashMap<Long, OHLC> h2, boolean reversed) {
        System.out.println("Simulating " + description + "...");
        float bestBalance = -Float.MAX_VALUE;
        SimResult bestRes = null;

        for(float tp = 0.03f; tp < 0.50; tp += 0.01f) {
            for(float sl = 0.03f; sl < 0.50; sl += 0.01f) {
                SimResult res = tradeSim(tp, sl, c1, c2, h2, 60, reversed, 0.100f, 0.02f);
                if (res.getBalance() > bestBalance) {
                    bestBalance = res.getBalance();
                    bestRes = res;
                }
            }
        }

        System.out.println("" + description + " " + bestRes.getBalance() + " " + (bestRes.getTotalProfit()/bestRes.getTotalLoss()) + " " + bestRes.getTakeProfit() + " " + bestRes.getStopLoss() + " " + bestRes.isReverse() + " " + (bestRes.getNumberOfShortTrades() + bestRes.getNumberOfLongTrades()));
        System.out.flush();
    }

    public static void main(String[] args) {
        String datadir = args[0];
        boolean reversed = Boolean.parseBoolean(args[1]);

        EURUSDM1 = new ArrayList<>();
        USDJPYM1 = new ArrayList<>();
        EURJPYM1 = new ArrayList<>();

        EURUSDH1 = new ArrayList<>();
        USDJPYH1 = new ArrayList<>();
        EURJPYH1 = new ArrayList<>();

        EURUSDH2 = new ArrayList<>();
        USDJPYH2 = new ArrayList<>();
        EURJPYH2 = new ArrayList<>();

        EURUSDH4 = new ArrayList<>();
        USDJPYH4 = new ArrayList<>();
        EURJPYH4 = new ArrayList<>();

        EURUSDH8 = new ArrayList<>();
        USDJPYH8 = new ArrayList<>();
        EURJPYH8 = new ArrayList<>();

        EURUSDH12 = new ArrayList<>();
        USDJPYH12 = new ArrayList<>();
        EURJPYH12 = new ArrayList<>();

        EURUSDD1 = new ArrayList<>();
        USDJPYD1 = new ArrayList<>();
        EURJPYD1 = new ArrayList<>();

        EURUSD_M1 = new HashMap<>();
        USDJPY_M1 = new HashMap<>();
        EURJPY_M1 = new HashMap<>();

        EURUSD_H1 = new HashMap<>();
        USDJPY_H1 = new HashMap<>();
        EURJPY_H1 = new HashMap<>();

        EURUSD_H2 = new HashMap<>();
        USDJPY_H2 = new HashMap<>();
        EURJPY_H2 = new HashMap<>();

        EURUSD_H4 = new HashMap<>();
        USDJPY_H4 = new HashMap<>();
        EURJPY_H4 = new HashMap<>();

        EURUSD_H8 = new HashMap<>();
        USDJPY_H8 = new HashMap<>();
        EURJPY_H8 = new HashMap<>();

        EURUSD_H12 = new HashMap<>();
        USDJPY_H12 = new HashMap<>();
        EURJPY_H12 = new HashMap<>();

        EURUSD_D1 = new HashMap<>();
        USDJPY_D1 = new HashMap<>();
        EURJPY_D1 = new HashMap<>();


        System.out.println("Reading EUR/USD...");
        System.out.println("Processing EUR/USD M1...");
        readData2("m1", "eurusd", datadir);
        System.out.println("Processing EUR/USD H1...");
        readData2("h1", "eurusd", datadir);
        System.out.println("Processing EUR/USD H2...");
        readData2("h2", "eurusd", datadir);
        System.out.println("Processing EUR/USD H4...");
        readData2("h4", "eurusd", datadir);
        System.out.println("Processing EUR/USD H8...");
        readData2("h8", "eurusd", datadir);
        System.out.println("Processing EUR/USD H12...");
        readData2("h12", "eurusd", datadir);
        System.out.println("Processing EUR/USD D1...");
        readData2("d1", "eurusd", datadir);


        System.out.println("Reading USD/JPY...");
        System.out.println("Processing USD/JPY M1...");
        readData2("m1", "usdjpy", datadir);
        System.out.println("Processing USD/JPY H1...");
        readData2("h1", "usdjpy", datadir);
        System.out.println("Processing USD/JPY H2...");
        readData2("h2", "usdjpy", datadir);
        System.out.println("Processing USD/JPY H4...");
        readData2("h4", "usdjpy", datadir);
        System.out.println("Processing USD/JPY H8...");
        readData2("h8", "usdjpy", datadir);
        System.out.println("Processing USD/JPY H12...");
        readData2("h12", "usdjpy", datadir);
        System.out.println("Processing USD/JPY D1...");
        readData2("d1", "usdjpy", datadir);


        System.out.println("Reading EUR/JPY...");
        System.out.println("Processing EUR/JPY M1...");
        readData2("m1", "eurjpy", datadir);
        System.out.println("Processing EUR/JPY H1...");
        readData2("h1", "eurjpy", datadir);
        System.out.println("Processing EUR/JPY H2...");
        readData2("h2", "eurjpy", datadir);
        System.out.println("Processing EUR/JPY H4...");
        readData2("h4", "eurjpy", datadir);
        System.out.println("Processing EUR/JPY H8...");
        readData2("h8", "eurjpy", datadir);
        System.out.println("Processing EUR/JPY H12...");
        readData2("h12", "eurjpy", datadir);
        System.out.println("Processing EUR/JPY D1...");
        readData2("d1", "eurjpy", datadir);


        // EURUSD

        Thread thread1 = new Thread(() -> runSim("EURUSD-H1", EURUSDM1, EURUSDH1, EURUSD_H1, reversed));
        thread1.start();

        Thread thread2 = new Thread(() -> runSim("EURUSD-H2", EURUSDM1, EURUSDH2, EURUSD_H2, reversed));
        thread2.start();

        Thread thread3 = new Thread(() -> runSim("EURUSD-H4", EURUSDM1, EURUSDH4, EURUSD_H4, reversed));
        thread3.start();

        Thread thread4 = new Thread(() -> runSim("EURUSD-H8", EURUSDM1, EURUSDH8, EURUSD_H8, reversed));
        thread4.start();

        Thread thread5 = new Thread(() -> runSim("EURUSD-H12", EURUSDM1, EURUSDH12, EURUSD_H12, reversed));
        thread5.start();

        Thread thread6 = new Thread(() -> runSim("EURUSD-D1", EURUSDM1, EURUSDD1, EURUSD_D1, reversed));
        thread6.start();

        // USDJPY

        Thread thread7 = new Thread(() -> runSim1("USDJPY-H1", USDJPYM1, USDJPYH1, USDJPY_H1, reversed));
        thread7.start();

        Thread thread8 = new Thread(() -> runSim1("USDJPY-H2", USDJPYM1, USDJPYH2, USDJPY_H2, reversed));
        thread8.start();

        Thread thread9 = new Thread(() -> runSim1("USDJPY-H4", USDJPYM1, USDJPYH4, USDJPY_H4, reversed));
        thread9.start();

        Thread thread10 = new Thread(() -> runSim1("USDJPY-H8", USDJPYM1, USDJPYH8, USDJPY_H8, reversed));
        thread10.start();

        Thread thread11 = new Thread(() -> runSim1("USDJPY-H12", USDJPYM1, USDJPYH12, USDJPY_H12, reversed));
        thread11.start();

        Thread thread12 = new Thread(() -> runSim1("USDJPY-D1", USDJPYM1, USDJPYD1, USDJPY_D1, reversed));
        thread12.start();

        // EURJPY

        Thread thread13 = new Thread(() -> runSim1("EURJPY-H1", EURJPYM1, EURJPYH1, EURJPY_H1, reversed));
        thread13.start();

        Thread thread14 = new Thread(() -> runSim1("EURJPY-H2", EURJPYM1, EURJPYH2, EURJPY_H2, reversed));
        thread14.start();

        Thread thread15 = new Thread(() -> runSim1("EURJPY-H4", EURJPYM1, EURJPYH4, EURJPY_H4, reversed));
        thread15.start();

        Thread thread16 = new Thread(() -> runSim1("EURJPY-H8", EURJPYM1, EURJPYH8, EURJPY_H8, reversed));
        thread16.start();

        Thread thread17 = new Thread(() -> runSim1("EURJPY-H12", EURJPYM1, EURJPYH12, EURJPY_H12, reversed));
        thread17.start();

        Thread thread18 = new Thread(() -> runSim1("EURJPY-D1", EURJPYM1, EURJPYD1, EURJPY_D1, reversed));
        thread18.start();
    }

}
