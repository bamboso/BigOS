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
import Komunikacja_Miedzyprocesowa.Pipe;
import Komunikacja_Miedzyprocesowa.IPC;
import SynchronizacjaProcesow.Lock;
import SynchronizacjaProcesow.SynchronizacjaProcesow;
import MemoryManagment.MemoryManagment;
import Scheduler.Scheduler;

import java.util.HashMap;


/**
 * @author Olga Kryspin
 */
public class main {


//    public static void main(String[] args) throws InterruptedException {
//
//         String program =
//"CF arytmetykaPlik\n" +
//"MV A,3\n" +
//"MV B,6\n" +
//"AD A,B\n" +
//"WF arytmetykaPlik,Wynik3+6=\n" +
//"WR arytmetykaPlik,A\n";
//
//        MemoryManagment RAM = new MemoryManagment();
//        FileSystem fileSystem = new FileSystem();
//        PCB PCBbox = new PCB("P0", RAM);
//
//        Interpreter a = new Interpreter(RAM,fileSystem,PCBbox);
//        HashMap<String, Integer> etykiety = new HashMap<String, Integer>();
//        a.work(program, etykiety);
//        fileSystem.showDiskAndVector();
//
//    }
}
