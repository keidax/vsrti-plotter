package beam.View;

import java.io.File;

public interface ViewListener {
    public void reset();
    
    public void fullReset();
    
    public void writeSaveFile(File f);
}
