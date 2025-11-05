package es.upm.dit.aled.lab4.er;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import es.upm.dit.aled.lab4.er.gui.EmergencyRoomGUI;
import es.upm.dit.aled.lab4.er.gui.Position2D;

/**
 * Models a patient in a hospital ER. Each Patient is characterized by its
 * number (which must be unique), its current location and a protocol. The
 * protocol is a List of Transfers. Each Patient also has an index to indicate
 * at which point of the protocol they are at the current time.
 * 
 * Patients are Threads, and therefore must implement the run() method.
 * 
 * Each Patient is represented graphically by a dot of diameter 10 px, centered
 * in a given position and with a custom color.
 * 
 * @author rgarciacarmona
 */
public class Patient extends Thread {

	private int number;
	private List<Transfer> protocol;
	private int indexProtocol;
	private Area location;
	private Position2D position;
	private Color color;

	/**
	 * Builds a new Patient.
	 * 
	 * @param numbre          The number of the Patient.
	 * @param initialLocation The initial location of the Patient.
	 */
	public Patient(int number, Area initialLocation) {
		this.number = number;
		this.protocol = new ArrayList<Transfer>();
		this.indexProtocol = 0;
		this.location = initialLocation;
		this.position = initialLocation.getPosition();
		this.color = Color.GRAY; // Default color
	}

	/**
	 * Returns the number of the Patient.
	 * 
	 * @return The number.
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Returns the protocol of the Patient.
	 * 
	 * @return The protocol.
	 */
	public List<Transfer> getProtocol() {
		return protocol;
	}

	/**
	 * Returns the current location of the Patient.
	 * 
	 * @return The current location.
	 */
	public Area getLocation() {
		return location;
	}

	/**
	 * Changes the current location of the Patient.
	 * 
	 * @param location The new location.
	 */
	public void setLocation(Area location) {
		this.location = location;
	}

	/**
	 * Returns the position of the Patient in the GUI.
	 * 
	 * @return The position.
	 */
	public Position2D getPosition() {
		return position;
	}

	/**
	 * Changes the position of the Patient in the GUI.
	 * 
	 * @param position The new position.
	 */
	public void setPosition(Position2D position) {
		this.position = position;
	}

	/**
	 * Returns the color of Patient in the GUI.
	 * 
	 * @return The color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Changes the color of the Patient in the GUI.
	 * 
	 * @param color The new color.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Adds a new Transfer at the end of the Patient's protocol.
	 * 
	 * @param transfer The new Transfer.
	 */
	public void addToProtocol(Transfer transfer) {
		this.protocol.add(transfer);
	}

	/**
	 * Advances the Patient's protocol. The Patient is moved to the new Area, the
	 * movement is animated by the GUI and the index is increased by one.
	 */
	private void advanceProtocol() { //NO ESPERAMOS AQUI, ya espera EmergencyRoomGUI
		// TODO
		if (indexProtocol < protocol.size()){
			//creo el movimiento del paciente, el que le toca en este protocolo 
			Transfer t = protocol.get(indexProtocol);
			try{
				EmergencyRoomGUI.getInstance().animateTransfer(this,t); //this es el paciente q mueves, animateTransfer(Patient,Time)
				System.out.println("Paciente " + number + ": se ha trasladado a " + location.getName());
			} catch(IllegalStateException e) { //si no se ha ploteado nada, niguna interfaz grafica yet
				System.out.println("EmergencyRoomGUI not yet initialized.");
			}
			setLocation(t.getTo()); // le pongo al paciente donde se encuentra al final del mvto, busco en su
									// transfer, su mvto ya definido, cual es el destino
			indexProtocol++;
		}
		
	}

	/**
	 * Simulates the treatment of the Patient at its current location. Therefore,
	 * the Patient must spend at this method the amount of time specified in such
	 * Area.
	 */
	private void attendedAtLocation() {
		// TODO
		 int time = location.getTime(); // tiempo que tarda ese área (location) en liberarse, tiempi que tardan en atenderte
		    try {
		        Thread.sleep(time); // el pac espera ese tiempo justo, esta siendo atendido, el puntito no se mueve!
		        System.out.println("Paciente " + number + ": ha sido atendido en " + location.getName());
		    } catch (InterruptedException e) { //catchea la excepción de sleep
		        Thread.currentThread().interrupt();
		        System.out.println("Se ha interrumpido la tarea");
		    }
	}

	/**
	 * Executes the Patient's behavior. It follows their protocol by being attended
	 * at the current location and then moving onto the next, until the last step of
	 * the protocol is reached. At that point, the Patient is removed from the GUI.
	 */
	@Override
	public void run() {
		// TODO
	    while (indexProtocol < protocol.size()) { // mientras no hayamos llegado al final del protocolo
	        attendedAtLocation();  // ser atendido en la ubicación actual
	        advanceProtocol(); // avanzar al siguiente paso en su protocolo
	    }
	    attendedAtLocation();  // ya se ha llegado al último paso del protocolo, atender una última vez en la ubicación final
	    try {
	        EmergencyRoomGUI.getInstance().removePatient(this); //el paciente deberá pedir a EmergencyRoomGUI que lo elimine
	        System.out.println("Paciente " + number + ": ha terminado su protocolo y ha sido dado de alta");
	    } catch (IllegalStateException e) {
	        System.out.println("GUI not initialized when removing patient " + number);
	    }
	}

}