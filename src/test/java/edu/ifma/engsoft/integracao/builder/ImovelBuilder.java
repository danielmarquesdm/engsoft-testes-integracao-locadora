package edu.ifma.engsoft.integracao.builder;

import edu.ifma.engsoft.integracao.enums.TipoImovel;
import edu.ifma.engsoft.integracao.model.Imovel;

import java.math.BigDecimal;

public class ImovelBuilder {
    private Imovel imovel;
    private static int contadorId = 1;

    private ImovelBuilder() { }

    private static ImovelBuilder umImovel() {
        ImovelBuilder builder = new ImovelBuilder();
        builder.imovel = new Imovel();
        builder.imovel.setIdImovel((long) contadorId++);
        builder.imovel.setTipoImovel(TipoImovel.APARTAMENTO);
        builder.imovel.setValorAluguelSugerido(BigDecimal.valueOf(1800));
        builder.imovel.setObservacao("boa localizacao");
        builder.imovel.setVagasGaragem(1);
        builder.imovel.setBanheiros(2);
        builder.imovel.setSuites(1);
        builder.imovel.setDormitorios(2);
        builder.imovel.setBairro("cohama");
        builder.imovel.setCep(65894325);

        return builder;
    }

    public ImovelBuilder noBairro(String nome) {
        imovel.setBairro("Araçagy");
        return this;
    }

    public ImovelBuilder comValorSugerido(BigDecimal valor) {
        imovel.setValorAluguelSugerido(valor);
        return this;
    }

    public Imovel constroi(){
        return imovel;
    }

}
