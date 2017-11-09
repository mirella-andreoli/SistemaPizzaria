
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

//Entidade com os dados de um pedido

public class Pedido extends EntidadeBase {

    SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");

    public Pedido() {
        this.ListaPizzas = new ArrayList<>();
        this.Data = new Date(System.currentTimeMillis());
    }

    private Cliente ClientePedido;
    private ArrayList<Pizza> ListaPizzas;
    private String ObservacoesPizza;
    private double TaxaEntrega;
    private double Total;
    private double ValorCartao;
    private double ValorDinheiro;
    private Date Data;

    // ------- getters e setters ------- //
    public Date getData(){
        return Data;
    }
    
    public void setData(Date data){
        Data= data;
    }
    
    public String getObservacoesPizza() {
        return ObservacoesPizza;
    }

    public boolean setObservacoesPizza(String ObservacoesPizza) {
        if (ObservacoesPizza.length() > 200) {
            System.out.print("\nAs observações podem ter, no máximo, 200 caracteres.\n"
                    + "As observações informadas têm " + ObservacoesPizza.length() + " caracteres");
            return false;
        } else {
            this.ObservacoesPizza = ObservacoesPizza;
            return true;
        }
    }

    public double getTaxaEntrega() {
        return TaxaEntrega;
    }

    public boolean setTaxaEntrega(double TaxaEntrega) {
        if (TaxaEntrega < 0) {
            System.out.print("\nA taxa de entrega deve ser maior ou igual a 0 (zero).\nTaxa informada: "
                    + String.format("%.2f", TaxaEntrega));
            return false;
        } else {
            this.TaxaEntrega = TaxaEntrega;
            return true;
        }
    }

    public double getTotal() {
        return Total + getTaxaEntrega();
    }

    public double getValorCartao() {
        return ValorCartao;
    }

    public boolean setValorCartao(double ValorCartao) {
        if (ValorCartao < 0) {
            System.out.print("\nO valor recebido em cartão deve ser maior ou igual a 0 (zero).\nValor informado: "
                    + String.format("%.2f", ValorCartao));
            return false;
        } else {
            this.ValorCartao = ValorCartao;
            return true;
        }
    }

    public double getValorDinheiro() {
        return ValorDinheiro;
    }

    public boolean setValorDinheiro(double ValorDinheiro) {
        if (ValorDinheiro < 0) {
            System.out.print("\nO valor em dinheiro recebido deve ser maior ou igual a 0 (zero).\nValor informado: "
                    + String.format("%.2f", ValorDinheiro));
            return false;
        } else {
            this.ValorDinheiro = ValorDinheiro;
            return true;
        }
    }

    public void addPizza(Pizza PizzaAdicional) {
        this.ListaPizzas.add(PizzaAdicional);
        this.Total += PizzaAdicional.getPreco();
    }

    public void removerPizza(Pizza PizzaAdicional, int indice) {
        this.ListaPizzas.remove(indice);
        this.Total -= PizzaAdicional.getPreco();
    }

    public ArrayList<Pizza> getListaPizzas() {
        return ListaPizzas;
    }
    
    public void setListasPizza(ArrayList<Pizza> listaPizzas){
        ListaPizzas= listaPizzas;
    }

    public Cliente getClientePedido() {
        return ClientePedido;
    }

    public void setClientePedido(Cliente ClientePedido) {
        this.ClientePedido = ClientePedido;
    }
    
// -------- Métodos de pesquisa -------- //
    public Map <Integer, Pedido> pesquisar (String rotinaSolicitante) throws SQLException{
       
        Map <Integer, Pedido> pedidosPesquisados = new LinkedHashMap<Integer, Pedido>(); 
        //Declaração do Map que armazenará os pedidos pesquisados
        //os códigos do pedidos serão armazenados nas keys dos registros

        String tipoPesquisa;
        if ((tipoPesquisa=tipoPesquisa(rotinaSolicitante)).equalsIgnoreCase("0")){
            //A string tipoPesquisa conterá a clausula where da select
            //porém se usuário cancelou a pesquisa (digitou 0 quando tipo pesquisa() foi executado), não prossegue
           //retorna ao método que chamou a pesquisa. 
            return pedidosPesquisados;
        }
        
        StringBuilder sql;
        sql = new StringBuilder("select \n" +
                "pedidos.codigo,\n" +
                "pedidos.codigocliente,\n" +
                "(select (dia ||'/'||mes||'/'||ano) from (select EXTRACT(DAY FROM pedidos.data) as dia, EXTRACT(MONTH FROM pedidos.data) as mes, EXTRACT(YEAR FROM pedidos.data) as ano) as result) as data,\n" +
                "pedidos.observacoespizza,\n" +
                "pedidos.taxaentrega,\n" +
                "pedidos.total,\n" +
                "pedidos.valorcartao,\n" +
                "pedidos.valordinheiro,\n" +
                "clientes.nome,\n" +
                "clientes.telefone,\n" +
                "clientes.endereco\n" +
                "from pedidos\n" +
                "inner join clientes on pedidos.codigocliente=clientes.codigo ");
        
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
            Cliente c = new Cliente();//criada variável de referencia para um objeto do tipo cliente, que armazenara os dados do cliente do pedido
            Pedido p = new Pedido();//criada variável de referencia para um objeto do tipo Pedido, que será incluido no Map
            Date dataPedido = new Date();
            Integer codigo = rs.getInt("codigo"); 
            p.setCodigo(rs.getInt("codigo"));
            c.setCodigo(rs.getInt("codigocliente"));
            c.setNome(rs.getString("nome"));
            c.setEndereco(rs.getString("endereco"));
            c.setTelefone(rs.getString("telefone"));
            p.setClientePedido(c);
            try{
            dataPedido=sd.parse(rs.getString("data"));
            } catch(ParseException e){
                
            }
            p.setData(dataPedido);
            p.setObservacoesPizza(rs.getString("observacoespizza"));
            p.setTaxaEntrega(rs.getDouble("taxaentrega"));
            p.setValorCartao(rs.getDouble("valorcartao"));
            p.setValorDinheiro(rs.getDouble("valordinheiro"));
            //Os métodos get do ResultSet 'rs' recebem como parâmetro
            //o nome de uma coluna da linha em que rs está posicionado e retorna o valor desta coluna.
            //Os valores foram armazenados nos atributos do objeto que sera adicionado ao map
            
            Map <Integer, Pizza> pedidosItensPesquisados = new LinkedHashMap<Integer, Pizza>();
            
            pedidosItensPesquisados=pesquisarItensPedidos(codigo);//Pesquisa os itens do pedidos
            int itens=pedidosItensPesquisados.size();//pega o tamanho do map de itens, que será a quantidade de itens
            while (pedidosItensPesquisados.containsKey(itens)){        
                p.addPizza(pedidosItensPesquisados.get(itens));
                //adiciona cada item no pedido
                itens-=1;//decrementa a quantidade
            }
            
            pedidosPesquisados.put(codigo, p);//adiciona o pedido no map de pedidos
            //Aravés do método put cada objeto pedido é indexado pelo seu codigo no LinkedHashMap
        }
        
        //O map de pedidos é retornado para quem chamou o pesquisar
        return pedidosPesquisados;
    }
    
    public Map<Integer, Pizza> pesquisarItensPedidos(int codPed) throws SQLException{
        
        Map <Integer, Pizza> pedidosItensPesquisados = new LinkedHashMap<Integer, Pizza>(); 
        //Declaração do Map que armazenará os pedidos pesquisados
        //os códigos do pedidos serão armazenados nas keys dos registros
        
        StringBuilder sql = new StringBuilder("select pedidositens.*,pizzas.sabor\n" +
                                                "from pedidos\n" +
                                                "inner join pedidositens on pedidos.codigo=pedidositens.codigopedido\n" +
                                                "inner join pizzas on pedidositens.codigopizza=pizzas.codigo\n" +
                                                "where pedidos.codigo="+codPed+" order by pizzas.sabor asc;");
        //Foi utilizado o tipo StringBuilder para a variável onde será armazenado o texto da query
        //Este tipo possui o método append que permite a concatenação de strings
        //de forma mais simples do que pela atribuição comum
        
        ResultSet rs = selectBanco(sql.toString());
        //A variável rs é do tipo ResultSet e armazenará o retorno do método selectBanco()
        //o método selectBanco() recebeu como parâmetro a váriavel sql que contém a string da select que deve ser feita
        Integer codigo=0;
        while (rs.next()){
            //Na primeira execução do método next() do objeto do tipo ResultSet ele irá se posicionar no primeiro registro
            //que está armazenado em sua coleção/lista
            //Este método next() retorna True enquanto ainda houver registros a serem acessados.
            //Estes registros são as linhas resultantes da select, na sequência em que retornaram do banco,
            //ou seja, de acordo a ordenação da query, se houve especificação.
            Pizza pizzaItem = new Pizza();
            codigo +=1;
            pizzaItem.setCodigo(rs.getInt("codigopizza"));
            pizzaItem.setPreco(rs.getDouble("valorpizza"));
            pizzaItem.setSabor(rs.getString("sabor"));
            //Os métodos get do ResultSet 'rs' recebem como parâmetro
            //o nome de uma coluna da linha em que rs está posicionado e retorna o valor desta coluna.
            //Os valores foram armazenados nos atributos do objeto que sera adicionado ao map
            
            pedidosItensPesquisados.put(codigo, pizzaItem);
            //Aravés do método put, cada item do pedido é indexado pelo seu codigo no LinkedHashMap
        }
        //O map de itens é retornado para quem chamou o pesquisar
        return pedidosItensPesquisados;
    }
    
    //Método para selecão da coluna e tipo de ordenacao da consulta que será gerada em sql
    public String tipoPesquisa(String rotinaSolicitante){
        PrintStream s = System.out;
        Scanner sc = new Scanner(System.in);
        Menu menu = new Menu();
        String tipoPesquisa;
        String saborPesquisar;
        String nomePesquisar;
        String dataPesquisar;
        
        if (rotinaSolicitante.equalsIgnoreCase("Pedido")){
            //Se a rotina que chamou foi o pedido
            do{
                menu.imprimeCabecalho("  Pizzaria Nacoes - Pesquisa de Pedidos  ");
                s.print("\n\t\t Atalhos para os tipos de pesquisa:");
                s.print("\n\t\t C - Cliente");
                s.print("\n\t\t SP - Sabor de pizza");
                s.print("\n\t\t D - Data dos pedidos");
                s.print("\n\t\t 0 - Cancelar pesquisa");
                s.print("\n\t\t#Informe a sigla de pesquisa ou zero '0' para cancelar: ");
                tipoPesquisa=sc.nextLine();

                //Se for zero cancela a pesquisa, retorna para o método que chamou
                if(tipoPesquisa.equalsIgnoreCase("0")){ return tipoPesquisa;}

            //Compara as strings ignorando se estão em uppercase ou lowcase
            } while((!(tipoPesquisa.equalsIgnoreCase("C")))&& (!(tipoPesquisa.equalsIgnoreCase("SP")))&& (!(tipoPesquisa.equalsIgnoreCase("D"))) && (!(tipoPesquisa.equalsIgnoreCase("0"))));
            
            menu.imprimeCabecalho("  Pizzaria Nacoes - Pesquisa de Pedidos  ");
            
                //Checa o tipo de pesquisa para solicitar o filtro de pesquisa e montar o argumento da clausula where
            if (tipoPesquisa.equalsIgnoreCase("C")){
                s.print("\n\t\t#Informe o nome do cliente ou * para todos: ");
                nomePesquisar=sc.nextLine();
                if (!nomePesquisar.equalsIgnoreCase("*")){
                        tipoPesquisa="where clientes.nome ilike '%"+nomePesquisar+"%' order by clientes.nome asc;";
                    } else {
                        tipoPesquisa="order by clientes.nome asc;";
                        }
            } else {
                if(tipoPesquisa.equalsIgnoreCase("SP")){
                    s.print("\n\t\t#Informe a descrição ou * para todos : ");
                    saborPesquisar=sc.nextLine();
                    if (!saborPesquisar.equalsIgnoreCase("*")){
                        tipoPesquisa="inner join pedidositens on pedidos.codigo=pedidositens.codigopedido\n" +
                                     "inner join pizzas on pedidositens.codigopizza=pizzas.codigo\n" +
                                     "where pizzas.sabor ilike '%"+saborPesquisar+"%' order by pizzas.sabor asc;";
                    } else {
                        tipoPesquisa="inner join pedidositens on pedidos.codigo=pedidositens.codigopedido\n" +
                                     "inner join pizzas on pedidositens.codigopizza=pizzas.codigo\n" +
                                     "order by pizzas.sabor asc;";
                        }
                } else {
                    do {
                    s.print("\n\t\t#Informe a data do pedido ou * para todos : ");
                    s.print("\n\t\tFavor utilizar formato: dd/mm/aaaa => ");
                    dataPesquisar=sc.nextLine();
                    dataPesquisar=dataPesquisar.trim();//Remove os espaços em branco do ínicio e do fim da string e retorna uma nova string
                    } while(!(dataPesquisar.matches("((^[3]{1}[0-1])|(^[0-2]{1}[0-9]))/(([0][1-9])|([1][1-2]))/([0-9]{4})")) && !(dataPesquisar.equalsIgnoreCase("*")));
                    //método matches retorna false se a string não confere com o padrão da expressão regular definida e retorna true se ela confere
                    //enquanto usuário não digitar uma data no formato correto ou *, permanece em loop

                    if (!dataPesquisar.equalsIgnoreCase("*")){
                        if(dataPesquisar.matches("(^[0]\\d)/\\d{2}/\\d{4}")){
                        dataPesquisar=dataPesquisar.replaceFirst("^[0]", "");//remove o zero do inicio da data se houver, necessário devido ao formato de data gravado no banco
                        }
                        tipoPesquisa="where (select (dia ||'/'||mes||'/'||ano) from (select EXTRACT(DAY FROM pedidos.data) as dia, EXTRACT(MONTH FROM pedidos.data) as mes, EXTRACT(YEAR FROM pedidos.data) as ano) as result)='"+dataPesquisar+"' order by data desc;";
                    } else {
                        tipoPesquisa="order by pedidos.data desc;";
                        }
                    }
                }
        } else {
            do {
                menu.imprimeCabecalho("   Pizzaria Nacoes - Fechamento diário   ");            
                s.print("\n\t\t#Informe a data do fechamento ou zero '0' para cancelar: : ");
                s.print("\n\t\tFavor utilizar formato: dd/mm/aaaa => ");
                dataPesquisar=sc.nextLine();
                dataPesquisar=dataPesquisar.trim();//Remove os espaços em branco do ínicio e do fim da string e retorna uma nova string
                } while((!(dataPesquisar.matches("((^[3]{1}[0-1])|(^[0-2]{1}[0-9]))/(([0][1-9])|([1][1-2]))/([0-9]{4})"))) && (!(dataPesquisar.equalsIgnoreCase("0"))));
                //método matches retorna false se a string não confere com o padrão da expressão regular definida e retorna true se ela confere
                //enquanto usuário não digitar uma data no formato correto ou 0, permanece em loop
                
                if(dataPesquisar.equalsIgnoreCase("0")){ return dataPesquisar;}
                
                if(dataPesquisar.matches("(^[0]\\d)/\\d{2}/\\d{4}")){
                    dataPesquisar=dataPesquisar.replaceFirst("^[0]", "");//remove o zero do inicio da data se houver, necessário devido ao formato de data gravado no banco
                }
                tipoPesquisa="where (select (dia ||'/'||mes||'/'||ano) from (select EXTRACT(DAY FROM pedidos.data) as dia, EXTRACT(MONTH FROM pedidos.data) as mes, EXTRACT(YEAR FROM pedidos.data) as ano) as result)='"+dataPesquisar+"';";
            }
        
        return tipoPesquisa;//retorna a string do tipo de pesquisa
    }
    
    //Método de impressão dos resultados da pesquisa a pedido no banco
    public Map <Integer, Pedido> imprimePesquisa(Map <Integer, Pedido> listaRegistros){
        PrintStream s = System.out;
        Menu menu = new Menu();
        menu.imprimeCabecalho("  Pizzaria Nacoes - Pesquisa de Pedidos  ");
        //O for abaixo percorre a lista de registros pelo codigo que é a key do map 
        //e imprime em tela os valores do objeto pedido que estão relacionadas a cada key
        for (Integer codigo: listaRegistros.keySet()){
            Pedido p = listaRegistros.get(codigo);//acessando o objeto pela key usando o metodo get do map
            s.print(p.toString());//Imprimindo dados do registro
        }
        return listaRegistros;//retorna o map de registros a quem chamou o método imprimePesquisa
    }
    
    //Método de impressão dos resultados diarios de vendas
    public Map <Integer, Pedido> imprimeFechamentoVendas(Map <Integer, Pedido> listaRegistros){
        PrintStream s = System.out;
        Menu menu = new Menu();
        double valorTotalDinheiro=0,valorTotalCartao=0,valorTotalTaxa=0, valorTotalDia=0;
        int qtdPedidos=0, qtdPizzas=0;
        Date dataVendas=null;
        
        menu.imprimeCabecalho(" Pizzaria Nacoes - Fechamento de vendas  ");
        //O for abaixo percorre a lista de registros pelo codigo que é a key do map 
        //e imprime em tela os valores do objeto pedido que estão relacionadas a cada key
        for (Integer codigo: listaRegistros.keySet()){
            Pedido p = listaRegistros.get(codigo);//acessando o objeto pela key usando o metodo get do map
            dataVendas=p.getData();//Pegando a data do pedido
            qtdPedidos+=1;//incrementando quantidade de pedidos
            valorTotalCartao+=p.getValorCartao();//incrementando valor cartao
            valorTotalDinheiro+=p.getValorDinheiro();//incrementando valor dinheiro
            valorTotalTaxa+=p.getTaxaEntrega();//incrementando valor taxa
            for (Pizza pizza: p.getListaPizzas()){
                //percorrendo itens do pedido para incrementar quantidade por sabor no Map
                qtdPizzas+=1;
            }
            
        }
        valorTotalDia=valorTotalCartao+valorTotalDinheiro+valorTotalTaxa;//Valor total de vendas no dia
        s.print(toStringVendas(dataVendas, valorTotalCartao, valorTotalDinheiro, valorTotalTaxa, valorTotalDia, qtdPedidos, qtdPizzas));
        return listaRegistros;//retorna o map de registros a quem chamou o método
    }
    
// -------- Métodos sobrepostos -------- //
    
//Método abstrato solicita dados para popular objeto das entidades filhas para insert banco
    public void solicitaDados(){
        PrintStream s = System.out;
        Scanner sc = new Scanner(System.in);
        Menu menu = new Menu();
        double subTotal = 0;
	Date dataAtual = new Date();
        setData(dataAtual);
        Cliente clientePedido = new Cliente();
        do {
            
            clientePedido = (menu.rotinaOpcaoCliente("Pedido",clientePedido,this));
        //Enquanto usuário não sair do menu de seleção de cliente ou selecionar um cliente
        //fica em loop, todo pedido deve ter cliente
        } while (clientePedido == null);
        
        setClientePedido(clientePedido);//Inserindo cliente no pedido
        
        //Informando as pizzas do pedido
        String unidadePizza;
        do{
            s.print("\n\n\t\t#Deseja adicionar nova pizza ao pedido?");
            s.print("S - Sim / N - Não");
            s.print("\n\n\t\t");
            unidadePizza= sc.nextLine();
            //Enquanto usuário informar que deseja inserir pizza, ou digitar opcao invalida, fica em loop
            if ((unidadePizza.equalsIgnoreCase("S"))){
                //Se deseja incluir pizza, abre a pesquisa seletiva
                Pizza pizzaPedido = new Pizza();
                if ((pizzaPedido=(pizzaPedido.pesquisaSeletiva())) == null){
                    //Se não encontrar nenhuma pizza, chama novamente o metodo de pesquisa
                    pizzaPedido=pizzaPedido.pesquisaSeletiva();
                } else {
                    addPizza(pizzaPedido);
                    subTotal+=pizzaPedido.getPreco();
                }
            } else if ((unidadePizza.equalsIgnoreCase("N"))){
                if(getListaPizzas().isEmpty()){
                    //Se não deseja inserir pizza e lista de pizzas está vazia
                    //retorna ao menu de pedidos
                    menu.rotinaOpcaoPedido(this);
                }
            } else {
                s.print("\n\n\t\tOpcao invalida!");
            }
        } while ((unidadePizza.equalsIgnoreCase("S")) || (!(unidadePizza.equalsIgnoreCase("N"))));
        
        //Informando observações do pedido
        String info;
        do{
            s.print("\n\n\t\t#Deseja informar observaçoes sobre o pedido?");
            s.print("S - Sim / N - Não");
            s.print("\n\n\t\t");
            info = sc.nextLine();
            if (info.equalsIgnoreCase("S")){
                s.print("\n\n\t\t#Informe as observaçoes sobre o pedido:");
                info=sc.nextLine();
            } else {
                setObservacoesPizza("");
            }
        }while(!(setObservacoesPizza(info)));
        
        //Informando a taxa de entrega
        do{
            s.print("\n\n\t\t#Valor da Taxa de entrega: R$ ");
        }while(!(setTaxaEntrega(Double.parseDouble((sc.nextLine()).replace(',', '.')))));
        
        //Informando tota do pedido para recebimentos
        subTotal+=getTaxaEntrega();
        s.print("\n\n\t\t#Valor Total = "+subTotal);
        
        //Informando valor em cartão
        do{
            s.print("\n\n\t\t#Valor em cartão: R$ ");
        }while(!(setValorCartao(Double.parseDouble((sc.nextLine()).replace(',', '.')))));
        
        //Informando valor em dinheiro
        do{
            s.print("\n\n\t\t#Valor em dinheiro: R$ ");
        //Os loops do..while dos valores são para validar o digitado pelo usuário
        //replace pega o valor retornado pelo scanner sc, que será string neste caso e substitui ',' por '.', se houver
        //Double.parseDouble converte a String para Double e setValorDinheiro preenche o atributo do objeto
        //setValorDinheiro retorna false se o valor digitado é inválido (por exemplo, se for menor  que zero)
        }while(!(setValorDinheiro(Double.parseDouble((sc.nextLine()).replace(',', '.')))));
    }
    
    //Salva um pedido no banco de dados
    public void salvarInclusaoBancoDados() {
        
        this.solicitaDados();
        //Com o objeto preenchido pela solicitaDados()
        //como todos os dados são string, foram diretamente concatenados na stringBuilder declarada abaixo    
        
        StringBuilder sql = new StringBuilder("insert into pedidos (codigocliente, data, observacoespizza, taxaentrega, valorcartao, valordinheiro, total) values (");
        //StringBuilder permitirá concatenar os valores dos atributos do objeto para a instrucao sql
        
        sql.append((this.getClientePedido()).getCodigo()+",");
        
        //Para inclusão no banco, necessário formatar data em formato americano
        SimpleDateFormat sdBanco = new SimpleDateFormat("MM/dd/yyyy");
        sql.append("'"+sdBanco.format(getData())+"'"+",");
        
        sql.append("'"+getObservacoesPizza()+"'"+",");
        sql.append(getTaxaEntrega()+",");
        sql.append(getValorCartao()+",");
        sql.append(getValorDinheiro()+",");
        sql.append(getTotal()+");");
        
        insertDeleteBanco(sql.toString());
        //Abaixo ocorre a chamada ao método inserteDeleteBanco() passando a string que
        //contém a instrução sql. Este método trata-se de um método 
        //genérico para execução de insert,updates e deletes nas tabelas do banco
        sql= new StringBuilder();
        for (Pizza pizza: getListaPizzas()){
        sql.append("insert into pedidositens (codigopedido, codigopizza, valorpizza) values (");
        sql.append("(select max(codigo) from pedidos),");
        sql.append(pizza.getCodigo()+",");
        sql.append(pizza.getPreco()+"); ");
        }
        //Abaixo a chamada do método genérico que executará as instruções de inserção dos itens do pedido
        insertDeleteBanco(sql.toString());
    }

    //Edita um pedido no banco de dados
    public void salvarEdicaoBancoDados() {
        PrintStream s = System.out;
        Scanner sc = new Scanner (System.in);
        int codigoRegistro; //Variável que armazenra o codigo do registro que sera alterado
        
        try {
            Map <Integer, Pedido> dados = pesquisar("Pedido");
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
                    s.print("\n\t\t e que deseja alterar ou informe zero '0' para cancelar: ");
                    codigoRegistro=Integer.parseInt(sc.nextLine());//leitura do código do registro que deve ser alterado;
                }

                if (codigoRegistro != 0) {
                    //Se usuário não digitou zero e o registro foi encontrado no map
                    Pedido pedidoEdicao = dados.get(codigoRegistro);
                    solicitaDadosEdicao(pedidoEdicao);//irá solicitar os dados de alteração
                    
                    StringBuilder sql = new StringBuilder("update pedidos set codigocliente=?, observacoespizza=? where codigo="+codigoRegistro+";");
                    //StringBuilder permitirá concatenar os valores dos atributos do objeto para a instrucao sql

                    sql.insert(1, pedidoEdicao.getClientePedido().getCodigo());
                    sql.insert(2,"'"+getObservacoesPizza()+"'");
                    
                    //Abaixo ocorre a chamada ao método inserteDeleteBanco() passando a string que
                    //contém a instrução sql. Este método trata-se de um método 
                    //genérico para execução de insert,updates e deletes nas tabelas do banco
                    insertDeleteBanco(sql.toString());
                }
            } 
            s.print("\n\t\tNenhum pedido encontrado/selecionado.");
        } catch (SQLException e){
            //Mostra exceção na tela em caso de erro de sql
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
            }
        //Se digitou zero voltará para o menu
    }

    //Retorna os dados de um pedido
    public String toString() {

        return    "\n\n\t\t ========== PEDIDO: "+super.toString()
                + "\n\t\t=> "+ sd.format(Data)
                + ClientePedido.toString()
                + "\n\t\t=> Lista de Pizzas: " + toStringListaPizzas()
                + "\n\n\n\t\t=> Taxa de entrega: " + String.format("%.2f", getTaxaEntrega())
                + "\n\t\t=> Total: R$ " + String.format("%.2f", getTotal())
                + "\n\t\t=> Pago em cartao: R$ " + String.format("%.2f", getValorCartao())
                + "\n\t\t=> Pago em dinheiro: R$ " + String.format("%.2f", getValorDinheiro())
                + "\n\n\t\t=> Observacoes: " + getObservacoesPizza();
    }

    // -------- Outros métodos -------- //
    //Obtem a lista de pizzas em String
    private String toStringListaPizzas() {
        String lista = "";
        int i = 1;

        for (Pizza pizza : ListaPizzas) {
            lista += "\n\t\t Pizza " + i + " :"
                    + pizza.toString();
            i++;
        }

        return lista;
    }
    
    //Imprime as vendas diárias
    public String toStringVendas(Date dataVendas, double cartao, double dinheiro, double taxa,double vendasTotal, int qtdPedidos, int qtdPizzas) {

        return "\n\n\t\t ========== Fechamento de Vendas Diário: "
                + "\n\t\t=> "+ sd.format(dataVendas)
                + "\n\t\t=> Total de Pedidos: "+String.valueOf(qtdPedidos) 
                //método valueOf da classe String retorna a representação em string do que foi passado como parâmetro
                + "\n\t\t=> Total de Pizzas: "+String.valueOf(qtdPizzas)
                + "\n\n\n\t\t=> Total Taxa de entrega: " + String.format("%.2f", taxa)
                + "\n\t\t=> Total recebido em cartao: R$ " + String.format("%.2f", cartao)
                + "\n\t\t=> Total recebido em dinheiro: R$ " + String.format("%.2f", dinheiro)
                + "\n\t\t=> Total: R$ " + String.format("%.2f", vendasTotal);
    }

    public Pedido solicitaDadosEdicao(Pedido pedidoEdicao){
        PrintStream s = System.out;
        Scanner sc = new Scanner(System.in);
        Menu menu = new Menu();
        String editar;
        
        //Alterar o cliente do pedido
        do{
            s.print("\n\n\t\t Deseja alterar o cliente do pedido? S - Sim / N - Não");
            editar=sc.nextLine();
            Cliente clientePedido = new Cliente();
            if (editar.equalsIgnoreCase("S")){
                //Se desejar editar o cliente
                do {
                    clientePedido = menu.rotinaOpcaoCliente("Pedido",clientePedido,this);
                    //Enquanto usuário não sair do menu de seleção de cliente ou selecionar um cliente
                    //fica em loop, todo pedido deve ter cliente
                } while (ClientePedido == null);
            }
            //Enquanto usuário não digitar sim ou não para a edicao do cliente, fica em loop
        } while (!(editar.equalsIgnoreCase("S"))&& !(editar.equalsIgnoreCase("N")));
                       
        //Alterar observações do pedido
        String info;
        do{
            s.print("\n\n\t\t#Deseja alterar a observaçoes sobre o pedido?");
            s.print("S - Sim / N - Não");
            s.print("\n\n\t\t");
            info = sc.nextLine();
            if (info.equalsIgnoreCase("S")){
                s.print("\n\n\t\t#Informe as observaçoes sobre o pedido:");
                info=sc.nextLine();
            } else {
                info=pedidoEdicao.getObservacoesPizza();
            }
            //Permanece em loop enquanto os dados digitados forem invalidos 
        }while(!(pedidoEdicao.setObservacoesPizza(info)) && !(editar.equalsIgnoreCase("S"))&& !(editar.equalsIgnoreCase("N")));
        
        return pedidoEdicao;
    }
    
    //Exclui um pedido no banco de dados
    public void salvarExclusaoBancoDados() {
        PrintStream s = System.out;
        Scanner sc = new Scanner (System.in);
        int codigoRegistro; //Variável que armazenra o codigo do registro que sera excluído
        
        try {
            Map <Integer, Pedido> dados = pesquisar("Pedido");
            //Variável dados armazenará o Map de registros retornado pelo metodo pesquisar()
            
            if (!dados.isEmpty()){
                //se o map que foi passado pelo método pesquisar() não estiver vazio
                imprimePesquisa(dados);//Imprime os dados da pesquisa
            
                s.print("\n\n\t\t Informe o codigo do registro que deseja deletar \n\t\t ou zero '0' para cancelar:");
                codigoRegistro=sc.nextInt();//leitura do código do registro que deve ser alterado;

                while ( dados.get(codigoRegistro) == null && codigoRegistro != 0){ 
                    //O método get do map dados retornará null enquanto não encontrar um 
                    //registro que a key corresponda ao valor passado como parâmetro, no caso a variável codigoRegistro.
                    //Enquanto não encontrar o registro no map dados ou o usuario 
                    //não digitou zero, solicita a digitacao de um codigo existente na pesquisa
                    s.print("\n\n\t\t Registro nao encontrado na consulta.");
                    s.print("\n\n\t\t Informe o codigo do registro presente na pesquisa");
                    s.print("\n\t\t e que deseja alterar ou informe zero '0' para cancelar: ");
                    codigoRegistro=sc.nextInt();
                }

                if (codigoRegistro != 0) {
                    //Se usuário não digitou zero e o registro foi encontrado no map, irá construir a instrução sql do update
                    
                    StringBuilder sql = new StringBuilder("delete from pedidoitens where codigopedido=");
                    //StringBuilder sql armazenará a instrucao sql
                    sql.append(codigoRegistro+"; ");
                    //Feita a concatenação do codigo na string com a instrução sql para delete dos itens
                    
                    sql.append("delete from pedidos where codigo=");
                    sql.append(codigoRegistro+";");
                    
                    //Chama o método da superclasse entidadeBase passando a string da instrução sql de delete como parâmetro
                    insertDeleteBanco(sql.toString());
                    
                }
            } 
        } catch (SQLException e){
            //Mostra exceção na tela em caso de erro de sql
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
            }
    //Se digitou zero voltará para o menu      
    }

}
