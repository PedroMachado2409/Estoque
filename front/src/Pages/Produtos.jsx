import React, { useState, useEffect } from "react";
import CadastroProduto from "../Components/CadastroProduto";
import ProdutoDetalhes from "../Components/ProdutoDetalhado";
import EditarProdutoForm from "../Components/EditarProdutoForm";
import { FiSearch, FiRefreshCw, FiArrowLeft, FiArrowRight, FiEye, FiEdit2  } from "react-icons/fi";
import { useProduto } from "../Contexts/ProdutoContext";
import Sidebar from "../Components/Siderbar";
import {
    useColorMode,
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
    Flex,
    Button,
    Text,
    Modal,
    ModalOverlay,
    ModalContent,
    ModalHeader,
    ModalCloseButton,
    ModalBody,
    useDisclosure
} from "@chakra-ui/react";

const ListaProdutos = () => {
    const { produto, loading, localizarProdutos } = useProduto();
    const [produtosFiltrados, setProdutosFiltrados] = useState([]);
    const [produtoSelecionadoParaEditar, setProdutoSelecionadoParaEditar] = useState(null);
    const [produtoPesquisado, setProdutoPesquisado] = useState('');
    
    // Modal cadastro produto
    const { isOpen: isCadastroOpen, onOpen: onCadastroOpen, onClose: onCadastroClose } = useDisclosure();
    // Modal detalhes produto
    const { isOpen: isDetalhesOpen, onOpen: onDetalhesOpen, onClose: onDetalhesClose } = useDisclosure();
    //Modal de editar produto
    const {isOpen: isEditOpen, onOpen: onEditOpen, onClose: onEditClose} = useDisclosure();

    const [produtoSelecionadoId, setProdutoSelecionadoId] = useState(null);

    const [pagina, setPagina] = useState(1);
    const [produtosPorPagina] = useState(10);
    const { colorMode } = useColorMode();

    useEffect(() => {
        setProdutosFiltrados(produto);
    }, [produto]);

    const proximaPagina = () => {
        if (pagina * produtosPorPagina < produtosFiltrados.length) {
            setPagina(pagina + 1);
        }
    };

    const paginaAnterior = () => {
        if (pagina > 1) {
            setPagina(pagina - 1);
        }
    };

    const pesquisarProdutos = () => {
        if (!produtoPesquisado) {
            setProdutosFiltrados(produto); 
            setPagina(1);
            return;
        }
        const filtro = produto.filter((p) =>
            p.nome.toLowerCase().includes(produtoPesquisado.toLowerCase())
        );
        setProdutosFiltrados(filtro);
        setPagina(1);
    };

    const abrirDetalhesProduto = (id) => {
        setProdutoSelecionadoId(id);
        onDetalhesOpen();
    };

   const abrirModalDeEditar = (id) => {
    const produtoObj = produto.find((p) => p.id === id);
    setProdutoSelecionadoParaEditar(produtoObj);
    setProdutoSelecionadoId(id);
    console.log(id);
    onEditOpen();
  };

    const ultimoProduto = pagina * produtosPorPagina;
    const primeiroProduto = ultimoProduto - produtosPorPagina;
    const produtosListados = produtosFiltrados.slice(primeiroProduto, ultimoProduto);

    return (
        <>
            <Sidebar />
            <Flex
                minH="100vh"
                alignItems="flex-start"
                justifyContent="center"
                pt={10}
                bg={colorMode === "light" ? "gray.100" : "gray.800"}
            >
                <Box w="80%" maxW="1000px">
                    {/* Botão no canto superior direito */}
                    <Flex justifyContent="flex-end" mb={2}>
                        <Button colorScheme="teal" onClick={onCadastroOpen}>
                            Cadastrar Produto
                        </Button>
                    </Flex>

                    {/* Barra de pesquisa abaixo do botão */}
                    <Flex mb={4}>
                        <Input
                            placeholder="Pesquisar Produtos"
                            size="lg"
                            mr={4}
                            flex="1"
                            value={produtoPesquisado}
                            onChange={(e) => setProdutoPesquisado(e.target.value)}
                        />
                        <IconButton
                            icon={<FiSearch />}
                            aria-label="Pesquisar"
                            colorScheme="blue"
                            onClick={pesquisarProdutos}
                        />
                        <IconButton
                            icon={<FiRefreshCw />}
                            aria-label="Atualizar"
                            colorScheme="green"
                            ml={2}
                            onClick={localizarProdutos}
                        />
                    </Flex>

                    {loading ? (
                        <Box textAlign="center" mt={10}>
                            <Spinner size="xl" />
                        </Box>
                    ) : (
                        <Box
                            overflow="auto"
                            borderRadius="md"
                            borderWidth="1px"
                            bg={colorMode === "light" ? "gray.50" : "gray.600"}
                            p={4}
                        >
                            <Table variant="simple">
                                <Thead>
                                    <Tr>
                                        <Th>ID</Th>
                                        <Th>NOME</Th>
                                        <Th>UNIDADE</Th>
                                        <Th>PREÇO</Th>
                                        <Th>AÇÕES</Th> {/* Nova coluna */}
                                    </Tr>
                                </Thead>
                                <Tbody>
                                    {produtosListados.map((produto) => (
                                        <Tr key={produto.id}>
                                            <Td>{produto.id}</Td>
                                            <Td>{produto.nome}</Td>
                                            <Td>{produto.unidade}</Td>
                                            <Td>R$: {produto.preco}</Td>
                                            <Td>
                                                <Button
                                                    size="sm"
                                                    colorScheme="blue"
                                                    onClick={() => abrirDetalhesProduto(produto.id)}
                                                >
                                                    <FiEye />
                                                </Button>
                                                <Button size="sm"
                                                        colorScheme="green"
                                                        onClick={() => abrirModalDeEditar(produto.id)}
                                                        ml={2}
                                                 >
                                                    <FiEdit2 />
                                                 </Button>
                                            </Td>
                                        </Tr>
                                    ))}
                                </Tbody>
                            </Table>

                            <Flex mt={4} alignItems="center" justifyContent="space-between">
                                <Button onClick={paginaAnterior} isDisabled={pagina === 1} colorScheme="blue">
                                    <FiArrowLeft />
                                </Button>

                                <Text fontWeight="bold" fontSize="lg" textAlign="center" flex="1">
                                    Página {pagina}
                                </Text>

                                <Button
                                    onClick={proximaPagina}
                                    isDisabled={pagina * produtosPorPagina >= produtosFiltrados.length}
                                    colorScheme="blue"
                                >
                                    <FiArrowRight />
                                </Button>
                            </Flex>
                        </Box>
                    )}
                </Box>
            </Flex>

            {/* Modal com o componente de cadastro */}
            <Modal isOpen={isCadastroOpen} onClose={onCadastroClose} size='xl' isCentered >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Cadastrar Produto</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody p={6}>
                        <CadastroProduto onClose={onCadastroClose} />
                    </ModalBody>
                </ModalContent>
            </Modal>

            {/* Modal detalhes do produto - tela cheia */}
            <Modal isOpen={isDetalhesOpen} onClose={onDetalhesClose} size='5xl'>
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Detalhes do Produto</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody p={6}>
                        <ProdutoDetalhes produtoId={produtoSelecionadoId} onClose={onDetalhesClose} />
                    </ModalBody>
                </ModalContent>
            </Modal>

           <Modal isOpen={isEditOpen} onClose={onEditClose} size="xl" isCentered>
            <ModalOverlay />
            <ModalContent>
              <ModalHeader>Editar Produto</ModalHeader>
              <ModalCloseButton />
              <ModalBody pb={6}>
                {produtoSelecionadoParaEditar ? (
                  <EditarProdutoForm
                    produtoId={produtoSelecionadoId}
                    produtoInicial={produtoSelecionadoParaEditar}
                    onClose={onEditClose}
                  />
                ) : (
                  <Text>Carregando...</Text>
                )}
              </ModalBody>
            </ModalContent>
          </Modal>
        </>
    );
};

export default ListaProdutos;
