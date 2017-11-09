
import java.sql.Connection;

//Interface com os métodos básicos de todas as entidades
public interface IEntidade {

    //Retorna os dados da entidade
    public String toString();
    
    //Método de conexão ao banco
    public Connection dbCon();
    
    //Método que salva a entidade no banco de dados
    public void salvarInclusaoBancoDados();

    //Método que salva a entidade no banco de dados
    public void salvarEdicaoBancoDados();
   
}
