//Entidade com os dados de um cliente
public class Cliente extends EntidadeBase {

    private String Nome;
    private String Telefone;
    private String Endereco;

    // -------- getters e setters -------- // 
    public String getNome() {
        return Nome;
    }

    public void setNome(String Nome) {
        this.Nome = Nome;
    }

    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String Telefone) {
        this.Telefone = Telefone;
    }

    public String getEndereco() {
        return Endereco;
    }

    public void setEndereco(String Endereco) {
        this.Endereco = Endereco;
    }

    // -------- Métodos sobrepostos -------- // 
    //Salva um cliente no banco de dados
    public boolean salvarBancoDados(EntidadeBase Entidade, String mensagemRetorno) {

        /*
        Implementar os método para salvar um cliente no BD
        TO DO: Fazer os tratamentos de erro retornando False + mensagem de erro 
        para retorno
         */
        //Se der certo
        mensagemRetorno = "Cliente salvo com sucesso!";
        return true;
    }

    //Retorna os dados de um cliente
    public String toString() {

        return "-------- Cliente -------- \n" + super.toString() + "Nome: " + getNome() + "\nTelefone: "
                + getTelefone() + "\nEndereco: " + getEndereco();
    }
}
