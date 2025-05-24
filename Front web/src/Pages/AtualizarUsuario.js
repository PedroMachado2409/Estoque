import React, { useState } from 'react';
import {
  Box,
  FormControl,
  FormLabel,
  Input,
  Button,
  useColorMode,
  Heading,
  VStack,
  Flex,
  Alert,
  AlertIcon,
  AlertTitle,
  AlertDescription,
} from '@chakra-ui/react';
import Sidebar from '../Componentes/Siderbar';
import { useUpdateUser } from '../Contexts/UpdateUserContext';

export default function AtualizarUsuario() {
  const { colorMode } = useColorMode();
  const { updateUser } = useUpdateUser();

  const [senhaAtual, setSenhaAtual] = useState('');
  const [novaSenha, setNovaSenha] = useState('');
  const [novoLogin, setNovoLogin] = useState('');
  const [formSubmitted, setFormSubmitted] = useState(false);
  const [error, setError] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!senhaAtual || !novaSenha) {
      setError(true);
      return;
    }

    const token = localStorage.getItem('token');

    try {
      await updateUser({ senhaAtual, novaSenha, novoLogin }, token);
      setFormSubmitted(true);
      setError(false);

      setSenhaAtual('');
      setNovaSenha('');
      setNovoLogin('');
    } catch (err) {
      setError(true);
      console.error(err.message);
    }
  };

  return (
    <div>
      <Sidebar />

      <Flex
        minH="100vh"
        justifyContent="center"
        alignItems="center"
        bg={colorMode === 'light' ? 'gray.100' : 'gray.800'}
      >
        <Box
          p={8}
          rounded="md"
          borderWidth="1px"
          boxShadow="lg"
          bg={colorMode === 'light' ? 'white' : 'gray.700'}
          w={['90%', '80%', '50%']}
          maxW="500px"
        >
          <VStack spacing={4}>
            <Heading as="h1" size="lg">Atualizar Usuário</Heading>

            {error && (
              <Alert status="error" mb={4}>
                <AlertIcon />
                <AlertTitle>Erro ao atualizar</AlertTitle>
                <AlertDescription>Verifique os campos e tente novamente.</AlertDescription>
              </Alert>
            )}

            {formSubmitted && (
              <Alert status="success" mb={4}>
                <AlertIcon />
                <AlertTitle>Sucesso!</AlertTitle>
                <AlertDescription>Usuário atualizado com sucesso.</AlertDescription>
              </Alert>
            )}

            <form style={{ width: '100%' }} onSubmit={handleSubmit}>
              <FormControl id="senhaAtual" mb={5} isRequired>
                <FormLabel fontSize={18}>Senha Atual</FormLabel>
                <Input
                  type="password"
                  value={senhaAtual}
                  onChange={(e) => setSenhaAtual(e.target.value)}
                  placeholder="Digite sua senha atual"
                  size="lg"
                  variant="flushed"
                />
              </FormControl>

              <FormControl id="novaSenha" mb={5} isRequired>
                <FormLabel fontSize={18}>Nova Senha</FormLabel>
                <Input
                  type="password"
                  value={novaSenha}
                  onChange={(e) => setNovaSenha(e.target.value)}
                  placeholder="Digite sua nova senha"
                  size="lg"
                  variant="flushed"
                />
              </FormControl>

              <FormControl id="novoLogin" mb={5}>
                <FormLabel fontSize={18}>Novo Login (opcional)</FormLabel>
                <Input
                  type="text"
                  value={novoLogin}
                  onChange={(e) => setNovoLogin(e.target.value)}
                  placeholder="Digite o novo login"
                  size="lg"
                  variant="flushed"
                />
              </FormControl>

              <Button type="submit" colorScheme="blue" size="lg" w="100%" mt={2}>
                Atualizar
              </Button>
            </form>
          </VStack>
        </Box>
      </Flex>
    </div>
  );
}
