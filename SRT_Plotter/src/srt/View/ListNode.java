package srt.View;

/**
 * List Node for Linked List
 *
 * @author Adam Pere
 */
public class ListNode {
    public double[] data, deleted, endChannels; // actual data points, deleted
    // data points, deleted end
    // channels
    public String title;
    public double fStart, fStep; // Start Frequency and Frequency Step
    public double angle;
    public ListNode next;

    public ListNode(String t, double[] d, double startFreq, double stepFreq) {
        title = t;
        data = d;
        fStart = startFreq;
        fStep = stepFreq;
        deleted = new double[d.length];
        endChannels = new double[d.length];
        angle = 0;

    }

    public ListNode(String t, double[] d, double startFreq, double stepFreq,
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
        int ans = 0;
        double count = 0.0;

        for (int i = 0; i < data.length; i++) {
            if (data[i] != -1) {
                ans += data[i];
            } else {
                count++;
            }
        }
        return ans / (data.length - count);
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
