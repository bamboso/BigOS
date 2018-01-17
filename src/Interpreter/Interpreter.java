/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interpreter;

import filesystem.FileSystem;
import filesystem.File;

import java.util.Scanner;

import proces.PCB;
import proces.Proces;
//import Komunikacja_Miedzyprocesowa.Pipe;
import Komunikacja_Miedzyprocesowa.IPC;
import SynchronizacjaProcesow.Lock;
import SynchronizacjaProcesow.SynchronizacjaProcesow;
import MemoryManagment.MemoryManagment;
import Scheduler.Scheduler;
import java.io.FileNotFoundException;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Interpreter {
    IPC pipes = new IPC(); 

    //////////////////////////////////////////
    // Labels for JM commands is saved in:
    public HashMap<String, Integer> labels;

    // counter to read from memory
    private int commandCounter = 0;

    // other counter not matter - too many explain
    private int otherCounter;


    // Create Box for PCB
    // private PCB PCBbox;
    static int RA;
    static int RB;
    static int PC;
    static boolean ZF;
    public int CPU;
    private Proces proces;
    private MemoryManagment RAM;
    public PCB PCBbox;
    private FileSystem fileSystem;
    

    public static boolean shutdown = false;
    boolean exit = false;
    public static boolean test = false;
    private int wynik;
    private int przelicz;
    private String Decision;
    private String tmp;
    boolean brak_stronicy = false;
    boolean wt = false;

    private static Scheduler scheduler;
    // private ProcessesManagement processesManagment;

    private ArrayList<String> rejestry;

    private ArrayList<String> rozkazy;

    private boolean manySpace = false;

    private boolean found = false;

    private int stan = 0; // command = 0, param1 = 1; param2 = 2;  //param3 = 3; ???? 
    
    public String program_oczekujacy;
    public String proces_oczekujacy;

    public Interpreter(MemoryManagment RAM, FileSystem fileSystem, PCB PCBbox)

    {
       // pipes = new IPC();
        scheduler = new Scheduler();
        labels = new HashMap<String, Integer>();

        //PCBbox = new PCB("p0");
        this.RAM = RAM;
        this.fileSystem = fileSystem;
        this.PCBbox = PCBbox;
        //PCBbox.cpu = CPU;
        commandCounter = 0;
        otherCounter = 0;

        rejestry = new ArrayList<String>();
        rozkazy = new ArrayList<String>();
        // [*] USTALONE ROZKAZY [*] //

        // ROZKAZY ARYTMETYCZNE DWUARGUMENTOWE
        rozkazy.add("AD"); // String Register + String Register/int Number
        rozkazy.add("SB"); // String Register - String Register/int Number
        rozkazy.add("MU"); // String Register * String Register/int Number
        rozkazy.add("MV"); // String Register <- String Register/int Number

        // ROZKAZY DWUARGUMENTOWE
        rozkazy.add("CP"); // String processName, String fileName
        rozkazy.add("WF"); // String FileName, String Content
        rozkazy.add("WR"); // String FileName, String Register
        rozkazy.add("RC"); //int ile_bajtów_chcesz_odczytać, String processName     // Czytanie komunikatu
        rozkazy.add("SC"); //String communicate,  String processName (DLA KOGO?)    // Wysyłanie komunikatu 

        // ROZKAZY JEDNOARGUMENTOWE
        rozkazy.add("DP"); // String processName
        rozkazy.add("RP"); // String processName
        rozkazy.add("ST"); // String processName
        rozkazy.add("JM"); // String label
        rozkazy.add("JN"); // String Address
        rozkazy.add("CF"); // String Name
        rozkazy.add("DF"); // String name
        rozkazy.add("RF");
        

        // ROZKAZY BEZARGUMENTOWE
        rozkazy.add("HT"); // END PROGRAM

        // [*] USTALONE REJESTRY [*] //
        rejestry.add("A");
        rejestry.add("B");
        rejestry.add("C");
        rejestry.add("D");

        manySpace = false;
        found = false;
        stan = 0; // command = 0, param1 = 1; param2 = 2
    }

    // podpiecie rejestrow do pcb
    public int A = 0, B = 0, C = 0, D = 0;

    private int getValue(String param1) {
        switch (param1) {
            case "A":
                return PCBbox.getproces(PCBbox.working()).A;
            case "B":
                return PCBbox.getproces(PCBbox.working()).B;
            case "C":
                return PCBbox.getproces(PCBbox.working()).C;
            case "D":
                return PCBbox.getproces(PCBbox.working()).D;
        }
        return 0;
    }

    private int setValue(String param1, int value) {
        switch (param1) {
            case "A":
                PCBbox.getproces(PCBbox.working()).A = value;
                break;
            case "B":
                PCBbox.getproces(PCBbox.working()).B = value;
                break;
            case "C":
                PCBbox.getproces(PCBbox.working()).C = value;
                break;
            case "D":
                PCBbox.getproces(PCBbox.working()).D = value;
                break;
            case "PCBbox.A":
                PCBbox.A = value;
                break;
            case "PCBbox.B":
                PCBbox.B = value;
                break;
            case "PCBbox.C":
                PCBbox.C = value;
                break;
            case "PCBbox.D":
                PCBbox.D = value;
                break;
            case "PCBbox.commandCounter":
                PCBbox.commandCounter = commandCounter;
                break;
        }
        return 0;
    }

    // Checking: Is command is the label?
    private boolean isLabel(StringBuilder command) {
        return command.length() > 0 && command.charAt(command.length() - 1) == ':';
    }
    
     private boolean isLabel(String command) {char i = 0;
    	if(command.length() > 3) {
    	i=command.charAt(command.length() - 2);
        }
        return command.length() > 0 && new StringBuilder().append(i).toString().equals(":");
       
    }
    
    String program2;
    
    public String getProgram(String procesName) {
        char znak;
        String program = "";

        int i = 0;
        while (true) {
            // Load char from RAM
            //String x = PCBbox.getproces(procesName).getpid(); //getpid() id aktualnie wykonywanego procesu
            commandCounter = PCBbox.getproces(procesName).commandCounter;
            znak = RAM.getCommandChar(commandCounter, procesName);

            PCBbox.getproces(procesName).commandCounter++;


            if (znak == ' ' && i == 1) {
                program += "\n";
                break;
            } else if (znak == ' ' && i == 0) {
                program += znak;
                i++;
            } else {
                program += znak;
            }

        }
        program2 = program;
        program = "";
        return program2;
    }

    // Follow the recognized command
    public void doCommand(String rozkaz, String param1, String param2, boolean argDrugiJestRejestrem,
                          HashMap<String, Integer> labels) {
        
        
        switch (rozkaz) {
            case "AD": // Dodawanie wartości
                if (argDrugiJestRejestrem) {
                    setValue(param1, getValue(param1) + getValue(param2));
                } else {
                    setValue(param1, getValue(param1) + Integer.parseInt(param2));
                }
                
                break;

            case "MU": // Mnożenie wartości
                if (argDrugiJestRejestrem) {
                    setValue(param1, getValue(param1) * getValue(param2));
                } else {
                    setValue(param1, getValue(param1) * Integer.parseInt(param2));
                }
                break;

            case "SB": // Odejmowanie wartości
                if (argDrugiJestRejestrem) {
                    setValue(param1, getValue(param1) - getValue(param2));
                } else {
                    setValue(param1, getValue(param1) - Integer.parseInt(param2));
                }
                break;

            case "MV": // kopiuje rejestr do rejestru, lub liczba
                if (argDrugiJestRejestrem) {
                    setValue(param1, getValue(param2));
                } else {
                    setValue(param1, Integer.parseInt(param2));
                }
                break;

               case "JM": // Skok do etykiety JM
            	 if (labels.containsKey(param1) && getValue("C") != 0) {

                    // setValue("C", labels.get(param1));
                    setValue("C", getValue("C") - 1);
                    PCBbox.getproces(PCBbox.working()).commandCounter = labels.get(param1);
                  
                }
                break;

            case "JN": // Skok do adresu

                break;


            case "HT": // Koniec programu

                String id = PCBbox.working();
                /////////zabijanie procesu jesli nie ma dzieci
                if(PCBbox.getproces(id).children == null){
                    PCBbox.kill(id);
                }
                else{
                    PCBbox.getproces(id).state = 5;
                }
                break;

            case "RC": // czytanie komunikatu;

                int count = Integer.parseInt(param1);
                System.out.println("lICZNIK: " + count);
                
                
             //   pipes.closepipe(des);
               // fileSystem.createFileWithContent("potok", received);
                boolean exists = false;
                for (int i = 0; i < pipes.pipes.size(); i++) {
                    if (pipes.pipes.get(i).pn.equals(param2)) { 
                        exists = true;
                    }
                }
                
                if(exists == false){
                    program_oczekujacy = program2;
                    proces_oczekujacy = PCBbox.working();
                }
                
                String received = pipes.read(count, param2, PCBbox);
                System.out.println(received);
                
                //ProcessorManager.RUNNING.pcb.receivedMsg = received;
                //fileSystem.createEmptyFile(ProcessorManager.RUNNING.GetName());
                //fileSystem.appendToFile(ProcessorManager.RUNNING.GetName(), received);
                break;

            case "SC": // -- Wysłanie komunikatu;
                int des = pipes.openpipe();
                pipes.pipes.get(des).od_kogo = PCBbox.working();
                for(int i=0;i<PCBbox.proceses.size(); i++){
                   
                if(PCBbox.proceses.get(i).pid.equals(param1))
                {
                    if(PCBbox.proceses.get(i).state == 3){
                        PCBbox.proceses.get(i).state = 4;
                        String s = PCBbox.working();
                        PCBbox.getproces(s).state=2;
                        
                    }
                    pipes.write(param2, param1, des);
                    break;
                }
                else if(PCBbox.proceses.get(i)==PCBbox.proceses.get(PCBbox.proceses.size()-1)){
                 System.out.print("Proces do ktorego chcesz wyslac nie istnieje\n");
               }
                }
               // (pipes.get(des));
                break;

		case "CP": {// -- tworzenie procesu (param1, param2);
	String dziala=PCBbox.working();
            try {
                PCBbox.getproces(dziala).fork(param1, param2);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
            }
                       
		}

            case "RP": // -- Uruchomienie procesu

                PCBbox.getproces(param1).setstate(4);
                //PCBbox.getproces(param1).setpriority(ProcessorManager.MAX_PRIORITY + 1);

                break;

            case "DP": // -- usuwanie procesu (param1);

           
                PCBbox.getproces(param1).exit();
                break;

            case "ST": // -- Zatrzymanie procesu
                PCBbox.getproces(param1).setstate(3);

                break;

            case "CF": // -- Create file
                
            	fileSystem.createEmptyFile(param1);
                break;

            case "DF": // -- Delete file
                fileSystem.deleteFile(param1);
                break;

            case "RF": // -- Print Output
                //System.out.println(fileSystem.getFileContent(param1));
                fileSystem.getFileContent(param1);
                break;

            case "WF": // dodaj do pliku
                fileSystem.appendToFile(param1, param2);
                break;

            case "WR":
                fileSystem.appendToFile(param1, Integer.toString(getValue(param2)));
                break;

        }
    }

    //testy


    public void work(String program, HashMap<String, Integer> labels) {
        StringBuilder command = new StringBuilder();
        StringBuilder param1 = new StringBuilder();
        StringBuilder param2 = new StringBuilder();
        String s_etykieta="";

        for (Character c : program.toCharArray()) {
            if (c == '\n') {
                otherCounter++;
                stan = 0;
                found = false;

               
                if(program.contains(" :")){
                    s_etykieta = program.replaceAll(" :", ":");
                }
              
                boolean rozkazToEtykieta = isLabel(s_etykieta);
                boolean poprawnoscRejestru = rejestry.contains(param1.toString());
                boolean poprawnoscRozkazu = rozkazy.contains(command.toString());
                boolean argDrugiJestRejestrem = rejestry.contains(param2.toString());

                if (rozkazToEtykieta) {
                    String temp = "";
                    for (Character z : (command.toString()).toCharArray()) {
                        if (z != ':') {
                            temp += z;
                        } else {
                            labels.put(temp, (otherCounter));
                        }
                    }

                } else if (poprawnoscRozkazu) {
                    doCommand(command.toString(), param1.toString(), param2.toString(), argDrugiJestRejestrem, labels);
                }

                command.delete(0, command.length());
                param1.delete(0, param1.length());
                param2.delete(0, param2.length());
                continue;
            }

            if (c == ',') {
                stan++;
                otherCounter++;
                found = true;
                manySpace = true;
                continue;
            }

            if (c == ' ') {
                otherCounter++;
                if (stan == 1 && !found) {
                    continue;
                }
                if (manySpace) {
                    continue;
                } else {
                    stan++;
                    manySpace = true;
                }
            }

            if (c != ' ') {
                otherCounter++;
                manySpace = false;
                switch (stan) {
                    case 0:
                        command.append(c);
                        break;
                    case 1:
                        param1.append(c);
                        break;
                    case 2:
                        param2.append(c);
                        break;
                }
            }
        }
    }
}