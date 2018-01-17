package MemoryManagment;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class ExchangeFile {
    private List<String> exchangeFile = new ArrayList<>();
    private List<String> processesNames = new ArrayList<>();
    Map<String, int[]> whichPagesInRam = new HashMap<>();

    private int currentPage;

    public String getPage(String processName, int page) {
        currentPage = page;
        StringBuilder p = new StringBuilder();
        String process = exchangeFile.get(getPositionInExchangeFile(processName));
        int firstLetter = page * 16;
        for (int i = firstLetter; i < firstLetter + 16; i++) {
            p.append(process.charAt(i));
        }
        return p.toString();
    }

    private int getPositionInExchangeFile(String processName) {
        int positionInExchangeFile = 0;
        for (int i = 0; i < processesNames.size(); i++) {
            if (processesNames.get(i).equals(processName)) {
                positionInExchangeFile = i;
            }
        }
        return positionInExchangeFile;
    }

    public void setProcess(String processName, String filePath) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader(filePath + ".txt"));
        StringBuilder sb = new StringBuilder();
        while (in.hasNext()) {
            sb.append(in.next() + " ");
        }
        in.close();
        String program = sb.toString();
        int lastPageBytes = program.length() % 16;
        if (lastPageBytes != 0) {
            for (int i = 0; i < 16 - lastPageBytes; i++) {
                program += ' ';
            }
        }
        for (int i = 0; i < processesNames.size(); i++) {
            if (processesNames.get(i).equals(processName)) {
                exchangeFile.set(i, program);

            }
        }
        exchangeFile.add(0, program);
        processesNames.add(0, processName);
        int[] pagesInRam = new int[16];
        for (int i = 0; i < 16; i++) {
            pagesInRam[i] = -1;
        }
        whichPagesInRam.put(processName, pagesInRam);
    }

    public int isInRAM(String processName, int page) {
        return whichPagesInRam.get(processName)[page];
    }

    public void updatePagesInformation(String processName, int framePosition) {
        whichPagesInRam.get(processName)[currentPage] = framePosition;
    }

    public int getProcessSize(String processName) {
        return exchangeFile.get(getPositionInExchangeFile(processName)).length();
    }

    public boolean containsProcess(String processName) {
        return processesNames.contains(processName);
    }

    public void removeFromRAM(String processName, int framePositionInRAM) {
        for (int i = 0; i < 16; i++) {
            if (whichPagesInRam.get(processName)[i] == framePositionInRAM && i != currentPage) {
                whichPagesInRam.get(processName)[i] = -1;
            }
        }
    }
}
