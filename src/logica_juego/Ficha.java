package logica_juego;

public class Ficha {
	
    private final int valor;

    public Ficha(int valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("El valor de una ficha debe ser positivo");
        }
        this.valor = valor;
    }

    public int getvalor() {
        return valor;
    }

    public boolean puedeSumarseCon(Ficha fichaB) {
        if (fichaB == null) {
            return false;
        }
        boolean esParUnoDos = (this.valor == 1 && fichaB.valor == 2)
                || (this.valor == 2 && fichaB.valor == 1);
        boolean esParDeMultiplos = this.valor >= 3
                && this.valor == fichaB.valor
                && this.valor % 3 == 0;
        return esParUnoDos || esParDeMultiplos;
    }

    public int valorSumado(Ficha fichaB) {
        if (!puedeSumarseCon(fichaB)) {
            throw new IllegalStateException("Estas fichas no se pueden sumar");
        }
        return (this.valor == 1 || this.valor == 2) ? 3 : this.valor * 2;
    }

    public int puntaje() {
        if (valor < 3) {
            return 0;
        }
        // 3 -> 3, 6 -> 9, 12 -> 27, 24 -> 81 ... (3^(log2(valor/3)+1))
        int power = 1;
        int v = valor;
        while (v > 3) {
            v /= 2;
            power++;
        }
        return (int) Math.pow(3, power);
    }

    @Override
    public String toString() {
        return String.valueOf(valor);
    }
}