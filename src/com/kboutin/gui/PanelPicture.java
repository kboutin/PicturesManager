package com.kboutin.gui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.kboutin.core.Picture;
import com.kboutin.utils.GUIUtils;

public class PanelPicture extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JLabel lblPicture = new JLabel();
	private TitledBorder border = GUIUtils.createEtchedTitledBorder("");

	public PanelPicture() {

		super(new BorderLayout());

		setBorder(border);
		add(lblPicture, BorderLayout.CENTER);
	}

	public final void updatePicture(Picture p) {

		if (p == null) {
			return;
		}

		File pictureFile = new File(p.getFilePath());
		updatePicture(pictureFile);
	}

	private final void updatePicture(File picture) {

		setBorder(GUIUtils.createEtchedTitledBorder(picture != null ? picture.getName() : ""));
		lblPicture.setIcon(getIconFromPicture(picture));
	}

	// FIXME KBO Fix case when picture is portrait side...
	private final Icon getIconFromPicture(File selectedPicture) {

		if (selectedPicture == null) {
			return null;
		}

		//System.out.println("PictureTitle : " + selectedPicture.getName());
		//System.out.println("PictureWidth : " + lblPicture.getWidth() + " - PictureHeight : " + lblPicture.getHeight());

		BufferedImage bufferedPicture = null;
		Image resizedPicture = null;
		try {
			bufferedPicture = ImageIO.read(selectedPicture);
			resizedPicture = bufferedPicture.getScaledInstance(lblPicture.getWidth(), -1, Image.SCALE_SMOOTH); // -1 is used to keep image ratio
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ImageIcon(resizedPicture);
	}
}
