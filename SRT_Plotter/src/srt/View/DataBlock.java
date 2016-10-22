package srt.View;

/**
 * Representation of one scan (one set of points)
 *
 * @author Adam Pere
 * @author Gabriel Holodak
 */
public class DataBlock {
    public double[] data, deleted, endChannels; // actual data points, deleted data points, deleted end channels
    public String title;
    public double fStart, fStep; // Start Frequency and Frequency Step
    public double angle;
    public double azumith, elevation;

    public DataBlock(String t, double[] d, double startFreq, double stepFreq) {
        title = t;
        data = d;
        fStart = startFreq;
        fStep = stepFreq;
        deleted = new double[d.length];
        endChannels = new double[d.length];
        angle = 0;

    }

    public DataBlock(String t, double[] d, double startFreq, double stepFreq,
                     double[] del, double[] end) {
        title = t;
        data = d;
        fStart = startFreq;
        fStep = stepFreq;
        deleted = del;
        endChannels = end;
        angle = 0;
    }

    /**
     * Returns the average of all data points.
     *
     * @return
     */
    public double getAverageOverFrequency() {
        double sum = 0;
        int count = 0;

        for (int i = 0; i < data.length; i++) {
            if (data[i] != -1) {
                sum += data[i];
                count++;
            }
        }
        return sum / count;
    }

    /**
     * Prints out the Title and the data
     */
    @Override
    public String toString() {
        String returning =
                title + "\n" + "0 0 0 0 0 " + fStart + " " + fStep + " " + 0
                        + " " + data.length;

        for (int i = 0; i < data.length; i++) {

            returning = returning + " " + data[i] + " ";
        }

        returning = returning + "\nDeleted Points:";
        for (int i = 0; i < deleted.length; i++) {
            returning = returning + " " + deleted[i];
        }

        returning = returning + "\nEnd Channels:";

        for (int i = 0; i < endChannels.length; i++) {
            returning = returning + " " + endChannels[i];
        }
        return returning;
    }

}
