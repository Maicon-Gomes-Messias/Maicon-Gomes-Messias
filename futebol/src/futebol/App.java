package futebol;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;

public class App {

    public static CRUD<futebol> arqfutebol;

    public static boolean validarEntrada(String entrada) {
        boolean error = false;

        try {
            int number = Integer.parseInt(entrada);
            error = number < 0 || number > 5;
        } catch (NumberFormatException nfe) {
            error = true;
        }

        return error;
    }

    public static String input() throws IOException {
        InputStream is = System.in;
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String entrada = br.readLine();

        return entrada;
    }

    // imprimir a saida
    public static void imprimirMenu() {
        System.out.println("\n====== Campeonato mineiro ======");
        System.out.println("(1) Criar um clube");
        System.out.println("(2) Listar Clube");
        // System.out.println("(3) Atualizar Clube");
        // System.out.println("(4) Deletar Clube");
        System.out.println("(3) Realizar uma Partida");

        System.out.println("(0) Sair");
    }

    // metodo pra criar arquivo
    public static void create() throws IOException {
        System.out.println("\nInforme o nome do clube: ");
        String nome = input();
        System.out.println("\nInforme o CNPJ: ");
        String cnpj = input();
        System.out.println("\nInforme a cidade: ");
        String cidade = input();

        futebol futebol = new futebol(-1, nome, cnpj, cidade, 0, 0);

        int id = arqfutebol.create(futebol);

        System.out.println("\nClube criado - ID[" + id + "]");
    }

    // metodo para listar os arquivos que foram criado
    public static boolean list() throws IOException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        List<futebol> futebol = new ArrayList<futebol>();
        boolean temfutebol = true;
        futebol = arqfutebol.read();

        if (futebol.isEmpty()) {
            System.out.println("\nNenhum comentário cadastrado");
            temfutebol = false;
        } else {
            for (futebol f : futebol) {
                System.out.println(f.toString());
            }
        }

        return temfutebol;
    }

    public static void realizar_partita() throws IOException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

        boolean temfutebol = list();

        if (temfutebol) {
            System.out.print("\nEscolha o ID do primeiro clube: ");
            String _ID1 = input();
            int ID1 = Integer.parseInt(_ID1);
            System.out.print("\nEscolha o ID do segundo clube: ");
            String _ID2 = input();
            int ID2 = Integer.parseInt(_ID2);
            futebol time1;
            futebol time2;

            time1 = arqfutebol.read(ID1);
            time2 = arqfutebol.read(ID2);

            System.out.println("TIME 1: " + time1);
            System.out.println("TIME 2: " + time2);

            System.out.print("Digite quantos gols fez o " + time1.nome + ": ");
            String _gols1 = input();
            int gols1 = Integer.parseInt(_gols1);
            System.out.print("Digite quantos gols fez o " + time2.nome + ": ");
            String _gols2 = input();
            int gols2 = Integer.parseInt(_gols2);

            if (gols1 > gols2) {
                System.out.println(time1.nome + " VENCEU!!! Chupa Frangas kkkkkkkk");
                time1.ponto += 3;
            } else if (gols2 > gols1) {
                System.out.println(time2.nome + " VENCEU!!! ");
                time2.ponto += 3;
            } else {
                System.out.println(" EMPATE ");
                time1.ponto++;
                time2.ponto++;
            }
            time1.partida_jogada++;
            time2.partida_jogada++;

            arqfutebol.update(time1);
            arqfutebol.update(time2);

        }

    }

    // metodo para atualizar
    public static void update() throws IOException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        boolean temfutebol = list();
        if (temfutebol) {
            System.out.println("\nEscolha o ID do comentário a ser atualizado: ");
            String entradaID = input();
            boolean error = false;

            try {
                int id = Integer.parseInt(entradaID);
                futebol futebol = arqfutebol.read(id);
                if (futebol != null) {
                    System.out.println("\nInforme o nome : ");
                    String nome = input();
                    futebol.setNome(nome);

                    System.out.println("\nInforme o cnpj: ");
                    String cnpj = input();
                    futebol.setCnpj(cnpj);

                    System.out.println("\nInforme a cidade: ");
                    String cidade = input();
                    futebol.setCidade(cidade);

                    error = !arqfutebol.update(futebol);
                } else {
                    error = true;
                }
            } catch (NumberFormatException nfe) {
                error = true;
            }

            if (error) {
                System.out.println("\nOcorreu algum erro ao atualizar este comentário");
            } else {
                System.out.println("\nComentário atualizado com sucesso!!");
            }
        }
    }

    // metodo para deletar
    public static void delete() throws IOException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        boolean temfutebol = list();
        if (temfutebol) {
            System.out.println("\nInforme o ID a ser deletado: ");

            String entradaId = input();

            boolean error = false;
            try {
                int id = Integer.parseInt(entradaId);
                error = !arqfutebol.delete(id);
            } catch (NumberFormatException nfe) {
                error = true;
            }

            if (error) {
                System.out.println("\nOcorreu algum erro na exclusão do comentário");
            } else {
                System.out.println("\nComentário excluído com sucesso!");
            }
        }

    }

    // menu de opções
    public static void escolherMetodo(int opcao) throws IOException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        switch (opcao) {
            case 1:
                create();
                break;
            case 2:
                list();
                break;
            case 3:
                realizar_partita();
                break;
            case 0:
                System.out.println("\nObrigado. By: Maicon Gomes");
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        // criando arquivo em .db
        try {
            new File("futebol.db");// .delete();
            arqfutebol = new CRUD<>(futebol.class.getConstructor(), "futebol.db");
            String entrada;
            boolean error;
            int opcao = 0;
            do {
                imprimirMenu();
                entrada = input();
                error = validarEntrada(entrada);
                if (!error) {
                    opcao = Integer.parseInt(entrada);
                    escolherMetodo(opcao);
                } else {
                    System.out.println("\nOcorreu um erro durante a entrada de valores, tente novamente");
                    opcao = 1;
                }
            } while (opcao != 0);

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException IOe) {
            IOe.printStackTrace();
        } catch (InstantiationException ie) {
            ie.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        } catch (InvocationTargetException ite) {
            ite.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}