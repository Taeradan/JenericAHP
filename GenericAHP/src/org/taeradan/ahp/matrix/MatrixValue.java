package org.taeradan.ahp.matrix;

/**
 * @author Jean-Pierre PRUNARET
 * @author Marianne
 */
public class MatrixValue {

	private double value  = 0;
	private int    row    = 0;
	private int    column = 0;

	public MatrixValue() {
	}

	private static double round(double what, int howmuch) {
		return (double) ((int) (what * Math.pow(10, howmuch) + .5)) / Math.pow(10, howmuch);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		if (value < 1) {
			this.value = round(value, 4);
		} else {
			this.value = round(value, 0);
		}
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	/** @return the column */
	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public boolean equals(Object o) {
		boolean isEquals = true;
		if (o instanceof MatrixValue) {
			MatrixValue aValue = (MatrixValue) o;
			if (this.getColumn() != aValue.getColumn()) {
				isEquals = false;
			}
			if (this.getRow() != aValue.getRow()) {
				isEquals = false;
			}
			if (this.getValue() != aValue.getValue()) {
				isEquals = false;
			}

		} else {
			isEquals = false;
		}
		return isEquals;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
		hash = 97 * hash + this.row;
		hash = 97 * hash + this.column;
		return hash;
	}

	@Override
	public String toString() {
		return value + " ";
	}
}
