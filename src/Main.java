import java.util.Scanner;

public class Main {
    private static ArbolBPlus arbol = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE ÁRBOL B+ ===");
        System.out.println("Implementación de árbol B+ para gestión de datos");

        // Primero pedir el orden del árbol
        inicializarArbol();

        // Mostrar menú principal
        menuPrincipal();
    }

    private static void inicializarArbol() {
        System.out.println("\n=== INICIALIZACIÓN DEL ÁRBOL B+ ===");
        System.out.print("Ingrese el orden del árbol (mínimo 3): ");

        int orden = 0;
        while (true) {
            try {
                orden = scanner.nextInt();
                if (orden >= 3) {
                    arbol = new ArbolBPlus(orden);
                    System.out.println("Árbol B+ creado con orden " + orden);
                    break;
                } else {
                    System.out.print("El orden debe ser al menos 3. Intente nuevamente: ");
                }
            } catch (Exception e) {
                System.out.print("Entrada inválida. Ingrese un número: ");
                scanner.nextLine();
            }
        }
        scanner.nextLine(); // Limpiar buffer
    }

    private static void menuPrincipal() {
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    insertarElemento();
                    break;
                case 2:
                    buscarElemento();
                    break;
                case 3:
                    eliminarElemento();
                    break;
                case 4:
                    recorrerDesdeClave();
                    break;
                case 5:
                    mostrarArbol();
                    break;
                case 6:
                    verificarIntegridad();
                    break;
                case 7:
                    ejecutarPruebasAutomaticas();
                    break;
                case 8:
                    cambiarOrden();
                    break;
                case 9:
                    System.out.println("¡Gracias por usar el sistema! ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }

            if (opcion != 9) {
                System.out.println("\nPresione Enter para continuar...");
                scanner.nextLine();
            }

        } while (opcion != 9);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n=== MENÚ PRINCIPAL - ÁRBOL B+ ===");
        System.out.println("1. Insertar elemento");
        System.out.println("2. Buscar elemento");
        System.out.println("3. Eliminar elemento");
        System.out.println("4. Recorrer desde clave");
        System.out.println("5. Mostrar estructura del árbol");
        System.out.println("6. Verificar integridad");
        System.out.println("7. Ejecutar pruebas automáticas");
        System.out.println("8. Cambiar orden del árbol");
        System.out.println("9. Salir");
    }

    private static void insertarElemento() {
        System.out.println("\n=== INSERTAR ELEMENTO ===");

        int clave = leerEntero("Ingrese la clave (número entero): ");

        System.out.print("Ingrese el valor asociado (texto): ");
        String valor = scanner.nextLine();

        arbol.insertar(clave, valor);
    }

    private static void buscarElemento() {
        System.out.println("\n=== BUSCAR ELEMENTO ===");

        int clave = leerEntero("Ingrese la clave a buscar: ");

        Object resultado = arbol.buscar(clave);

        if (resultado == null) {
            System.out.println("La clave " + clave + " no se encuentra en el árbol.");
        } else {
            System.out.println("Clave " + clave + " encontrada. Valor: " + resultado);
        }
    }

    private static void eliminarElemento() {
        System.out.println("\n=== ELIMINAR ELEMENTO ===");

        int clave = leerEntero("Ingrese la clave a eliminar: ");

        boolean eliminado = arbol.eliminar(clave);

        if (eliminado) {
            System.out.println("Clave " + clave + " eliminada exitosamente.");
        } else {
            System.out.println("No se pudo eliminar la clave " + clave + " (posiblemente no existe).");
        }
    }

    private static void recorrerDesdeClave() {
        System.out.println("\n=== RECORRER DESDE CLAVE ===");

        int claveInicio = leerEntero("Ingrese la clave de inicio: ");
        int n = leerEntero("Ingrese cuántos elementos mostrar: ");

        arbol.recorrer(claveInicio, n);
    }

    private static void mostrarArbol() {
        arbol.mostrarArbol();
    }

    private static void verificarIntegridad() {
        arbol.verificarIntegridad();
    }

    private static void ejecutarPruebasAutomaticas() {
        System.out.println("\n=== EJECUTANDO PRUEBAS AUTOMÁTICAS ===");

        // Guardar el árbol actual
        ArbolBPlus arbolOriginal = arbol;

        // Crear nuevo árbol para pruebas
        System.out.println("Creando árbol de prueba con orden 4...");
        arbol = new ArbolBPlus(4);

        // Insertar elementos de prueba
        System.out.println("\n1. Insertando elementos secuenciales...");
        int[] clavesPrueba = {10, 20, 5, 15, 30, 25, 35, 3, 7, 12, 18, 22, 28, 33, 38};

        for (int i = 0; i < clavesPrueba.length; i++) {
            System.out.println("Insertando clave " + clavesPrueba[i] + " con valor 'Valor-" + clavesPrueba[i] + "'");
            arbol.insertar(clavesPrueba[i], "Valor-" + clavesPrueba[i]);

            // Mostrar cada 5 inserciones
            if ((i + 1) % 5 == 0) {
                System.out.println("--- Estado después de " + (i + 1) + " inserciones ---");
                arbol.mostrarArbol();
            }
        }

        // Mostrar estructura final
        System.out.println("\n2. Estructura final del árbol:");
        arbol.mostrarArbol();

        // Verificar integridad
        System.out.println("\n3. Verificando integridad...");
        arbol.verificarIntegridad();

        // Probar búsquedas
        System.out.println("\n4. Probando búsquedas:");
        arbol.buscar(15);  // Debería existir
        arbol.buscar(100); // No debería existir
        arbol.buscar(7);   // Debería existir

        // Probar recorrido
        System.out.println("\n5. Probando recorrido desde clave 10 (5 elementos):");
        arbol.recorrer(10, 5);

        System.out.println("\n6. Probando recorrido desde clave 25 (3 elementos):");
        arbol.recorrer(25, 3);

        // Probar eliminaciones
        System.out.println("\n7. Probando eliminaciones:");
        arbol.eliminar(15);
        arbol.eliminar(3);
        arbol.eliminar(35);

        // Mostrar después de eliminaciones
        System.out.println("\n8. Estado después de eliminaciones:");
        arbol.mostrarArbol();

        // Restaurar árbol original
        System.out.println("\n=== PRUEBAS COMPLETADAS ===");
        System.out.println("Restaurando árbol original...");
        arbol = arbolOriginal;
    }

    private static void cambiarOrden() {
        System.out.println("\n=== CAMBIAR ORDEN DEL ÁRBOL ===");
        System.out.println("ADVERTENCIA: Esta operación eliminará todos los datos actuales.");
        System.out.print("¿Desea continuar? (S/N): ");

        String respuesta = scanner.nextLine();
        if (respuesta.equalsIgnoreCase("S")) {
            inicializarArbol();
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // Método auxiliar para leer enteros con validación
    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                int valor = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
                return valor;
            } catch (Exception e) {
                System.out.println("Error: Debe ingresar un número entero válido.");
                scanner.nextLine(); // Limpiar buffer
            }
        }
    }
}