package TP02.models;

import java.time.LocalDate;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Random;

public class Curso implements Registro {

    public static final byte ATIVO_INSCRICOES = 0;
    public static final byte ATIVO_SEM_INSCRICOES = 1;
    public static final byte CONCLUIDO = 2;
    public static final byte CANCELADO = 3;

    public int id;
    public String nome;
    public LocalDate dataInicio;
    public String descricao;
    public String codigoCompartilhavel;
    public byte estado;
    public int idUsuario;

    public Curso(int id, String nome, LocalDate dataInicio, String descricao, String codigoCompartilhavel, byte estado,
            int idUsuario) {
        this.id = id;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.descricao = descricao;
        this.codigoCompartilhavel = codigoCompartilhavel;
        this.estado = estado;
        this.idUsuario = idUsuario;
    }

    public Curso() {
        this(-1, "", LocalDate.now(), "", gerarCodigo(), (byte) 0, -1);
    }

    public Curso(String nome, LocalDate dataInicio, String descricao, byte estado, int idUsuario) {
        this(-1, nome, dataInicio, descricao, gerarCodigo(), estado, idUsuario);
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setEstado(byte estado) {
        this.estado = estado;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setNome(String n) {
        this.nome = n;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCodigoCompartilhavel() {
        return codigoCompartilhavel;
    }

    public byte getEstado() {
        return estado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    @Override
    public String toString() {
        return "\nID................: " + this.id +
                "\nNome..............: " + this.nome +
                "\nData de início....: " + this.dataInicio +
                "\nDescrição.........: " + this.descricao +
                "\nCódigo............: " + this.codigoCompartilhavel +
                "\nEstado............: " + this.estado +
                "\nID do usuário.....: " + this.idUsuario;
    }

    public static String gerarCodigo() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder codigo = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return codigo.toString();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeLong(dataInicio.toEpochDay());
        dos.writeUTF(this.descricao);
        dos.writeUTF(this.codigoCompartilhavel);
        dos.writeByte(this.estado);
        dos.writeInt(this.idUsuario);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.dataInicio = LocalDate.ofEpochDay(dis.readLong());
        this.descricao = dis.readUTF();
        this.codigoCompartilhavel = dis.readUTF();
        this.estado = dis.readByte();
        this.idUsuario = dis.readInt();
    }

}
