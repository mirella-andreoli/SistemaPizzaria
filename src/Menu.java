
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Scanner;

//Classe controladora dos menus

public class Menu{
    private int opcao=-1;
    PrintStream s = System.out;
    Scanner sc = new Scanner (System.in);
    public Menu(){};//Construtor
    
    //Método para impressão do cabecalho das rotinas
    public void imprimeCabecalho(String rotina){
        s.print("\n\n\t\t\u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557");
        s.print("\n\t\t\u2551                                         \u2551");
        s.print("\n\t\t\u2551"+rotina+"\u2551");
        s.print("\n\t\t\u2551                                         \u2551");
        s.print("\n\t\t\u255A\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255D");
    //ref. p/ caber no layout| Pizzaria Nacoes - Fechamento de vendas  |
    //ref. p/ caber no layout| Pizzaria Nacoes - Cadastro de Clientes  |
    
    };
       
    //----Métodos de impressão das opções dos menus principais de cada rotina
    //Efetuam a leitura da opção e a retorna para o método de rotina que o chamou
    public int exibeMenuPrincipal(){
        imprimeCabecalho("             Pizzaria Nacoes             ");
        s.print("\n\n\t\t 1 - Pedidos");   
        s.print("\n\n\t\t 2 - Clientes");
        s.print("\n\n\t\t 3 - Pizzas");
        s.print("\n\n\t\t 4 - Listar clientes");
        s.print("\n\n\t\t 5 - Listar pizzas");
        s.print("\n\n\t\t 6 - Listar pedidos");
        s.print("\n\n\t\t 7 - Vendas - Fechamento Diario");
        s.print("\n\n\t\t 0 - Sair do sistema");
        s.print("\n\n\n\t\t #Informe a opcao desejada: ");
        return opcao=Integer.parseInt(sc.nextLine());
    }
      
    public int exibeMenuCliente(String rotinaSolicitante){
        
        if (rotinaSolicitante.equalsIgnoreCase("Pedido")){
            imprimeCabecalho("  Pizzaria Nacoes - Cadastro de Pedidos  "); 
            s.print("\n\n\t                ##Cliente do Pedido##                 ");
        } else {
            imprimeCabecalho(" Pizzaria Nacoes - Cadastro de Clientes  ");
        }

        s.print("\n\n\t\t 1 - Cadastrar cliente");
        s.print("\n\n\t\t 2 - Alterar cliente");

        if (rotinaSolicitante.equalsIgnoreCase("Pedido")){
            s.print("\n\n\t\t 3 - Selecionar cliente");
            s.print("\n\n\t\t 4 - Menu Pedido");
        } else {
            s.print("\n\n\t\t 3 - Pesquisar cliente");
            s.print("\n\n\t\t 4 - Menu Principal");
        }

        s.print("\n\n\t\t 0 - Sair do sistema");
        s.print("\n\n\n\t\t #Informe a opcao desejada: ");
        return opcao=Integer.parseInt(sc.nextLine());
    }
        
    public int exibeMenuPizza(){
        
        imprimeCabecalho("   Pizzaria Nacoes - Cadastro de Pizzas  ");
        s.print("\n\n\t\t 1 - Cadastrar pizza");   
        s.print("\n\n\t\t 2 - Alterar pizza");
        s.print("\n\n\t\t 3 - Menu Principal");
        s.print("\n\n\t\t 0 - Sair do sistema");
        s.print("\n\n\n\t\t #Informe a opcao desejada: ");
        return opcao=Integer.parseInt(sc.nextLine());
    }
    
    public int exibeMenuPedido(){
        
        imprimeCabecalho("  Pizzaria Nacoes - Cadastro de Pedidos  ");
        s.print("\n\n\t\t 1 - Novo pedido");   
        s.print("\n\n\t\t 2 - Alterar pedido");
        s.print("\n\n\t\t 3 - Excluir pedido");
        s.print("\n\n\t\t 4 - Menu Principal");
        s.print("\n\n\t\t 0 - Sair do sistema");
        s.print("\n\n\n\t\t #Informe a opcao desejada: ");
        return opcao=Integer.parseInt(sc.nextLine());
    }
    
    //----Métodos de escolha-caso dos menus de cada rotina
    public void rotinaOpcaoPrincipal(){
        opcao=exibeMenuPrincipal();

        switch(opcao){
            case 0://Sair do sistema
                sair();
                break;
            
            case 1://Menu pedido
                Pedido pe = new Pedido();
                rotinaOpcaoPedido(pe);
                break;
            
            case 2://Menu cliente
                Cliente c = new Cliente ();
                rotinaOpcaoCliente("Menu",c,null);
                break;
                
            case 3://Menu pizza
                Pizza pi = new Pizza();
                rotinaOpcaoPizza(pi);
                break;
            case 4://Listar clientes
                Cliente c2 = new Cliente();
                try{
                    if((c2.imprimePesquisa(c2.pesquisar())).isEmpty()){
                        //Se a consulta não retornar nada
                        s.print("\n\n\t\tNenhum registro encontrado.");
                    }
                } catch (SQLException e){
                    //Mostra exceção na tela em caso de erro de sql
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                }
                s.print("\n\n\t\t ## Pressione enter para retornar ao menu.. ##");
                sc.nextLine();
                rotinaOpcaoPrincipal();
                break;
                
            case 5://Listar pizzas - ver se dá tempo de implementar com pesquisa, se não, só ordernar
                Pizza p2 = new Pizza();
                try{
                    if((p2.imprimePesquisa(p2.pesquisar())).isEmpty()){
                        //Se a consulta não retornar nada
                        s.print("\n\n\t\tNenhum registro encontrado.");
                    }
                } catch (SQLException e){
                    //Mostra exceção na tela em caso de erro de sql
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                }
                s.print("\n\n\t\t ## Pressione enter para retornar ao menu.. ##");
                sc.nextLine();
                rotinaOpcaoPrincipal();
                break;
            case 6://Listar pedidos
                Pedido pe2= new Pedido();
                try{
                    if((pe2.imprimePesquisa(pe2.pesquisar("Pedido"))).isEmpty()){
                        //Se a consulta não retornar nada
                        s.print("\n\n\t\tNenhum registro encontrado.");
                    }
                } catch (SQLException e){
                    //Mostra exceção na tela em caso de erro de sql
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                }
                s.print("\n\n\t\t ## Pressione enter para retornar ao menu.. ##");
                sc.nextLine();
                rotinaOpcaoPrincipal();
                break;
            case 7://Vendas - Fechamento
                Pedido pe3= new Pedido();
                try{
                    if((pe3.imprimeFechamentoVendas(pe3.pesquisar("Menu"))).isEmpty()){
                        //Se a consulta não retornar nada
                        s.print("\n\n\t\tNenhum registro encontrado.");
                    }
                } catch (SQLException e){
                    //Mostra exceção na tela em caso de erro de sql
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                }
                s.print("\n\n\t\t ## Pressione enter para retornar ao menu.. ##");
                sc.nextLine();
                rotinaOpcaoPrincipal();
                break;
            default:
                imprimeCabecalho("             Pizzaria Nacoes             ");
                s.print("\n\n\t\t Opcao invalida!");
                s.print("\n\n\t\t Favor informar uma das opcoes do menu.");
                rotinaOpcaoPrincipal();
                break;
        }
    
    }
    
    public Cliente rotinaOpcaoCliente(String rotinaSolicitante, Cliente c, Pedido p){  
        opcao=exibeMenuCliente(rotinaSolicitante);
        switch(opcao){
            case 0://Sair do sistema
                sair();
                break;
            case 1://Inserir novo cadastro de cliente
                c.salvarInclusaoBancoDados();
                s.print("\n\n\t\t ## Pressione enter para retornar ao menu.. ##");
                sc.nextLine();
                rotinaOpcaoCliente(rotinaSolicitante,c,p);
                break;
            case 2://Alterar cadastro de cliente
                c.salvarEdicaoBancoDados();
                s.print("\n\n\t\t ## Pressione enter para retornar ao menu.. ##");
                sc.nextLine();
                rotinaOpcaoCliente(rotinaSolicitante,c,p);
                break;
            case 3://Pesquisar cliente
                if (rotinaSolicitante.equalsIgnoreCase("Pedido")){
                    return c=(c.pesquisaSeletiva());
                } else {
                    try{
                        if (c.imprimePesquisa(c.pesquisar()).isEmpty()){
                            s.print("\n\t\tNenhum registro encontrado!");
                        }
                    } catch(SQLException e){
                            ////Mostra exceção na tela em caso de erro de sql
                            System.err.println( e.getClass().getName() + ": " + e.getMessage());
                            }
                    s.print("\n\n\t\t ## Pressione enter para retornar ao menu.. ##");
                    sc.nextLine();
                    rotinaOpcaoCliente(rotinaSolicitante,c,p);
                }
                break; 
            case 4://Voltar
                if (rotinaSolicitante.equalsIgnoreCase("Pedido")){
                    rotinaOpcaoPedido(p);
                } else {
                    rotinaOpcaoPrincipal();
                }
                break;
            default:
                s.print("\n\n\t\t Opcao invalida!");
                s.print("\n\n\t\t Favor informar uma das opcoes do menu.");
                s.print("\n\n\t\t ## Pressione enter para retornar ao menu.. ##");
                sc.nextLine();
                rotinaOpcaoCliente(rotinaSolicitante, c, p);
                break;
        }
        return c;
    }
    
    public void rotinaOpcaoPizza (Pizza pi){
        opcao=exibeMenuPizza();
        
        switch (opcao) {          
            case 0://Sair do sistema
                sair();
                break;
            case 1://Inserir novo sabor de Pizza
                pi.salvarInclusaoBancoDados();
                s.print("\n\n\t\t ## Pressione enter para retornar ao menu.. ##");
                sc.nextLine();
                rotinaOpcaoPizza(pi);
                break;
            case 2://Alterar sabor de Pizza
                pi.salvarEdicaoBancoDados();
                s.print("\n\n\t\t ## Pressione enter para retornar ao menu.. ##");
                sc.nextLine();
                rotinaOpcaoPizza(pi);
                break;   
            case 3://Menu principal
                rotinaOpcaoPrincipal();
                break;
            default:
                imprimeCabecalho("   Pizzaria Nacoes - Cadastro de Pizzas  ");
                s.print("\n\n\t\t Opcao invalida!");
                s.print("\n\n\t\t Favor informar uma das opcoes do menu.");
                rotinaOpcaoPizza(pi);
                break;
        }
    }
    
    public void rotinaOpcaoPedido(Pedido p){
        opcao=exibeMenuPedido();
        
        switch (opcao) {
            case 0://Sair do sistema
                sair();
                break;
            case 1://Inserir Pedido
                p.salvarInclusaoBancoDados();
                s.print("\n\n\t\t ## Pressione enter para retornar ao menu.. ##");
                sc.nextLine();
                rotinaOpcaoPedido(p);
                break;
            case 2://Alterar Pedido
                p.salvarEdicaoBancoDados();
                s.print("\n\n\t\t ## Pressione enter para retornar ao menu.. ##");
                sc.nextLine();
                rotinaOpcaoPedido(p);
                break;
            case 3://Excluir Pedido
                p.salvarExclusaoBancoDados();
                s.print("\n\n\t\t ## Pressione enter para retornar ao menu.. ##");
                sc.nextLine();
                rotinaOpcaoPedido(p);
                break;
            case 4://Menu principal
                rotinaOpcaoPrincipal();
                break;
            default:
                imprimeCabecalho("  Pizzaria Nacoes - Cadastro de Pedidos  ");
                s.print("\n\n\t\t Opcao invalida!");
                s.print("\n\n\t\t Favor informar uma das opcoes do menu.");
                rotinaOpcaoPedido(p);
                break;
        }
    }
       
    //----Método de encerramento do sistema    
    public void sair(){
        //Sair do sistema
                imprimeCabecalho("             Pizzaria Nacoes             ");
                imprimeCabecalho("        O sistema sera encerrado!!       ");
                try {
                   Thread.sleep(2000);//Faz o console aguardar 2 segundos antes de fechar
                } catch (Exception e) {
                   e.printStackTrace();
                }
                System.exit(0);//encerra a execução
    }
}
