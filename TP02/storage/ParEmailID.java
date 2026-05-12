package TP02.storage;

import TP02.models.*;
import java.io.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParEmailID implements RegistroHashExtensivel<ParEmailID> {
    public String email;
    public int id;
    private final short TAMANHO = 44;

    public ParEmailID() {
        this.email = "";
        this.id = -1;
    }

    public ParEmailID(String e, int id) {
        try {
            this.email = e;
            this.id = id;
            if (e.getBytes().length + 4 > TAMANHO)
                throw new Exception("Número de caracteres do email maior que o permitido. Os dados serão cortados.");
        } catch (Exception ec) {
            ec.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String e) {
        this.email = e;
    }

    @Override
    public int hashCode() {
        return hash(this.email);
    }

    public short size() {
        return this.TAMANHO;
    }

    public String toString() {
        return "(" + this.email + ";" + this.id + ")";
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeUTF(this.email);
        dos.writeInt(this.id);

        byte[] bs = baos.toByteArray();
        byte[] bs2 = new byte[TAMANHO];

        for (int i = 0; i < TAMANHO; i++) {
            bs2[i] = ' ';
        }

        for (int i = 0; i < bs.length && i < TAMANHO; i++) {
            bs2[i] = bs[i];
        }

        return bs2;
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.email = dis.readUTF();
        this.id = dis.readInt();
    }

    public static int hash(String email) {
        return Math.abs(email.hashCode());
    }
}

