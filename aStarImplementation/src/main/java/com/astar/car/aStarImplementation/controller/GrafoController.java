package com.astar.car.aStarImplementation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.astar.car.aStarImplementation.algorithms.CocheGraph;
import com.astar.car.aStarImplementation.model.Casilla;

/** Controlador de ejecución del grafo para el front */
@RestController
public class GrafoController {

	/** Get que solo pinta una matriz predeterminada que se puede cambiar aquí */
	@CrossOrigin
	@GetMapping("/array")
	public String[][] generateArray() {
		String[][] grafoBarrancos = { { "c", "b", "o" }, { "", "o", "" }, { "b", "o", "f" }, { "", "o", "" },
				{ "b", "b", "o" } };
		return grafoBarrancos;
	}

	/**
	 * Controlador que ejecuta el front
	 * 
	 * @param grafo la matriz grafo con obstáculos y barrancos
	 * @return la matriz con la "c" que representa el coche actualizada
	 */
	@CrossOrigin
	@PostMapping("/execute")
	public String[][] execute(@RequestBody String[][] grafo) {
		CocheGraph coche = new CocheGraph();
		int filas = grafo.length;
		int columnas = grafo[0].length;
		coche.createGraph(filas, columnas);
		obtenerCasillaInicial(grafo, coche);
		coche.setPos_destino(coche.getCasillaInGrafo(new Casilla(2, 2)));
		List<Casilla> listaVertices = coche.caminoMasCorto().getVertexList();
		Casilla siguiente;
		if (listaVertices.size() == 1) {
			siguiente = coche.caminoMasCorto().getVertexList().get(0);
		} else {
			siguiente = coche.caminoMasCorto().getVertexList().get(1);
		}
		grafo[siguiente.getX()][siguiente.getY()] = "c";
		return grafo;
	}

	/**
	 * Método que detecta cual es la posición actual del coche y la introduce en el
	 * algoritmo al tiempo que borra la c indicativa del grafo para poder meter el
	 * siguiente paso
	 */
	public void obtenerCasillaInicial(String[][] grafo, CocheGraph c) {

		for (int i = 0; i < grafo.length; i++) {
			for (int j = 0; j < grafo[i].length; j++) {
				if (grafo[i][j].equals("c")) {
					grafo[i][j] = "";
					Casilla cas = c.getCasillaInGrafo(new Casilla(i, j));
					c.setPos_inicial(cas);
				}
			}
		}
	}

}
