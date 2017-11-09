
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

//Entidade com os dados e metódos relacionados a um cliente

public class Cliente extends EntidadeBase {

    private String Nome;
    private String Telefone;
    private String Endereco;
    
    // -------- getters e setters -------- // 
    public String getNome() {
        return Nome;
    }

    public boolean setNome(String Nome) {
        if (Nome.length() > 30) {
            System.out.print("\n\n\t\t O nome do cliente pode ter, no máximo, 30 caracteres.\n"
                    + "\t\t O nome informado tem " + Nome.length() + " caracteres");
            return false;
        } else {
            this.Nome = Nome;
            return true;
        }
    }

    public String getTelefone() {
        return Telefone;
    }

    public boolean setTelefone(String Telefone) {
        if (Telefone.length() > 12) {
            System.out.print("\n\n\t\tO telefone do cliente pode ter, no máximo, 12 caracteres.\n"
                    + "\t\tO telefone informado tem " + Telefone.length() + " caracteres");
            return false;
        } else {
            this.Telefone = Telefone;
            return true;
        }
    }

    public String getEndereco() {
        return Endereco;
    }

    public boolean setEndereco(String Endereco) {
        if (Endereco.length() > 250) {
            System.out.print("\n\n\t\tO endereço do cliente pode ter, no máximo, 250 caracteres.\n"
                    + "\t\tO endereço informado tem " + Endereco.length() + " caracteres");
            return false;
        } else {
            this.Endereco = Endereco;
            return true;
        }
    }
    
    //-----------Outros Métodos-----------//
    
    //Método de pesquisa de clientes no banco de dados
    //retorna um Map de objetos do tipo cliente que estarão preenchidos com
    //os registros dos clientes localizados no banco. A key do map será preenchida com o código do cliente no banco de dados
    public Map <Integer, Cliente> pesquisar () throws SQLException{
       
        Map <Integer, Cliente> clientesPesquisados = new LinkedHashMap<Integer, Cliente>(); 
        //Declaração do Map que armazenará os clientes pesquisados
        //os códigos do clientes serão armazenados nas keys dos registros

        String tipoPesquisa;
        if ((tipoPesquisa=tipoPesquisa()).equalsIgnoreCase("0")){
            //A string tipoPesquisa conterá a clausula where da select
            //porém se usuário cancelou a pesquisa (digitou 0 quando tipo pesquisa() foi executado), não prossegue
           //retorna ao método que chamou a pesquisa. 
            return clientesPesquisados;
        }
        
        StringBuilder sql = new StringBuilder("select * from clientes ");
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

            Cliente c = new Cliente();//criada variável de referencia para um objeto do tipo Cliente, que será incluido no Map
            Integer codigo = rs.getInt("codigo");
            c.setCodigo(rs.getInt("codigo"));
            c.setEndereco(rs.getString("endereco"));
            c.setNome(rs.getString("nome"));
            c.setTelefone(rs.getString("telefone"));
            
            //Os métodos get do ResultSet 'rs' recebem como parâmetro
            //o nome de uma coluna da linha em que rs está posicionado e retorna o valor desta coluna.
            //Os valores foram armazenados nos atributos do objeto que sera adicionado ao map
            
            clientesPesquisados.put(codigo, c);
            //Aravés do método put cada objeto cliente é indexado pelo seu codigo no LinkedHashMap
        }
               
        //O map de clientes é retornado para quem chamou o pesquisar
        return clientesPesquisados;
    }
    
    //Método para selecão da coluna e tipo de ordenacao da consulta que será gerada em sql
    public String tipoPesquisa(){
        PrintStream s = System.out;
        Scanner sc = new Scanner(System.in);
        Menu menu = new Menu();
        String tipoPesquisa;
        String telefonePesquisar;
        String nomePesquisar;
             
        do{
            menu.imprimeCabecalho(" Pizzaria Nacoes - Pesquisa de Clientes  ");
            s.print("\n\n\t\t Atalhos para os tipos de pesquisa:");
            s.print("\n\t\t T - Telefone");
            s.print("\n\t\t NC - Nomes ordem crescente");
            s.print("\n\t\t ND - Nomes ordem descrescente");
            s.print("\n\t\t 0 - Cancelar pesquisa");
            s.print("\n\t\t#Informe a sigla de pesquisa ou zero '0' para cancelar: ");
            tipoPesquisa=sc.nextLine();
            
            //Se for zero cancela a pesquisa, retorna para o método que chamou
            if(tipoPesquisa.equalsIgnoreCase("0")){ return tipoPesquisa;}
            
        //Compara as strings ignorando se estão em uppercase ou lowcase
        } while((!(tipoPesquisa.equalsIgnoreCase("T")))&& (!(tipoPesquisa.equalsIgnoreCase("ND")))&& (!(tipoPesquisa.equalsIgnoreCase("NC"))) && (!(tipoPesquisa.equalsIgnoreCase("0"))));
        
        menu.imprimeCabecalho(" Pizzaria Nacoes - Pesquisa de Clientes  ");
        
        //Checa o tipo de pesquisa para solicitar o filtro de pesquisa e montar o argumento da clausula where
        if (tipoPesquisa.equalsIgnoreCase("T")){
            s.print("\n\n\t\t#Informe o telefone completo, parcial ou * para todos: ");
            telefonePesquisar=sc.nextLine();
            
            if (!telefonePesquisar.equalsIgnoreCase("*")){
                    tipoPesquisa="where telefone ilike '%"+telefonePesquisar+"%' order by telefone desc;";
                } else {
                    tipoPesquisa="order by telefone desc;";
                    }
        } else {
            s.print("\n\n\t\t#Informe o nome completo, parcial ou * para todos : ");
            nomePesquisar=sc.nextLine();
            
            if(tipoPesquisa.equalsIgnoreCase("ND")){
                
                if (!nomePesquisar.equalsIgnoreCase("*")){
                    tipoPesquisa="where nome ilike '%"+nomePesquisar+"%' order by nome desc;";
                } else {
                    tipoPesquisa="order by nome desc;";
                    }
                
            } else {
                
                if (!nomePesquisar.equalsIgnoreCase("*")){
                    tipoPesquisa="where nome ilike '%"+nomePesquisar+"%' order by nome asc;";
                } else {
                    tipoPesquisa="order by nome asc;";
                    }
                }
            
            }
        
        return tipoPesquisa;//retorna a string do tipo de pesquisa
    }
    
    //Método de impressão dos resultados da pesquisa a cliente no banco
    public Map <Integer, Cliente> imprimePesquisa(Map <Integer, Cliente> listaRegistros){
        PrintStream s = System.out;
        Menu menu = new Menu();
        menu.imprimeCabecalho(" Pizzaria Nacoes - Pesquisa de Clientes  ");
        //O for abaixo percorre a lista de registros pelo codigo que é a key do map 
        //e imprime em tela os valores do objeto cliente que estão relacionadas a cada key
        for (Integer codigo: listaRegistros.keySet()){
            Cliente c = listaRegistros.get(codigo);//acessando o objeto pela key usando o metodo get do map
            s.print(c.toString());//Imprimindo dados do registro
        }
        return listaRegistros;//retorna o map de registros a quem chamou o método imprimePesquisa
    }
    
    //Método de pesquisa no banco de dados que retorna um unico objeto
    public Cliente pesquisaSeletiva(){
        PrintStream s = System.out;
        Scanner sc = new Scanner (System.in);
        Map <Integer, Cliente> dados = null;
       
        try{
            dados = pesquisar();
            //Variável dados armazenará o Map de registros retornado pelo metodo pesquisar()
            
        }catch(SQLException e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        if (!dados.isEmpty()){
                //se o map que foi passado pelo método pesquisar() não estiver vazio
                imprimePesquisa(dados);//Imprime os dados da pesquisa
            
                s.print("\n\n\t\t Informe o codigo do registro que deseja \n\t\t selecionar ou '0' para cancelar:");
                //Variável que armazenra o codigo do registro que sera alterado
                int codigoRegistro=Integer.parseInt(sc.nextLine());//leitura do código do registro que deve ser alterado;
                
                while ( dados.get(codigoRegistro) == null && codigoRegistro != 0){ 
                    //O método get do map dados retornará null enquanto não encontrar um 
                    //registro que a key corresponda ao valor passado como parâmetro, no caso a variável codigoRegistro.
                    //Enquanto não encontrar o registro no map dados ou o usuario 
                    //não digitou zero, solicita a digitacao de um codigo existente na pesquisa
                    s.print("\n\n\t\t Registro nao encontrado na consulta.");
                    s.print("\n\n\t\t Informe o codigo do registro presente na pesquisa");
                    s.print("\n\t\t e que deseja alterar ou informe zero '0' para cancelar: ");
                    codigoRegistro=Integer.parseInt(sc.nextLine());
                    
                }
                if (codigoRegistro != 0){
                    return dados.get(codigoRegistro);//Se não foi digitado zero, retorna o objeto correspondente
                }
            }
        s.print("\n\t\tNenhum cliente encontrado/selecionado.");
        return null;//Se usuário cancelou(digitou zero), retorna nulo para quem chamou a pesquisa
    }
    // -------- Métodos sobrepostos -------- // 
    
    //Método para entrada dos dados do novo cadastro de cliente
    public void solicitaDados(){
        PrintStream s = System.out;
        Scanner sc = new Scanner(System.in);
        Menu menu = new Menu();
        menu.imprimeCabecalho(" Pizzaria Nacoes - Cadastro de Clientes  ");
        do {
            s.print("\n\n\t\t#Informe o nome do cliente: ");
        } while (!(setNome(sc.nextLine())));
        
        do {
            s.print("\n\n\t\t#Informe o endereco do cliente: ");
        } while (!(setEndereco(sc.nextLine())));
        
        do {
            s.print("\n\n\t\t#Informe o telefone do cliente: ");
        } while (!(setTelefone(sc.nextLine())));
        
    }
    
    //Salva um cliente no banco de dados
    public void salvarInclusaoBancoDados() {
        solicitaDados();
        //Com o objeto preenchido pela solicitaDados()
        //como todos os dados são string, foram diretamente concatenados na stringBuilder declarada abaixo    
        
        StringBuilder sql = new StringBuilder("insert into clientes (endereco,nome,telefone) values (");
        //StringBuilder permitirá concatenar os valores dos atributos do objeto para a instrucao sql
        
        sql.append("'"+getEndereco()+"'"+",");
        sql.append("'"+getNome()+"'"+",");
        sql.append("'"+getTelefone()+"'"+");");
        
        //Abaixo ocorre a chamada ao método inserteDeleteBanco() passando a string que
        //contém a instrução sql. Este método trata-se de um método 
        //genérico para execução de insert,updates e deletes nas tabelas do banco
        insertDeleteBanco(sql.toString());
    }

    //Edita um Cliente no banco de dados, após pesquisa e seleção do registro
    public void salvarEdicaoBancoDados(){
        PrintStream s = System.out;
        Scanner sc = new Scanner (System.in);
        int codigoRegistro; //Variável que armazenra o codigo do registro que sera alterado
        
        try {
            Map <Integer, Cliente> dados = pesquisar();
            //Variável dados armazenará o Map de registros retornado pelo metodo pesquisar()
            
            if (!dados.isEmpty()){
                //se o map que foi passado pelo método pesquisar() não estiver vazio
                imprimePesquisa(dados);//Imprime os dados da pesquisa
            
                s.print("\n\n\t\t Informe o codigo do registro que deseja \n\t\t alterar ou zero '0' para cancelar:");
                
                codigoRegistro=Integer.parseInt(sc.nextLine());//leitura do código do registro que deve ser alterado;
                
                while ( dados.get(codigoRegistro) == null && codigoRegistro != 0){ 
                    //O método get do map dados retornará null enquanto não encontrar um 
                    //registro que a key corresponda ao valor passado como parâmetro, no caso a variável codigoRegistro.
                    //Enquanto não encontrar o registro no map dados ou o usuario 
                    //não digitou zero, solicita a digitacao de um codigo existente na pesquisa
                    s.print("\n\n\t\t Registro nao encontrado na consulta.");
                    s.print("\n\n\t\t Informe o codigo do registro presente na pesquisa");
                    s.print("\n\t\t\t e que deseja alterar ou informe zero '0' para cancelar: ");
                    codigoRegistro=Integer.parseInt(sc.nextLine());//leitura do código do registro que deve ser alterado;
                    
                }

                if (codigoRegistro != 0) {
                    //Se usuário não digitou zero e o registro foi encontrado no map, irá construir a instrução sql do update

                    StringBuilder sql = new StringBuilder("update clientes set ");
                    //StringBuilder armazenará os atributos do objeto para a instrucao sql

                    solicitaDados();
                    //Após solicitar ao usuário os novos valores do registro, irá concatenar os dados em sql
                    sql.append("nome ='"+getNome()+"',");
                    sql.append("endereco ='"+getEndereco()+"',");
                    sql.append("telefone ='"+getTelefone()+"'");
                    sql.append(" where codigo ="+codigoRegistro+";");

                    //Chama o método da superclasse entidade base passando a string da instrução sql de update como parâmetro
                    insertDeleteBanco(sql.toString());
                }
            } 
            s.print("\n\t\tNenhum cliente encontrado/selecionado.");
        } catch (SQLException e){
            //Mostra exceção na tela em caso de erro de sql
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
            }
        //Se digitou zero voltará para o menu
    }

    //Retorna os dados de um cliente em String
    public String toString() {

        return "\n\n\t\t ========== CLIENTE: "+super.getCodigo()
                +"\n\t\t=> Nome: "+getNome()
                +"\n\t\t=> Telefone: "+getTelefone()
                +"\n\t\t=> Endereco de entrega: "+getEndereco();
    }
    
    
}
