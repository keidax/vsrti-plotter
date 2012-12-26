package fft.View;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

public interface ViewListener {
	public void reset();
	public void fullReset();
	public void writeSaveFile(File f);
}
