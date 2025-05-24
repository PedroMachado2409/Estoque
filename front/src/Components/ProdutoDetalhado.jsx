import React, { useEffect, useState } from 'react';
import {
    Box,
    Card,
    CardHeader,
    CardBody,
    Heading,
    Text,
    Table,
    Thead,
    Tbody,
    Tr,
    Th,
    Td,
    Spinner,
    Button,
    Flex,
    Stack,
    Divider,
    useColorModeValue,
} from '@chakra-ui/react';
import { useProduto } from '../Contexts/ProdutoContext';

const ProdutoDetalhes = ({ produtoId, onClose }) => {
    const {
        obterProdutoPorId,
        obterSaldoDoProduto,
        obterMovimentacoesPorProduto,
        produtoDetalhado,
        saldo,
        movimentacoes,
        loading,
    } = useProduto();

    const [paginaAtual, setPaginaAtual] = useState(1);
    const itensPorPagina = 5;

    // Cores conforme tema
    const bgCard = useColorModeValue('white', 'gray.800');
    const bgHeader = useColorModeValue('gray.50', 'gray.900');
    const textPrimary = useColorModeValue('gray.800', 'gray.100');
    const textSecondary = useColorModeValue('gray.600', 'gray.400');
    const borderColor = useColorModeValue('gray.200', 'gray.700');
    const tableHeaderBg = useColorModeValue('gray.100', 'gray.700');
    const corTextoGeral = useColorModeValue('gray.500', 'gray.50');

    useEffect(() => {
        if (produtoId) {
            obterProdutoPorId(produtoId);
            obterSaldoDoProduto(produtoId);
            obterMovimentacoesPorProduto(produtoId);
        }
    }, [produtoId]);

    const indexInicial = (paginaAtual - 1) * itensPorPagina;
    const indexFinal = indexInicial + itensPorPagina;
    const movimentacoesPaginadas = movimentacoes ? movimentacoes.slice(indexInicial, indexFinal) : [];
    const totalPaginas = movimentacoes ? Math.ceil(movimentacoes.length / itensPorPagina) : 1;

    if (loading || !produtoDetalhado) {
        return (
            <Flex justify="center" align="center" minH="300px">
                <Spinner size="xl" thickness="4px" speed="0.8s" color="blue.500" />
            </Flex>
        );
    }

    const renderValor = (valor) => {
        if (valor === null || valor === undefined) return '—';
        if (typeof valor === 'object') return JSON.stringify(valor);
        return valor.toString();
    };

    return (
        <Box p={6} maxW="900px" mx="auto" borderRadius="lg" boxShadow="2xl" bg={bgCard}>
            <Card
                variant="outline"
                borderRadius="lg"
                borderWidth="1px"
                borderColor={borderColor}
                boxShadow="md"
                mb={10}
                bg={bgCard}
            >
                <CardHeader bg={bgHeader} borderBottomWidth="1px" borderColor={borderColor} borderRadius="lg  lg  0  0">
                    <Heading size="md" color={textPrimary}>Informações Gerais</Heading>
                </CardHeader>
                <CardBody px={8} py={6}>
                    <Stack spacing={5}>
                        <Flex justify="space-between">
                            <Text fontWeight="bold" color={textSecondary}>Nome:</Text>
                            <Text fontSize="xl" fontWeight="medium" color={textPrimary}>{renderValor(produtoDetalhado.nome)}</Text>
                        </Flex>
                        <Flex justify="space-between">
                            <Text fontWeight="bold" color={textSecondary}>Preço:</Text>
                            <Text fontSize="xl" fontWeight="medium" color={textPrimary}>R$ {produtoDetalhado.preco?.toFixed(2) ?? '—'}</Text>
                        </Flex>
                        <Flex justify="space-between">
                            <Text fontWeight="bold" color={textSecondary}>Unidade:</Text>
                            <Text fontSize="xl" fontWeight="medium" color={textPrimary}>{renderValor(produtoDetalhado.unidade)}</Text>
                        </Flex>
                        <Flex justify="space-between">
                            <Text fontWeight="bold" color={textSecondary}>Observação:</Text>
                            <Text fontSize="xl" fontWeight="medium" color={textPrimary}>{produtoDetalhado.observacao ? renderValor(produtoDetalhado.observacao) : 'Nenhuma'}</Text>
                        </Flex>
                        <Flex justify="space-between">
                            <Text fontWeight="bold" color={textSecondary}>Saldo Atual:</Text>
                            <Text fontSize="xl" fontWeight="bold" color={saldo && saldo.saldoAtual < 0 ? 'red.500' : textSecondary}>
                                {saldo && saldo.saldoAtual !== undefined ? renderValor(saldo.saldoAtual) : 'Carregando...'}
                            </Text>
                        </Flex>
                    </Stack>
                </CardBody>
            </Card>

            <Divider borderColor={borderColor} mb={6} />

            <Heading size="lg" mb={4} color="blue.600" fontWeight="bold" letterSpacing="wider">
                Movimentações
            </Heading>

            {(!movimentacoes || movimentacoes.length === 0) ? (
                <Text fontStyle="italic" color={textSecondary} textAlign="center" py={10} fontSize="lg">
                    Nenhuma movimentação encontrada.
                </Text>
            ) : (
                <>
                    <Box
                        borderRadius="md"
                        overflowX="auto"
                        boxShadow="sm"
                        borderWidth="1px"
                        borderColor={borderColor}
                        mb={6}
                    >
                        {/* Aqui removi o colorScheme da tabela */}
                        <Table variant="striped" size="md">
                            <Thead bg={tableHeaderBg}>
                                <Tr>
                                    <Th>Data</Th>
                                    <Th>Quantidade</Th>
                                    <Th>Tipo</Th>
                                    <Th>Observação</Th>
                                </Tr>
                            </Thead>
                            <Tbody>
                                {movimentacoesPaginadas.map((mov, index) => (
                                    <Tr key={index}>
                                        <Td>{renderValor(mov.dtMovimentacao)}</Td>
                                        <Td>{renderValor(mov.quantidade)}</Td>
                                        <Td>{renderValor(mov.tipo)}</Td>
                                        <Td>{renderValor(mov.observacao)}</Td>
                                    </Tr>
                                ))}
                            </Tbody>
                        </Table>
                    </Box>

                    <Flex mt={6} justify="space-between" align="center">
                        <Button
                            onClick={() => setPaginaAtual((p) => Math.max(p - 1, 1))}
                            isDisabled={paginaAtual === 1}
                            colorScheme="blue"
                            variant="outline"
                            _disabled={{ opacity: 0.5, cursor: 'not-allowed' }}
                        >
                            Anterior
                        </Button>
                        <Text fontWeight="medium" color={textSecondary}>
                            Página {paginaAtual} de {totalPaginas}
                        </Text>
                        <Button
                            onClick={() => setPaginaAtual((p) => Math.min(p + 1, totalPaginas))}
                            isDisabled={paginaAtual === totalPaginas}
                            colorScheme="blue"
                            variant="outline"
                            _disabled={{ opacity: 0.5, cursor: 'not-allowed' }}
                        >
                            Próxima
                        </Button>
                    </Flex>
                </>
            )}

            <Flex mt={12} justify="flex-end">
                <Button
                    colorScheme="red"
                    onClick={onClose}
                    size="md"
                    fontWeight="bold"
                    _hover={{ bg: 'red.600' }}
                    borderRadius="md"
                    px={8}
                >
                    Fechar
                </Button>
            </Flex>
        </Box>
    );
};

export default ProdutoDetalhes;
