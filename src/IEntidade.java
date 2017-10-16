//Interface com os métodos básicos de todas as entidades
public interface IEntidade {

    //Retorna os dados da entidade
    public String toString();

    //Método que salva a entidade no banco de dados
    public boolean salvarInclusaoBancoDados(EntidadeBase Entidade, String mensagemRetorno);

    //Método que salva a entidade no banco de dados
    public boolean salvarEdicaoBancoDados(EntidadeBase Entidade, String mensagemRetorno);

    //Método que salva a entidade no banco de dados
    public boolean salvarExclusaoBancoDados(String Codigo, String mensagemRetorno);
}
