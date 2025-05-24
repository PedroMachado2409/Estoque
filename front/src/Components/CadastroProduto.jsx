import React, { useState } from "react";
import {
  FormControl,
  FormLabel,
  Input,
  Button,
  VStack,
  useToast
} from "@chakra-ui/react";
import { useProduto } from "../Contexts/ProdutoContext";

const CadastroProduto = ({ onClose }) => {
  const { adicionarProduto } = useProduto();
  const toast = useToast();

  const [formData, setFormData] = useState({
    nome: "",
    unidade: "",
    observacao:"",
    preco: ""
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
  e.preventDefault();

  if (!formData.nome.trim() || !formData.unidade.trim() || !formData.preco.trim()) {
    toast({title: "Campos obrigatórios.",description: "Preencha nome, unidade e preço antes de salvar.",
      status: "error",duration: 4000,isClosable: true,position: "top",
    });
    return; 
  }

  const novoProduto = {
    ...formData,
    preco: parseFloat(formData.preco)
  };

  await adicionarProduto(novoProduto);

  toast({title: "Produto cadastrado!",description: "O produto foi adicionado com sucesso.",
    status: "success", duration: 3000,isClosable: true,position: "top",
  });

  onClose(); 
};

  return (
    <form onSubmit={handleSubmit}>
      <VStack spacing={4}>
        <FormControl >
          <FormLabel>Nome</FormLabel>
          <Input name="nome" value={formData.nome} onChange={handleChange} />
        </FormControl>
        <FormControl >
          <FormLabel>Unidade</FormLabel>
          <Input name="unidade" value={formData.unidade} onChange={handleChange} />
        </FormControl>
        <FormControl >
          <FormLabel>Preço</FormLabel>
          <Input
            type="number"
            name="preco"
            step="0.01"
            value={formData.preco}
            onChange={handleChange}
          />
        </FormControl>
         <FormControl >
          <FormLabel>Observação</FormLabel>
          <Input name="observacao" value={formData.observacao} onChange={handleChange} />
        </FormControl>
        <Button type="submit" colorScheme="teal" width="full">
          Salvar Produto
        </Button>
      </VStack>
    </form>
  );
};

export default CadastroProduto;
