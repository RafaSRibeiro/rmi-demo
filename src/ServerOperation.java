import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerOperation extends UnicastRemoteObject implements RMIInterface {

    private static final long serialVersionUID = 1L;

    private String resposta01 = "";
    private String resposta02 = "";

    protected ServerOperation() throws RemoteException {
        super();
    }

    @Override
    public String helloTo(String resposta) throws RemoteException {
        int prisioneiro;
        if (!respostaExists(1)) {
            prisioneiro = 1;
        } else {
            prisioneiro = 2;
        }
        System.err.println("Prisioneiro: " + prisioneiro);
        inicializaResposta(prisioneiro, resposta);
        while (!respostaExists(1) || !respostaExists(2)) {
            try { Thread.sleep (1000); } catch (InterruptedException ex) {}
        }
        System.err.println("Prisioneiro: " + prisioneiro);
        return "Prisioneiro " + prisioneiro + ": " + responde(prisioneiro);
    }

    public static void main(String[] args) {
        try {
            Naming.rebind("//localhost/MyServer", new ServerOperation());
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private void inicializaResposta(int prisioneiro, String resposta) {
        if (prisioneiro == 1)
            resposta01 = resposta;
        else
            resposta02 = resposta;
    }

    private boolean respostaExists(int prisioneiro) {
        String resposta = (prisioneiro == 1 ? resposta01 : resposta02);
        return resposta != "";
    }

    private String responde(int prisioneiro) {
        if (resposta01.equals("1") && resposta02.equals("1"))
            return "5 Anos";
        else if (resposta01.equals("1") && resposta02.equals("0"))
            return prisioneiro == 1 ? "Solto" : "10 anos";
        else if (resposta01.equals("0") && resposta02.equals("1"))
            return prisioneiro == 1 ? "10 anos" : "Solto";
        else if (resposta01.equals("0") && resposta02.equals("0"))
            return "6 meses";
        return "";
    }

}