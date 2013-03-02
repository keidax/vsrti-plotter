package srt.View;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class InputFile extends common.View.InputFile {
    
    @Override
    public boolean isBaselineParsable() {
        if (getFile() == null) {
            return false;
        }
        String str = getFile().getName();
        return str.matches(".*'[0-9]+([.][0-9]+)?'.*"); // 3 3.2 not 2. not .3
    }
    
    // TODO find out if I really need to override these methods:
    // I think the common.View.InputFile.countRms method is correct, but I'm not sure
    @Override
    public Double countRms() {
        double sum = 0;
        for (Double i : getIntensities()) {
            sum += Math.pow(i - getAverageIntensity(), 2);
        }
        double tempRMS = Math.sqrt(sum / getIntensities().size());// *intensities.size()-1));
        setRms(tempRMS);
        return tempRMS;
    }
    
    @Override
    public void parseFile() {
        try {
            FileInputStream fstream = new FileInputStream(getFile());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                if (strLine.charAt(0) != '*') {
                    getIntensities().add(Math.pow(Double.parseDouble(strLine.substring(strLine.lastIndexOf(" ") + 1)), 2));
                }
            }
            in.close();
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
}
