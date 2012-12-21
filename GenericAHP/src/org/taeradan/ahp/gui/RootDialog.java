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
package org.taeradan.ahp.gui;

import org.nfunk.jep.JEP;
import org.taeradan.ahp.AHPRoot;
import org.taeradan.ahp.Criteria;
import org.taeradan.ahp.PairWiseMatrix;

/**
 * Dialog used to configure AHPRoot's informations and preference matrix
 *
 * @author Yves Dubromelle
 */
public class RootDialog
	extends javax.swing.JDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *
	 */
	final private AHPRoot                  AHPRoot;
	/**
	 *
	 */
	final private PairWiseMatrixTableModel guiPrefMatrix;

	/**
	 * Creates new form CriteriaDialog
	 *
	 * @param parent
	 * @param modal
	 * @param AHPRoot
	 */
	public RootDialog(java.awt.Frame parent, boolean modal, AHPRoot AHPRoot) {
		super(parent, modal);
		this.AHPRoot = AHPRoot;
		guiPrefMatrix = new PairWiseMatrixTableModel();
		initTable();
		initComponents();
		guiPrefMatrix.addTableModelListener(new PairWiseMatrixChangeListener());
		jTextFieldObjective.setText(AHPRoot.getName());
	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jLabelObjective = new javax.swing.JLabel();
		jTextFieldObjective = new javax.swing.JTextField();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTablePrefMatrix = new javax.swing.JTable();
		jButtonReload = new javax.swing.JButton();
		jButtonSave = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Criteria properties");
		setResizable(false);

		jLabelObjective.setText("Global objective");

		jTablePrefMatrix.setModel(guiPrefMatrix);
		jTablePrefMatrix.setRowHeight(22);
		jScrollPane1.setViewportView(jTablePrefMatrix);

		jButtonReload.setText("Reload");
		jButtonReload.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonReloadActionPerformed(evt);
			}
		});

		jButtonSave.setText("Save");
		jButtonSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonSaveActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
							.addComponent(jButtonSave)
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(jButtonReload))
						.addGroup(layout.createSequentialGroup()
							.addComponent(jLabelObjective)
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(jTextFieldObjective, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)))
					.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(jLabelObjective)
						.addComponent(jTextFieldObjective, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
					.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(jButtonReload)
						.addComponent(jButtonSave))
					.addContainerGap())
		);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void jButtonReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReloadActionPerformed
		initTable();
		jTextFieldObjective.setText(AHPRoot.getName());
	}//GEN-LAST:event_jButtonReloadActionPerformed

	private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
		AHPRoot.setName(jTextFieldObjective.getText());
		final PairWiseMatrix matrix =
			new PairWiseMatrix(guiPrefMatrix.getRowCount(), guiPrefMatrix.getColumnCount());
		for (int i = 0; i < guiPrefMatrix.getRowCount(); i++) {
			for (int j = 0; j < guiPrefMatrix.getColumnCount(); j++) {
				double value = 0;
				if (guiPrefMatrix.getValueAt(i, j) instanceof Double) {
					value = (Double) guiPrefMatrix.getValueAt(i, j);
				}
				if (guiPrefMatrix.getValueAt(i, j) instanceof String) {
					final JEP myParser = new JEP();
					myParser.parseExpression((String) guiPrefMatrix.getValueAt(i, j));
					value = myParser.getValue();
				}
				matrix.set(i, j, value);
			}
		}
		AHPRoot.setMatrixCriteriaCriteria(matrix);
		this.dispose();
	}//GEN-LAST:event_jButtonSaveActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButtonReload;
	private javax.swing.JButton jButtonSave;
	private javax.swing.JLabel jLabelObjective;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTable jTablePrefMatrix;
	private javax.swing.JTextField jTextFieldObjective;
	// End of variables declaration//GEN-END:variables

	/**
	 *
	 */
	private void initTable() {
		final int matrixSize = AHPRoot.getCriterias().size();
		String[] columnNames = new String[matrixSize];
		Double[][] data = new Double[matrixSize][matrixSize];
		for (int i = 0; i < matrixSize; i++) {
			columnNames[i] = ((Criteria) AHPRoot.getCriterias().toArray()[i]).getIdentifier();
			for (int j = 0; j < matrixSize; j++) {
				data[i][j] = AHPRoot.getMatrixCriteriaCriteria().get(i, j);
			}
		}
		guiPrefMatrix.setDataVector(data, columnNames);
	}

	/**
	 * @param row
	 * @param column
	 */
	public void reloadCell(final int row, final int column) {
		guiPrefMatrix.setValueAt(AHPRoot.getMatrixCriteriaCriteria().get(row, column), row, column);
	}

	/**
	 * @return
	 */
	public PairWiseMatrixTableModel getGuiPrefMatrix() {
		return guiPrefMatrix;
	}
}
