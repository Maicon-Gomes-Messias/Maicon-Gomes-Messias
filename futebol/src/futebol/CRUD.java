package futebol;


import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.ArrayList;

public class CRUD<futebol extends Registro> {
    private RandomAccessFile arq;
    private Constructor<futebol> construtor;

    public CRUD(Constructor<futebol> construtor, String file) throws FileNotFoundException, IOException{
        this.construtor = construtor;

        // Abrir arquivo e realizar primeiro preenchimento
        arq = new RandomAccessFile(file, "rw");
        if (arq.length() < 1) 
            arq.writeInt(0); // Insere o id inicial
    }

    public int create(futebol objeto) throws IOException {
        arq.seek(0);
        
        // Gerar novo ID
        int ultimoID = arq.readInt();

        System.out.println("----> Ultimo ID: " + ultimoID);

        int objID = ultimoID + 1;
        arq.seek(0); // Move para o início do arquivo
        arq.writeInt(objID); // Escreve o novo ID máximo
        
        // Gerar dados em byte
        objeto.setID(objID);
        byte[] ba = objeto.toByteArray();
        short tamanhoReg = (short)ba.length;

        // Escrita no arquivo
        arq.seek(arq.length()); // ir para a última Pos;
        arq.writeByte(0); // Lápide
        arq.writeShort(tamanhoReg); // Tamanho do registro
        arq.write(ba); // Registro;
        int posPonteiro = (int)arq.getFilePointer(); 
        arq.writeInt(0); // Aloca posição do ponteiro para proximo;
        int posProx = (int)arq.getFilePointer(); // Pega posição do próximo obj
        arq.seek(posPonteiro); // Vai para a posição para registrar no ponteiro a posição do próximo
        arq.writeInt(posProx);

        return objID;
    }

    public futebol read(int ID) throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException {
        // Pular "maxID"
        arq.seek(4);
        futebol objeto = null;

        boolean achou = false;
        byte[] ba;
        while(!achou && (int)arq.getFilePointer() != arq.length()) {
            byte lapide = arq.readByte();
            short tamanhoReg = arq.readShort();
            ba = new byte[tamanhoReg];
            arq.read(ba);
            int posProx = arq.readInt();
            if(lapide != 1) {
                objeto = construtor.newInstance();
                objeto.fromByteArray(ba);
                if(objeto.getID() == ID) {
                    achou = true;
                }
            }
            arq.seek(posProx);
        }

        return achou ? objeto : null;
    }

    public List<futebol> read() throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<futebol> objetos = new ArrayList<futebol>(); 
        futebol objeto;
        // Pular "maxID"
        arq.seek(4);

        byte[] ba;
        while((int)arq.getFilePointer() != arq.length()) {
            byte lapide = arq.readByte();
            short tamanhoReg = arq.readShort();
            ba = new byte[tamanhoReg];
            arq.read(ba);
            int posProx = arq.readInt();
            if(lapide != 1) {
                objeto = construtor.newInstance();
                objeto.fromByteArray(ba);
                objetos.add(objeto);
            }
            arq.seek(posProx);
        }
        return objetos;
    }

    public boolean delete(int ID) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        futebol objeto;
        
        // Pular "maxID"
        arq.seek(4);

        byte[] ba;
        boolean success = false;
        while(!success && (int)arq.getFilePointer() != arq.length()) {
            int posLapide = (int)arq.getFilePointer();
            byte lapide = arq.readByte();
            short tamanhoReg = arq.readShort();
            ba = new byte[tamanhoReg];
            arq.read(ba);
            int posProx = arq.readInt();
            if(lapide != 1) {
                objeto = construtor.newInstance();
                objeto.fromByteArray(ba);

                if(objeto.getID() == ID) {
                    arq.seek(posLapide);
                    arq.writeByte(1);
                    success = true;
                }
            }
            arq.seek(posProx);
        }

        return success;
    }

    public boolean update(futebol objeto) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        futebol tempObjeto;
        arq.seek(4); // Pular o "maxID"
        
        boolean success = false;
        byte[] ba;
        while(!success && (int)arq.getFilePointer() != arq.length()) {
            int posLapide = (int)arq.getFilePointer(); // Inicio do registro
            byte lapide = arq.readByte();
            short tamanhoReg = arq.readShort();
            ba = new byte[tamanhoReg];
            arq.read(ba);
            int posProx = arq.readInt();

            if(lapide != 1) {
                tempObjeto = construtor.newInstance();
                tempObjeto.fromByteArray(ba);

                if(tempObjeto.getID() == objeto.getID()) {
                    byte[] novoReg = objeto.toByteArray();
                    int tamanhoNovoReg = novoReg.length;
                    // Verificar se o tamanho é igual/menor/maior
                    if(tamanhoReg == tamanhoNovoReg) {
                        // Apenas substituir o antigo pelo novo
                        arq.seek(posLapide);
                        arq.writeByte(0); // lapide
                        arq.writeShort(tamanhoNovoReg);
                        arq.write(novoReg);
                        // A posição do próximo registro é a mesma (não precisar setar o ponteiro)
                    // Verificar se o tamanho do registro é maior que o do novo registro
                    } else if (tamanhoReg > tamanhoNovoReg) {
                        // Considerar o restante dos dados como lixo
                        arq.seek(posLapide);
                        arq.writeByte(0); // Lápide
                        arq.writeShort(tamanhoNovoReg);
                        arq.write(novoReg);
                        arq.writeInt(posProx); // Mantém a referência do próximo objeto da lista encadeada
                    // Verificar se o tamanho do registro é menor que a do novo registro
                    } else if (tamanhoReg < tamanhoNovoReg) {
                        // Excluir o objeto atual e escrever esse objeto no final do arquivo;
                        arq.seek(posLapide);
                        arq.writeByte(1); // Seta elemento como excluído
                        arq.seek(arq.length()); // Ir para final do arquivo
                        arq.writeByte(0); // Lápide
                        arq.writeShort(tamanhoNovoReg);
                        arq.write(novoReg);
                        int ponteiroPosProx = (int)arq.getFilePointer();
                        arq.writeInt(0); // posProx;
                        int novaPosProx = (int)arq.getFilePointer();
                        arq.seek(ponteiroPosProx); // volta para a posição de marcação do ponteiro
                        arq.writeInt(novaPosProx);
                    }
                    success = true;
                }
            }
        }

        return success;
    }
}