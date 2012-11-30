package fft.Viewer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import fft.Model.Point;

public class InputFile implements Comparable {

    public File file;
    public double baseline = -1;
    private double averageIntensity = -1;
    private double rms = 0;
    public List<Double> intensities = new ArrayList<Double>();
    public static TableModel tableModel;

    public InputFile() {
        intensities = new ArrayList<Double>();
    }

    static boolean isFormatCorrect(File file) {
         try {
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {

                if (strLine.trim().length()>0 && strLine.trim().charAt(0) != '*') {
                    int li = strLine.trim().lastIndexOf(" ");
                    if(li==-1)
                        return false;
                    Double.parseDouble(strLine.substring(strLine.lastIndexOf(" ") + 1));
                }
            }
            in.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
            return false;
        }
         return true;
    }

    public InputFile(File f) {
        setFile(f);
        if (isBaselineParsable()) {
            setBaseline(parseBaseline());
        }
    }

    public double getAverageIntensity() {
        if (averageIntensity == -1) {
            setAverageIntensity(this.countAverageIntensity());
        }
        return averageIntensity;
    }

    public void setAverageIntensity(double averageIntensity) {
        this.averageIntensity = averageIntensity;
    }

    public double getRms() {
        if (rms == 0) {
            countRms();
        }
        return rms;
    }

    public void setRms(double r) {
        rms = r;
    }

    public InputFile(String fstr) {
        setFile(new File(fstr));
        if (isBaselineParsable()) {
            setBaseline(parseBaseline());
        }
    }

    public boolean isBaselineParsable() {
        if (file == null) {
            return false;
        }
        String str = getFile().getName();
        return str.matches(".*'[0-9]*([.][0-9]*)?'.*"); // 3 3.2 not 2. not .3
    }

    public double parseBaseline() {
        String str = file.getName();
        String[] split = str.split("'");
        return Double.parseDouble(split[split.length - 2]);
    }

    @Override
    public int compareTo(Object arg0) {
        InputFile f = (InputFile) arg0;
        if (f.baseline > this.baseline) {
            return -1;
        } else {
            return 1;
        }
    }

    public Vector<Object> createVector() {
        Vector<Object> v = new Vector<Object>();
        v.add(baseline);
        v.add(this.file);
        return v;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        parseFile();
        countAverageIntensity();
    }

    public Double countAverageIntensity() {
        if (intensities == null || intensities.size() == 0) {
            parseFile();
        }
        int sum = 0;
        for (Double d : intensities) {
            sum += d;
        }
        averageIntensity = sum / ((Integer) intensities.size()).doubleValue();
        return averageIntensity;
    }

    public Double countRms() {
        if (intensities == null || intensities.size() == 0) {
            this.getIntensities();
        }
        double sum = 0;
        for (Double i : intensities) {
            sum += Math.pow(i - getAverageIntensity(), 2);
        }
        rms = Math.sqrt(sum / (intensities.size()*intensities.size()-1));
        return rms;
    }

    public void parseFile() {
        try {
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                if (strLine.charAt(0) != '*') {
                    intensities.add(Double.parseDouble(strLine.substring(strLine.lastIndexOf(" ") + 1)));
                }
            }
            in.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static double getCollectiveIntensityAverage(ArrayList<InputFile> f) {
        double sum = 0;
        int count = 0;
        //System.out.println("CollectiveIntensityAverageOf:");
        for (InputFile i : f) {
            //System.out.println(i.getBaseline()+" "+i.getAverageIntensity());
            for (Double intensity : i.getIntensities()) {
                sum += intensity;
                count++;
            }
        }
        //System.out.println("result is "+sum/(double)count);
        return sum / count;
    }

    public static ArrayList<InputFile> getInputFilesByX(TreeMap<Double, InputFile> inputFiles, ArrayList<Point> xs) {
        ArrayList<InputFile> ret = new ArrayList<InputFile>();
        for (Point d : xs) {
            if (inputFiles.containsKey(d.getX())) {
                ret.add(inputFiles.get(d.getX()));
            }
        }
        return ret;
    }

    public double getBaseline() {
        return baseline;
    }

    public void setBaseline(double baseline) {
        this.baseline = baseline;
        tableModel.viewer.sendAdapterFiles();
    }

    public List<Double> getIntensities() {
        return intensities;
    }

    public void setIntensities(List<Double> intensities) {
        this.intensities = intensities;
    }
}
