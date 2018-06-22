package com.kboutin.gui;

import com.kboutin.utils.GUIUtils;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

public class PanelScanDir extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JLabel lblDirToScan = new JLabel();
	private JButton btnChooseDir = new JButton("...");

	public PanelScanDir() {

		setLayout(new BorderLayout());

		add(lblDirToScan, BorderLayout.CENTER);
		add(btnChooseDir, BorderLayout.EAST);
		setBorder(GUIUtils.createEtchedTitledBorder("Repertoire a analyser"));
	}

	public final void addActionListener(ActionListener l) {

		btnChooseDir.addActionListener(l);
	}

	public final void setActionCommand(String actionCommand) {
		btnChooseDir.setActionCommand(actionCommand);
	}

	public final void setDirToScan(String dirToScanPath) {

		lblDirToScan.setText(" " + dirToScanPath);
	}
}
