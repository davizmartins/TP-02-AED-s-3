package TP02.storage;

import TP02.models.*;
import java.io.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParUsuarioID implements RegistroArvoreBMais<ParUsuarioID> {

    public int idUsuario;
    public int idInscricao;

    public ParUsuarioID() {
        this.idUsuario = -1;
        this.idInscricao = -1;
    }

    public ParUsuarioID(int idUsuario, int idInscricao) {
        this.idUsuario = idUsuario;
        this.idInscricao = idInscricao;
    }

    public int getIdUsuario() {
        return idUsuario;
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

        dos.writeInt(idUsuario);
        dos.writeInt(idInscricao);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.idUsuario = dis.readInt();
        this.idInscricao = dis.readInt();
    }

    @Override
    public int compareTo(ParUsuarioID obj) {
        if (this.idUsuario != obj.idUsuario) {
            return Integer.compare(this.idUsuario, obj.idUsuario);
        }
        return Integer.compare(this.idInscricao, obj.idInscricao);
    }

    @Override
    public ParUsuarioID clone() {
        return new ParUsuarioID(this.idUsuario, this.idInscricao);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idUsuario);
    }

    @Override
    public String toString() {
        return "(" + idUsuario + ";" + idInscricao + ")";
    }
}

