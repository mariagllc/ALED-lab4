package es.upm.dit.aled.lab4.er;

import java.awt.Color;
import java.util.Objects;

import es.upm.dit.aled.lab4.er.gui.Position2D;

/**
 * Models an area in a hospital ER. Each Area is characterized by its name
 * (which must be unique) and the time a patient must spend there to be treated
 * (in milliseconds).
 * 
 * Each Area is represented graphically by a square of side 120 px, centered in
 * a given position and with a custom color.
 * 
 * @author rgarciacarmona
 */
public class Area {

	private String name;
	private int time;
	private Position2D position;
	private Color color;

	/**
	 * Builds a new Area.
	 * 
	 * @param name     The name of the Area.
	 * @param time     The amount of time (in milliseconds) needed to treat a
	 *                 Patient in this Area.
	 * @param position The location of the Area in the GUI.
	 */
	public Area(String name, int time, Position2D position) {
		this.name = name;
		this.time = time;
		this.position = position;
	}

	/**
	 * Returns the name of the Area.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the time (in milliseconds) that a Patient must spend in the Area to
	 * be treated.
	 * 
	 * @return The time.
	 */
	public int getTime() { 
		return time;
	}

	/**
	 * Returns the position of the center of the Area in the GUI.
	 * 
	 * @return The position.
	 */
	public Position2D getPosition() {
		return position;
	}

	/**
	 * Returns the color of Area in the GUI.
	 * 
	 * @return The color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Changes the color of the Area in the GUI.
	 * 
	 * @param color The new color.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Area other = (Area) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return name;
	}
}