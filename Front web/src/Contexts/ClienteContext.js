import React, { createContext, useContext, useState, useEffect } from 'react';
import { useToast } from '@chakra-ui/react';
import { useAuth } from './AuthContext';

const ClienteContext = createContext();

export const ClienteProvider = ({ children }) => {
  const { token } = useAuth();
  const [clientes, setClientes] = useState([]);
  const [usuarios, setUsuarios] = useState([]); // Novo estado para os usuários
  const [loading, setLoading] = useState(false);
  const [hasFetched, setHasFetched] = useState(false); // <- NOVO
  const toast = useToast();

  const API_URL = 'http://localhost:8080/api/clientes';
  const USUARIO_API_URL = 'http://localhost:8080/api/usuarios'; // API para obter usuários

  const fetchClientes = async () => {
    if (!token || hasFetched) return;
    setLoading(true);
    try {
      const response = await fetch(API_URL, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (!response.ok) throw new Error('Erro ao buscar clientes');
      const data = await response.json();
      setClientes(data);
    } catch (error) {
      console.error('Erro ao buscar clientes:', error);
      toast({
        title: 'Erro ao buscar clientes.',
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
    } finally {
      setLoading(false);
    }
  };

  const fetchUsuarios = async () => {
    if (!token) return;
    try {
      const response = await fetch(USUARIO_API_URL, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (!response.ok) throw new Error('Erro ao buscar usuários');
      const data = await response.json();
      console.log("Usuários carregados:", data); // Verifique o que está sendo retornado
      setUsuarios(data);
    } catch (error) {
      console.error('Erro ao buscar usuários:', error);
      toast({
        title: 'Erro ao buscar usuários.',
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
    }
  };

  const cadastrarCliente = async (cliente) => {
    if (!token) return;
    try {
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(cliente),
      });

      if (!response.ok) throw new Error('Erro ao cadastrar cliente');
      toast({
        title: 'Cliente cadastrado com sucesso!',
        status: 'success',
        duration: 3000,
        isClosable: true,
      });
      fetchClientes(); // Atualiza a lista
    } catch (error) {
      console.error('Erro ao cadastrar cliente:', error);
      toast({
        title: 'Erro ao cadastrar cliente.',
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
    }
  };

  const editarCliente = async (id, cliente) => {
    if (!token) return;
    try {
      const response = await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(cliente),
      });

      if (!response.ok) throw new Error('Erro ao editar cliente');
      toast({
        title: 'Cliente atualizado com sucesso!',
        status: 'success',
        duration: 3000,
        isClosable: true,
      });
      fetchClientes();
    } catch (error) {
      console.error('Erro ao editar cliente:', error);
      toast({
        title: 'Erro ao editar cliente.',
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
    }
  };

  const deletarCliente = async (id) => {
    if (!token) return;
    try {
      const response = await fetch(`${API_URL}/${id}`, {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) throw new Error('Erro ao deletar cliente');
      toast({
        title: 'Cliente deletado com sucesso!',
        status: 'success',
        duration: 3000,
        isClosable: true,
      });
      fetchClientes();
    } catch (error) {
      console.error('Erro ao deletar cliente:', error);
      toast({
        title: 'Erro ao deletar cliente.',
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
    }
  };

  useEffect(() => {
    fetchClientes();
    fetchUsuarios(); // Chama para obter os usuários
  }, [token]);
  

  return (
    <ClienteContext.Provider
      value={{
        clientes,
        usuarios, // Agora podemos acessar os usuários
        loading,
        fetchClientes,
        cadastrarCliente,
        editarCliente,
        deletarCliente,
      }}
    >
      {children}
    </ClienteContext.Provider>
  );
};

export const useClientes = () => useContext(ClienteContext);
