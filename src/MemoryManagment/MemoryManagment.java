package MemoryManagment;

import java.io.FileNotFoundException;

public class MemoryManagment {

    public ExchangeFile exchangeFile;

    private char ram[] = new char[128];
    private int pageLifeTime[] = new int[8];
    private String framesProcessesNames[] = new String[8];

    public MemoryManagment() {
        exchangeFile = new ExchangeFile();
        clearPageLifeTime();
        clearRAM();
    }

    private void addPageToFrame(int frameLRU, String process) {
        for (int i = 0; i < 16; i++) {
            ram[frameLRU * 16 + i] = process.charAt(i);
        }
    }

    private int getLRU() {
        int LRU = 0;
        int LRULifeTime = pageLifeTime[0];
        for (int i = 1; i < 8; i++) {
            if (pageLifeTime[i] < LRULifeTime) {
                LRULifeTime = pageLifeTime[i];
                LRU = i;
            }
        }
        changeLRU(LRU);
        return LRU;
    }

    public void addProcess(String processName, String filePath) throws FileNotFoundException {
        exchangeFile.setProcess(processName, filePath);
    }

    private void changeLRU(int framePosition) {
        int highestValue = pageLifeTime[0];
        for (int i = 1; i < 8; i++) {
            if (pageLifeTime[i] > highestValue) {
                highestValue = pageLifeTime[i];
            }
        }
        if (pageLifeTime[framePosition] == 0 || pageLifeTime[framePosition] != highestValue) {
            pageLifeTime[framePosition] = highestValue + 1;
        }
    }

    public char getCommandChar(int charPosition, String processName) {
        char commandChar = ' ';
        if (!exchangeFile.containsProcess(processName)) {
            return commandChar;
        }
        if (exchangeFile.getProcessSize(processName) > charPosition && charPosition >= 0 && exchangeFile.containsProcess(processName)) {
            int page;
            page = charPosition / 16;
            charPosition = charPosition % 16;
            int pagePositionInRAM = exchangeFile.isInRAM(processName, page);
            if (pagePositionInRAM == -1) {
                pagePositionInRAM = getLRU();
                framesProcessesNames[pagePositionInRAM] = processName;
                addPageToFrame(pagePositionInRAM, exchangeFile.getPage(processName, page));
                exchangeFile.updatePagesInformation(processName, pagePositionInRAM);
                exchangeFile.removeFromRAM(processName, pagePositionInRAM);
            }
            commandChar = ram[pagePositionInRAM * 16 + charPosition];
            changeLRU(pagePositionInRAM);
        }
        return commandChar;
    }

    private void clearRAM() {
        for (int i = 0; i < 128; i++) {
            ram[i] = ' ';
        }
    }

    private void clearPageLifeTime() {
        for (int i = 0; i < 8; i++) {
            pageLifeTime[i] = 0;
        }
    }

    public void getCurrentLRU() {
        String s = "";
        for (int i = 0; i < 8; i++) {
            s += pageLifeTime[i] + "  ";
        }
        System.out.println(s);
    }

    public void getCurrentRAM() {
        StringBuilder currentRAM = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            currentRAM.append(i + ".");
            for (int j = 0; j < 16; j++) {
                currentRAM.append(ram[i * 16 + j]);
            }
            currentRAM.append("\n");
        }
        System.out.println(currentRAM);
    }


}
