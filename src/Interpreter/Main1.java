/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interpreter;

import java.util.Scanner;

import proces.PCB;
import proces.Proces;
import Komunikacja_Miedzyprocesowa.Pipe;
import Komunikacja_Miedzyprocesowa.IPC;
import SynchronizacjaProcesow.Lock;
import SynchronizacjaProcesow.SynchronizacjaProcesow;
import MemoryManagment.MemoryManagment;
import Scheduler.Scheduler;
import filesystem.FileSystem;

import java.io.FileNotFoundException;
import java.util.HashMap;


/**
 * @author Olga Kryspin
 */
public class Main1 {

//    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
//
//         String plik =
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
//        PCBbox.fork("p1", "txt");
//        PCBbox.fork("p2", "txt2");
//        Interpreter a = new Interpreter(RAM,fileSystem, PCBbox);
//
//        System.out.println("RAM: " + RAM.getCurrentRAM());
//        System.out.println("RAM: " + RAM.getCurrentLRU());
//        HashMap<String, Integer> etykiety = new HashMap<String, Integer>();
////
//        String test2 = a.getProgram("p2");
//        System.out.println(test2);
//        a.work(test2, etykiety);
//       test2 = a.getProgram("p2");
//       System.out.println(test2);
//       a.work(test2, etykiety);
//      test2 = a.getProgram("p2");
//      System.out.println(test2);
//      a.work(test2, etykiety);
//     test2 = a.getProgram("p2");
//     System.out.println(test2);
//     a.work(test2, etykiety);
//    test2 = a.getProgram("p2");
//    System.out.println(test2);
//    a.work(test2, etykiety);
//   test2 = a.getProgram("p2");
//        System.out.println(test2);
//        a.work(test2, etykiety);
//        test2 = a.getProgram("p2");
//
//        String test = a.getProgram("p1");
//        a.work(test, etykiety);
//       test = a.getProgram("p1");
//        System.out.println(test);
//        a.work(test, etykiety);
//      test = a.getProgram("p1");
//        System.out.println(test);
//        a.work(test, etykiety);
//        test = a.getProgram("p1");
//        System.out.println(test);
//        a.work(test, etykiety);
//
//        test = a.getProgram("p1");
//        System.out.println(test);
//        a.work(test, etykiety);
//        System.out.println(PCBbox.A);
//        System.out.println(PCBbox.B);
//        test = a.getProgram("p1");
//        System.out.println(test);
//        a.work(test, etykiety);
//        fileSystem.showDiskAndVector();
//        System.out.println("RAM: " + RAM.getCurrentRAM());
//        System.out.println("RAM: " + RAM.getCurrentLRU());
//    }
}
