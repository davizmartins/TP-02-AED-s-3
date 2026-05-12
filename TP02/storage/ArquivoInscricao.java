package TP02.storage;

import TP02.models.*;
import java.io.*;

import java.util.ArrayList;

public class ArquivoInscricao extends Arquivo<CursoUsuario> {

    private ArvoreBMais<ParCursoID> arvorePorCurso;
    private ArvoreBMais<ParUsuarioID> arvorePorUsuario;

    public ArquivoInscricao() throws Exception {
        super("inscricoes", CursoUsuario.class.getConstructor());

        // Árvore B+ indexada por idCurso
        arvorePorCurso = new ArvoreBMais<>(
                ParCursoID.class.getConstructor(),
                8,
                DIRETORIO_DADOS+"\\inscricoes\\arvoreCurso.db");

        // Árvore B+ indexada por idUsuario
        arvorePorUsuario = new ArvoreBMais<>(
                ParUsuarioID.class.getConstructor(),
                8,
                DIRETORIO_DADOS+"\\inscricoes\\arvoreUsuario.db");
    }

    // Getter para ArquivoUsuario - será injetado depois
    private ArquivoUsuario arquivoUsuario;

    public void setArquivoUsuario(ArquivoUsuario arquivoUsuario) {
        this.arquivoUsuario = arquivoUsuario;
    }

    public ArquivoUsuario getArquivoUsuario() {
        return arquivoUsuario;
    }

    @Override
    public int create(CursoUsuario inscricao) throws Exception {
        // Criar a inscrição no arquivo
        int id = super.create(inscricao);
        inscricao.setId(id);

        // Adicionar nas árvores B+
        arvorePorCurso.create(new ParCursoID(inscricao.getIdCurso(), id));
        arvorePorUsuario.create(new ParUsuarioID(inscricao.getIdUsuario(), id));

        return id;
    }

    @Override
    public CursoUsuario read(int id) throws Exception {
        return super.read(id);
    }

    /**
     * Retorna todas as inscrições de um determinado curso
     */
    public ArrayList<CursoUsuario> readByCurso(int idCurso) throws Exception {
        ArrayList<CursoUsuario> inscricoes = new ArrayList<>();

        // Recupera todos os pares da árvore e filtra pelo curso desejado
        ArrayList<ParCursoID> pares = arvorePorCurso.read(null);

        if (pares == null || pares.isEmpty()) {
            return inscricoes;
        }

        for (ParCursoID par : pares) {
            if (par.getIdCurso() == idCurso) {
                CursoUsuario inscricao = super.read(par.getIdInscricao());
                if (inscricao != null) {
                    inscricoes.add(inscricao);
                }
            }
        }

        return inscricoes;
    }

    /**
     * Retorna todas as inscrições de um determinado usuário
     */
    public ArrayList<CursoUsuario> readByUsuario(int idUsuario) throws Exception {
        ArrayList<CursoUsuario> inscricoes = new ArrayList<>();

        // Recupera todos os pares da árvore e filtra pelo usuário desejado
        ArrayList<ParUsuarioID> pares = arvorePorUsuario.read(null);

        if (pares == null || pares.isEmpty()) {
            return inscricoes;
        }

        for (ParUsuarioID par : pares) {
            if (par.getIdUsuario() == idUsuario) {
                CursoUsuario inscricao = super.read(par.getIdInscricao());
                if (inscricao != null) {
                    inscricoes.add(inscricao);
                }
            }
        }

        return inscricoes;
    }

    /**
     * Retorna todas as inscrições
     */
    public ArrayList<CursoUsuario> readAll() throws Exception {
        ArrayList<CursoUsuario> inscricoes = new ArrayList<>();

        try {
            ArrayList<ParUsuarioID> pares = arvorePorUsuario.read(null);

            if (pares == null || pares.isEmpty()) {
                return inscricoes;
            }

            for (ParUsuarioID par : pares) {
                CursoUsuario inscricao = super.read(par.getIdInscricao());
                if (inscricao != null) {
                    inscricoes.add(inscricao);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return inscricoes;
    }

    /**
     * Verifica se um usuário já está inscrito em um curso
     */
    public boolean jaInscrito(int idCurso, int idUsuario) throws Exception {
        ArrayList<CursoUsuario> inscricoes = readByCurso(idCurso);
        for (CursoUsuario inscricao : inscricoes) {
            if (inscricao.getIdUsuario() == idUsuario) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        CursoUsuario inscricao = super.read(id);
        if (inscricao != null) {
            // Remover das árvores B+
            arvorePorCurso.delete(new ParCursoID(inscricao.getIdCurso(), id));
            arvorePorUsuario.delete(new ParUsuarioID(inscricao.getIdUsuario(), id));

            // Remover do arquivo
            return super.delete(id);
        }
        return false;
    }

}

