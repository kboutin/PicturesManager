package com.kboutin.gui;

import com.kboutin.core.Picture;
import com.kboutin.utils.GUIUtils;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serial;

public class PanelPicture extends JPanel {

	@Serial
	private static final long serialVersionUID = 1L;

	private final JLabel lblPicture = new JLabel();

	public PanelPicture() {

		super(new BorderLayout());

		setBorder(GUIUtils.createEtchedTitledBorder(""));
		add(lblPicture, BorderLayout.CENTER);
	}

	public final void updatePicture(Picture p) {

		if (p == null) {
			return;
		}

		File pictureFile = new File(p.getFilePath());
		setBorder(GUIUtils.createEtchedTitledBorder(pictureFile.getName()));
		lblPicture.setIcon(getIconFromPicture(pictureFile));
	}

	// FIXME KBO Fix case when picture is portrait side...
	private Icon getIconFromPicture(File selectedPicture) {

		if (selectedPicture == null) {
			return null;
		}

		BufferedImage bufferedPicture;
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
