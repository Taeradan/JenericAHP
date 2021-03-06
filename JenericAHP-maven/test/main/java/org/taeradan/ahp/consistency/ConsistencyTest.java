/* Copyright 2009-2010 Yves Dubromelle
 *
 * This file is part of JenericAHP.
 *
 * JenericAHP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JenericAHP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JenericAHP.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.taeradan.ahp.consistency;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.junit.*;
import org.junit.rules.MethodRule;
import org.taeradan.ahp.ConsistencyChecker;
import org.taeradan.ahp.PairWiseMatrix;
import org.taeradan.ahp.PriorityVector;
import org.taeradan.ahp.TimeConsumeRule;

import static org.junit.Assert.*;


/**
 * @author Yves Dubromelle
 * @author Jean-Pierre PRUNARET
 */
public final class ConsistencyTest {
	private static final ConsistencyChecker consistencyChecker = new ConsistencyChecker();

	@Rule
	public MethodRule rule = new TimeConsumeRule();

	@BeforeClass
	public static void setUpClass()
			throws
			Exception {
	}

	@AfterClass
	public static void tearDownClass()
			throws
			Exception {
	}

	@Before
	public void setUp() {

	}

	@After
	public void tearDown() {
	}

	@Test
	public void test100_consistentMatrix() {

		final SAXBuilder parser = new SAXBuilder();
		try {
			final Document inDocument = parser.build(ConsistencyTest.class.getResource("consistent-matrix.xml"));
			final PairWiseMatrix matrix = PairWiseMatrix.builder(inDocument.getRootElement());
			final PriorityVector vector = PriorityVector.build(matrix);

			final boolean isConsistent = consistencyChecker.isConsistent(matrix, vector);

			assertTrue(isConsistent);
		} catch (Exception ex) {
			fail("Exception");
		}
	}

	@Test
	public void test200_unconsistentMatrix() {
		final SAXBuilder parser = new SAXBuilder();
		try {
			final Document inDocument = parser.build(ConsistencyTest.class.getResource("unconsistent-matrix.xml"));
			final PairWiseMatrix matrix = PairWiseMatrix.builder(inDocument.getRootElement());
			final PriorityVector vector = PriorityVector.build(matrix);

			final boolean isConsistent = consistencyChecker.isConsistent(matrix, vector);

			assertFalse(isConsistent);
		} catch (Exception ex) {
			fail("Exception");
		}
	}
}
