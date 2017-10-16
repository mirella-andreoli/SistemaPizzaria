//Entidade com os dados de um pedido
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    public String getObservacoesPizza() {
        return ObservacoesPizza;
    }

    public void setObservacoesPizza(String ObservacoesPizza) {
        this.ObservacoesPizza = ObservacoesPizza;
    }

    public double getTaxaEntrega() {
        return TaxaEntrega;
    }

    public void setTaxaEntrega(double TaxaEntrega) {
        this.TaxaEntrega = TaxaEntrega;
    }

    public double getTotal() {
        return Total + getTaxaEntrega();
    }

    public double getValorCartao() {
        return ValorCartao;
    }

    public void setValorCartao(double ValorCartao) {
        this.ValorCartao = ValorCartao;
    }

    public double getValorDinheiro() {
        return ValorDinheiro;
    }

    public void setValorDinheiro(double ValorDinheiro) {
        this.ValorDinheiro = ValorDinheiro;
    }

    public void addPizza(Pizza PizzaAdicionaca) {
        this.ListaPizzas.add(PizzaAdicionaca);
        this.Total += PizzaAdicionaca.getPreco();
    }

    public void removerPizza(Pizza PizzaAdicionaca) {
        this.ListaPizzas.remove(PizzaAdicionaca);
        this.Total -= PizzaAdicionaca.getPreco();
    }

    public ArrayList<Pizza> getListaPizzas() {
        return ListaPizzas;
    }

    public Cliente getClientePedido() {
        return ClientePedido;
    }

    public void setClientePedido(Cliente ClientePedido) {
        this.ClientePedido = ClientePedido;
    }

// -------- Métodos sobrepostos -------- //
    //Salva um pedido no banco de dados
    public boolean salvarInclusaoBancoDados(EntidadeBase Entidade, String mensagemRetorno) {
        /*
        Implementar os método para salvar no BD
        TO DO: Fazer os tratamentos de erro retornando False + mensagem de erro 
        para retorno
         */
        //Se der certo
        mensagemRetorno = "Pedido salvo com sucesso!";
        return true;
    }
    
    //Edita um pedido no banco de dados
    public boolean salvarEdicaoBancoDados(EntidadeBase Entidade, String mensagemRetorno) {
        /*
        Implementar os método para salvar no BD
        TO DO: Fazer os tratamentos de erro retornando False + mensagem de erro 
        para retorno
         */
        //Se der certo
        mensagemRetorno = "Pedido editado com sucesso!";
        return true;
    }

    //Exclui um pedido no banco de dados
    public boolean salvarExclusaoBancoDados(String Codigo, String mensagemRetorno) {
        /*
        Implementar os método para salvar no BD
        TO DO: Fazer os tratamentos de erro retornando False + mensagem de erro 
        para retorno
         */
        //Se der certo
        mensagemRetorno = "Pedido Excluido com sucesso!";
        return true;
    }

    //Retorna os dados de um pedido
    public String toString() {

        return "-------- Pedido -------- \n" + sd.format(this.Data)
                + "\n" + super.toString() + ClientePedido.toString()
                + "\nPizzas: " + toStringListaPizzas()
                + "\nTaxa de entrega: " + String.format("%.2f", getTaxaEntrega())
                + "\nTotal: R$" + String.format("%.2f", getTotal())
                + "\nPago em cartao: R$" + String.format("%.2f", getValorCartao())
                + "\nPago em dinheiro: R$" + String.format("%.2f", getValorDinheiro())
                + "\nObservacoes: " + getObservacoesPizza();
    }

    // -------- Outros métodos -------- //
    //Obtem a lista de pizzas em String
    private String toStringListaPizzas() {
        String lista = "";
        int i = 1;

        for (Pizza pizza : ListaPizzas) {
            lista += "\n Pizza " + i + ": \n"
                    + pizza.toString();
            i++;
        }

        return lista;
    }
}
