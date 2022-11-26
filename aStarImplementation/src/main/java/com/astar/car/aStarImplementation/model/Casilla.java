package com.astar.car.aStarImplementation.model;

/**
 * Clase que define los vértices del grafo. Sólo tiene de especial el método
 * equals que define dos casillas iguales si su X y su Y coinciden, siendo estas
 * las posiciones de fila y columna en la matriz
 */
public class Casilla {

	private int x;
	private int y;

	public Casilla(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		Casilla cas = (Casilla) obj;
		return this.x == cas.x && this.y == cas.y;
	}

	@Override
	public String toString() {
		return "Casilla:\n" + "fila= " + this.x + "\n" + "columna= " + this.y+"\n";
	}

}
