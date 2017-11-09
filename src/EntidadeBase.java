
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//Base com os campos e métodos comum a todas as entidades concretas
     
public abstract class EntidadeBase implements IEntidade {

    //Código interno da entidade
    private int Codigo;

    //Obtem o valor de Codigo
    public int getCodigo() {
        return Codigo;
    }

    public void setCodigo(int codigo){
        Codigo = codigo;
    }
    
    //Método que imprime as informações
    public String toString() {
        return "\n\t\t=>Codigo: " + this.getCodigo();
    }

    //-------Métodos DAO-----------
    //Método de conexão ao banco
    public Connection dbCon(){
        
        String url = "jdbc:postgresql://localhost:5432/pizzaria";  
        String usuario = "postgres";  
        String senha = "postgres";  
        Connection con = null;//Cria instacia do objeto conexao
        try {  
                Class.forName("org.postgresql.Driver");//Tenta localizar o driver

                con = DriverManager.getConnection(url, usuario, senha);
                //DriverManager tentará encontrar o Driver e estabelecer 
                //conexão ao banco específicado na string url com o usuario e senha também informados

            } catch (Exception e) {  
                 e.printStackTrace();  
            }
            return con;
    } 
    
    //Método para execução de inserção e remoção de dados no banco de dados por instrução sql passadas para este método como parâmetro String
    public boolean insertDeleteBanco (String sql){
        Connection c = dbCon();//Abre a conexão com o bando de dados e armazena no objeto do tipo Connection
        PreparedStatement pstm = null;//Objeto do tipo instrução pre-compilada de sql
        int result=0;
        
        try{
            pstm = c.prepareStatement(sql); //Prepara e compila a instrucao sql passada por string
            System.out.print("\n\n\n\t\t ## Efetuando operação! ##");
            result=pstm.executeUpdate();//Retorna a quantidade de linhas alteradas
            pstm.close();//fecha a instancia de instrucao pre-compilada
            c.close(); //fecha a conexão
            if (result > 0){
                System.out.print("\n\n\n\t\t ## Operação efetuada com sucesso! ##");
                return true;
            }
        } 
        catch(SQLException e){
            //Caso ocorra exceção, exibe mensagem
            System.out.print("\n\n\n\t\t ## Operaçao não pode ser realizada. ##");
            //Mostra exceção na tela em caso de erro de sql
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            } 
        return result > 0;
    }
    
    //Método para retornar dados de selects (ResultSet) feitas no banco de dados e passadas para este método como parâmetro String
    public ResultSet selectBanco (String sql){ 
        Connection c = dbCon();//Abre a conexão com o bando de dados e armazena no objeto do tipo Connection
        Statement stmt = null;//Objeto do tipo instrução simples de sql
        ResultSet rs =null; //Objeto de linhas/coleção de resultados da consulta ao banco
        try{
            stmt = c.createStatement(); //criar uma instancia de instrução sql a partir da conexão e armazenar no objeto já declarado
            rs = stmt.executeQuery(sql);//executara instrução sql e armazenará os resultados no objeto ResultSet declarado
            } catch(SQLException e){
                //Caso ocorra exceção, exibe mensagem
                System.out.print("\n\n\n\t\t ## Operaçao não pode ser realizada. ##");
                //Mostra exceção na tela em caso de erro de sql
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                }
        //Se conseguiu fazer a consulta, retorna o objeto ResultSet com os resultados da select
        return rs;
    }
    
    //-----Métodos abstratos para implementação em entidades filhas-----
    
    public abstract void solicitaDados();
    
    public abstract void salvarInclusaoBancoDados();

    public abstract void salvarEdicaoBancoDados();
    
}