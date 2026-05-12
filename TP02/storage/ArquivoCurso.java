package TP02.storage;

import TP02.models.*;
import java.io.*;

import java.util.ArrayList;

public class ArquivoCurso extends Arquivo<Curso> {

    HashExtensivel<ParCodigoID> indiceIndiretoCodigo;
    ArvoreBMais<ParUsuarioCurso> arvoreBMais;

    public ArquivoCurso() throws Exception {
        super("cursos", Curso.class.getConstructor());

        indiceIndiretoCodigo = new HashExtensivel<>(
                ParCodigoID.class.getConstructor(),
                4,
                DIRETORIO_DADOS+"\\cursos\\indiceCodigo.d.db",
                DIRETORIO_DADOS+"\\cursos\\indiceCodigo.c.db");
        arvoreBMais = new ArvoreBMais<>(ParUsuarioCurso.class.getConstructor(), 8, DIRETORIO_DADOS+"\\cursos\\arvoreCursos.db");

    }

    @Override
    public int create(Curso c) throws Exception {
        // Criar curso
        int id = super.create(c);

        // Criar índice indireto para código compartilhável (ParCodigoID)
        ParCodigoID parCodigo = new ParCodigoID(c.getCodigoCompartilhavel(), id);
        indiceIndiretoCodigo.create(parCodigo);

        // Criar o par (idUsuario, idCurso) e adicionar na Árvore B+
        ParUsuarioCurso par = new ParUsuarioCurso(c.getIdUsuario(), id);
        boolean inserido = arvoreBMais.create(par); // Insere o par na Árvore B+

        System.out.println("DEBUG: Curso criado. ID=" + id + ", IdUsuario=" + c.getIdUsuario() + ", Inserido na árvore=" + inserido);
        return id;
    }

   public  ArrayList<Curso> readByUser(int idUsuario) throws Exception {
        ArrayList<Curso> cursos = new ArrayList<>();

        try {
            ArrayList<ParUsuarioCurso> pares = arvoreBMais.read(null);
            
            if (pares == null || pares.isEmpty()) {
                return cursos;
            }

            
            ArrayList<ParUsuarioCurso> filteredPares = new ArrayList<>();
            for (ParUsuarioCurso p : pares) {
                if (p.getIdUsuario() == idUsuario) {
                    filteredPares.add(p);
                }
            }

            // Ler cada curso
            for (ParUsuarioCurso par : filteredPares) {
                Curso curso = super.read(par.getIdCurso());
                if (curso != null) {
                    cursos.add(curso);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cursos;
    }

    @Override
    public Curso read(int id) throws Exception {
        return super.read(id);
    }

    public Curso read(String codigoCompartilhavel) throws Exception {
        // Usamos o índice indireto para buscar o id do curso pelo código compartilhável
        ParCodigoID parCodigo = indiceIndiretoCodigo.read(ParCodigoID.hash(codigoCompartilhavel)); // Busca pelo hash do
                                                                                                   // código
                                                                                                   // compartilhável

        // Se o código for encontrado no índice, lê o curso correspondente usando o id
        if (parCodigo != null) {
            int idCurso = parCodigo.getId(); // Obtém o id do curso associado ao código
            return super.read(idCurso); // Lê o curso do arquivo com o id encontrado
        }

        // Se o código não for encontrado, retorna null
        return null;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Curso c = super.read(id);
        if (c != null) {
            // Excluir o par (idUsuario, idCurso) da Árvore B+
            ParUsuarioCurso par = new ParUsuarioCurso(c.getIdUsuario(), id);
            arvoreBMais.delete(par); // Remove o par da Árvore B+

            // Excluir o curso do arquivo
            if (super.delete(id)) {
                return indiceIndiretoCodigo.delete(ParCodigoID.hash(c.getCodigoCompartilhavel()));
            }
        }
        return false;
    }

    @Override
    public boolean update(Curso novoCurso) throws Exception {
        // Lê o curso antigo
        Curso cursoVelho = super.read(novoCurso.getId());

        // Atualiza o curso no arquivo
        if (super.update(novoCurso)) {
            // Se o idUsuario mudou, atualiza a Árvore B+
            if (novoCurso.getIdUsuario() != cursoVelho.getIdUsuario()) {
                // Excluir o par antigo da Árvore B+
                ParUsuarioCurso parVelho = new ParUsuarioCurso(cursoVelho.getIdUsuario(), novoCurso.getId());
                arvoreBMais.delete(parVelho);

                // Criar o novo par (idUsuario, idCurso) na Árvore B+
                ParUsuarioCurso parNovo = new ParUsuarioCurso(novoCurso.getIdUsuario(), novoCurso.getId());
                arvoreBMais.create(parNovo);
            }
            return true;
        }
        return false;
    }
}

