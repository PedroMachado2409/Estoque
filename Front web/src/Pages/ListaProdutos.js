import React, { useState, useEffect } from 'react';
import {
  Box,
  Input,
  IconButton,
  Table,
  Thead,
  Tbody,
  Tr,
  Th,
  Td,
  Spinner,
  useColorMode,
  Flex,
  Button,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  useDisclosure,
  useToast,
} from '@chakra-ui/react';
import { FiSearch, FiRefreshCw, FiArrowLeft, FiArrowRight } from 'react-icons/fi';
import { useProdutos } from '../Contexts/ProductContext';
import Sidebar from '../Componentes/Siderbar';

const ProdutoList = () => {
  const { produtos, loading, fetchProdutos, adicionarProduto } = useProdutos();
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredProdutos, setFilteredProdutos] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [productsPerPage] = useState(10);
  const { colorMode } = useColorMode();
  const { isOpen, onOpen, onClose } = useDisclosure();
  const toast = useToast();

  const [novoProduto, setNovoProduto] = useState({
    nome: '',
    estoque: 0,
    unidade: '',
  });

  useEffect(() => {
    setFilteredProdutos(produtos);
  }, [produtos]);

  const handleSearch = () => {
    if (!searchTerm) {
      setFilteredProdutos(produtos);
      return;
    }
    const filtered = produtos.filter((produto) =>
      produto.nome.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setFilteredProdutos(filtered);
    setCurrentPage(1);
  };

  const handleRefresh = async () => {
    await fetchProdutos();
    setSearchTerm('');
    setCurrentPage(1);
  };

  const handleCadastrar = async () => {
    try {
      await adicionarProduto(novoProduto);
      toast({
        title: 'Produto cadastrado com sucesso!',
        status: 'success',
        duration: 3000,
        isClosable: true,
      });
      onClose();
      setNovoProduto({ nome: '', estoque: 0, unidade: '' });
    } catch (error) {
      toast({
        title: 'Erro ao cadastrar produto',
        status: 'error',
        duration: 3000,
        isClosable: true,
      });
    }
  };

  const indexOfLastProduct = currentPage * productsPerPage;
  const indexOfFirstProduct = indexOfLastProduct - productsPerPage;
  const currentProducts = filteredProdutos.slice(indexOfFirstProduct, indexOfLastProduct);

  const handleNextPage = () => {
    if (currentPage * productsPerPage < filteredProdutos.length) {
      setCurrentPage(currentPage + 1);
    }
  };

  const handlePreviousPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  return (
    <div>
      <Sidebar />
      <Flex
        minH="100vh"
        alignItems="center"
        justifyContent="center"
        bg={colorMode === 'light' ? 'gray.100' : 'gray.800'}
      >
        <Box w="80%" maxW="1000px" p={5}>
          <Flex
            mb={4}
            alignItems="center"
            gap={2}
            flexWrap="wrap"
            direction={{ base: 'column', md: 'row' }}
          >
            <Input
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              placeholder="Pesquisar produtos"
              size="lg"
              flex="1"
              minW="200px"
            />
            <IconButton
              icon={<FiSearch />}
              onClick={handleSearch}
              aria-label="Pesquisar"
              colorScheme="blue"
            />
            <IconButton
              icon={<FiRefreshCw />}
              onClick={handleRefresh}
              aria-label="Atualizar"
              colorScheme="green"
            />
            <Button colorScheme="teal" onClick={onOpen}>
              Cadastrar Produto
            </Button>
          </Flex>

          {loading ? (
            <Box textAlign="center" mt={10}>
              <Spinner size="xl" />
            </Box>
          ) : (
            <Box
              overflowX="auto"
              borderRadius="md"
              borderWidth="1px"
              bg={colorMode === 'light' ? 'gray.50' : 'gray.600'}
              p={4}
            >
              <Table variant="simple">
                <Thead>
                  <Tr>
                    <Th>ID</Th>
                    <Th>Nome</Th>
                    <Th>Estoque</Th>
                    <Th>Unidade</Th>
                    <Th>Data de Cadastro</Th>
                  </Tr>
                </Thead>
                <Tbody>
                  {currentProducts.map((produto) => (
                    <Tr key={produto.id}>
                      <Td>{produto.id}</Td>
                      <Td>{produto.nome}</Td>
                      <Td>{produto.estoque}</Td>
                      <Td>{produto.unidade}</Td>
                      <Td>{new Date(produto.dtCadastro).toLocaleDateString()}</Td>
                    </Tr>
                  ))}
                </Tbody>
              </Table>
              <Flex mt={4} justifyContent="space-between">
                <Button
                  onClick={handlePreviousPage}
                  isDisabled={currentPage === 1}
                  colorScheme="blue"
                >
                  <FiArrowLeft />
                </Button>
                <Button
                  onClick={handleNextPage}
                  isDisabled={currentPage * productsPerPage >= filteredProdutos.length}
                  colorScheme="blue"
                >
                  <FiArrowRight />
                </Button>
              </Flex>
            </Box>
          )}
        </Box>
      </Flex>

      {/* Modal de Cadastro */}
      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Cadastrar Novo Produto</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <Input
              placeholder="Nome"
              value={novoProduto.nome}
              onChange={(e) => setNovoProduto({ ...novoProduto, nome: e.target.value })}
              mb={3}
            />
            <Input
              placeholder="Estoque"
              type="number"
              value={novoProduto.estoque}
              onChange={(e) => setNovoProduto({ ...novoProduto, estoque: Number(e.target.value) })}
              mb={3}
            />
            <Input
              placeholder="Unidade"
              value={novoProduto.unidade}
              onChange={(e) => setNovoProduto({ ...novoProduto, unidade: e.target.value })}
              mb={3}
            />
          </ModalBody>
          <ModalFooter>
            <Button colorScheme="blue" mr={3} onClick={handleCadastrar}>
              Salvar
            </Button>
            <Button variant="ghost" onClick={onClose}>
              Cancelar
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </div>
  );
};

export default ProdutoList;
