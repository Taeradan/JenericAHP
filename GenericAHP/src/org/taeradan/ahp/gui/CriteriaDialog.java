/* Copyright 2009 Yves Dubromelle @ LSIS(www.lsis.org)
 * 
 * This file is part of GenericAHP.
 * 
 * GenericAHP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GenericAHP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GenericAHP.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.taeradan.ahp.gui;

import org.taeradan.ahp.Criteria;
import Jama.Matrix;
import java.util.logging.Logger;
import org.nfunk.jep.JEP;
import org.taeradan.ahp.Indicator;

/**
 * Dialog used to configure a Criteria's informations and preference matrix
 * @author Yves Dubromelle
 */
public class CriteriaDialog extends javax.swing.JDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *
	 */
	private Criteria criteria;
	/**
	 *
	 */
	private PrefMatrixTableModel guiPrefMatrix;

	/** Creates new form CriteriaDialog
	 * @param parent
	 * @param modal
	 * @param criteria
	 */
	public CriteriaDialog(java.awt.Frame parent, boolean modal, Criteria criteria) {
		super(parent, modal);
		this.criteria = criteria;
		guiPrefMatrix = new PrefMatrixTableModel();
		initTable();
		initComponents();
		guiPrefMatrix.addTableModelListener(new PrefMatrixChangeListener());
		jTextFieldId.setText(criteria.getId());
		jTextFieldName.setText(criteria.getName());
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents()
        {

                jLabelId = new javax.swing.JLabel();
                jLabelName = new javax.swing.JLabel();
                jTextFieldId = new javax.swing.JTextField();
                jTextFieldName = new javax.swing.JTextField();
                jScrollPane1 = new javax.swing.JScrollPane();
                jTablePrefMatrix = new javax.swing.JTable();
                jButtonReload = new javax.swing.JButton();
                jButtonSave = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                setTitle("Criteria properties");
                setResizable(false);

                jLabelId.setText("ID");

                jLabelName.setText("Name");

                jTablePrefMatrix.setModel(guiPrefMatrix);
                jTablePrefMatrix.setRowHeight(22);
                jScrollPane1.setViewportView(jTablePrefMatrix);

                jButtonReload.setText("Reload");
                jButtonReload.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                                jButtonReloadActionPerformed(evt);
                        }
                });

                jButtonSave.setText("Save");
                jButtonSave.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
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
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabelName)
                                                        .addComponent(jLabelId))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jTextFieldName, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                                                        .addComponent(jTextFieldId, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jButtonSave)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButtonReload)))
                                .addContainerGap())
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabelId)
                                        .addComponent(jTextFieldId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabelName)
                                        .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
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
		jTextFieldId.setText(criteria.getId());
		jTextFieldName.setText(criteria.getName());
	}//GEN-LAST:event_jButtonReloadActionPerformed

	private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
		criteria.setId(jTextFieldId.getText());
		criteria.setName(jTextFieldName.getText());
		Matrix matrix = new Matrix(guiPrefMatrix.getRowCount(), guiPrefMatrix.getColumnCount());
		Logger.getAnonymousLogger().info(guiPrefMatrix.getValueAt(1, 1).getClass().getCanonicalName());
		for(int i = 0; i < guiPrefMatrix.getRowCount(); i++) {
			for(int j = 0; j < guiPrefMatrix.getColumnCount(); j++) {
				double value = 0;
				if(guiPrefMatrix.getValueAt(i, j) instanceof Double) {
					value = (Double) guiPrefMatrix.getValueAt(i, j);
				}
				if(guiPrefMatrix.getValueAt(i, j) instanceof String) {
					JEP myParser = new JEP();
					myParser.parseExpression((String) guiPrefMatrix.getValueAt(i, j));
					value = myParser.getValue();
				}
				matrix.set(i, j, value);
			}
		}
		criteria.getMatrixInd().setMatrix(matrix);
		this.dispose();
	}//GEN-LAST:event_jButtonSaveActionPerformed

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton jButtonReload;
        private javax.swing.JButton jButtonSave;
        private javax.swing.JLabel jLabelId;
        private javax.swing.JLabel jLabelName;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTable jTablePrefMatrix;
        private javax.swing.JTextField jTextFieldId;
        private javax.swing.JTextField jTextFieldName;
        // End of variables declaration//GEN-END:variables

	/**
	 *
	 */
	private void initTable() {
		int matrixSize = criteria.getIndicators().size();
		String[] columnNames = new String[matrixSize];
		Double[][] data = new Double[matrixSize][matrixSize];
		for(int i = 0; i < matrixSize; i++) {
			columnNames[i] = ((Indicator)criteria.getIndicators().toArray()[i]).getId();
			for(int j = 0; j < matrixSize; j++) {
				data[i][j] = criteria.getMatrixInd().getMatrix().get(i, j);
			}
		}
		guiPrefMatrix.setDataVector(data, columnNames);
	}

	/**
	 *
	 * @param row
	 * @param column
	 */
	public void reloadCell(int row, int column) {
		guiPrefMatrix.setValueAt(criteria.getMatrixInd().getMatrix().get(row, column), row, column);
	}

	/**
	 *
	 * @return
	 */
	public PrefMatrixTableModel getGuiPrefMatrix() {
		return guiPrefMatrix;
	}
}
