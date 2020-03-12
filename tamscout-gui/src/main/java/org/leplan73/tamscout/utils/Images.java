package org.leplan73.tamscout.utils;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public class Images {
	public final static String ROOT_FOLDER = "org/leplan73/tamscout";

	public static Image getIcon() {
		return Toolkit.getDefaultToolkit()
				.getImage(Images.class.getClassLoader().getResource(ROOT_FOLDER + "/icone.png"));
	}
	
	public static ImageIcon getIconCog() {
		return new ImageIcon(Images.class.getClassLoader().getResource(ROOT_FOLDER + "/cog.png"));
	}
	
	public static ImageIcon getImage() {
		return new ImageIcon(Images.class.getClassLoader().getResource(ROOT_FOLDER + "/panneau_gauche.png"));
	}
}
