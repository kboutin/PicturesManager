package com.kboutin.utils;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GUIUtils {

	public static TitledBorder createEtchedTitledBorder(String title) {

		return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), title);
	}
}
