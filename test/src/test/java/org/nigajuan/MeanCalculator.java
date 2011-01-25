package org.nigajuan;

/**
 * Created by IntelliJ IDEA.
 * User: nigajuan
 * Date: 25/01/11
 * Time: 22:08
 * To change this template use File | Settings | File Templates.
 */
public class MeanCalculator {
    protected float sum;
    protected float count;
    protected float scale = 0.999f;

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public synchronized void add(float duration){
        sum = sum * scale;
        sum = sum + duration;
        count = count * scale + 1;
    }

    public float mean(){
        return sum / count;
    }
}
