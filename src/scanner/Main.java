package scanner;

import Interpreter.Interpreter;
import MemoryManagment.MemoryManagment;
import filesystem.FileSystem;
import proces.PCB;
import Scheduler.Scheduler;

import java.io.FileNotFoundException;
import java.util.Scanner;



public class Main {
    
    

    public static void main(String[] args) throws FileNotFoundException {
        // TODO Auto-generated method stub
        int CPU = 0;

        MemoryManagment mm = new MemoryManagment();
        PCB proc = new PCB("P0", mm);
        FileSystem dupa = new FileSystem();
        Interpreter a = new Interpreter(mm, dupa, proc);
        String string;
        Scheduler Sched = new Scheduler();
        do {
            System.out.println("command: ");


            Scanner in = new Scanner(System.in);
            string = in.nextLine();
            String[] tab = string.split(" ");


            if (tab[0].equals("DEL"))   ///////////////////////////////////////////////////////////////////usuwanie pliku
            {
                if (tab.length == 2) {
                    dupa.deleteFile(tab[1]);
                    System.out.println("usuwanie pliku");
                } else {
                    System.out.println("nieprawidlowe wywolanie komendy");
                }
            } else if (tab[0].equals("DIR"))   ///////////////////////////////////////////////////////////////////pokaz katalog glowny
            {
                if (tab.length == 1) {
                    dupa.showMainCatalog();
                    System.out.println("wyswietlanie katalogu: ");
                } else
                    System.out.println("nieprawidlowe wywolanie komendy");
            } else if (tab[0].equals("COPY") && tab[1].equals("NULL"))  ////////////////////////////////////////////tworzenie pustego pliku
            {
                if (tab.length == 3) {
                    dupa.createEmptyFile(tab[2]);
                    System.out.println("tworzenie pustego pliku");
                } else
                    System.out.println("nieprawidlowe wywolanie komendy");
            } else if (tab[0].equals("OVR"))  ///////////////////////////////////////////////////////////////////dopisywanie danych do pliku
            {
                if (tab.length > 2) {
                    String temp = "";
                    for (int i = 2; i < tab.length; i++) {
                        temp += tab[i];
                        temp += " ";
                    }
                    dupa.appendToFile(tab[1], temp);
                    System.out.println("dopisywanie danych do pliku");
                } else
                    System.out.println("nieprawidlowe wywolanie komendy");
            } else if (tab[0].equals("CRF"))  ///////////////////////////////////////////////////////////////////tworzenie pliku z dopisaniem danych
            {
                if (tab.length > 2) {
                    String temp = "";
                    for (int i = 2; i < tab.length; i++) {
                        temp += tab[i];
                        temp += " ";
                    }
                    dupa.createFileWithContent(tab[1], temp);
                    System.out.println("tworzenie pliku z dopisaniem danych");
                } else
                    System.out.println("nieprawidlowe wywolanie komendy");
            } else if (tab[0].equals("OPF"))   ////////////////////////////////////////////////////////////////////otwieranie pliku
            {
                if (tab.length == 2) {
                    dupa.getFileContent(tab[1]);
                    System.out.println("otwieranie pliku");
                } else
                    System.out.println("nieprawidlowe wywolanie komendy");
            } else if (tab[0].equals("HELP"))   ////////////////////////////////////////////////////////////////////pomoc
            {
                if (tab.length == 1) {

                    System.out.println("DEL nazwa_pliku: usuwanie wskazanego pliku; ");
                    System.out.println("COPY NULL nazwa_pliku: tworzenie pustego pliku o wskazanej nazwie; ");
                    System.out.println("OVR nazwa_pliku: dopisanie danych do danego pliku; ");
                    System.out.println("CRF nazwa_pliku dane: tworzenie pliku z dopisaniem danych; ");
                    System.out.println("OPF nazwa_pliku: otwieranie wskazanego pliku; ");
                    System.out.println("DIR: wyswietlanie katalogu glownego; ");
                    System.out.println("MEMORY: sprawdzanie stanu pamieci; ");
                    System.out.println("LRU: LRU; ");
                    System.out.println("SHV: sprawdzanie zawartosci wektora bitowego; ");
                    System.out.println("CHKDSK: sprawdzanie zawartości dysku; ");
                    System.out.println("TASKLIST: sprawdzanie listy procesow; ");
                    System.out.println("TASKKILL: nazwa_procesu: zabijanie procesu; ");
                    System.out.println("GO: kolejny krok wykonywanego procesu; ");
                    System.out.println("START nazwa_istniejacego_procesu nazwa_procesu: stworzenie procesu; ");


                } else
                    System.out.println("nieprawidlowe wywolanie komendy");
            } else if (tab[0].equals("MEMORY"))   //////////////////////////////////////////////////////////////////pamięć
            {
                if (tab.length == 1) {
                    //funkcja pokazująca stan pamięci
                    mm.getCurrentRAM();
//                    System.out.println();
                    // System.out.println("stan pamieci");


                } else
                    System.out.println("nieprawidlowe wywolanie komendy");

            } else if (tab[0].equals("CHKDSK"))   //////////////////////////////////////////////////////////////////dysk
            {
                if (tab.length == 1) {

                    dupa.showDiskAndVector();
                    System.out.println("zawartość dysku");


                } else
                    System.out.println("nieprawidlowe wywolanie komendy");

            } else if (tab[0].equals("SHV"))   //////////////////////////////////////////////////////////////////dvector bitowy
            {
                if (tab.length == 1) {

                    dupa.showBitVector();
                    System.out.println("Wektor Bitowy: ");


                } else
                    System.out.println("nieprawidlowe wywolanie komendy");
            } else if (tab[0].equals("TASKLIST"))   ///////////////////////////////////////////////////////////////lista procesów
            {
                if (tab.length == 1) {
                    proc.showproceses();
                    //funkcja pokazująca liste procesów
                    System.out.println("lista procesow");

                } else
                    System.out.println("nieprawidlowe wywolanie komendy");
            } else if (tab[0].equals("TASKKILL"))   /////////////////////////////////////////////////////////////////zabijanie procesu
            {
                if (tab.length == 2)
                    //tab[1] to nazwa procesu
                    proc.kill(tab[1]);
                    // System.out.println("zabicie procesu");
                else
                    System.out.println("nieprawidlowe wywolanie komendy");
            } else if (tab[0].equals("GO"))   /////////////////////////////////////////////////////////////////////kolejny krok w procesie
            {
                if (tab.length == 1) {
                    
                    System.out.println("kolejny krok w procesie");
                    
                    if(CPU == 8){
                        Sched.przelicz(PCB.proceses);
                        CPU = 0;
                    }
                    CPU++;
                    
                    //proc.cpu++;
                    int ct=0;
                    for(int i=1;i<PCB.proceses.size();i++){  
                        if(PCB.proceses.get(i).state==4){
                            ct=1;
                        }
                    }
                    
                    if(ct==0){
                        proc.planista();
                        ct++;
                    }
                    
                    String pom = proc.working();
                    String test = a.getProgram(pom);
                    
                    a.work(test, a.labels);
                    
                    for(int i=1;i<PCB.proceses.size();i++){  
                        if(PCB.proceses.get(i).pid.equals(pom)){
                            PCB.proceses.get(i).cpu = PCB.proceses.get(i).cpu + 2;
                        }
                    }
                    
                    
                    

                    //stan procesu po wykonaniu jednego kroku - od interpretera
                    
                    
                    
                    //Sched.przelicz(proc.proceses);
                    
                    //proc.showproceses();
                } else
                    System.out.println("nieprawidlowe wywolanie komendy");
            } else if (tab[0].equals("START"))   //////////////////////////////////////////////////////////////////tworzenie procesu
            {
                if (tab.length == 4) {
                    /*
                     tab[1] istniejący proces
                     tab[2] nazwa procesu
                     tab[3] nazwa pliku
                     */
                    System.out.println("tworzenie procesu");
                    //tworzenie procesu
                    proc.getproces(tab[1]).fork(tab[2], tab[3]);

                    

                } else
                    System.out.println("nieprawidlowe wywolanie komendy");
            } else if (tab[0].equals("CHG"))   /////////////////////////////////////////////////////////////////////kolejny krok w procesie
            {
                if (tab.length == 3) {
                    //1 - NAZWA PROCESU
                    //2 - STAN
                    //stan procesu po wykonaniu jednego kroku - od interpretera
                    proc.getproces(tab[1]).setstate(Integer.parseInt(tab[2]));
                    System.out.println(proc.getproces(tab[1]).state);
                } else
                    System.out.println("nieprawidlowe wywolanie komendy");
            } else if (tab[0].equals("LRU")) {
                mm.getCurrentLRU();
            } else if (!tab[0].equals("exit")) {
                System.out.println("NIEPOPRAWNA KOMENDA ZIOMUS");
            }
        } while (!string.equals("exit"));


    }

}