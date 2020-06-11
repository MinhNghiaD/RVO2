package model;

import java.util.ArrayList;

import sim.util.Double2D;

public class Obstacle {
	private Double2D position;
	private int taille;
	private int type;
	
	public Obstacle(Double2D position, int taille, int type) {
		this.position = position;
		this.taille = taille;
		this.type = type;
	}
	
	public Double2D getPosition() {
		return position;
	}
	public void setPosition(Double2D position) {
		this.position = position;
	}
	
	public int getTaille() {
		return taille;
	}
	public void setTaille(int taille) {
		this.taille = taille;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}