package TP02.models;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class Usuario implements Registro {
    public int id;
    public String nome;
    public String email;
    public String hashSenha;
    public String perguntaSecreta;
    public String hashRespostaSecreta;

    public Usuario() {
        this(-1, "", "", "", "", "");
    }

    public Usuario(int id, String n, String e, String hs, String ps, String rs) {
        this.id = id;
        this.nome = n;
        this.email = e;
        this.hashSenha = hs;
        this.perguntaSecreta = ps;
        this.hashRespostaSecreta = rs;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashSenha() {
        return hashSenha;
    }

    public void setHashSenha(String hashSenha) {
        this.hashSenha = hashSenha;
    }

    public String getPerguntaSecreta() {
        return perguntaSecreta;
    }

    public String getHashRespostaSecreta() {
        return hashRespostaSecreta;
    }

    public String toString() {
        return "\nID........:" + this.id +
                "\nNome........:" + this.nome +
                "\nEmail........:" + this.email +
                "\nHashSenha........:" + this.hashSenha +
                "\nPergunta Secreta........:" + this.perguntaSecreta +
                "\nHash Resposta Secreta........:" + this.hashRespostaSecreta;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.email);
        dos.writeUTF(this.hashSenha);
        dos.writeUTF(this.perguntaSecreta);
        dos.writeUTF(this.hashRespostaSecreta);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.email = dis.readUTF();
        this.hashSenha = dis.readUTF();
        this.perguntaSecreta = dis.readUTF();
        this.hashRespostaSecreta = dis.readUTF();

    }
}
