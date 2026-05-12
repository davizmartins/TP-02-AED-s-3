package TP02.models;
import java.io.IOException;

public interface Registro {
    public int getId();
    public void setId(int id);

    public byte[] toByteArray() throws IOException;
    public void fromByteArray(byte[] b) throws IOException;
}
