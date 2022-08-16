package com.kboutin.gui;

import com.kboutin.utils.GUIUtils;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.Serial;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;

public class PanelScanDir extends JPanel {

	@Serial
	private static final long serialVersionUID = 1L;

	private static PanelScanDir INSTANCE = null;

	private final JLabel lblDirToScan = new JLabel();
	private final JButton btnChooseDir = new JButton("...");

	private PanelScanDir() {

		setLayout(new BorderLayout());

		//btnChooseDir.addActionListener(genFrame);

		add(lblDirToScan, CENTER);
		add(btnChooseDir, EAST);
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
