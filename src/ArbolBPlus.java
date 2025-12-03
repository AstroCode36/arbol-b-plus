import java.util.ArrayList;
import java.util.List;

public class ArbolBPlus {
    private NodoBPlus raiz;
    private int orden;

    public ArbolBPlus(int orden) {
        this.orden = orden;
        this.raiz = new NodoBPlus(orden, true); // Inicialmente la raíz es una hoja
    }

    // GETTERS
    public NodoBPlus getRaiz() { return raiz; }
    public int getOrden() { return orden; }

    // INSERTAR
    public void insertar(int clave, Object valor) {
        System.out.println("\n=== INSERTANDO clave " + clave + " ===");

        // Si la raíz está llena, dividirla
        if (raiz.estaLleno()) {
            System.out.println("Raíz llena, dividiendo...");
            NodoBPlus nuevaRaiz = new NodoBPlus(orden, false);
            nuevaRaiz.getHijos().add(raiz);
            dividirHijo(nuevaRaiz, 0, raiz);
            raiz = nuevaRaiz;
        }

        insertarNoLleno(raiz, clave, valor);
        System.out.println("Inserción completada.");
    }

    private void insertarNoLleno(NodoBPlus nodo, int clave, Object valor) {
        if (nodo.isEsHoja()) {
            // Insertar en hoja
            System.out.println("Insertando en hoja: clave " + clave);
            nodo.insertarEnHoja(clave, valor);
        } else {
            // Encontrar hijo apropiado
            int i = nodo.getNumClaves() - 1;
            while (i >= 0 && clave < nodo.getClaves().get(i)) {
                i--;
            }
            i++;

            NodoBPlus hijo = nodo.getHijos().get(i);
            System.out.println("Bajando al hijo " + i + " del nodo interno");

            // Si el hijo está lleno, dividirlo
            if (hijo.estaLleno()) {
                System.out.println("Hijo lleno, dividiendo...");
                dividirHijo(nodo, i, hijo);

                // Determinar a qué hijo ir después de la división
                if (clave > nodo.getClaves().get(i)) {
                    i++;
                    hijo = nodo.getHijos().get(i);
                }
            }

            insertarNoLleno(hijo, clave, valor);
        }
    }

    private void dividirHijo(NodoBPlus padre, int indiceHijo, NodoBPlus hijo) {
        if (hijo.isEsHoja()) {
            // Dividir hoja
            NodoBPlus nuevoHijo = hijo.dividirHoja();
            int clavePromocionada = nuevoHijo.getClaves().get(0);

            // Insertar nuevo hijo en el padre
            padre.getHijos().add(indiceHijo + 1, nuevoHijo);
            padre.insertarEnInterno(clavePromocionada, nuevoHijo);

            System.out.println("Hoja dividida. Clave promocionada: " + clavePromocionada);
        } else {
            // Dividir nodo interno
            NodoBPlus nuevoHijo = hijo.dividirInterno();
            int clavePromocionada = hijo.getClaves().get(hijo.getClaves().size() / 2);

            // Insertar nuevo hijo en el padre
            padre.getHijos().add(indiceHijo + 1, nuevoHijo);
            padre.insertarEnInterno(clavePromocionada, nuevoHijo);

            System.out.println("Nodo interno dividido. Clave promocionada: " + clavePromocionada);
        }
    }

    // BUSCAR
    public Object buscar(int clave) {
        System.out.println("\n=== BUSCANDO clave " + clave + " ===");

        if (raiz == null || raiz.getNumClaves() == 0) {
            System.out.println("Árbol vacío");
            return null;
        }

        NodoBPlus nodoActual = raiz;
        int nivel = 0;

        // Desplazarse hacia las hojas
        while (!nodoActual.isEsHoja()) {
            System.out.println("Nivel " + nivel + ", nodo interno: " + nodoActual);

            boolean encontrado = false;
            List<Integer> claves = nodoActual.getClaves();

            for (int i = 0; i < claves.size(); i++) {
                if (clave < claves.get(i)) {
                    nodoActual = nodoActual.getHijos().get(i);
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                nodoActual = nodoActual.getHijos().get(claves.size());
            }

            nivel++;
        }

        System.out.println("Llegado a hoja: " + nodoActual);

        // Buscar en la hoja
        Object resultado = nodoActual.buscarEnHoja(clave);

        if (resultado != null) {
            System.out.println("Clave " + clave + " encontrada. Valor: " + resultado);
        } else {
            System.out.println("Clave " + clave + " NO encontrada");
        }

        return resultado;
    }

    // ELIMINAR (Implementación simplificada - muy compleja para B+)
    public boolean eliminar(int clave) {
        System.out.println("\n=== INTENTANDO ELIMINAR clave " + clave + " ===");

        if (raiz == null) {
            System.out.println("Árbol vacío");
            return false;
        }

        boolean eliminado = eliminarRecursivo(raiz, clave);

        // Si la raíz queda vacía después de eliminar
        if (raiz.getNumClaves() == 0 && !raiz.isEsHoja()) {
            raiz = raiz.getHijos().get(0);
            System.out.println("Reduciendo altura del árbol");
        }

        if (eliminado) {
            System.out.println("Clave " + clave + " eliminada exitosamente");
        } else {
            System.out.println("Clave " + clave + " NO encontrada para eliminar");
        }

        return eliminado;
    }

    private boolean eliminarRecursivo(NodoBPlus nodo, int clave) {
        if (nodo.isEsHoja()) {
            // Eliminar de hoja
            return nodo.eliminarDeHoja(clave);
        } else {
            // Encontrar hijo apropiado
            int i = 0;
            while (i < nodo.getNumClaves() && clave >= nodo.getClaves().get(i)) {
                i++;
            }

            NodoBPlus hijo = nodo.getHijos().get(i);
            System.out.println("Bajando al hijo " + i + " para eliminar");

            boolean eliminado = eliminarRecursivo(hijo, clave);

            // Después de eliminar, verificar si el hijo necesita ajuste
            if (hijo.tienePocasClaves()) {
                System.out.println("Hijo con pocas claves, necesita ajuste");
                // Aquí iría la lógica de redistribución/fusión (compleja)
                // Por simplicidad en este ejemplo, solo mostramos mensaje
            }

            return eliminado;
        }
    }

    // RECORRER n elementos desde una clave
    public void recorrer(int claveInicio, int n) {
        System.out.println("\n=== RECORRIENDO " + n + " elementos desde clave " + claveInicio + " ===");

        // Primero encontrar la hoja con la clave de inicio
        NodoBPlus hojaActual = encontrarHojaConClave(claveInicio);

        if (hojaActual == null) {
            System.out.println("Clave inicial no encontrada");
            return;
        }

        // Buscar la posición de la clave inicial en la hoja
        int indiceInicio = -1;
        List<Integer> claves = hojaActual.getClaves();
        for (int i = 0; i < claves.size(); i++) {
            if (claves.get(i) >= claveInicio) {
                indiceInicio = i;
                break;
            }
        }

        if (indiceInicio == -1) {
            // Ir a la siguiente hoja
            hojaActual = hojaActual.getSiguiente();
            indiceInicio = 0;
        }

        // Recorrer n elementos usando los enlaces entre hojas
        int contador = 0;
        while (hojaActual != null && contador < n) {
            List<Integer> clavesHoja = hojaActual.getClaves();
            List<Object> valoresHoja = hojaActual.getValores();

            for (int i = indiceInicio; i < clavesHoja.size() && contador < n; i++) {
                System.out.println("Clave: " + clavesHoja.get(i) + ", Valor: " + valoresHoja.get(i));
                contador++;
            }

            hojaActual = hojaActual.getSiguiente();
            indiceInicio = 0; // En hojas siguientes empezar desde el principio
        }

        if (contador < n) {
            System.out.println("Solo se encontraron " + contador + " elementos");
        }
    }

    private NodoBPlus encontrarHojaConClave(int clave) {
        NodoBPlus nodoActual = raiz;

        while (nodoActual != null && !nodoActual.isEsHoja()) {
            List<Integer> claves = nodoActual.getClaves();
            int i = 0;

            while (i < claves.size() && clave >= claves.get(i)) {
                i++;
            }

            nodoActual = nodoActual.getHijos().get(i);
        }

        return nodoActual;
    }

    // MOSTRAR ÁRBOL COMPLETO
    public void mostrarArbol() {
        System.out.println("\n=== ESTRUCTURA COMPLETA DEL ÁRBOL B+ ===");
        System.out.println("Orden: " + orden);

        if (raiz == null || raiz.getNumClaves() == 0) {
            System.out.println("Árbol vacío");
            return;
        }

        List<NodoBPlus> nivelActual = new ArrayList<>();
        nivelActual.add(raiz);
        int nivel = 0;

        while (!nivelActual.isEmpty()) {
            System.out.println("\n--- Nivel " + nivel + " ---");

            List<NodoBPlus> siguienteNivel = new ArrayList<>();

            for (NodoBPlus nodo : nivelActual) {
                nodo.mostrar();

                if (!nodo.isEsHoja()) {
                    siguienteNivel.addAll(nodo.getHijos());
                }
            }

            nivelActual = siguienteNivel;
            nivel++;
        }

        // Mostrar enlaces entre hojas
        System.out.println("\n=== ENLACES ENTRE HOJAS ===");
        NodoBPlus hojaActual = obtenerPrimeraHoja();
        while (hojaActual != null) {
            System.out.print(hojaActual + " ");
            hojaActual = hojaActual.getSiguiente();
        }
        System.out.println();
    }

    private NodoBPlus obtenerPrimeraHoja() {
        NodoBPlus nodoActual = raiz;

        while (nodoActual != null && !nodoActual.isEsHoja()) {
            nodoActual = nodoActual.getHijos().get(0);
        }

        return nodoActual;
    }

    // VERIFICAR INTEGRIDAD
    public void verificarIntegridad() {
        System.out.println("\n=== VERIFICANDO INTEGRIDAD ===");

        if (raiz == null) {
            System.out.println("ERROR: Raíz nula");
            return;
        }

        boolean ok = verificarNodo(raiz, true);

        if (ok) {
            System.out.println("✅ Árbol B+ correctamente estructurado");
        } else {
            System.out.println("❌ Problemas detectados en la estructura");
        }
    }

    private boolean verificarNodo(NodoBPlus nodo, boolean esRaiz) {
        if (nodo == null) {
            System.out.println("ERROR: Nodo nulo encontrado");
            return false;
        }

        // Verificar número de claves
        int minClaves = esRaiz ? 1 : (orden + 1) / 2;
        if (!esRaiz && nodo.getNumClaves() < minClaves) {
            System.out.println("ERROR: Nodo con muy pocas claves: " + nodo.getNumClaves() + " < " + minClaves);
            return false;
        }

        if (nodo.getNumClaves() > orden) {
            System.out.println("ERROR: Nodo con demasiadas claves: " + nodo.getNumClaves() + " > " + orden);
            return false;
        }

        // Verificar orden de claves
        List<Integer> claves = nodo.getClaves();
        for (int i = 1; i < claves.size(); i++) {
            if (claves.get(i) <= claves.get(i - 1)) {
                System.out.println("ERROR: Claves no ordenadas: " + claves.get(i - 1) + " >= " + claves.get(i));
                return false;
            }
        }

        // Verificar hijos si es nodo interno
        if (!nodo.isEsHoja()) {
            List<NodoBPlus> hijos = nodo.getHijos();

            if (hijos.size() != claves.size() + 1) {
                System.out.println("ERROR: Número incorrecto de hijos: " + hijos.size() + " != " + (claves.size() + 1));
                return false;
            }

            // Verificar recursivamente cada hijo
            for (NodoBPlus hijo : hijos) {
                if (!verificarNodo(hijo, false)) {
                    return false;
                }
            }
        }

        return true;
    }
}