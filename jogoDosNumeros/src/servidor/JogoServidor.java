package servidor;


import java.util.Random;

public class JogoServidor {
    private Integer numero1;
    private Integer numero2;

    private Integer resultado;

    public JogoServidor() {
        this.numero1 = numeroAleatorio();
        this.numero2 = numeroAleatorio();
        this.resultado = numero1 + numero2;
    }

    private static Integer numeroAleatorio() {
        Random r = new Random();
        return r.nextInt(100);
    }

    public Integer getNumero1() {
        return numero1;
    }

    public void setNumero1(Integer numero1) {
        this.numero1 = numero1;
    }

    public Integer getNumero2() {
        return numero2;
    }

    public void setNumero2(Integer numero2) {
        this.numero2 = numero2;
    }

    public Integer getResultado() {
        return resultado;
    }

    public void setResultado(Integer resultado) {
        this.resultado = resultado;
    }
}
