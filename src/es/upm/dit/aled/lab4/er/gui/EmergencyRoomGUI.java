package es.upm.dit.aled.lab4.er.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collection;
import javax.swing.JFrame;
import javax.swing.JPanel;

import es.upm.dit.aled.lab4.er.Area;
import es.upm.dit.aled.lab4.er.EmergencyRoom;
import es.upm.dit.aled.lab4.er.Patient;
import es.upm.dit.aled.lab4.er.Transfer;

/**
 * Shows a live graphical representation of a hospital ER simulation modeled by
 * the EmergencyRoom class. A singleton.
 * 
 * @author rgarciacarmona
 */
public class EmergencyRoomGUI { // Es un singleton: una clase que solo permite crear un único objeto, porque no
								// hay más de una sala de emergencia e un hospital, por eso usamos getInstance

	private JFrame frame;
	private DrawingPanel drawingPanel;

	private static EmergencyRoomGUI instance;

	private Collection<Area> areas;
	private Collection<Patient> patients;

	// Constants for drawing
	private static final int AREA_SIZE = 120;
	private static final int PATIENT_DIAMETER = 10;

	// Private constructor. Call initialize() or getInstance()
	private EmergencyRoomGUI(EmergencyRoom er) {
		this.areas = er.getAreas();
		this.patients = er.getPatients();

		frame = new JFrame("Emergency Room Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);

		drawingPanel = new DrawingPanel();
		drawingPanel.setPreferredSize(new Dimension(800, 600));
		frame.add(drawingPanel);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/**
	 * Returns the already existing EmergencyRoomGUI singleton's instance. If no
	 * instance already exists it throws an IllegalStateException.
	 * 
	 * @return The EmergencyRoomGUI instance.
	 */
	public static EmergencyRoomGUI getInstance() {
		if (instance == null) {
			throw new IllegalStateException("EmergencyRoomGUI not yet initialized. Call initialize() first.");
		}
		return instance;
	}

	/**
	 * Initializes the EmergencyRoomGUI singleton's instance from an EmergencyRoom.
	 * 
	 * @param er The EmergencyRoom.
	 */
	public static EmergencyRoomGUI initialize(EmergencyRoom er) {
		if (instance == null) {
			instance = new EmergencyRoomGUI(er);
		}
		return instance;
	}

	/**
	 * Animates a Patient's Transfer between Areas. This method takes as much time
	 * to perform the animation as the time stated in the Transfer. Therefore,
	 * whichever calls this method don't need to wait any additional amount of time
	 * to represent the Transfer.
	 * 
	 * @param p The Patient.
	 * @param t The Transfer.
	 */
	public void animateTransfer(Patient p, Transfer t) {
		Position2D startPos = p.getPosition();
		Area endArea = t.getTo();
		Position2D endPos = endArea.getPosition();
		int duration = t.getTime(); // Total duration in ms

		final int updateInterval = 20;
		int steps = duration / updateInterval;
		if (steps == 0)
			steps = 1;

		double dx = (endPos.getX() - startPos.getX()) / steps;
		double dy = (endPos.getY() - startPos.getY()) / steps;

		try {
			for (int i = 0; i < steps; i++) {
				double newX = startPos.getX() + dx * (i + 1);
				double newY = startPos.getY() + dy * (i + 1);
				Position2D newPos = new Position2D(newX, newY);

				p.setPosition(newPos);
				drawingPanel.repaint();
				Thread.sleep(updateInterval);
			}

			p.setPosition(endPos);
			drawingPanel.repaint();

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			System.err.println("Transfer animation interrupted for patient " + p.getNumber());
		}
	}

	/**
	 * Removes a Patient from the screen.
	 * @param p The Patient.
	 */
	public void removePatient(Patient p) {
		patients.remove(p);
		drawingPanel.repaint();
	}

	/**
	 * Inner class for the drawing canvas.
	 */
	private class DrawingPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD));

			// Draw Areas
			for (Area area : areas) {
				// Draw rectangle
				Position2D pos = area.getPosition();
				int x = (int) (pos.getX() - AREA_SIZE / 2);
				int y = (int) (pos.getY() - AREA_SIZE / 2);
				g2d.setColor(area.getColor());
				g2d.fillRect(x, y, AREA_SIZE, AREA_SIZE);
				g2d.setColor(Color.BLACK);
				g2d.drawRect(x, y, AREA_SIZE, AREA_SIZE);

				// Draw text
				String areaName = area.getName();
				FontMetrics fm = g2d.getFontMetrics();
				int textX = x + (AREA_SIZE - fm.stringWidth(areaName)) / 2 + 2;
				int textY = y + 15;
				areaName = areaName.replace('_', ' ');
				g2d.drawString(areaName, textX, textY);

			}

			// Draw Patients
			for (Patient patient : patients) {
				// Draw dot
				Position2D pos = patient.getPosition();
				int x = (int) (pos.getX() - PATIENT_DIAMETER / 2);
				int y = (int) (pos.getY() - PATIENT_DIAMETER / 2);
				g2d.setColor(patient.getColor());
				g2d.fillOval(x, y, PATIENT_DIAMETER, PATIENT_DIAMETER);

				// Draw text
				g2d.setColor(Color.BLACK);
				g2d.drawString("P" + patient.getNumber(), x + PATIENT_DIAMETER + 2, y + PATIENT_DIAMETER);
			}
		}
	}
}