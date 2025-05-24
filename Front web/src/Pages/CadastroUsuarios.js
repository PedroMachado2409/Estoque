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
  Select,
} from '@chakra-ui/react';
import Sidebar from '../Componentes/Siderbar';
import { useRegister } from '../Contexts/RegisterContext'; // Importa o context

export default function Cadastro() {
  const { colorMode } = useColorMode();
  const { register } = useRegister(); // Hook do context

  const [role, setRole] = useState('');
  const [name, setName] = useState('');
  const [password, setPassword] = useState('');
  const [formSubmitted, setFormSubmitted] = useState(false);
  const [error, setError] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    if (!name || !password || !role) {
      setError(true);
      return;
    }
  
    try {
      const successMessage = await register({ login: name, password, role });
      setFormSubmitted(true);
      setError(false);

      // Limpa os campos
      setName('');
      setPassword('');
      setRole('');
    } catch (err) {
      setError(true);
      console.error(err.message);  // Exibe o erro no console
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
            <Heading as="h1" size="lg">Cadastrar</Heading>

            {error && (
              <Alert status="error" mb={4}>
                <AlertIcon />
                <AlertTitle>Erro ao cadastrar</AlertTitle>
                <AlertDescription>Preencha todos os campos corretamente.</AlertDescription>
              </Alert>
            )}

            {formSubmitted && (
              <Alert status="success" mb={4}>
                <AlertIcon />
                <AlertTitle>Sucesso!</AlertTitle>
                <AlertDescription>Usuário cadastrado com sucesso.</AlertDescription>
              </Alert>
            )}

            <form style={{ width: '100%' }} onSubmit={handleSubmit}>
              <FormControl id="name" mb={5}>
                <FormLabel fontSize={18}>Nome</FormLabel>
                <Input
                  type="text"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  placeholder="Insira seu nome"
                  size="lg"
                  variant="flushed"
                />
              </FormControl>

              <FormControl id="password" mb={5}>
                <FormLabel fontSize={18}>Senha</FormLabel>
                <Input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Insira sua senha"
                  size="lg"
                  variant="flushed"
                />
              </FormControl>

              <FormControl id="role" mb={5}>
                <FormLabel fontSize={18}>Regra</FormLabel>
                <Select
                  placeholder="Selecione a regra"
                  value={role}
                  onChange={(e) => setRole(e.target.value)}
                  size="lg"
                  variant="flushed"
                >
                  <option value="ADMIN">ADMIN</option>
                  <option value="USER">USER</option>
                </Select>
              </FormControl>

              <Button type="submit" colorScheme="blue" size="lg" w="100%" mt={2}>
                Cadastrar
              </Button>
            </form>
          </VStack>
        </Box>
      </Flex>
    </div>
  );
}
