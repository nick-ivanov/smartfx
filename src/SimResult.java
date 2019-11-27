public class SimResult {
    private float balance;
    private float totalProfit;
    private float totalLoss;
    private float maxLoss;
    private float maxProfit;
    private int numberOfStopLosses;
    private int numberOfTakeProfits;
    private int numberOfLongTrades;
    private int numberOfShortTrades;
    private float takeProfit;
    private float stopLoss;
    private boolean reverse;

    public SimResult(float balance, float totalProfit, float totalLoss, float maxLoss, float maxProfit, int numberOfStopLosses, int numberOfTakeProfits, int numberOfLongTrades, int numberOfShortTrades, float takeProfit, float stopLoss, boolean reverse) {
        this.balance = balance;
        this.totalProfit = totalProfit;
        this.totalLoss = totalLoss;
        this.maxLoss = maxLoss;
        this.maxProfit = maxProfit;
        this.numberOfStopLosses = numberOfStopLosses;
        this.numberOfTakeProfits = numberOfTakeProfits;
        this.numberOfLongTrades = numberOfLongTrades;
        this.numberOfShortTrades = numberOfShortTrades;
        this.takeProfit = takeProfit;
        this.stopLoss = stopLoss;
        this.reverse = reverse;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(float totalProfit) {
        this.totalProfit = totalProfit;
    }

    public float getTotalLoss() {
        return totalLoss;
    }

    public void setTotalLoss(float totalLoss) {
        this.totalLoss = totalLoss;
    }

    public float getMaxLoss() {
        return maxLoss;
    }

    public void setMaxLoss(float maxLoss) {
        this.maxLoss = maxLoss;
    }

    public float getMaxProfit() {
        return maxProfit;
    }

    public void setMaxProfit(float maxProfit) {
        this.maxProfit = maxProfit;
    }

    public int getNumberOfStopLosses() {
        return numberOfStopLosses;
    }

    public void setNumberOfStopLosses(int numberOfStopLosses) {
        this.numberOfStopLosses = numberOfStopLosses;
    }

    public int getNumberOfTakeProfits() {
        return numberOfTakeProfits;
    }

    public void setNumberOfTakeProfits(int numberOfTakeProfits) {
        this.numberOfTakeProfits = numberOfTakeProfits;
    }

    public int getNumberOfLongTrades() {
        return numberOfLongTrades;
    }

    public void setNumberOfLongTrades(int numberOfLongTrades) {
        this.numberOfLongTrades = numberOfLongTrades;
    }

    public int getNumberOfShortTrades() {
        return numberOfShortTrades;
    }

    public void setNumberOfShortTrades(int numberOfShortTrades) {
        this.numberOfShortTrades = numberOfShortTrades;
    }

    public float getTakeProfit() {
        return takeProfit;
    }

    public void setTakeProfit(float takeProfit) {
        this.takeProfit = takeProfit;
    }

    public float getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(float stopLoss) {
        this.stopLoss = stopLoss;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    @Override
    public String toString() {
        return "SimResult{" +
                "balance=" + balance +
                ", totalProfit=" + totalProfit +
                ", totalLoss=" + totalLoss +
                ", maxLoss=" + maxLoss +
                ", maxProfit=" + maxProfit +
                ", numberOfStopLosses=" + numberOfStopLosses +
                ", numberOfTakeProfits=" + numberOfTakeProfits +
                ", numberOfLongTrades=" + numberOfLongTrades +
                ", numberOfShortTrades=" + numberOfShortTrades +
                ", takeProfit=" + takeProfit +
                ", stopLoss=" + stopLoss +
                ", reverse=" + reverse +
                '}';
    }
}
