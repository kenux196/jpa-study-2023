package study.kenux.jpa.test;

import jakarta.persistence.EntityManager;

public abstract class DataGenerator {

    protected final EntityManager em;

    protected DataGenerator(EntityManager em) {
        this.em = em;
    }

    public abstract void generate();
}
