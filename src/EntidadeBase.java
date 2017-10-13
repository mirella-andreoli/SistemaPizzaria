//Base com os campos comum a todas as entidades
public abstract class EntidadeBase implements IEntidade {

    //Código interno da entidade
    private String Codigo;

    //Obtem o valor de Codigo
    public String getCodigo() {
        return Codigo;
    }

    //Atribui o valor de Codigo
    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    //Método que imprime as informações
    public String toString() {
        return "Codigo: " + this.getCodigo() + "\n";
    }

    //Método abstrato para implementação em entidades filhas
    public abstract boolean salvarBancoDados(EntidadeBase Entidade, String mensagemRetorno);

}
