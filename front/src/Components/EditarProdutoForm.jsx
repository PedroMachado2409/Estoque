import React, { useState, useEffect } from 'react';
import {
  VStack,
  FormControl,
  FormLabel,
  Input,
  Button,
  useToast,
} from '@chakra-ui/react';
import { useProduto } from '../Contexts/ProdutoContext';

const EditarProdutoForm = ({ produtoId, produtoInicial, onClose }) => {
  const { editarProduto } = useProduto();
  const toast = useToast();

  const [formData, setFormData] = useState({
    nome: '',
    unidade: '',
    preco: '',
    observacao: '',
  });

  useEffect(() => {
    if (produtoInicial) {
      setFormData({
        nome: produtoInicial.nome || '',
        unidade: produtoInicial.unidade || '',
        preco: produtoInicial.preco || '',
        observacao: produtoInicial.observacao || '',
      });
    }
  }, [produtoInicial]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((old) => ({
      ...old,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const produtoParaEditar = {
      ...formData,
      preco: parseFloat(formData.preco),
    };

    const sucesso = await editarProduto(produtoId, produtoParaEditar);

    if (sucesso) {
      toast({
        title: 'Produto editado',
        description: 'Produto atualizado com sucesso.',
        status: 'success',
        duration: 3000,
        isClosable: true,
      });
      if (onClose) onClose();
    } else {
      toast({
        title: 'Erro',
        description: 'Falha ao editar o produto.',
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <VStack spacing={4}>
        <FormControl isRequired>
          <FormLabel>Nome</FormLabel>
          <Input name="nome" value={formData.nome} onChange={handleChange} />
        </FormControl>

        <FormControl isRequired>
          <FormLabel>Unidade</FormLabel>
          <Input name="unidade" value={formData.unidade} onChange={handleChange} />
        </FormControl>

        <FormControl isRequired>
          <FormLabel>Preço</FormLabel>
          <Input
            type="number"
            name="preco"
            step="0.01"
            value={formData.preco}
            onChange={handleChange}
          />
        </FormControl>

        <FormControl>
          <FormLabel>Observação</FormLabel>
          <Input
            name="observacao"
            value={formData.observacao}
            onChange={handleChange}
          />
        </FormControl>

        <Button type="submit" colorScheme="teal" width="full">
          Salvar Produto
        </Button>
      </VStack>
    </form>
  );
};

export default EditarProdutoForm;
