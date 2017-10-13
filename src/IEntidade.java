//Interface com os métodos básicos de todas as entidades
public interface IEntidade {
    
    //Retorna os dados da entidade
    public String toString();
    
    //Método que salva a entidade no banco de dados
    public boolean salvarBancoDados(EntidadeBase Entidade, String mensagemRetorno);    
}