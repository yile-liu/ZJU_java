import Utils.BaseShape;
import Utils.FileIO;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Model implements FileIO {
    ArrayList<BaseShape> shapes;

    public Model() {
        shapes = new ArrayList<>();
    }

    public void LoadFile(String file_name) throws IOException, ClassNotFoundException {
        Path              path = Paths.get(file_name);
        ObjectInputStream ois  = new ObjectInputStream(new FileInputStream(path.toFile()));

        Object input_object = ois.readObject();
        if (input_object instanceof ArrayList<?>){
            for(Object o: (ArrayList<?>)input_object){
                if(o instanceof BaseShape){
                    ((BaseShape) o).stroke = new BasicStroke(((BaseShape) o).stroke_width);
                    shapes.add((BaseShape) o);
                }else{
                    throw new IOException("读入非法的文件");
                }
            }
        }else{
            throw new IOException("读入非法的文件");
        }
        ois.close();
    }

    public void SaveFile(String file_name) throws IOException {
        Path               path = Paths.get(file_name);
        ObjectOutputStream oos  = new ObjectOutputStream(new FileOutputStream(path.toFile()));
        oos.writeObject(shapes);
        oos.close();
    }
}
