package Domain;

import java.util.Objects;

/**
    O relatie/pereche formata din 2 elemente ordonate
 @param <E1> tipul primul element
 @param <E2>  tipul celui de al doilea element
 */
public class Tuple<E1, E2> {
    /**
     * Primul element al relatiei
     */
    protected E1 e1;
    /**
     * Al doilea element al relatiei
     */
    protected E2 e2;

    /**
     * Creeaza o pereche din 2 elemente
     *
     * @param e1 the first element
     * @param e2 the second element
     */
    public Tuple(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    /**
     * Returneaza primul element
     * @return e1 primul element
     */
    public E1 getFirst() {
        return e1;
    }

    /**
     * Returneaza al doilea element
     * @return e2 al doilea element
     */
    public E2 getSecond() {
        return e2;
    }

    /**
     * Seteaza primul element
      * @param e1 primul element
     */
    public void setE1(E1 e1) {
        this.e1 = e1;
    }

    /**
     * Seteaza al doilea element
     * @param e2 al doilea element
     */
    public void setE2(E2 e2) {
        this.e2 = e2;
    }


    /**
     * Doua tupluri sunt egale daca primele elemente, respectiv ultimele elemente sunt egale.
     * Poate primi si {@code null} fara sa arunce exceptie
     *
     * @param obj the object to compare with
     * @return {@code true} if {@code obj} is a Tuple with the same e1 and e2,
     *         {@code false} otherwise (including when {@code obj} is null)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        //!verifica tipul obiectului si compara continutul
        //!dacă o este un Tuple, îl pune direct într-o variabilă nouă numită ((other)), deja de tipul Tuple
        if (!(obj instanceof Tuple<?, ?> other)) return false;
        return Objects.equals(e1, other.e1) && Objects.equals(e2, other.e2);
    }

    /**
     * Calculeaza hashcode din ambele elemente
     *
     * @return hash code pentru perechea instantei
     */
    @Override
    public int hashCode() {
        return Objects.hash(e1, e2);
    }

    /**
     * Returneaza un string cu valorile celor doua perechi
     *
     * @return text de forma "(e1, e2)"
     */
    @Override
    public String toString() {
        return "(" + e1 + ", " + e2 + ")";
    }

}
