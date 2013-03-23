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
package org.taeradan.ahp.prototype.gui.netbeans_gui_modeler;

import org.taeradan.ahp.AHPRoot;
import org.taeradan.ahp.Criterion;
import org.taeradan.ahp.EmptyAHPRoot;
import org.taeradan.ahp.Indicator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Graphical user interface to configure the AHP tree and the preference matrix
 *
 * @author Yves Dubromelle
 * @author Jean-Pierre PRUNARET
 */
public final class ConfigurationFrame
		extends JFrame {

	private static final long serialVersionUID = 1L;
	private final transient DefaultTreeModel guiAhpTree;
	private transient File currentFile = new File(System.getProperty("user.dir"));
	private transient AHPRoot ahpAHPRoot;
	private transient boolean fileOpened = false;

	/** Creates new form ConfigurationFrame */
	private ConfigurationFrame() {
		super();
//		Instanciation of an empty TreeModel
		guiAhpTree = new DefaultTreeModel(new DefaultMutableTreeNode());
//		Instantiation of an empty AHP root to use as default while no file is loaded
		ahpAHPRoot = new EmptyAHPRoot();
//		The real AHP tree is attached to the graphical TreeModel to be displayed dynamically
		guiAhpTree.setRoot(processAhpHierarchy(ahpAHPRoot));
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	private void initComponents() {

		jFileChooser = new JFileChooser();
		jScrollPane1 = new JScrollPane();
		jTreeAhp = new JTree();
		jMenuBar1 = new JMenuBar();
		jMenuFile = new JMenu();
		jMenuItemOpen = new JMenuItem();
		jMenuItemSave = new JMenuItem();
		jMenuItemSaveUnder = new JMenuItem();
		jMenuItemQuit = new JMenuItem();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("AHP Tree Configuration");

		jTreeAhp.setModel(guiAhpTree);
		jTreeAhp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				jTreeAhpMouseClicked(evt);
			}
		});
		jScrollPane1.setViewportView(jTreeAhp);

		jMenuFile.setText("File");

		jMenuItemOpen.setText("Open");
		jMenuItemOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				jMenuItemOpenActionPerformed();
			}
		});
		jMenuFile.add(jMenuItemOpen);

		jMenuItemSave.setText("Save");
		jMenuItemSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				jMenuItemSaveActionPerformed(evt);
			}
		});
		jMenuFile.add(jMenuItemSave);

		jMenuItemSaveUnder.setText("Save under...");
		jMenuItemSaveUnder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				jMenuItemSaveUnderActionPerformed();
			}
		});
		jMenuFile.add(jMenuItemSaveUnder);

		jMenuItemQuit.setText("Quit");
		jMenuFile.add(jMenuItemQuit);

		jMenuBar1.add(jMenuFile);

		setJMenuBar(jMenuBar1);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					  .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
								 );
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					  .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
							   );

		pack();
	}

	/**
	 * Handles the actions of the mouse clicks.
	 *
	 * @param evt
	 */
	private void jTreeAhpMouseClicked(MouseEvent evt) {
//			System.out.println("bouton="+evt.getButton()+"nbClick"+evt.getClickCount());
//			If the mouse is over a valid tree node...
		if (jTreeAhp.getPathForLocation(evt.getX(), evt.getY()) != null) {
//				Retrieve the selected node
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeAhp.getPathForLocation(
					evt.getX(),
					evt.getY()).getLastPathComponent();
//				Handling of the the left button double click
			if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
				final Object object = node.getUserObject();
				editActionPerformed(object);
			}
//				Handling of the right button double click
			if (evt.getButton() == MouseEvent.BUTTON3) {
				final Object object = node.getUserObject();
				final JPopupMenu contextMenu = new JPopupMenu();
				if (object instanceof AHPRoot) {
					final AHPRoot AHPRoot = (AHPRoot) object;
					final JMenuItem addItem = new JMenuItem("Add criteria");
					addItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent evt) {

						}
					});
//						addItem.addActionListener(this);
					contextMenu.add(addItem);
					final JMenuItem editItem = new JMenuItem("Edit");
					editItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent evt) {
							editActionPerformed(object);
						}
					});
					contextMenu.add(editItem);
					final JMenuItem delItem = new JMenuItem("Delete");
					delItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent evt) {
							delRootActionPerformed();
						}
					});
					contextMenu.add(delItem);
				}
				if (object instanceof Criterion) {
					final Criterion criterion = (Criterion) object;
					final JMenuItem addItem = new JMenuItem("Add indicator");
					addItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent evt) {
						}
					});
					contextMenu.add(addItem);
					final JMenuItem editItem = new JMenuItem("Edit");
					editItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent evt) {
							editActionPerformed(object);
						}
					});
					contextMenu.add(editItem);
					final JMenuItem delItem = new JMenuItem("Delete");
					delItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent evt) {
							delCriteriaActionPerformed(criterion);
						}
					});
					contextMenu.add(delItem);
				}
				if (object instanceof Indicator) {
					Indicator indicator = (Indicator) object;
					final JMenuItem editItem = new JMenuItem("Edit");
					editItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent evt) {
						}
					});
					contextMenu.add(editItem);
					final JMenuItem delItem = new JMenuItem("Delete");
					delItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent evt) {
							editActionPerformed(object);
						}
					});
					contextMenu.add(delItem);
				}
				contextMenu.show(jTreeAhp, evt.getX(), evt.getY());
			}
		}
	}

	/**
	 * Handles the event of the "Open" menu. Launch a file selector to choose a configuration file to open.
	 *
	 */
	private void jMenuItemOpenActionPerformed() {
		jFileChooser = new JFileChooser(currentFile);
		jFileChooser.setApproveButtonText("Open");
		jFileChooser.setFileFilter(new FileNameExtensionFilter("XML document", "xml"));
		if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			currentFile = jFileChooser.getSelectedFile();
			ahpAHPRoot = new AHPRoot(new File(currentFile.getAbsolutePath()), AHPRoot.DEFAULT_INDICATOR_PATH);
			guiAhpTree.setRoot(processAhpHierarchy(ahpAHPRoot));
			fileOpened = true;
		}
	}

	/**
	 * Handles the event of the "Save" menu. It just save to the file previously opened. If no file is opened, launch the
	 * "Save under" action.
	 *
	 * @param evt
	 */
	private void jMenuItemSaveActionPerformed(ActionEvent evt) {
		if (fileOpened) {
			ahpAHPRoot.saveConfiguration(currentFile.getAbsolutePath());
		} else {
			jMenuItemSaveUnderActionPerformed();
		}
	}

	/** Handles the event of the "Save under" menu. Launch a file selector to choose a file to write. */
	private void jMenuItemSaveUnderActionPerformed() {
		jFileChooser = new JFileChooser(currentFile);
		jFileChooser.setApproveButtonText("Save");
		jFileChooser.setFileFilter(new FileNameExtensionFilter("XML document", "xml"));
		if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			currentFile = jFileChooser.getSelectedFile();
			ahpAHPRoot.saveConfiguration(currentFile.getAbsolutePath());
		}
	}

	private void editActionPerformed(final Object object) {
		if (object instanceof AHPRoot) {
			final AHPRoot AHPRoot = (AHPRoot) object;
			final RootDialog dialog = new RootDialog(this, true, AHPRoot);
			dialog.setVisible(true);
		}
		if (object instanceof Criterion) {
			final Criterion criterion = (Criterion) object;
			final CriteriaDialog dialog = new CriteriaDialog(this, true, criterion);
			dialog.setVisible(true);
		}
		if (object instanceof Indicator) {
			final Indicator indicator = (Indicator) object;
			final IndicatorDialog dialog = new IndicatorDialog(this, true, indicator);
			dialog.setVisible(true);
		}
	}

	private void delRootActionPerformed() {
		if (JOptionPane.showConfirmDialog(
				this,
				"Are you sure ? The whole tree will be destroyed.",
				"Confirmation needed",
				JOptionPane.YES_NO_OPTION) == 0) {

			ahpAHPRoot = new AHPRoot(null, AHPRoot.DEFAULT_INDICATOR_PATH);
			guiAhpTree.setRoot(processAhpHierarchy(ahpAHPRoot));
			editActionPerformed(ahpAHPRoot);
		}
	}

	private void delCriteriaActionPerformed(final Criterion criterion) {
		if (JOptionPane.showConfirmDialog(
				this,
				"Are you sure ? The criterion and its indicators will be destroyed.",
				"Confirmation needed",
				JOptionPane.YES_NO_OPTION) == 0) {

			ahpAHPRoot.guiMethods.removeCriterion(criterion);
			guiAhpTree.setRoot(processAhpHierarchy(ahpAHPRoot));
		}
	}

	private void delIndicatorActionPerformed() {
		if (JOptionPane.showConfirmDialog(
				this,
				"Are you sure ? The indicator will be destroyed.",
				"Confirmation needed",
				JOptionPane.YES_NO_OPTION) == 0) {
		}
	}

	public static void main(final String args[]) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ConfigurationFrame().setVisible(true);
			}
		});
	}

	private JFileChooser jFileChooser;
	private JMenuBar     jMenuBar1;
	private JMenu        jMenuFile;
	private JMenuItem    jMenuItemOpen;
	private JMenuItem    jMenuItemQuit;
	private JMenuItem    jMenuItemSave;
	private JMenuItem    jMenuItemSaveUnder;
	private JScrollPane  jScrollPane1;
	private JTree        jTreeAhp;

	/**
	 * Takes an initialized AHP root element and produces a tree by processing the AHP hierarchy
	 *
	 * @param ahpAHPRoot Initialised AHP root
	 * @return node containing a AHP tree
	 */
	private static DefaultMutableTreeNode processAhpHierarchy(final AHPRoot ahpAHPRoot) {
//		Creation of the root node
		final DefaultMutableTreeNode guiRoot = new DefaultMutableTreeNode(ahpAHPRoot);
		final Collection<Criterion> ahpCriteria = ahpAHPRoot.guiMethods.getCriteria();
		ArrayList<DefaultMutableTreeNode> guiCriteria = new ArrayList<>();
//		For each criteria in root
		for (int i = 0; i < ahpCriteria.size(); i++) {
//			Real criteria attached to a criteria node
			guiCriteria.add(new DefaultMutableTreeNode(ahpCriteria.toArray()[i]));
//			Criterion node attached to the root node
			guiRoot.add(guiCriteria.get(i));
			final Collection<Indicator> ahpIndicators = ((Criterion) ahpCriteria.toArray()[i]).
																									  getIndicators();
			ArrayList<DefaultMutableTreeNode> guiIndicators =
					new ArrayList<>();
//			For each indicator in the criteria
			for (int j = 0; j < ahpIndicators.size(); j++) {
//				Real indicator attached to an indicator node
				guiIndicators.add(new DefaultMutableTreeNode(ahpIndicators.toArray()[j]));
//				Indicator node attached to the criteria node
				guiCriteria.get(i).add(guiIndicators.get(j));
			}
		}
		return guiRoot;
	}
}
