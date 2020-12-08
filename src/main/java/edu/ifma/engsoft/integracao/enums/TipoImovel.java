package edu.ifma.engsoft.integracao.enums;

public enum TipoImovel {
    CASA(1),
    APARTAMENTO(2);

    private int id;

    TipoImovel(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }
}
