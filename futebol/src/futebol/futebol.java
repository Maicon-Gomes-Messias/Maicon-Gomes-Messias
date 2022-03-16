package futebol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.util.Scanner;

//classe futebol
public class futebol implements Registro {
    Scanner console = new Scanner(System.in);
    protected int id;
    protected String nome;
    protected String cnpj;
    protected String cidade;
    protected int partida_jogada = 0;
    protected int ponto = 0;

    
    // construtor
    public futebol(int id, String nome, String cnpj, String cidade, int partida_jogada, int ponto) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.cidade = cidade;
        this.partida_jogada = partida_jogada;
        this.ponto = ponto;
    }

    // construtor
    public futebol() {
        this.id = 0;
        this.nome = "";
        this.cnpj = "";
        this.cidade = "";
        this.partida_jogada = 0;
        this.ponto = 0;
    }

    // criar o arquivo em .db

    public int getID() {

        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }
    
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeUTF(nome);
        dos.writeUTF(cnpj);
        dos.writeUTF(cidade);
        dos.writeInt(partida_jogada);
        dos.writeInt(ponto);

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        id = dis.readInt();
        nome = dis.readUTF();
        cnpj = dis.readUTF();
        cidade = dis.readUTF();
        partida_jogada = dis.readInt();
        ponto = dis.readInt();

    }
    //atributos utilizado no vetor de byte
    public String toString() {

        return (" ## CRIAR UM CLUBE ## \n" +
                "ID: " + this.id + "\n" +
                "NOME: " + this.nome + "\n" +
                "CNPJ: " + this.cnpj + "\n" +
                "CIDADE: " + this.cidade + "\n" +
                "PARTIDAS JOGADAS: " + this.partida_jogada + "\n" +
                "PONTOS: " + this.ponto + "\n");
    }
    // get e set
    public String getCidade() {
        return cidade;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getNome() {
        return nome;
    }

    public int getPartida_jogada() {
        return partida_jogada;
    }

    public int getPonto() {
        return ponto;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPartida_jogada(int partida_jogada) {
        this.partida_jogada = partida_jogada;
    }

    public void setPonto(int ponto) {
        this.ponto = ponto;
    }

}