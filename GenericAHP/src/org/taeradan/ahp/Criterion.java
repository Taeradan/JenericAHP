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
package org.taeradan.ahp;

import org.jdom.Element;

import java.lang.reflect.Constructor;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents the criteria of the AHP tree, it contains Indicators and it executes its part of the AHP
 * algorithm.
 *
 * @author Yves Dubromelle
 */
public class Criterion {

	//	AHP configuration attributes
	private final PriorityVector        vectorIndicatorCriteria;
	private final Collection<Indicator> indicators;
	private       String                identifier;
	private       String                name;
	private       PairWiseMatrix        matrixIndicatorIndicator;

	//	AHP execution attributes
	private final ConsistencyChecker consistencyChecker = new ConsistencyChecker();
	private PriorityVector vectorAlternativesCriteria;

	/**
	 * Creates a AHP Criterion from a JDOM Element
	 *
	 * @param xmlCriteria
	 */
	public Criterion(final Element xmlCriteria) {
//		Initialisation of the id of the criteria
		identifier = xmlCriteria.getAttributeValue("id");
//		System.out.println("\tCriterion.id="+id);

//		Initialisation of the name
		name = xmlCriteria.getChildText("name");
//		System.out.println("\tCriterion.name="+name);

//		Initialisation of the preference matrix
		final Element xmlPrefMatrix = xmlCriteria.getChild("prefmatrix");
		matrixIndicatorIndicator = PairWiseMatrix.builder(xmlPrefMatrix);
//		System.out.println("\tCriterion.matrixIndicatorIndicator="+matrixIndicatorIndicator);
		vectorIndicatorCriteria = PriorityVector.build(matrixIndicatorIndicator);

//		Consistency verification
		if (!consistencyChecker.isConsistent(matrixIndicatorIndicator, vectorIndicatorCriteria)) {
			Logger.getAnonymousLogger().log(Level.SEVERE,
											"Is not consistent (criteria {0})",
											identifier);
		}
//		Initialisation of the Indicators
		@SuppressWarnings("unchecked")
		final List<Element> xmlIndicatorsList = (List<Element>) xmlCriteria.getChildren("indicator");
		@SuppressWarnings("unchecked")
		final List<Element> xmlRowsList = (List<Element>) xmlPrefMatrix.getChildren("row");
		indicators = new ArrayList<>(xmlIndicatorsList.size());
//		Verification that the number of indicators matches the size of the matrix
		if (xmlIndicatorsList.size() != xmlRowsList.size()) {
			Logger.getAnonymousLogger().severe(
					"Error : the number of Indicators and the size of the preference matrix does not match !");
		}
//		For each indicator declared in the configuration file
		final Iterator<Element> itxmlIndList = xmlIndicatorsList.iterator();
		while (itxmlIndList.hasNext()) {
			final Element xmlIndicator = itxmlIndList.next();
//				System.out.println("\tCriterion.xmlIndicator="+xmlIndicator);
//				System.out.println("\tCriterion.xmlIndicator.attValue="+xmlIndicator.getAttributeValue("id"));
			final String indicatorName = AHPRoot.indicatorPath
										 + Indicator.class.getSimpleName()
										 + xmlIndicator.getAttributeValue("id");
			try {
//					Research of the class implementing the indicator , named "org.taeradan.ahp.ind.IndicatorCxIy"
				@SuppressWarnings("unchecked")
				final Class<? extends Indicator> indClass = (Class<? extends Indicator>) Class.forName(indicatorName);
//					System.out.println("\t\tCriterion.indClass="+indClass);
//					Extraction of its constructor
				final Constructor<? extends Indicator> indConstruct = indClass.getConstructor(Element.class);
//					System.out.println("\t\tCriterion.indConstruct="+indConstruct);
//					Instanciation of the indicator with its constructor
				indicators.add(indConstruct.newInstance(xmlIndicator));

//					System.out.println("\tCriterion.indicator="+indicators.get(i));
			} catch (NoSuchMethodException e) {
				Logger.getAnonymousLogger().log(Level.SEVERE, "Error : no such constructor :{0}", e);
			} catch (ClassNotFoundException e) {
				Logger.getAnonymousLogger().log(Level.SEVERE,
												"Error : class {0} not found :{1}",
												new Object[]{indicatorName, e});
			} catch (Exception e) {
				Logger.getAnonymousLogger().log(Level.SEVERE, "Error :{0}", e);
			}
		}
	}

	public PriorityVector calculateAlternativesPriorityVector(
			final Collection<? extends Alternative> alternatives) {
		PairWiseMatrix matrixAlternativesIndicator;
		matrixAlternativesIndicator = new PairWiseMatrix(alternatives.size(), indicators.size());

//		Concatenation of the indicators' alternatives vectors
		final Iterator<Indicator> indicatorIterator = indicators.iterator();
		int index = 0;
		while (indicatorIterator.hasNext()) {
			matrixAlternativesIndicator.setMatrix(
					0,
					alternatives.size() - 1,
					index,
					index,
					indicatorIterator.next().calculateAlternativesPriorityVector(alternatives));

			index++;
		}

//		Calculation of the criteria's alternatives vector
		vectorAlternativesCriteria = new PriorityVector(matrixAlternativesIndicator.getRowDimension());
		vectorAlternativesCriteria.setMatrix(
				matrixAlternativesIndicator.getRowDimension() - 1,
				matrixAlternativesIndicator.times(vectorIndicatorCriteria));
		return vectorAlternativesCriteria;
	}

	/**
	 * Returns a string describing the criteria, but not its children
	 *
	 * @return Criterion as a String
	 */
	@Override
	public String toString() {
		return "Criterion " + identifier + " : " + name;
	}

	/**
	 * Returns a string describing the criteria and all its children
	 *
	 * @return Criterion and children as a String
	 */
	public String toStringRecursive() {
		final StringBuilder sb = new StringBuilder(this.toString());
		sb.append("\n").append(matrixIndicatorIndicator.toString("\t"));
		DecimalFormat printFormat = new DecimalFormat("0.000");
		final Iterator<Indicator> itIndicators = indicators.iterator();
		int index = 0;
		while (itIndicators.hasNext()) {
			sb.append("\n\t\t(").
					append(printFormat.format(vectorIndicatorCriteria.get(index, 0))).
						  append(") ").
						  append(itIndicators.next().toString());
			index++;
		}
		return sb.toString();
	}

	/**
	 * Returns a JDOM element that represents the criteria and all its children
	 *
	 * @return JDOM Element representing the criteria and children
	 */
	public Element toXml() {
		final Element xmlCriteria = new Element("criteria");
		xmlCriteria.setAttribute("id", identifier);
		xmlCriteria.addContent(new Element("name").setText(name));
		xmlCriteria.addContent(matrixIndicatorIndicator.toXml());
		final Iterator<Indicator> itIndicators = indicators.iterator();
		while (itIndicators.hasNext()) {
			xmlCriteria.addContent(itIndicators.next().toXml());
		}
		return xmlCriteria;
	}

	public StringBuilder resultToString() {
		final StringBuilder sb = new StringBuilder(this.toString());
		final Iterator<Indicator> itIndicators = indicators.iterator();
		while (itIndicators.hasNext()) {
			final Indicator next = itIndicators.next();

			sb.append("\n\t\t");
			sb.append(next.resultToString());
		}
		sb.append("\n\tvectorAlternativesCriteria=\n");
		sb.append(PairWiseMatrix.toString(vectorAlternativesCriteria, "\t"));

		return sb;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	public PairWiseMatrix getMatrixIndicatorIndicator() {
		return matrixIndicatorIndicator;
	}

	public void setMatrixIndicatorIndicator(final PairWiseMatrix matrixIndicatorIndicator) {
		this.matrixIndicatorIndicator = matrixIndicatorIndicator;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Collection<Indicator> getIndicators() {
		return indicators;
	}
}
