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
    Select,
} from '@chakra-ui/react';
import { FiSearch, FiRefreshCw, FiArrowLeft, FiArrowRight } from 'react-icons/fi';
import { useClientes } from '../Contexts/ClienteContext';
import Sidebar from '../Componentes/Siderbar';

const ClienteList = () => {
    const { clientes, usuarios, loading, fetchClientes, cadastrarCliente } = useClientes();
    const [searchTerm, setSearchTerm] = useState('');
    const [filteredClientes, setFilteredClientes] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [clientesPerPage] = useState(10);
    const { colorMode } = useColorMode();
    const { isOpen, onOpen, onClose } = useDisclosure();
    const toast = useToast();

    const [novoCliente, setNovoCliente] = useState({
        nome: '',
        cpf: '',
        email: '',
        dtNascimento: '',
        endereco: '',
        cep: '',
        idUsuario: '',
    });

    useEffect(() => {
        setFilteredClientes(clientes);
    }, [clientes]);

    const handleSearch = () => {
        if (!searchTerm) {
            setFilteredClientes(clientes);
            return;
        }
        const filtered = clientes.filter((cliente) =>
            cliente.nome.toLowerCase().includes(searchTerm.toLowerCase())
        );
        setFilteredClientes(filtered);
        setCurrentPage(1);
    };

    const handleRefresh = async () => {
        await fetchClientes();
        setSearchTerm('');
        setCurrentPage(1);
    };

    const formatDate = (dateString) => {
        if (!dateString) return '';
        const date = new Date(dateString);
        const day = date.getDate().toString().padStart(2, '0');
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const year = date.getFullYear();
        return `${day}/${month}/${year}`;
    };

    const handleCadastrar = async () => {
        try {
            const clienteToSend = {
                ...novoCliente,
                dtNascimento: novoCliente.dtNascimento ? formatDate(novoCliente.dtNascimento) : '',
            };
            console.log("Dados do novo cliente para enviar:", clienteToSend); // Verifique o formato da data
            await cadastrarCliente(clienteToSend);
            toast({
                title: 'Cliente cadastrado com sucesso!',
                status: 'success',
                duration: 3000,
                isClosable: true,
            });
            onClose();
            setNovoCliente({ nome: '', cpf: '', email: '', dtNascimento: '', endereco: '', cep: '', idUsuario: '' });
        } catch (error) {
            toast({
                title: 'Erro ao cadastrar cliente',
                status: 'error',
                duration: 3000,
                isClosable: true,
            });
        }
    };

    const indexOfLastCliente = currentPage * clientesPerPage;
    const indexOfFirstCliente = indexOfLastCliente - clientesPerPage;
    const currentClientes = filteredClientes.slice(indexOfFirstCliente, indexOfLastCliente);

    const handleNextPage = () => {
        if (currentPage * clientesPerPage < filteredClientes.length) {
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
                            placeholder="Pesquisar cliente"
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
                            Cadastrar Cliente
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
                                        <Th>CPF</Th>
                                       
                                        <Th>Data de Nascimento</Th>
                                        <Th>Data de Cadastro</Th>
                                    </Tr>
                                </Thead>
                                <Tbody>
                                    {currentClientes.map((cliente) => (
                                        <Tr key={cliente.id}>
                                            <Td>{cliente.id}</Td>
                                            <Td>{cliente.nome}</Td>
                                            <Td>{cliente.cpf}</Td>
                                      
                                            <Td>{new Date(cliente.dtNascimento).toLocaleDateString()}</Td>
                                            <Td>{new Date(cliente.dtCadastro).toLocaleDateString()}</Td>
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
                                    isDisabled={currentPage * clientesPerPage >= filteredClientes.length}
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
                    <ModalHeader>Cadastrar Novo Cliente</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody>
                        <Input
                            placeholder="Nome"
                            value={novoCliente.nome}
                            onChange={(e) => setNovoCliente({ ...novoCliente, nome: e.target.value })}
                            mb={3}
                        />
                        <Input
                            placeholder="CPF"
                            value={novoCliente.cpf}
                            onChange={(e) => setNovoCliente({ ...novoCliente, cpf: e.target.value })}
                            mb={3}
                        />
                        <Input
                            placeholder="Data de Nascimento"
                            type="date"
                            value={novoCliente.dtNascimento}
                            onChange={(e) => setNovoCliente({ ...novoCliente, dtNascimento: e.target.value })}
                            mb={3}
                        />
                        <Input
                            placeholder="Endereço"
                            value={novoCliente.endereco}
                            onChange={(e) => setNovoCliente({ ...novoCliente, endereco: e.target.value })}
                            mb={3}
                        />
                        <Input
                            placeholder="CEP"
                            value={novoCliente.cep}
                            onChange={(e) => setNovoCliente({ ...novoCliente, cep: e.target.value })}
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

export default ClienteList;