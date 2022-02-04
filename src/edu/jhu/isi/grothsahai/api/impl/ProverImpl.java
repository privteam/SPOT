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
package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.api.Prover;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Matrix;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.SingleProof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Vector;
import edu.jhu.isi.grothsahai.entities.Witness;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ProverImpl implements Prover {
	private CommonReferenceString crs;

	private final ExecutorService executor;

	public ProverImpl(final CommonReferenceString crs, final ExecutorService executor) {
		this.crs = crs;
		this.executor = executor;
	}

	/** Function modified with respect to Groth-Sahai to support parallelization **/
	public Proof proof(final List<Statement> statements, final Witness witness) {
		Callable<Matrix> call_R = new Callable<Matrix>() {// Parallelize matrix R computation
			@Override
			public Matrix call() throws Exception {
				return Matrix.random(crs.getZr(), witness.getX().getLength(), 2);
			}
		};

		Callable<Matrix> call_S = new Callable<Matrix>() {// Parallelize matrix S computation
			@Override
			public Matrix call() throws Exception {
				return Matrix.random(crs.getZr(), witness.getY().getLength(), 2);
			}
		};

		Future<Matrix> ftr_R = executor.submit(call_R);
		Future<Matrix> ftr_S = executor.submit(call_S);

		Matrix r = null;
		Matrix s = null;
		try {// Retrieves the values of R and S once the calculations are finished
			r = ftr_R.get();
			s = ftr_S.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		final Matrix R = r;
		final Matrix S = s;

		Callable<Vector> call_c = new Callable<Vector>() {// Parallelize vector C computation
			@Override
			public Vector call() throws Exception {
				return R != null ? crs.iota(1, witness.getX()).add(R.multiply(crs.getU1())) : null;
			}
		};

		Callable<Vector> call_d = new Callable<Vector>() {// Parallelize vector D computation
			@Override
			public Vector call() throws Exception {
				return S != null ? crs.iota(2, witness.getY()).add(S.multiply(crs.getU2())) : null;
			}
		};
		Future<Vector> ftr_d = executor.submit(call_d);
		Future<Vector> ftr_c = executor.submit(call_c);

		Vector C = null;
		Vector D = null;
		try {// Retrieves the values of R and S once the calculations are finished

			C = ftr_c.get();
			D = ftr_d.get();

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		final Vector c = C;
		final Vector d = D;

		final ArrayList<SingleProof> proofs = statements.stream()
				.map(statement -> getSingleProof(statement, witness, R, S))
				.collect(Collectors.toCollection(ArrayList::new));

		return new Proof(c, d, proofs);
	}
	/** Function originally coded in Groth-Sahai **/
//    public Proof proof(final List<Statement> statements, final Witness witness) {
//
//        final Matrix R = Matrix.random(crs.getZr(), witness.getX().getLength(), 2);
//        final Matrix S = Matrix.random(crs.getZr(), witness.getY().getLength(), 2);
//
//        final Vector c = R != null ? crs.iota(1, witness.getX()).add(R.multiply(crs.getU1())) : null;
//
//        final Vector d = S != null ? crs.iota(2, witness.getY()).add(S.multiply(crs.getU2())) : null;
//
//        final ArrayList<SingleProof> proofs = statements.stream().map(statement -> getSingleProof(statement, witness, R, S)).collect(Collectors.toCollection(ArrayList::new));
//        return new Proof(c, d, proofs);
//    }

	/** Function originally coded in Groth-Sahai **/
	private SingleProof getSingleProof(final Statement statement, final Witness witness, final Matrix R,
			final Matrix S) {
		final Matrix T = Matrix.random(crs.getZr(), 2, 2);
		Vector pi;
		if (R != null) {
			pi = R.getTranspose().multiply(crs.iota(2, statement.getB()));
			if (statement.getGamma() != null) {
				pi = pi.add(R.getTranspose().multiply(statement.getGamma()).multiply(crs.iota(2, witness.getY())))
						.add(R.getTranspose().multiply(statement.getGamma()).multiply(S).multiply(crs.getU2()));
			}
		} else {
			pi = Vector.getQuadraticZeroVector(crs.getB2(), crs.getPairing(), 2);
		}
		pi = pi.sub(T.getTranspose().multiply(crs.getU2()));

		Vector theta;
		if (S != null) {
			theta = S.getTranspose().multiply(crs.iota(1, statement.getA()));
			if (statement.getGamma() != null) {
				theta = theta.add(S.getTranspose().multiply(statement.getGamma().getTranspose())
						.multiply(crs.iota(1, witness.getX())));
			}
		} else {
			theta = Vector.getQuadraticZeroVector(crs.getB1(), crs.getPairing(), 2);
		}
		theta = theta.add(T.multiply(crs.getU1()));

		return new SingleProof(pi, theta);
	}

}
