package proces;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import MemoryManagment.MemoryManagment;
import Scheduler.Scheduler;
import java.util.Collections;
import SynchronizacjaProcesow.Lock;
import java.util.Scanner;

public class PCB {
    
    MemoryManagment memory;
    public Lock lock = new Lock();
    public String pid;
    public String file;
    public int priority;
    public int uspri;
    public int A = 0, B = 0, C = 0, D = 0;
    public int commandCounter = 0;
    public int pri; //P
    public int nice = 20; //P parametr nadawany przez uzytkownika
    public int cpu; //P wykorzystanie procesora
    public int pRA;  //P
    public int pRB;  //P
    public int pPC;  //P
    public boolean pZF;  //P
    public int state;//1-new, 2-ready,3-waiting,4-running,5-finished
    public PCB parent = null;
    public ArrayList<PCB> children = new ArrayList<PCB>();
    static public ArrayList<PCB> proceses = new ArrayList<PCB>();
    public Scheduler Sched = new Scheduler();
    //struct files_struct files; /* list of open files */ 

    //struct mm_struct mm; /* address space of this process */


    //public ArrayList<Pipe> pipes = new ArrayList<>();

    // public IPC childPipe = null;


    public PCB(String PID, MemoryManagment memory) {
        this.pid = PID;
        this.memory = memory;
        proceses.add(this);
        priority = 8;
        state = 0;
        uspri = 0; //P
    }

    public void fork(String PID, String file) throws FileNotFoundException { //Tworzenie procesu
        boolean x = true;
        for (int i = 0; i < proceses.size(); i++) {
            if (proceses.get(i).getpid().equals(PID)) {
                x = false;
            }
        }
        if (x) {
            PCB child = new PCB(PID, memory);
            child.parent = this;
            child.state = 1;
            child.file = file;
            children.add(child);
            uspri = nice; //P
            child.priority = 8; //P
            pRA = child.pRA;
            pRB = child.pRB;
            pPC = child.pPC;
            pZF = child.pZF;
            System.out.println("stworzono proces o ID:" + child.pid+" ");
            //proceses.add(child);
            child.memory.addProcess(child.pid, file);
            child.state=2;
            System.out.println("wczytano dane do procesu\nstan procesu zmieniony na gotowy");
//            for(int i= 0;i<children.size();i++){
//            System.out.println(children.get(i).pid);
//            }
//            for(int i= 0;i<proceses.size();i++){
//            System.out.println(proceses.get(i).pid);
//            }
            Sched.przelicz(proceses);
        } else {
            System.out.println("proces o podanym ID ju� istnieje");
        }
    }

    public void exit() { //usuwanie procesu
        this.state = 5;
        proceses.remove(this);
        if (parent == null) {
        } else {
           // parent.kill(this);
        }
        this.parent = null;
    }

    public PCB getproces(String PID) { //wyszukiwanie procesu
        //PCB temp;
        int x = 0;
        for (int i = 1; i < proceses.size(); i++) {
            if (proceses.get(i).getpid().equals(PID)) {
                x = i;
                //PCB temp=proceses.get(i);
            }
        }
//        if (x > 0) {
//        } else {
//
////              System.out.println("nie odnaleziono procesu");
//
//        }
        return proceses.get(x);
    }

    void orphan() {
        if (this.parent == null) {
            this.parent = proceses.get(0);
        }
    }

     public void showproceses() {
        for (int i = 1; i < proceses.size(); i++) {
            System.out.print("Proces PID : " + proceses.get(i).pid + "\n");
            System.out.print("Priorytet: " + proceses.get(i).priority + "\n");
            System.out.print("Stan: " + proceses.get(i).state + "\n");
            System.out.print("Cpu: " + proceses.get(i).cpu+ "\n");
            System.out.print("lista dzieci: " + proceses.get(i).children+ "\n");
            System.out.print("rodzic: " + proceses.get(i).parent.pid+ "\n");
            switch (proceses.get(i).getState()) {
                case 1:
                    System.out.println("nowy");
                    break;
                case 2:
                    System.out.println("gotowy");
                    break;
                case 3:
                    System.out.println("oczekujacy");
                    break;
                case 4:
                    System.out.println("aktywny");
                    break;
                case 5:
                    System.out.println("zombie");
                    break;
                case 6:
                    System.out.println("usuniety");
                    break;
            }
        }
    }
    
    public void planista(){
        ArrayList<PCB> pir = new ArrayList<PCB>();
        for (int i = 1; i < proceses.size(); i++) {
            if (proceses.get(i).state == 2)
                pir.add(proceses.get(i));
        }
        int priority = 999;
        for(int j=0; j<pir.size();j++){
            if(pir.get(j).priority<priority){
                priority = pir.get(j).priority;
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
    }
    
    public String working() {
        String s = "";
        for (int i = 1; i < proceses.size(); i++) {
            if (proceses.get(i).state == 4)
                s = proceses.get(i).pid;
        }
        return s;
    }

    public void kill(String s) {
    	 if (s.equals("P0")) {
             System.out.println("Ta operacja spowoduje zamknięcie systemu!\nCzy na pewno chcesz ją wykonać?\n1- Tak/0 - Nie");
             int c;
             Scanner v = new Scanner(System.in);
             c = v.nextInt();
             
             if (c == 1) {
                 System.out.println("Zamykanie systemu...");
                 System.exit(0);
             }
             if (c == 0) {
                 System.out.println("Anulowano.");
             } else {
                 System.out.println("Wprowadzono złe dane!");
             }
         }
    	 //PCB a=this.getproces(s);
    	 //this.proceses.remove(a);

         
            // if (this.getproces(s).state == 5) {
        this.getproces(s).state = 6;
                // process p5 = p.child.previous;
            	

                // System.out.println("Dziecko procesu " + p.PID + "zostało usunięte.");
            // }
         
         
         
         this.getproces(s).parent.children.remove(this.getproces(s));
         //this.getproces(s).parent=null;
         
         
         if(!this.getproces(s).children.isEmpty())
         { 
        	 for(int i=0;i<this.getproces(s).children.size();i++){
        		 this.getproces(s).children.get(i).parent=this;
        	 }
         }
         
         if(this.getproces(s).parent.state==5){
             this.getproces(s).parent.kill(this.getproces(s).parent.pid);
             
         }
         
         
         for (int i = 0; i < proceses.size(); i++) 
         { 
         
        
             if ( proceses.get(i).state==6) {
                 proceses.remove(i); 
                 
             }
         }
    }

    public String getpid() {
        return this.pid;
    }

    void setpriority(int a) {
        priority = a;
    }

    public void setstate(int a) {
        this.state = a;
    }

    public int getState() {
        return this.state;
    }

}
