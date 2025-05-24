import {
  Box,
  Button,
  FormControl,
  FormLabel,
  Input,
  Modal,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  Select,
  useToast,
  VStack,
} from '@chakra-ui/react'
import { useContext, useState } from 'react'
import { ClientesContext } from '../Contexts/ClienteContext'
import { ProdutosContext } from '../Contexts/ProductContext'
import { FaPlusCircle } from 'react-icons/fa'
import { FaTimesCircle } from 'react-icons/fa'

export function ModalNovaVenda({ isOpen, onClose, onSave }) {
  const { clientes } = useContext(ClientesContext)
  const { produtos } = useContext(ProdutosContext)
  const toast = useToast()

  const [clienteId, setClienteId] = useState('')
  const [produtoId, setProdutoId] = useState('')
  const [quantidade, setQuantidade] = useState(1)
  const [itens, setItens] = useState([])

  function adicionarItem() {
    const produto = produtos.find((p) => p.id === produtoId)
    if (!produto) return

    const item = {
      id: crypto.randomUUID(),
      produto,
      quantidade,
      preco: produto.preco,
    }

    setItens([...itens, item])
    setProdutoId('')
    setQuantidade(1)
  }

  function salvarVenda() {
    const cliente = clientes.find((c) => c.id === clienteId)
    if (!cliente || itens.length === 0) {
      toast({
        title: 'Erro',
        description: 'Cliente ou itens inválidos.',
        status: 'error',
        duration: 3000,
        isClosable: true,
      })
      return
    }

    const total = itens.reduce((acc, item) => acc + item.preco * item.quantidade, 0)

    const novaVenda = {
      id: crypto.randomUUID(),
      cliente,
      data: new Date().toISOString(),
      total,
      itens,
    }

    onSave(novaVenda)
    setClienteId('')
    setProdutoId('')
    setQuantidade(1)
    setItens([])
    onClose()
  }

  return (
    <Modal isOpen={isOpen} onClose={onClose} size="xl">
      <ModalOverlay />
      <ModalContent borderRadius="2xl" p={4}>
        <ModalHeader>Nova Venda</ModalHeader>
        <ModalBody>
          <VStack spacing={4} align="stretch">
            <FormControl>
              <FormLabel>Cliente</FormLabel>
              <Select
                placeholder="Selecione o cliente"
                value={clienteId}
                onChange={(e) => setClienteId(e.target.value)}
              >
                {clientes.map((cliente) => (
                  <option key={cliente.id} value={cliente.id}>
                    {cliente.nome}
                  </option>
                ))}
              </Select>
            </FormControl>

            <FormControl>
              <FormLabel>Produto</FormLabel>
              <Select
                placeholder="Selecione o produto"
                value={produtoId}
                onChange={(e) => setProdutoId(e.target.value)}
              >
                {produtos.map((produto) => (
                  <option key={produto.id} value={produto.id}>
                    {produto.nome} - R$ {produto.preco.toFixed(2)}
                  </option>
                ))}
              </Select>
            </FormControl>

            <FormControl>
              <FormLabel>Quantidade</FormLabel>
              <Input
                type="number"
                value={quantidade}
                min={1}
                onChange={(e) => setQuantidade(Number(e.target.value))}
              />
            </FormControl>

            <Button
              colorScheme="teal"
              leftIcon={<FaPlusCircle />}
              onClick={adicionarItem}
            >
              Adicionar Item
            </Button>

            <Box>
              {itens.map((item) => (
                <Box
                  key={item.id}
                  p={3}
                  borderWidth="1px"
                  borderRadius="lg"
                  mt={2}
                  shadow="sm"
                >
                  {item.quantidade}x {item.produto.nome} — R$ {item.preco.toFixed(2)}
                </Box>
              ))}
            </Box>
          </VStack>
        </ModalBody>
        <ModalFooter>
          <Button leftIcon={<FaTimesCircle />} onClick={onClose} mr={3}>
            Cancelar
          </Button>
          <Button colorScheme="blue" onClick={salvarVenda}>
            Salvar
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  )
}
