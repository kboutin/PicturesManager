package com.kboutin.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.kboutin.utils.GUIUtils;
import com.kboutin.utils.StringUtils;

public class PanelScanDir extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JLabel lblDirToScan = new JLabel();
	private JButton btnChooseDir = new JButton("...");

	public PanelScanDir() {

		setLayout(new BorderLayout());

		btnChooseDir.setActionCommand(StringUtils.SCAN_DIR_COMMAND);

		add(lblDirToScan, BorderLayout.CENTER);
		add(btnChooseDir, BorderLayout.EAST);
		setBorder(GUIUtils.createEtchedTitledBorder("Repertoire a analyser"));
	}

	public final void updateDirToScan(String dirToScanPath) {

		lblDirToScan.setText(" " + dirToScanPath);
	}

	public final void addActionListener(ActionListener l) {
		btnChooseDir.addActionListener(l);
	}
}
