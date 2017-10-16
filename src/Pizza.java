//Entidade com os dados de uma pizza
public class Pizza extends EntidadeBase {

    private String Sabor;
    private double Preco;

    // ------- getters e setters ------- //
    public String getSabor() {
        return Sabor;
    }

    public void setSabor(String Sabor) {
        this.Sabor = Sabor;
    }

    public double getPreco() {
        return Preco;
    }

    public void setPreco(double Preco) {
        this.Preco = Preco;
    }

    // -------- Métodos sobrepostos -------- // 
    //Salva uma pizza no banco de dados
    public boolean salvarInclusaoBancoDados(EntidadeBase Entidade, String mensagemRetorno) {
        /*
        Implementar os método para salvar no BD
        TO DO: Fazer os tratamentos de erro retornando False + mensagem de erro 
        para retorno
         */
        //Se der certo
        mensagemRetorno = "Pizza salva com sucesso!";
        return true;
    }

    //Edita uma pizza no banco de dados
    public boolean salvarEdicaoBancoDados(EntidadeBase Entidade, String mensagemRetorno) {
        /*
        Implementar os método para salvar no BD
        TO DO: Fazer os tratamentos de erro retornando False + mensagem de erro 
        para retorno
         */
        //Se der certo
        mensagemRetorno = "Pizza editada com sucesso!";
        return true;
    }

    //Exclui uma pizza no banco de dados
    public boolean salvarExclusaoBancoDados(String Codigo, String mensagemRetorno) {
        /*
        Implementar os método para salvar no BD
        TO DO: Fazer os tratamentos de erro retornando False + mensagem de erro 
        para retorno
         */
        //Se der certo
        mensagemRetorno = "Pizza Excluida com sucesso!";
        return true;
    }

    //Retorna os dados de uma pizza
    public String toString() {
        
        return "-------- Pizza -------- \n" + super.toString() + "Sabor: " + getSabor() + "\nPreco: " + String.format("%.2f", getPreco());
    }
}
