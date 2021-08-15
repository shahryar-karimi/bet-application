package model.bets;

import model.Constant;

public class RangeBet extends Bet {
    private int start;
    private int end;
    private boolean isForList;

    public RangeBet(double cost, int start, int end, boolean isForList) {
        super(cost);
        this.start = start;
        this.end = end;
        this.isForList = isForList;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public boolean isForList() {
        return isForList;
    }

    @Override
    protected double prizeRatio(int[] numbers) {
        if (checkForWin(numbers)) {
            double length = end - start + 1;
            return ((double) Constant.RANDOM_SIZE) / length;
        }
        return 0;
    }

    @Override
    protected boolean checkForWin(int[] numbers) {
        return start <= numbers[0] && numbers[0] <= end;
    }

    @Override
    public String toString() {
        if (isForList) return "RangeBet{" +
                "start=" + start +
                ", end=" + end +
                '}';
        return "RangeBet{" +
                "cost=" + cost +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
