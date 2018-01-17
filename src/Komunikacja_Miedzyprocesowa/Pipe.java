
package Komunikacja_Miedzyprocesowa;
import SynchronizacjaProcesow.SynchronizacjaProcesow;
import SynchronizacjaProcesow.Lock;
//mechanizmy synchronizacji ?


/**
 * @author Olga Kryspin
 */


public class Pipe {

    protected String Dane; //max 16 bajtów
    protected int free_bytes; // liczba wolnych bajtów
    protected int occupied_bytes; //liczba zapisanych danych w bajtach
    protected String pn; // nazwa procesu, dla którego przeznaczone jest łącze
    protected int des; //deskryptor potoku
    protected boolean odczyt, zapis; // deskryptory pliku
    protected boolean lock;// by tylko jednen proces miał dostęp do potoku; ustawiane na true kiiedy coś jest zapisywane bądź odczytywane // do synchronizacji
    /// czy mam używać do synchro test_set ?? CzCzn

    int buffer_lenght = 16; //w bajtach

    protected Pipe() {
        this.free_bytes = buffer_lenght;
        this.occupied_bytes = 0;
        this.pn = "root";
        this.des = 0;
        this.odczyt = true; // hmm może na false, ale raczej bez znaczenia
        this.zapis = true;
        this.lock = false;
    }

    protected void lock() {
        this.lock = true;
    }

    protected void unlock() {
        this.lock = false;
    }

    protected void odczyt() {
        this.odczyt = true;
        this.zapis = false;

    }

    protected void zapis() {
        this.zapis = true;
        this.odczyt = false;

    }

}
