package Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface FileIO {
    void LoadFile(String file_name) throws IOException, ClassNotFoundException;

    void SaveFile(String file_name) throws IOException;
}
