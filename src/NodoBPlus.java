import java.util.ArrayList;
import java.util.List;

public class NodoBPlus {
    private List<Integer> claves;
    private List<Object> valores; // Solo usado en nodos hoja
    private List<NodoBPlus> hijos;
    private NodoBPlus siguiente; // Enlace a siguiente hoja (solo para hojas)
    private boolean esHoja;
    private int orden;

    public NodoBPlus(int orden, boolean esHoja) {
        this.orden = orden;
        this.esHoja = esHoja;
        this.claves = new ArrayList<>();

        if (esHoja) {
            this.valores = new ArrayList<>();
            this.hijos = null; // Las hojas no tienen hijos
        } else {
            this.valores = null; // Nodos internos no tienen valores
            this.hijos = new ArrayList<>();
        }

        this.siguiente = null; // Inicialmente sin enlace
    }

    // GETTERS
    public List<Integer> getClaves() { return claves; }
    public List<Object> getValores() { return valores; }
    public List<NodoBPlus> getHijos() { return hijos; }
    public NodoBPlus getSiguiente() { return siguiente; }
    public boolean isEsHoja() { return esHoja; }
    public int getOrden() { return orden; }
    public int getNumClaves() { return claves.size(); }

    // SETTERS
    public void setSiguiente(NodoBPlus siguiente) { this.siguiente = siguiente; }

    // MÉTODOS FUNDAMENTALES

    // Insertar clave en nodo hoja
    public void insertarEnHoja(int clave, Object valor) {
        if (!esHoja) {
            System.out.println("Error: No se puede insertar valor en nodo interno");
            return;
        }

        // Encontrar posición para insertar
        int i = 0;
        while (i < claves.size() && clave > claves.get(i)) {
            i++;
        }

        // Insertar clave y valor en posición ordenada
        claves.add(i, clave);
        valores.add(i, valor);
    }

    // Insertar clave en nodo interno
    public void insertarEnInterno(int clave, NodoBPlus hijoDerecho) {
        if (esHoja) {
            System.out.println("Error: No se puede insertar en nodo hoja como interno");
            return;
        }

        // Encontrar posición para insertar
        int i = 0;
        while (i < claves.size() && clave > claves.get(i)) {
            i++;
        }

        // Insertar clave y su hijo derecho
        claves.add(i, clave);
        hijos.add(i + 1, hijoDerecho);
    }

    // Verificar si el nodo está lleno
    public boolean estaLleno() {
        return claves.size() >= orden;
    }

    // Verificar si el nodo tiene pocas claves (para eliminación)
    public boolean tienePocasClaves() {
        if (esHoja) {
            return claves.size() < (orden + 1) / 2;
        } else {
            return claves.size() < orden / 2;
        }
    }

    // Dividir nodo hoja
    public NodoBPlus dividirHoja() {
        if (!esHoja) {
            return null;
        }

        NodoBPlus nuevoNodo = new NodoBPlus(orden, true);
        int mitad = claves.size() / 2;

        // Mover la mitad derecha al nuevo nodo
        for (int i = mitad; i < claves.size(); i++) {
            nuevoNodo.claves.add(claves.get(i));
            nuevoNodo.valores.add(valores.get(i));
        }

        // Eliminar las claves movidas del nodo original
        for (int i = claves.size() - 1; i >= mitad; i--) {
            claves.remove(i);
            valores.remove(i);
        }

        // Actualizar enlaces entre hojas
        nuevoNodo.siguiente = this.siguiente;
        this.siguiente = nuevoNodo;

        return nuevoNodo;
    }

    // Dividir nodo interno
    public NodoBPlus dividirInterno() {
        if (esHoja) {
            return null;
        }

        NodoBPlus nuevoNodo = new NodoBPlus(orden, false);
        int mitad = claves.size() / 2;
        int clavePromocionada = claves.get(mitad);

        // Mover la mitad derecha al nuevo nodo
        for (int i = mitad + 1; i < claves.size(); i++) {
            nuevoNodo.claves.add(claves.get(i));
        }

        // Mover los hijos correspondientes
        for (int i = mitad + 1; i < hijos.size(); i++) {
            nuevoNodo.hijos.add(hijos.get(i));
        }

        // Eliminar las claves e hijos movidos del nodo original
        for (int i = claves.size() - 1; i >= mitad; i--) {
            claves.remove(i);
        }
        for (int i = hijos.size() - 1; i >= mitad + 1; i--) {
            hijos.remove(i);
        }

        // La clave promocionada no se queda en ninguno de los nodos divididos
        claves.remove((Integer) clavePromocionada);

        return nuevoNodo;
    }

    // Buscar clave en nodo hoja
    public Object buscarEnHoja(int clave) {
        if (!esHoja) {
            return null;
        }

        for (int i = 0; i < claves.size(); i++) {
            if (claves.get(i) == clave) {
                return valores.get(i);
            }
        }

        return null;
    }

    // Obtener el hijo apropiado para una clave dada
    public NodoBPlus getHijoParaClave(int clave) {
        if (esHoja) {
            return null;
        }

        int i = 0;
        while (i < claves.size() && clave >= claves.get(i)) {
            i++;
        }

        return hijos.get(i);
    }

    // Eliminar clave de nodo hoja
    public boolean eliminarDeHoja(int clave) {
        if (!esHoja) {
            return false;
        }

        for (int i = 0; i < claves.size(); i++) {
            if (claves.get(i) == clave) {
                claves.remove(i);
                valores.remove(i);
                return true;
            }
        }

        return false;
    }

    // Fusionar con hermano derecho
    public void fusionarCon(NodoBPlus hermanoDerecho) {
        if (esHoja) {
            // Fusionar claves y valores
            claves.addAll(hermanoDerecho.claves);
            valores.addAll(hermanoDerecho.valores);
            // Actualizar enlace
            siguiente = hermanoDerecho.siguiente;
        } else {
            // Para nodos internos, necesitamos una clave del padre
            // (esto se maneja en la clase árbol)
        }
    }

    // Tomar prestada clave del hermano izquierdo
    public void tomarPrestadoDeIzquierdo(NodoBPlus hermanoIzquierdo, int claveDelPadre) {
        if (esHoja) {
            // Mover última clave y valor del hermano izquierdo
            int ultimaClave = hermanoIzquierdo.claves.remove(hermanoIzquierdo.claves.size() - 1);
            Object ultimoValor = hermanoIzquierdo.valores.remove(hermanoIzquierdo.valores.size() - 1);

            claves.add(0, ultimaClave);
            valores.add(0, ultimoValor);

            // Actualizar clave en padre
            // (esto se maneja en la clase árbol)
        }
    }

    // Tomar prestada clave del hermano derecho
    public void tomarPrestadoDeDerecho(NodoBPlus hermanoDerecho, int claveDelPadre) {
        if (esHoja) {
            // Mover primera clave y valor del hermano derecho
            int primeraClave = hermanoDerecho.claves.remove(0);
            Object primerValor = hermanoDerecho.valores.remove(0);

            claves.add(primeraClave);
            valores.add(primerValor);

            // Actualizar clave en padre
            // (esto se maneja en la clase árbol)
        }
    }

    // Representación textual del nodo
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < claves.size(); i++) {
            sb.append(claves.get(i));
            if (i < claves.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("]");
        if (esHoja && siguiente != null) {
            sb.append(" -> ");
        }

        return sb.toString();
    }

    // Mostrar contenido del nodo
    public void mostrar() {
        System.out.print("Nodo " + (esHoja ? "Hoja" : "Interno") + ": ");
        System.out.println(this);

        if (esHoja && valores != null) {
            System.out.println("Valores: " + valores);
        }
    }
}