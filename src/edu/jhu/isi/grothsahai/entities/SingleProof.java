/*
 * Copyright (c) 2016 Gijs Van Laer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package edu.jhu.isi.grothsahai.entities;

import java.util.Objects;

public class SingleProof {
    private Vector pi;
    private Vector theta;

    public SingleProof(final Vector pi, final Vector theta) {
        this.pi = pi;
        this.theta = theta;
    }

    public SingleProof() {
    }

    public Vector getPi() {
        return pi;
    }

    public Vector getTheta() {
        return theta;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SingleProof that = (SingleProof) o;
        return Objects.equals(pi, that.pi) &&
                Objects.equals(theta, that.theta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pi, theta);
    }

    @Override
    public String toString() {
        return "SingleProof{" +
                "pi=" + pi +
                ", theta=" + theta +
                '}';
    }
}
