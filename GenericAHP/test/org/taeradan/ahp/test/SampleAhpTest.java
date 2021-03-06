/* Copyright 2009-2010 Yves Dubromelle @ LSIS(www.lsis.org)
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
package org.taeradan.ahp.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.taeradan.ahp.AHPRoot;
import org.taeradan.ahp.TimeConsumeRule;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Test class for the AHP tree
 *
 * @author Jean-Pierre PRUNARET
 * @author Yves Dubromelle
 */
public final class SampleAhpTest {

	private static AHPRoot ahpRoot;
	private static final String ALTERNATIVE_1 = "Alternative 1";
	private static final String ALTERNATIVE_2 = "Alternative 2";
	private static final String ALTERNATIVE_3 = "Alternative 3";
	private static final String ALTERNATIVE_4 = "Alternative 4";
	private static final String ALTERNATIVE_5 = "Alternative 5";
	private static List<TestingAlternative> ALTERNATIVES = makeAlternatives();

	public SampleAhpTest() {
	}

	private static List<TestingAlternative> makeAlternatives() {
		final ArrayList<TestingAlternative> alts = new ArrayList<>(4);

		alts.add(new TestingAlternative(ALTERNATIVE_1, 3, 5, 3, 1 / 2., 3, 1200, 1, 100, 1));
		alts.add(new TestingAlternative(ALTERNATIVE_2, 3, 5, 3, 1 / 2., 1, 8638, 1, 250, 1));
		alts.add(new TestingAlternative(ALTERNATIVE_3, 5, 5, 5, 2 / 3., 3, 1800, 1, 30, 1));
		alts.add(new TestingAlternative(ALTERNATIVE_4, 3, 3, 3, 3 / 4., 3, 7136, 1, 300, 1));
		alts.add(new TestingAlternative(ALTERNATIVE_5, 3, 3, 3, 3 / 4., 3, 7136, 1, 300, 1));

		return Collections.unmodifiableList(alts);
	}

	private static void printRanking(Collection<TestingAlternative> alternatives) {
		for (TestingAlternative currentAlt : alternatives) {
			System.out.println(currentAlt.name + " = " + currentAlt.getRank());
		}
	}

	@Rule
	public MethodRule rule = new TimeConsumeRule();

	@Before
	public void setUp() {
		ALTERNATIVES = makeAlternatives();

		URL resource = SampleAhpTest.class.getResource("/org/taeradan/ahp/conf/ahp_conf.xml");
		File aFile = new File(resource.getFile());

		ahpRoot = new AHPRoot(aFile, AHPRoot.DEFAULT_INDICATOR_PATH);
	}

	@Test
	public void test100_initialState() {
		final ArrayList<TestingAlternative> alternatives = new ArrayList<>(ALTERNATIVES);
		assertTrue(alternatives.size() == 5);

		assertTrue(alternatives.get(1 - 1).name.equalsIgnoreCase(ALTERNATIVE_1));
		assertTrue(alternatives.get(2 - 1).name.equalsIgnoreCase(ALTERNATIVE_2));
		assertTrue(alternatives.get(3 - 1).name.equalsIgnoreCase(ALTERNATIVE_3));
		assertTrue(alternatives.get(4 - 1).name.equalsIgnoreCase(ALTERNATIVE_4));
		assertTrue(alternatives.get(5 - 1).name.equalsIgnoreCase(ALTERNATIVE_5));

		System.out.println("alternatives.get(1 - 1).getRank() :" + alternatives.get(1 - 1).getRank());
		assertTrue(alternatives.get(1 - 1).getRank() == Integer.MAX_VALUE);
		assertTrue(alternatives.get(2 - 1).getRank() == Integer.MAX_VALUE);
		assertTrue(alternatives.get(3 - 1).getRank() == Integer.MAX_VALUE);
		assertTrue(alternatives.get(4 - 1).getRank() == Integer.MAX_VALUE);
		assertTrue(alternatives.get(5 - 1).getRank() == Integer.MAX_VALUE);
	}

	@Test
	public void test200_ranking() {
		final ArrayList<TestingAlternative> alternatives = new ArrayList<>(ALTERNATIVES);

		ahpRoot.calculateRanking(alternatives);

		assertTrue(alternatives.size() == 5);

		assertTrue(alternatives.get(1 - 1).name.equalsIgnoreCase(ALTERNATIVE_1));
		assertTrue(alternatives.get(2 - 1).name.equalsIgnoreCase(ALTERNATIVE_2));
		assertTrue(alternatives.get(3 - 1).name.equalsIgnoreCase(ALTERNATIVE_3));
		assertTrue(alternatives.get(4 - 1).name.equalsIgnoreCase(ALTERNATIVE_4));
		assertTrue(alternatives.get(5 - 1).name.equalsIgnoreCase(ALTERNATIVE_5));

		// by id
		assertTrue(alternatives.get(1 - 1).getRank() == 1);
		assertTrue(alternatives.get(2 - 1).getRank() == 5);
		assertTrue(alternatives.get(3 - 1).getRank() == 2);
		assertTrue(alternatives.get(4 - 1).getRank() == 4);
		assertTrue(alternatives.get(5 - 1).getRank() == 3);

		// by rank
		assertTrue(alternatives.get(1 - 1).getRank() == 1);
		assertTrue(alternatives.get(3 - 1).getRank() == 2);
		assertTrue(alternatives.get(5 - 1).getRank() == 3);
		assertTrue(alternatives.get(4 - 1).getRank() == 4);
		assertTrue(alternatives.get(2 - 1).getRank() == 5);
	}
}
