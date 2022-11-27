package com.astar.car.aStarImplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import java.util.stream.Stream;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import com.astar.car.aStarImplementation.algorithms.CocheGraph;
import com.astar.car.aStarImplementation.model.Casilla;

/** Clase de test del algoritmo y funcionamiento sin front */
@SpringBootTest
class AStarImplementationApplicationTests {

	CocheGraph coche;

	/** Instanciación de la clase del grafo antes de cada método */
	@BeforeEach
	void setUp() {
		coche = new CocheGraph();

	}

	/**
	 * Matrices de prueba, en este caso 3x3,3x4 y 5x3
	 */
	public static Stream<int[]> grafosSet() {
		return Stream.of(new int[] { 3, 3 }, new int[] { 3, 4 }, new int[] { 5, 3 });
	}

	/**
	 * Matrices de prueba con barrancos y obstáculos, se pueden configurar a gustos,
	 * poniendo b en los barrancos y o en los obstáculos
	 */
	public static Stream<String[][]> barrancosSet() {
		String[][] grafoConBarrancos1 = { { "", "o", "" }, { "", "", "o" }, { "o", "", "f" } };
		String[][] grafoConBarrancos2 = { { "", "b", "o", "" }, { "o", "", "", "" }, { "o", "", "f", "o" } };
		String[][] grafoConBarrancos3 = { { "", "b", "o" }, { "", "o", "" }, { "b", "o", "f" }, { "", "o", "" },
				{ "b", "b", "o" } };
		return Stream.of(grafoConBarrancos1, grafoConBarrancos2, grafoConBarrancos3);
	}

	/**
	 * Método que comprueba que el tamaño del grafo sea el mismo que el de la
	 * matriz, en relacion cada elemento de la matriz es un vértice de nuestro grafo
	 * 
	 * @param dimension matriz{{}}; de dos elementos con el primero el número de
	 *                  filas y el segundo el número de columnas
	 */
	@ParameterizedTest
	@MethodSource("grafosSet")
	void testCreateGrafoVertices(int[] dimension) {
		int filas = dimension[0];
		int columnas = dimension[1];
		coche.createGraph(filas, columnas);
		Set<Casilla> casillas = coche.getGrafo().vertexSet();
		assertEquals(casillas.size(), filas * columnas);

	}

	/**
	 * Método que comprueba que cada vértice esté conectado con otros 4 en nuestro
	 * grafo, en la matriz todo elemento tiene 4 vecinos (arriba, abajo, derecha,
	 * izquierda)
	 * 
	 * @param dimension matriz de dos elementos con el primero el número de filas y
	 *                  el segundo el número de columnas
	 */
	@ParameterizedTest
	@MethodSource("grafosSet")
	void testCreateGrafoAristas(int[] dimension) {
		int filas = dimension[0];
		int columnas = dimension[1];
		coche.createGraph(filas, columnas);
		Graph<Casilla, DefaultWeightedEdge> grafo = coche.getGrafo();
		Set<Casilla> casillas = grafo.vertexSet();
		int check = 0;
		for (Casilla c : casillas) {
			if (grafo.degreeOf(c) != 4) {
				check = 1;
			}
			// System.out.println(coche.getGrafo().edgesOf(c));

		}
		assertEquals(check, 0);

	}

	/**
	 * Método que comprueba la detección de barrancos en vecinos adyacentes
	 * 
	 * @param grafoReal El array con las posiciones de los barrancos y obstáculos
	 */
	@ParameterizedTest
	@MethodSource("barrancosSet")
	void testBarrancosAdyacentes(String[][] grafoReal) {
		int filas = grafoReal.length;
		int columnas = grafoReal[0].length;
		coche.createGraph(filas, columnas);
		coche.checkBarrancos(grafoReal);
		int dimension = filas * columnas;
		// Para este ejemplo, la matriz de 3x3 no tiene barrancos vecinos, la
		// de 3x4 1 y la de 5x3 dos
		if (dimension == 9) {
			assertEquals(coche.getGrafo().vertexSet().size(), 9);
		} else if (dimension == 12) {
			assertEquals(coche.getGrafo().vertexSet().size(), 11);
		} else {
			assertEquals(coche.getGrafo().vertexSet().size(), 13);
		}
	}

	/**
	 * Método que comprueba el funcionamiento del algoritmo con las restricciones de
	 * barrancos iniciales únicamente
	 * 
	 * @param grafo el grafo con barrancos y obstáculos que sólo comprobará en la
	 *              primera casilla
	 */
	@ParameterizedTest
	@MethodSource("barrancosSet")
	void testCaminoMasCortoRestriccionInicial(String[][] grafo) {
		int filas = grafo.length;
		int columnas = grafo[0].length;
		coche.createGraph(filas, columnas);
		coche.checkBarrancos(grafo);
		if (coche.caminoMasCorto() != null) {
			System.out.println(coche.caminoMasCorto().getVertexList() + "\n");
			assertEquals(coche.caminoMasCorto().getEndVertex(), coche.getPos_destino());
		} else {
			assertEquals(coche.getGrafo().degreeOf(coche.getPos_inicial()), 0);
		}

	}

	/**
	 * Método que ejecuta el algoritmo de principio a fin con todas sus variables
	 * 
	 * @param grafo el grafo con barrancos y obstáculos
	 */
	@ParameterizedTest
	@MethodSource("barrancosSet")
	void testCaminoMasCortoCaminoFinal(String[][] grafo) {
		int filas = grafo.length;
		int columnas = grafo[0].length;
		coche.createGraph(filas, columnas);
		coche.checkBarrancos(grafo);
		int i = coche.ejecutaAlgoritmo(grafo);
		if (i == 0) {
			System.out.println("Te has quedado sin vidas");
			System.out.println("Camino alcanzado");
			System.out.println(coche.getCaminoCorto() + "\n");
			assertEquals(coche.getVidas(), 0);
		} else {
			System.out.println("Camino más corto definitivo");
			System.out.println(coche.getCaminoCorto() + "\n");
			System.out.println("Vidas restantes" + coche.getVidas());
			assertEquals(coche.getPos_inicial(), coche.getPos_destino());
		}

	}
}
