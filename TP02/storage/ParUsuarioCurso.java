package TP02.storage;

import TP02.models.*;
import java.io.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParUsuarioCurso implements RegistroArvoreBMais<ParUsuarioCurso> {

    public int idUsuario;
    public int idCurso;

    public ParUsuarioCurso() {
        this.idUsuario = -1;
        this.idCurso = -1;
    }

    public ParUsuarioCurso(int idUsuario, int idCurso) {
        this.idUsuario = idUsuario;
        this.idCurso = idCurso;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdCurso() {
        return idCurso;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idUsuario);
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(idUsuario);
        dos.writeInt(idCurso);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.idUsuario = dis.readInt();
        this.idCurso = dis.readInt();
    }

    @Override
    public short size() {
        return Integer.BYTES * 2; // Cada inteiro ocupa 4 bytes
    }

    @Override
    public String toString() {
        return "(" + idUsuario + ", " + idCurso + ")";
    }

    @Override
    public int compareTo(ParUsuarioCurso o) {
        // Primeiro compara pelo idUsuario, depois pelo idCurso se idUsuario for igual
        int result = Integer.compare(this.idUsuario, o.idUsuario);
        if (result == 0) {
            result = Integer.compare(this.idCurso, o.idCurso);
        }
        return result;
    }

    @Override
    public ParUsuarioCurso clone() {
        return new ParUsuarioCurso(this.idUsuario, this.idCurso);
    }
}

