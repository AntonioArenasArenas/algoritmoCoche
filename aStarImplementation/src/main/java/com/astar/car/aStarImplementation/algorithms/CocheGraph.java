package com.astar.car.aStarImplementation.algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.astar.car.aStarImplementation.model.Casilla;

/** Esta clase genera el esqueleto de un grafo al llamar al constructor vacío */
public class CocheGraph {

	// Grafo en cuestión
	private Graph<Casilla, DefaultWeightedEdge> grafo;
	/**
	 * Vidas del coche, se considera propiedad del grafo ya que afecta a la
	 * heurística del mismo. El peso de cada arista se incrementará al detectar
	 * obstáculos en función del número de vidas restantes
	 */
	private int vidas;
	private Casilla pos_inicial;
	private Casilla pos_destino;
	private List<Casilla> caminoCorto;

	/**
	 * Por defecto el grafo se instanciará con posición inicial (0,0), posición
	 * final (2,2) y 10 vidas, Estos datos se setearán en la configuración con front
	 * La posición destino sigue siendo necesario cogerla con indexOf y get para que
	 * lo detecte correctamente
	 */
	public CocheGraph() {

		this.grafo = new DefaultUndirectedWeightedGraph<Casilla, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.vidas = 10;
		this.pos_inicial = new Casilla(0, 0);
		this.pos_destino = new Casilla(2, 2);
		this.caminoCorto = new LinkedList<>();

	}

	public List<Casilla> getCaminoCorto() {
		return caminoCorto;
	}

	public void setCaminoCorto(List<Casilla> caminoCorto) {
		this.caminoCorto = caminoCorto;
	}

	public Graph<Casilla, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(Graph<Casilla, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}

	public int getVidas() {
		return vidas;
	}

	public void setVidas(int vidas) {
		this.vidas = vidas;
	}

	public Casilla getPos_inicial() {
		return pos_inicial;
	}

	public void setPos_inicial(Casilla pos_inicial) {
		this.pos_inicial = pos_inicial;
	}

	public Casilla getPos_destino() {
		return pos_destino;
	}

	public void setPos_destino(Casilla pos_destino) {
		this.pos_destino = pos_destino;
	}

	/**
	 * Método usado para crear el grafo, crea el grafo con las dimensiones adecuadas
	 * y conectados los vértices correspondientes con aristas de peso 1
	 * 
	 * @param filas    las filas de la matriz
	 * @param columnas las columnas de la matriz
	 */
	public void createGraph(int filas, int columnas) {

		for (int i = 0; i < filas; i++) {

			for (int j = 0; j < columnas; j++) {

				grafo.addVertex(new Casilla(i, j));
			}

		}
		List<Casilla> casillas = new ArrayList<Casilla>(grafo.vertexSet());
		for (Casilla c : casillas) {
			this.introducirArista(c, filas, columnas);

		}
	}

	/**
	 * Método que introduce las 4 aristas que tiene cada casilla, si son limítrofes,
	 * conecta con el otro extremo
	 * 
	 * @param c        casilla a la que se le van a poner las aristas
	 * @param filas    de la matriz
	 * @param columnas de la matriz
	 */
	public void introducirArista(Casilla c, int filas, int columnas) {
		// En el sentido de las agujas del reloj, las coordenadas de los vecinos
		// de la casilla actual

		List<Casilla> vecinos = calculaVecinos(c, filas, columnas);
		// Vertice superior
		grafo.addEdge(c, vecinos.get(0));
		// Vertice derecho
		grafo.addEdge(c, vecinos.get(1));
		// Vertice inferior
		grafo.addEdge(c, vecinos.get(2));
		// Vertice izquierdo
		grafo.addEdge(c, vecinos.get(3));

	}

	/**
	 * Método que calcula los Vecinos de una casilla determinada
	 * 
	 * @param c        Casilla en cuestión a la que se le calcula los vecinos
	 * @param filas    filas de la matriz
	 * @param columnas de la matriz
	 * @return array de casillas vecinas en este orden: [0]-> Vértice superior [1]->
	 *         Vértice derecho [2]-> Vértice inferior [3]-> Vértice izquierdo
	 */
	public List<Casilla> calculaVecinos(Casilla c, int filas, int columnas) {
		List<Casilla> vecinos = new LinkedList<>();
		int m1 = c.getX() - 1;
		int m2 = c.getY() + 1;
		int m3 = c.getX() + 1;
		int m4 = c.getY() - 1;
		// Vertice superior
		Casilla superior;
		Casilla derecha;
		Casilla inferior;
		Casilla izquierda;
		if (m1 < 0) {
			superior = getCasillaInGrafo(new Casilla(filas - 1, c.getY()));
			if (superior != null) {
				vecinos.add(superior);
			}
		} else {
			superior = getCasillaInGrafo(new Casilla(m1, c.getY()));
			if (superior != null) {
				vecinos.add(superior);
			}
		}
		// Vertice derecho
		if (m2 == columnas) {
			derecha = getCasillaInGrafo(new Casilla(c.getX(), 0));
			if (derecha != null) {
				vecinos.add(derecha);
			}
		} else {
			derecha = getCasillaInGrafo(new Casilla(c.getX(), m2));
			if (derecha != null) {
				vecinos.add(derecha);
			}
		}
		// Vertice inferior
		if (m3 == filas) {
			inferior = getCasillaInGrafo(new Casilla(0, c.getY()));
			if (inferior != null) {
				vecinos.add(inferior);
			}
		} else {
			inferior = getCasillaInGrafo(new Casilla(m3, c.getY()));
			if (inferior != null) {
				vecinos.add(inferior);
			}
		}
		// Vertice izquierdo
		if (m4 < 0) {
			izquierda = getCasillaInGrafo(new Casilla(c.getX(), columnas - 1));
			if (izquierda != null) {
				vecinos.add(izquierda);
			}
		} else {
			izquierda = getCasillaInGrafo(new Casilla(c.getX(), m4));
			if (izquierda != null) {
				vecinos.add(izquierda);
			}
		}

		return vecinos;

	}

	/**
	 * Método que comprueba cuales de los vecinos son barrancos y los elimina del
	 * grafo
	 * 
	 * @param grafoReal el array con los barrancos y obstáculos
	 */
	public void checkBarrancos(String[][] grafoReal) {
		List<Casilla> vecinos = calculaVecinos(this.pos_inicial, grafoReal.length, grafoReal[0].length);
		for (int i = 0; i < vecinos.size(); i++) {
			if (grafoReal[vecinos.get(i).getX()][vecinos.get(i).getY()].equals("b")) {
				this.grafo.removeVertex(vecinos.get(i));
			}
		}
	}

	/**
	 * Método que devuelve el vértice del grafo que corresponde a la casilla que se
	 * pasa por parámetros
	 * 
	 * @param c Casilla con las coordenadas que queremos encontrar en el grafo
	 * @return Casilla del grafo con las coordenadas deseadas o null si ese vecino
	 *         ya fue eliminado por ser un barranco
	 */
	public Casilla getCasillaInGrafo(Casilla c) {
		List<Casilla> lista = new ArrayList<Casilla>(this.grafo.vertexSet());
		int index = lista.indexOf(c);
		if (index == -1) {
			return null;
		}
		return lista.get(index);
	}

	/**
	 * Método que calcula con las condiciones actuales el camino más corto entre la
	 * posición inicial y la posición final de los atributos de esta clase}
	 * 
	 * @return El camino más corto entre los dos puntos, se puede obtener mucha
	 *         información de esta clase
	 */
	public GraphPath<Casilla, DefaultWeightedEdge> caminoMasCorto() {
		this.pos_inicial = getCasillaInGrafo(pos_inicial);
		this.pos_destino = getCasillaInGrafo(pos_destino);
		DijkstraShortestPath<Casilla, DefaultWeightedEdge> algoritmo = new DijkstraShortestPath<>(this.grafo);
		SingleSourcePaths<Casilla, DefaultWeightedEdge> iPaths = algoritmo.getPaths(pos_inicial);
		return iPaths.getPath(pos_destino);
	}

	/**
	 * Método que ejecuta el algoritmo de principio a fin.
	 * 
	 * @param grafoReal grafo con los barrancos y obstáculos
	 * @return 0 si te quedas sin vidas, 1 si todo va bien
	 */
	public int ejecutaAlgoritmo(String[][] grafoReal) {
		caminoCorto.add(pos_inicial);
		while (this.pos_inicial != this.pos_destino) {
			// Comprobamos los barrancos de los vértices vecinos
			checkBarrancos(grafoReal);
			// Obtenemos el primer paso del algoritmo
			Casilla paso = caminoMasCorto().getVertexList().get(1);
			// Lo actualizamos como posiciñon inicial para la próxima iteración
			this.pos_inicial = paso;
			// Añadimos como primer paso del camino más corto
			caminoCorto.add(paso);
			// Comprobamos que esta casilla tiene obstáculos, si los tiene,
			// restamos 1 vida y actualizamos el peso de todas las aristas de
			// este vértice proporcionalmente a la vida restante.
			if (grafoReal[paso.getX()][paso.getY()].equals("o")) {
				this.vidas--;
				if (vidas == 0) {
					return 0;
				}
				for (DefaultWeightedEdge d : grafo.edgesOf(paso)) {
					grafo.setEdgeWeight(d, 10 - vidas + 1);
				}
			}
		}

		return 1;
	}

}
