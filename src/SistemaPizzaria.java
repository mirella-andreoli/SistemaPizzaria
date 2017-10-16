
import java.io.PrintStream;


public class SistemaPizzaria{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Cliente cli = new Cliente();
        cli.setCodigo("02002");
        cli.setNome("Nath");
        cli.setEndereco("Teste");
        cli.setTelefone("15555");
        System.out.println(cli.toString());

        Pizza pizza = new Pizza();
        pizza.setCodigo("010101");
        pizza.setPreco(52.6);
        pizza.setSabor("Queijo");
        System.out.println(pizza.toString());

        Pizza pizza2 = new Pizza();
        pizza.setCodigo("010102");
        pizza.setPreco(23.6);
        pizza.setSabor("Massa");

        Pedido pedido = new Pedido();
        pedido.setClientePedido(cli);
        pedido.setObservacoesPizza("Obseseseseseseseseseseseses");
        pedido.setTaxaEntrega(2);
        pedido.setCodigo("00001");
        pedido.setValorCartao(30);
        pedido.setValorDinheiro(80);
        pedido.addPizza(pizza);
        pedido.addPizza(pizza2);
        System.out.println(pedido.toString());
        
        PrintStream s = System.out; // Importada a Java.io.PrintStream e criad
        
        
        s.println("\n \t \t \u2554");
        
        
        /*
        If you are developing with JDK 1.5 you can solve this using java.util.Formatter:

        String format = "%-20s %5d\n";
        System.out.format(format, "test", 1);
        System.out.format(format, "test2", 20);
        System.out.format(format, "test3", 5000);
        
        
        */
    }

}
