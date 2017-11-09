
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

//Entidade com os dados de uma pizza

public class Pizza extends EntidadeBase {

    private String Sabor;
    private double Preco;

    // ------- getters e setters ------- //
    public String getSabor() {
        return Sabor;
    }

    public boolean setSabor(String Sabor) {
        if (Sabor.length() > 50) {
            System.out.print("\nO sabor da pizza pode ter, no máximo, 50 caracteres.\n"
                    + "O sabor informado tem " + Sabor.length() + " caracteres");
            return false;
        } else {
            this.Sabor = Sabor;
            return true;
        }
    }

    public double getPreco() {
        return Preco;
    }

    public boolean setPreco(double Preco) {
        if (Preco <= 0) {
            System.out.print("\nO preço da pizza deve ser maior que 0 (zero).\nPreço informado: "
                    + String.format("%.2f", Preco));
            return false;
        } else {
            this.Preco = Preco;
            return true;
        }
    }
    
    //-----------Outros Métodos-----------//
    
    //Método de pesquisa de Pizzas no banco de dados
    //retorna um Map de objetos do tipo Pizza que estarão preenchidos com
    //os registros das Pizzas localizadas no banco. A key do map será preenchida com o código da Pizza no banco de dados
    public Map <Integer, Pizza> pesquisar () throws SQLException{
       
        Map <Integer, Pizza> pizzasPesquisadas = new LinkedHashMap<Integer, Pizza>(); 
        //Declaração do Map que armazenará as Pizzas pesquisadas
        //os códigos das Pizzas serão armazenados nas keys dos registros

        String tipoPesquisa;
        if ((tipoPesquisa=tipoPesquisa()).equalsIgnoreCase("0")){
            //A string tipoPesquisa conterá a clausula where da select
            //porém se usuário cancelou a pesquisa (digitou 0 quando tipo pesquisa() foi executado), não prossegue
           //retorna ao método que chamou a pesquisa. 
            return pizzasPesquisadas;
        }
        
        StringBuilder sql = new StringBuilder("select * from pizzas ");
        sql.append(tipoPesquisa);
        //Foi utilizado o tipo StringBuilder para a variável onde será armazenado o texto da query
        //Este tipo possui o método append que permite a concatenação de strings
        //de forma mais simples do que pela atribuição comum
        
        ResultSet rs = selectBanco(sql.toString());
        //A variável rs é do tipo ResultSet e armazenará o retorno do método selectBanco()
        //o método selectBanco() recebeu como parâmetro a váriavel sql que contém a string da select que deve ser feita
        
        
        while (rs.next()){
            //Na primeira execução do método next() do objeto do tipo ResultSet ele irá se posicionar no primeiro registro
            //que está armazenado em sua coleção/lista
            //Este método next() retorna True enquanto ainda houver registros a serem acessados.
            //Estes registros são as linhas resultantes da select, na sequência em que retornaram do banco,
            //ou seja, de acordo a ordenação da query, se houve especificação.

            Pizza p = new Pizza();//criada variável de referencia para um objeto do tipo Pizza, que será incluido no Map
            Integer codigo = rs.getInt("codigo"); 
            p.setCodigo(rs.getInt("codigo"));
            p.setSabor(rs.getString("sabor"));
            p.setPreco(rs.getDouble("preco"));

            //Os métodos get do ResultSet 'rs' recebem como parâmetro
            //o nome de uma coluna da linha em que rs está posicionado e retorna o valor desta coluna.
            //Os valores foram armazenados nos atributos do objeto que sera adicionado ao map
            
            pizzasPesquisadas.put(codigo, p);
            //Aravés do método put cada objeto Pizza é indexado pelo seu codigo no LinkedHashMap
        }
        
        //O map de Pizzas é retornado para quem chamou o pesquisar
        return pizzasPesquisadas;
    }
    
    //Método para selecão da coluna e tipo de ordenacao da consulta que será gerada em sql
    public String tipoPesquisa(){
        PrintStream s = System.out;
        Scanner sc = new Scanner(System.in);
        Menu menu = new Menu();
        String tipoPesquisa;
        String precoPesquisar;
        String saborPesquisar;
             
        do{
            menu.imprimeCabecalho("   Pizzaria Nacoes - Pesquisa de Pizzas  ");
            s.print("\n\t\t Atalhos para os tipos de pesquisa:");
            s.print("\n\t\t PC - Preco ordem crescente");
            s.print("\n\t\t PD - Preco ordem descrescente");
            s.print("\n\t\t SC - Sabor ordem crescente");
            s.print("\n\t\t SD - Sabor ordem descrescente");
            s.print("\n\t\t 0 - Cancelar pesquisa");
            s.print("\n\t\t#Informe a sigla de pesquisa ou zero '0' para cancelar: ");
            tipoPesquisa=sc.nextLine();
            
            //Se for zero cancela a pesquisa, retorna para o método que chamou
            if(tipoPesquisa.equalsIgnoreCase("0")){ return tipoPesquisa;}
            
        //Compara as strings ignorando se estão em uppercase ou lowcase
        } while((!(tipoPesquisa.equalsIgnoreCase("PC")))&& (!(tipoPesquisa.equalsIgnoreCase("PD")))&& (!(tipoPesquisa.equalsIgnoreCase("SC"))) && (!(tipoPesquisa.equalsIgnoreCase("SD"))) && (!(tipoPesquisa.equalsIgnoreCase("0"))));
        
        menu.imprimeCabecalho("   Pizzaria Nacoes - Pesquisa de Pizzas  ");
        
        //Checa o tipo de pesquisa para solicitar o filtro de pesquisa e montar o argumento da clausula where
        if (tipoPesquisa.equalsIgnoreCase("PC")){
            s.print("\n\t\t#Informe o preco maximo ou * para todos : ");
            precoPesquisar=(sc.nextLine().replace(',','.'));
            if (!precoPesquisar.equalsIgnoreCase("*")){
                tipoPesquisa="where preco<="+precoPesquisar+" order by preco asc;";
            } else {
                tipoPesquisa="order by sabor asc;";
            }
        }
        
       if (tipoPesquisa.equalsIgnoreCase("PD")){
            s.print("\n\t\t#Informe o preco maximo ou * para todos : ");
            precoPesquisar=(sc.nextLine().replace(',','.'));
            if (!precoPesquisar.equalsIgnoreCase("*")){
                tipoPesquisa="where preco<="+precoPesquisar+" order by preco desc;";
            } else {
                tipoPesquisa="order by preco desc;";
            }
        }
        
        if(tipoPesquisa.equalsIgnoreCase("SC")){
            s.print("\n\t\t#Informe o sabor completo, parcial ou * para todos : ");
            saborPesquisar=sc.nextLine();
            if (!saborPesquisar.equalsIgnoreCase("*")){
                tipoPesquisa="where sabor ilike '%"+saborPesquisar+"%' order by sabor asc;";
            } else {
                tipoPesquisa="order by sabor asc;";
            }
        }

        if(tipoPesquisa.equalsIgnoreCase("SD")){
            s.print("\n\t\t#Informe o sabor completo, parcial ou * para todos : ");
            saborPesquisar=sc.nextLine();
            if (!saborPesquisar.equalsIgnoreCase("*")){
                tipoPesquisa="where sabor ilike '%"+saborPesquisar+"%' order by sabor desc;";
            } else {
                tipoPesquisa="order by sabor desc;";
            }
        }
        
        return tipoPesquisa;//retorna a string do tipo de pesquisa
    }
    
    //Método de impressão dos resultados da pesquisa a Pizza no banco
    public Map <Integer, Pizza> imprimePesquisa(Map <Integer, Pizza> listaRegistros){
        PrintStream s = System.out;
        Menu menu = new Menu();
        menu.imprimeCabecalho("   Pizzaria Nacoes - Pesquisa de Pizzas  ");
        //O for abaixo percorre a lista de registros pelo codigo que é a key do map 
        //e imprime em tela os valores do objeto Pizza que estão relacionadas a cada key
        for (Integer codigo: listaRegistros.keySet()){
            Pizza pizzas = listaRegistros.get(codigo);//acessando o objeto pela key usando o metodo get do map
            s.print(pizzas.toString());//Imprimindo dados do registro
        }
        return listaRegistros;//retorna o map de registros a quem chamou o método imprimePesquisa
    }
    
//Método de pesquisa no banco de dados que retorna um unico objeto
    public Pizza pesquisaSeletiva(){
        PrintStream s = System.out;
        Scanner sc = new Scanner (System.in);
        Map <Integer, Pizza> dados = null;
       
        try{
            dados = pesquisar();
            //Variável dados armazenará o Map de registros retornado pelo metodo pesquisar()
            
        }catch(SQLException e){
            //Caso ocorra exceção, exibe mensagem
            System.out.print("\n\n\n\t\t ## Operaçao não pode ser realizada. ##");
            //Mostra exceção na tela em caso de erro de sql
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        if (!dados.isEmpty()){
                //se o map que foi passado pelo método pesquisar() não estiver vazio
                imprimePesquisa(dados);//Imprime os dados da pesquisa
            
                s.print("\n\n\t\t Informe o codigo do registro que deseja \n\t\t selecionar ou '0' para cancelar:");
                //Variável que armazenra o codigo do registro que sera alterado
                int codigoRegistro=Integer.parseInt((sc.nextLine()));//leitura do código do registro que deve ser alterado;
                
                while ( dados.get(codigoRegistro) == null && codigoRegistro != 0){ 
                    //O método get do map dados retornará null enquanto não encontrar um 
                    //registro que a key corresponda ao valor passado como parâmetro, no caso a variável codigoRegistro.
                    //Enquanto não encontrar o registro no map dados ou o usuario 
                    //não digitou zero, solicita a digitacao de um codigo existente na pesquisa
                    s.print("\n\n\t\t Registro nao encontrado na consulta.");
                    s.print("\n\n\t\t Informe o codigo do registro presente na pesquisa");
                    s.print("\n\t\t e que deseja alterar ou informe zero '0' para cancelar: ");
                    codigoRegistro=Integer.parseInt((sc.nextLine()));
                    
                }
                if (codigoRegistro != 0){
                    return dados.get(codigoRegistro);//Se não foi digitado zero, retorna o objeto correspondente
                }
            }
        s.print("\n\t\tNenhuma pizza encontrada/selecionada.");
        return null;//Se usuário cancelou(digitou zero), retorna nulo para quem chamou a pesquisa
    }
    // -------- Métodos sobrepostos -------- // 
    
    //Método abstrato solicita dados para popular objeto das entidades filhas para insert banco
    public void solicitaDados(){
        PrintStream s = System.out;
        Scanner sc = new Scanner(System.in);
        Menu menu = new Menu();
        menu.imprimeCabecalho("   Pizzaria Nacoes - Cadastro de Pizzas  ");
        
        do {
            
            s.print("\n\n\t\t#Informe o sabor da pizza: ");
        } while (!(setSabor(sc.nextLine())));
        
        do {
            
            s.print("\n\n\n\t\t#Informe o preco da pizza: ");
        } while (!(setPreco(Double.parseDouble((sc.nextLine().replace(',','.'))))));
    }
    
    //Salva uma Pizza no banco de dados
    public void salvarInclusaoBancoDados() {
        
        solicitaDados();
        //Com o objeto preenchido pela solicitaDados()
        //como todos os dados são string, foram diretamente concatenados na stringBuilder declarada abaixo    
        
        StringBuilder sql = new StringBuilder("insert into pizzas (sabor,preco) values (");
        //StringBuilder permitirá concatenar os valores dos atributos do objeto para a instrucao sql
        
        sql.append("'"+getSabor()+"'"+",");
        sql.append(getPreco()+");");
        
        //Abaixo ocorre a chamada ao método inserteDeleteBanco() passando a string que
        //contém a instrução sql. Este método trata-se de um método 
        //genérico para execução de insert,updates e deletes nas tabelas do banco
        insertDeleteBanco(sql.toString());
    }

    //Edita uma Pizza no banco de dados, após pesquisa e seleção do registro
    public void salvarEdicaoBancoDados(){
        PrintStream s = System.out;
        Scanner sc = new Scanner (System.in);
        int codigoRegistro; //Variável que armazenará o codigo do registro que sera alterado
        
        try {
            Map <Integer, Pizza> dados = pesquisar();
            //Variável dados armazenará o Map de registros retornado pelo metodo pesquisar()
            
            if (!dados.isEmpty()){
                //se o map que foi passado pelo método pesquisar() não estiver vazio
                imprimePesquisa(dados);//Imprime os dados da pesquisa
            
                s.print("\n\n\t\t Informe o codigo do registro que deseja \n\t\t alterar ou zero '0' para cancelar:");
                codigoRegistro=Integer.parseInt((sc.nextLine()));//leitura do código do registro que deve ser alterado;
                
                while ( dados.get(codigoRegistro) == null && codigoRegistro != 0){ 
                    //O método get do map dados retornará null enquanto não encontrar um 
                    //registro que a key corresponda ao valor passado como parâmetro, no caso a variável codigoRegistro.
                    //Enquanto não encontrar o registro no map dados ou o usuario 
                    //não digitou zero, solicita a digitacao de um codigo existente na pesquisa
                    s.print("\n\n\t\t Registro nao encontrado na consulta.");
                    s.print("\n\n\t\t Informe o codigo do registro presente na pesquisa");
                    s.print("\n\t\t\t e que deseja alterar ou informe zero '0' para cancelar: ");
                    codigoRegistro=Integer.parseInt((sc.nextLine()));
                }

                if (codigoRegistro != 0) {
                    //Se usuário não digitou zero e o registro foi encontrado no map, irá construir a instrução sql do update

                    StringBuilder sql = new StringBuilder("update pizzas set ");
                    //StringBuilder armazenará os atributos do objeto para a instrucao sql
        
                    solicitaDados();
                    //Após solicitar ao usuário os novos valores do registro, irá concatenar os dados em sql
                    sql.append("sabor ='"+getSabor()+"',");
                    sql.append("preco ="+getPreco());
                    sql.append(" where codigo ="+codigoRegistro+";");

                    //Chama o método da superclasse entidade base passando a string da instrução sql de update como parâmetro
                    insertDeleteBanco(sql.toString());
                }
            } 
            s.print("\n\t\tNenhuma pizza encontrada/selecionada.");
        } catch (SQLException e){
            //Mostra exceção na tela em caso de erro de sql
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
            }
        //Se digitou zero voltará para o menu
    }
    
    //Retorna os dados de uma pizza
    public String toString() {

        return "\n\t\t ========== PIZZA: "+super.toString()
                +"\n\t\t=> Sabor: "+getSabor()
                +"\n\t\t=> Preco: "+getPreco();
    }
    
}
