package com.kboutin.gui;

import com.kboutin.core.Picture;

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

import static com.kboutin.utils.GUIUtils.createEtchedTitledBorder;
import static java.awt.BorderLayout.CENTER;

public class PanelPicture extends JPanel {

	@Serial
	private static final long serialVersionUID = 1L;

	private final JLabel lblPicture = new JLabel();

	public PanelPicture() {
		super(new BorderLayout());
		setBorder(createEtchedTitledBorder(""));
		add(lblPicture, CENTER);
	}

	public final void updatePicture(Picture p) {
		if (p == null) {
			lblPicture.setIcon(null);
			setBorder(createEtchedTitledBorder(""));
			return;
		}
		File pictureFile = new File(p.getFilePath());
		setBorder(createEtchedTitledBorder(pictureFile.getName()));
		lblPicture.setIcon(getIconFromPicture(pictureFile));
	}

	// FIXME KBO Fix case when picture is portrait side...
	private Icon getIconFromPicture(File selectedPicture) {
		if (selectedPicture == null) {
			return null;
		}
		try {
			BufferedImage bufferedPicture = ImageIO.read(selectedPicture);
			Image resizedPicture = bufferedPicture.getScaledInstance(lblPicture.getWidth(), -1, Image.SCALE_SMOOTH); // -1 is used to keep image ratio
			return new ImageIcon(resizedPicture);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
