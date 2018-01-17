package Scheduler;

import java.util.ArrayList;

import proces.PCB;
import static proces.PCB.proceses;


public class Scheduler {

    static public ArrayList<ArrayList<PCB>> qs = new ArrayList<ArrayList<PCB>>(8); // kolejka procesow gotowych
    static ArrayList<PCB> wait_list = new ArrayList<PCB>(); //kolejka procesow oczekujacych
    static boolean[] whichqs = new boolean[8]; //ktore kolejki sa nie puste
    public int base = 8; //baza
    public PCB pr_rdy; //aktualnie wykonywany proces

    //Konstruktor
    public Scheduler() {
        for (int i = 0; i < 7; i++) {
            qs.add(i, new ArrayList<PCB>());
            whichqs[i] = true;
        }
    }

    //Dodawanie procesu do kolejki procesow gotowych - do uzytku zewnetrznego
    public void add_to_ready(PCB pr) {
        add_to_ready_list(pr);
        //Output.write("Dodano proces pid: "+pr.pid+" do kolejki procesow gotowych.");
    }

    //Dodawanie procesu do kolejki procesow gotowych - metoda wewnetrzna
    public void add_to_ready_list(PCB pr) {
        if (qs.get(pr.priority / 4).size() > 0) //poszukiwanie miejsca przed procesem o wyzszym priorytecie
        {
            boolean add = false;
            for (int i = 0; i < qs.get(pr.priority / 4).size(); i++) {
                if (pr.priority < qs.get(pr.priority / 4).get(i).priority) {
                    qs.get(pr.priority / 4).add(i, pr);
                    add = true;
                    break;
                }
            }
            if (add == false)
                qs.get(pr.priority / 4).add(pr);
        } else {
            qs.get(pr.priority / 4).add(pr);
        }
        pr.state = 2;

        whichqs[pr.priority / 4] = true;

    }

    //Dodanie procesu testowego na pierwsza pozycje kolejki procesow gotowych
    public void add_to_ready_test(PCB pr) {
        pr.priority = 0;
        pr.uspri = 0;
        qs.get(0).add(0, pr);
        pr.state = 2;

        whichqs[0] = true;
        //Output.write("Dodano proces pid: "+pr.pid+" do kolejki procesow gotowych na pozycje pierwsza.");
    }

    //Usuwanie procesu z kolejki gotowych
    public void remove(PCB pr) {
        qs.get(pr.priority / 4).remove(pr);
        if (qs.get(pr.priority / 4).size() == 0)
            whichqs[pr.priority / 4] = false;
    }

    //Dodawanie procesu do kolejki procesow oczekujacych
    public void add_to_wait(PCB pr) {
        int tmp = qs.get(pr.priority / 4).indexOf(pr);
        wait_list.add(qs.get(pr.priority / 4).get(tmp));
        qs.get(pr.priority / 4).remove(tmp);
        pr.state = 3;
    }

    //wybudzanie procesu
    public void wakeup(String pid) {
        for (PCB temp : wait_list) {
            if (temp.pid == pid) {
                //Output.write("PCB pid: "+temp.pid+" zostal obudzony.");
                add_to_ready(temp);
                wait_list.remove(temp);
                break;
            }
        }
    }

    //Metoda przeliczajaca priorytet
    public void przelicz(ArrayList<PCB> qs1) {
        for(int i=0;i<qs1.size();i++){
            if(qs1.get(i).state==2||qs1.get(i).state==4){
                qs.get(0).add(qs1.get(i));
            }
            //System.out.print(" priorytet:" + qs1.get(i).cpu);
        }
        //System.out.print(qs);
       // ArrayList<ArrayList<PCB>> tmp_qs = new ArrayList<ArrayList<PCB>>(8); //tymczasowa kolejka procesow gotowych
        
        //Output.write("Przeliczanie priorytetow");
         for (int i = 0; i < 7; i++) //wlasciwe przeliczanie priorytetow
        {
            if (whichqs[i]) {
                for (int j = 0; j < qs.get(i).size(); j++) {
                    //System.out.println("Wartosc cpu: "+qs.get(i).get(j).cpu);
                    //System.out.println(" "+qs.get(i).get(j).nice);
                    
                    //int x = (qs.get(i).get(j).cpu) / 2;
                    qs.get(i).get(j).cpu = (qs.get(i).get(j).cpu) / 2;
                    qs.get(i).get(j).uspri = base + (qs.get(i).get(j).cpu) / 2 + qs.get(i).get(j).nice;
                    qs.get(i).get(j).priority = qs.get(i).get(j).uspri;
                    //qs.get(i).get(j).priority=6;
                }
            }
        }
//        for (int i = 0; i < 8; i++) //tymczasowe przeniesienie procesow do dodatkowej kolejki
//        {
//            tmp_qs.add(i, new ArrayList<PCB>());
//            if (whichqs[i] == true)
//                for (int j = 0; j < qs.get(i).size(); j++)
//                    tmp_qs.get(i).add(qs.get(i).get(j));
//        }
//
//        for (int i = 0; i < 7; i++) //uporzadkowanie procesow w glownej kolejce
//        {
//
//            if (whichqs[i] == true) {
//                for (int j = 0; j < tmp_qs.get(i).size(); j++) {
//                    add_to_ready_list(tmp_qs.get(i).get(j));
//                }
//            }
//        }

        for (int i = 0; i < 7; i++) //aktualizacja tablicy wskazujacej na niepuste kolejki
        {
            whichqs[i] = qs.get(i).size() > 0;
            
        }

        int first_not_empty = 8;
        
            for(int j=0; j<proceses.size();j++){
                if(proceses.get(j).state == 4){
                    proceses.get(j).state = 2;
                }
            }
                
            int priority = 999;
            for(int j=0; j<proceses.size();j++){
                if(proceses.get(j).priority<priority){
                    priority = proceses.get(j).priority;
                }
            }
            for(int j=0; j<proceses.size();j++){
                if(proceses.get(j).state==2){
                    if(proceses.get(j).priority==priority){
                    proceses.get(j).state=4;
                    break;
                }
                }
            }  
            
            
        
        
        for (int i = 0; i < 7; i++) //wyczyszczenie kolejki glownej
        {
            qs.get(i).clear();
        }

        //if(first_not_empty != 8 && qs.get(first_not_empty).get(0).priority<pr_rdy.priority)
        //{ //Wywlaszczenie jesli odnaleziony proces ma wyzszy priorytet niz proces aktualny
        //Output.write("Znaleziono proces o wyzszym priorytecie. Wykonywanie wywlaszczenia.");
        //return true;
        //}

        //return false;

    }

    //Metoda zmieniajaca kontekst
    public boolean change_context() {
        //Output.write("Proba zmiany kontekstu");
        int first_not_empty = 8;
        for (int i = 0; i < 8; i++) //Odszukanie procesu o najwyzszym priorytecie
        {
            if (whichqs[i] == true) {
                first_not_empty = i;
                break;
            }
        }
        if (first_not_empty == 8) //nie udalo sie zmienic kontekstu / brak gotowych procesow
        {
            //Output.write("Brak gotowych procesow");
            return false;
        }
        if (pr_rdy == qs.get(first_not_empty).get(0)) //Zmiana wykonywanego procesu
        {
            qs.get(first_not_empty).remove(0);
            add_to_ready_list(pr_rdy);
            pr_rdy = qs.get(first_not_empty).get(0);
        } else
            pr_rdy = qs.get(first_not_empty).get(0);
        //Output.write("Kontekst zmieniono.\nAktualny proces pid: "+pr_rdy.pid);

        return true; // udalo sie zmienic kontekst
    }

    //Metoda wyswietlajaca aktualny state kolejki procesow gotowych
    public static void show_ready_list() {
        //Output.write("Wypisanie kolejki procesow gotowych");
        for (int i = 0; i < 7; i++) {
            //Output.write("Kolejka "+(i*4)+"-"+(i*4+3)+":");
            if (whichqs[i]) {
                for (PCB temp : qs.get(i)) {
                    //Output.write("PCB pid: "+temp.pid+" pri: "+temp.pri+" uspri: "+temp.uspri+" CPU: "+temp.cpu);
                }
            }
            //else
            //Output.write("---PUSTA---");
        }
    }

    //Metoda wyswietlajaca aktualny state kolejki procesow oczekujacych
    public static void show_wait_list() {
        //Output.write("Wypisanie kolejki procesow oczekujacych");
        if (wait_list.size() > 0) {
            for (PCB temp : wait_list) {
                //Output.write("PCB pid: "+temp.pid+" pri: "+temp.pri+" uspri: "+temp.uspri+" CPU: "+temp.cpu);
            }
        }
        //else
        //Output.write("---PUSTA---");
    }


}

