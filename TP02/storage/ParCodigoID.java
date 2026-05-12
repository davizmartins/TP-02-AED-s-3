package TP02.storage;

import TP02.models.*;
import java.io.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParCodigoID implements RegistroHashExtensivel<ParCodigoID> {

    public String codigoCompartilhavel;
    public int id;

    private final short TAMANHO = 44;  // Tamanho fixo

    public ParCodigoID() {
        this.codigoCompartilhavel = "";
        this.id = -1;
    }

    public ParCodigoID(String codigo, int id) {
        this.codigoCompartilhavel = codigo;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCodigoCompartilhavel() {
        return codigoCompartilhavel;
    }

    public void setCodigoCompartilhavel(String codigo) {
        this.codigoCompartilhavel = codigo;
    }

    public short size() {
        return this.TAMANHO;
    }

    public String toString() {
        return "(" + this.codigoCompartilhavel + ";" + this.id + ")";
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(this.codigoCompartilhavel);
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
        this.codigoCompartilhavel = dis.readUTF();
        this.id = dis.readInt();
    }
    
    @Override
    public int hashCode() {
        return codigoCompartilhavel.hashCode();
    }

    public static int hash(String codigo) {
        // Aqui, usamos o hashCode do código para gerar o valor de hash
        return codigo.hashCode();
    }
}

