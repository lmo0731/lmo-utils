/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lmo.utils;

/**
 *
 * @author munkhochir <munkhochir@mobicom.mn>
 */
public class Pair<F, S> {

    public F first;
    public S second;

    public Pair() {
    }

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", first, second);
    }

    public F getFirst() {
        return first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }

}
