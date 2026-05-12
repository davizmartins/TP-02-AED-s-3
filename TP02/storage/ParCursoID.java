package TP02.storage;

import TP02.models.*;
import java.io.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParCursoID implements RegistroArvoreBMais<ParCursoID> {

    public int idCurso;
    public int idInscricao;

    public ParCursoID() {
        this.idCurso = -1;
        this.idInscricao = -1;
    }

    public ParCursoID(int idCurso, int idInscricao) {
        this.idCurso = idCurso;
        this.idInscricao = idInscricao;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public int getIdInscricao() {
        return idInscricao;
    }

    @Override
    public short size() {
        return 8; // 4 bytes para int + 4 bytes para int
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(idCurso);
        dos.writeInt(idInscricao);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.idCurso = dis.readInt();
        this.idInscricao = dis.readInt();
    }

    @Override
    public int compareTo(ParCursoID obj) {
        if (this.idCurso != obj.idCurso) {
            return Integer.compare(this.idCurso, obj.idCurso);
        }
        return Integer.compare(this.idInscricao, obj.idInscricao);
    }

    @Override
    public ParCursoID clone() {
        return new ParCursoID(this.idCurso, this.idInscricao);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idCurso);
    }

    @Override
    public String toString() {
        return "(" + idCurso + ";" + idInscricao + ")";
    }
}

