package pe.edu.upeu.calcfx.modelo;


public class CalcTO {
    String num1;
    String num2;
    String resultado;
    char operador;

    @Override
    public String toString() {
        return "CalcTO{" +
                "num1='" + num1 + '\'' +
                ", num2='" + num2 + '\'' +
                ", resultado='" + resultado + '\'' +
                ", operador=" + operador +
                '}';
    }

    public String getNum1() {
        return num1;
    }

    public void setNum1(String num1) {
        this.num1 = num1;
    }

    public String getNum2() {
        return num2;
    }

    public void setNum2(String num2) {
        this.num2 = num2;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public char getOperador() {
        return operador;
    }

    public void setOperador(char operador) {
        this.operador = operador;
    }
}
