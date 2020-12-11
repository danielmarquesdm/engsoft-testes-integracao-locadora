package edu.ifma.engsoft.integracao.service;

import edu.ifma.engsoft.integracao.model.Aluguel;
import edu.ifma.engsoft.integracao.model.Cliente;
import edu.ifma.engsoft.integracao.model.Imovel;
import edu.ifma.engsoft.integracao.model.Locacao;
import edu.ifma.engsoft.integracao.util.exception.LocacaoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class LocacaoService {
    private final CadastroClienteService clienteService;
    private final CadastroImovelService imovelService;

    private LocacaoService(){
        clienteService = new CadastroClienteService();
        imovelService = new CadastroImovelService();
    }

    public Locacao efetuaLocacao(Cliente cliente, Imovel imovel, BigDecimal valorAluguel) throws LocacaoException {
        Cliente clienteCadastrado = clienteService.buscaPor(cliente.getIdCliente());
        Imovel imovelCadastrado = imovelService.buscaPor(imovel.getIdImovel());

        if (Objects.isNull(clienteCadastrado) || Objects.isNull(imovelCadastrado)) {
            String erro = "Não foi possível efetuar locação. Imovel ou Cliente não cadastrado.";
            throw new LocacaoException(erro);
        }

        Locacao locacao = new Locacao(imovelCadastrado, clienteCadastrado);
        locacao.setValorAluguel(BigDecimal.valueOf(1250));
        locacao.setDiaVencimento(10);
        locacao.setDataInicio(LocalDate.of(2020,11,30));
        locacao.setDataFim(LocalDate.now());
        locacao.setAtivo(true);
        locacao.setObservacao("Bem localizado");
        Aluguel aluguel = new Aluguel(locacao);
        locacao.adicionaAluguel(aluguel);

        return locacao;
    }
}
