import {
  Box,
  Button,
  Flex,
  Heading,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  Table,
  Tbody,
  Td,
  Text,
  Th,
  Thead,
  Tr,
  useDisclosure,
} from '@chakra-ui/react'
import { FaPlusCircle, FaTrashAlt } from 'react-icons/fa' // Usando ícones do react-icons
import { useContext } from 'react'
import { VendasContext } from '../Contexts/VendaContext'
import { ModalNovaVenda } from '../Pages/ModalNovaVenda'

export function Vendas() {
  const {
    vendas,
    vendaSelecionada,
    adicionarVenda,
    removerItemVenda,
    selecionarVenda,
  } = useContext(VendasContext)

  const {
    isOpen: isOpenModalVenda,
    onOpen: onOpenModalVenda,
    onClose: onCloseModalVenda,
  } = useDisclosure()

  return (
    <Box p={6}>
      <Flex justify="space-between" align="center" mb={6}>
        <Heading size="lg">Vendas</Heading>
        <Button colorScheme="blue" leftIcon={<FaPlusCircle />} onClick={onOpenModalVenda}>
          Nova Venda
        </Button>
      </Flex>

      <Box borderWidth="1px" borderRadius="2xl" overflow="hidden" shadow="md">
        <Table variant="simple">
          <Thead bg="gray.100">
            <Tr>
              <Th>Cliente</Th>
              <Th>Data</Th>
              <Th>Valor Total</Th>
              <Th>Ações</Th>
            </Tr>
          </Thead>
          <Tbody>
            {vendas.map((venda) => (
              <Tr key={venda.id} _hover={{ bg: 'gray.50' }}>
                <Td>{venda.cliente.nome}</Td>
                <Td>{new Date(venda.data).toLocaleDateString()}</Td>
                <Td>R$ {venda.total.toFixed(2)}</Td>
                <Td>
                  <Button
                    size="sm"
                    colorScheme="teal"
                    onClick={() => selecionarVenda(venda)}
                  >
                    Ver Itens
                  </Button>
                </Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
      </Box>

      {/* Modal para mostrar itens da venda */}
      <Modal isOpen={!!vendaSelecionada} onClose={() => selecionarVenda(null)} size="xl">
        <ModalOverlay />
        <ModalContent borderRadius="2xl" p={4}>
          <ModalHeader>Itens da Venda</ModalHeader>
          <ModalBody>
            {vendaSelecionada?.itens.map((item) => (
              <Flex
                key={item.id}
                justify="space-between"
                align="center"
                p={3}
                borderBottom="1px solid"
                borderColor="gray.200"
              >
                <Box>
                  <Text fontWeight="bold">{item.produto.nome}</Text>
                  <Text fontSize="sm">Quantidade: {item.quantidade}</Text>
                  <Text fontSize="sm">Preço: R$ {item.preco.toFixed(2)}</Text>
                </Box>
                <IconButton
                  icon={<FaTrashAlt />} // Usando ícone de delete do react-icons
                  aria-label="Remover item"
                  colorScheme="red"
                  size="sm"
                  onClick={() => removerItemVenda(vendaSelecionada.id, item.id)}
                />
              </Flex>
            ))}
          </ModalBody>
          <ModalFooter>
            <Button onClick={() => selecionarVenda(null)}>Fechar</Button>
          </ModalFooter>
        </ModalContent>
      </Modal>

      {/* Modal de nova venda */}
      <ModalNovaVenda isOpen={isOpenModalVenda} onClose={onCloseModalVenda} onSave={adicionarVenda} />
    </Box>
  )
}
