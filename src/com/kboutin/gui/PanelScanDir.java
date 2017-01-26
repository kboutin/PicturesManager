package com.kboutin.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.kboutin.utils.GUIUtils;

public class PanelScanDir extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static PanelScanDir INSTANCE = null;

	private JLabel lblDirToScan = new JLabel();
	private JButton btnChooseDir = new JButton("...");

	private PanelScanDir() {

		setLayout(new BorderLayout());

		//btnChooseDir.addActionListener(genFrame);

		add(lblDirToScan, BorderLayout.CENTER);
		add(btnChooseDir, BorderLayout.EAST);
		setBorder(GUIUtils.createEtchedTitledBorder("Repertoire a analyser"));
	}

	public static PanelScanDir getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new PanelScanDir();
		}

		return INSTANCE;
	}

	public final void addActionListener(ActionListener l) {

		btnChooseDir.addActionListener(l);
	}

	public final void updateDirToScan(String dirToScanPath) {

		lblDirToScan.setText(" " + dirToScanPath);
	}
}
