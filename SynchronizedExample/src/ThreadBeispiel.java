public class ThreadBeispiel extends Thread {

    // Objekte zur Synchronisation
    static Object monitor = new Object();
    Object o = new Object();
    static boolean SWITCH;

    // Für die Ausgabe auf der Konsole
    static int nextChar = (int) 'a';
    char name = (char) nextChar++;

    @Override
    public String toString () {
        return String.valueOf(this.name);
    }

    /**
     * Die run-Methode der Threads gibt nur name auf der Konsole aus.
     */
    @Override
    public void run () {

        // Die switch Variable bestimmt, welches Objekt zur Synchronisation benutzt wird
        if (SWITCH) {

            /*
            Wenn eine Klassenvariable, d. h. dasselbe Objekt für alle Threads, benutzt wird,
            kann jeweils nur ein einziger Thread den Code aus dem synchronized-Block ausführen.
             */
            synchronized (monitor) {
                while (true) {
                    System.out.println(this + " says hello.");
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {}
                }
            }
        } else {

            /*
            Wenn jeder Thread ein anderes Objekt zur Synchronisation verwendet, führen alle Threads
            gleichzeitig den Code aus dem synchronized-Block aus => Die Threads werden also eventuell 
            nicht geeignet koordiniert.
             */
            synchronized (o) {
                while (true) {
                    System.out.println(this + " says hello.");
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {}
                }
            }
        }
    }

    /**
     * Die main-Methode startet neue Threads
     */
    public static void main (String[] args) {

        // Konfiguration des Programms
        final int NUMTHREADS = 10;
        ThreadBeispiel.SWITCH = true;

        // starten der Threads
        for (int i = 0; i < NUMTHREADS; i++) {
            new ThreadBeispiel().start();
        }
    }
}
