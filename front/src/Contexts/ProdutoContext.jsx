import React, { createContext, useContext, useState, useEffect } from 'react';

const ProdutoContext = createContext(null);

export const ProdutoProvider = ({ children }) => {
  const [produto, setProduto] = useState([]);
  const [saldo, setSaldo] = useState(null);
  const [produtoDetalhado, setProdutoDetalhado] = useState(null);
  const [movimentacoes, setMovimentacoes] = useState([]);
  const [loading, setLoading] = useState(true);

  const API = 'http://localhost:8080/api/produto';

  const localizarProdutos = async () => {
    setLoading(true);
    try {
      const resposta = await fetch(API);
      if (!resposta.ok) throw new Error('Erro ao buscar produtos!');
      const produtos = await resposta.json();
      setProduto(produtos);
    } catch (error) {
      console.error('Erro ao buscar produtos', error);
    } finally {
      setLoading(false);
    }
  };

  const adicionarProduto = async (novoProduto) => {
    try {
      const resposta = await fetch(API, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(novoProduto),
      });
      if (!resposta.ok) throw new Error('Erro ao adicionar produto!');
      const salvo = await resposta.json();
      setProduto((prev) => [...prev, salvo]);
    } catch (error) {
      console.error('Erro ao adicionar produto', error);
    }
  };

  const obterSaldoDoProduto = async (produtoId) => {
    setLoading(true);
    try {
      const resposta = await fetch(`${API}/${produtoId}/saldo`);
      if (!resposta.ok) throw new Error("Erro ao obter saldo do produto");
      const dados = await resposta.json();
      setSaldo(dados);
    } catch (error) {
      console.log('Erro ao obter saldo do produto', error);
    } finally {
      setLoading(false);
    }
  };

  const obterProdutoPorId = async (produtoId) => {
    setLoading(true);
    try {
      const resposta = await fetch(`${API}/${produtoId}`);
      if (!resposta.ok) throw new Error("Erro ao obter produto por ID");
      const dados = await resposta.json();
      setProdutoDetalhado(dados);
    } catch (error) {
      console.error("Erro ao obter produto por ID", error);
    } finally {
      setLoading(false);
    }
  };

  const obterMovimentacoesPorProduto = async (produtoId) => {
    try {
      const resposta = await fetch(`${API}/${produtoId}/movimentacao`);
      if (!resposta.ok) throw new Error("Erro ao obter movimentações");
      const dados = await resposta.json();
      setMovimentacoes(dados);
    } catch (error) {
      console.error("Erro ao obter movimentações", error);
    }
  };

   const editarProduto = async (id, produtoEditado) => {
    setLoading(true);
    try {
      const response = await fetch(`${API}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(produtoEditado),
      });

      if (!response.ok) {
        throw new Error('Erro ao editar produto');
      }

      setProdutoDetalhado({ ...produtoEditado, id });
      return true;
    } catch (error) {
      console.error(error);
      return false;
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    localizarProdutos();
  }, []);

  return (
    <ProdutoContext.Provider
      value={{
        produto,
        loading,
        saldo,
        produtoDetalhado,
        movimentacoes,
        localizarProdutos,
        adicionarProduto,
        obterSaldoDoProduto,
        obterProdutoPorId,
        obterMovimentacoesPorProduto,
        editarProduto
      }}
    >
      {children}
    </ProdutoContext.Provider>
  );
};

export const useProduto = () => {
  const ctx = useContext(ProdutoContext);
  if (ctx === null) {
    throw new Error('useProduto deve ser usado dentro de ProdutoProvider');
  }
  return ctx;
};
