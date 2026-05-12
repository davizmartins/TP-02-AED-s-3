package TP02.models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class CursoUsuario implements Registro {

    public int id;
    public int idCurso;
    public int idUsuario;
    public LocalDate dataInscricao;

    public CursoUsuario() {
        this(-1, -1, -1, LocalDate.now());
    }

    public CursoUsuario(int id, int idCurso, int idUsuario, LocalDate dataInscricao) {
        this.id = id;
        this.idCurso = idCurso;
        this.idUsuario = idUsuario;
        this.dataInscricao = dataInscricao;
    }

    public CursoUsuario(int idCurso, int idUsuario, LocalDate dataInscricao) {
        this(-1, idCurso, idUsuario, dataInscricao);
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public LocalDate getDataInscricao() {
        return dataInscricao;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);
        dos.writeInt(idCurso);
        dos.writeInt(idUsuario);
        dos.writeLong(dataInscricao.toEpochDay());

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.idCurso = dis.readInt();
        this.idUsuario = dis.readInt();
        this.dataInscricao = LocalDate.ofEpochDay(dis.readLong());
    }

    @Override
    public String toString() {
        return "CursoUsuario{" +
                "id=" + id +
                ", idCurso=" + idCurso +
                ", idUsuario=" + idUsuario +
                ", dataInscricao=" + dataInscricao +
                '}';
    }
}
