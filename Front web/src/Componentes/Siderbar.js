import {
  Box,
  VStack,
  Text,
  Icon,
  Switch,
  IconButton,
  Avatar,
  Divider
} from '@chakra-ui/react';
import {
  FaHome,
  FaSignOutAlt,
  FaUsers,
  FaArchive,
  FaDollarSign,
  FaAngleDoubleLeft,
  FaAngleDoubleRight,
  FaPaperPlane,
  FaUser,
  FaUserCog
} from 'react-icons/fa';
import { useColorMode } from '@chakra-ui/color-mode';
import { Link } from 'react-router-dom';
import { useState } from 'react';
import { useAuth } from '../Contexts/AuthContext';

const sidebarItems = [
  { id: 1, name: 'Home', icon: FaHome, link: '/home' },
  { id: 2, name: 'Clientes', icon: FaUsers, link: '/Clientes' },
  { id: 3, name: 'Estoque', icon: FaArchive, link: '/produtos' },
  { id: 4, name: 'Financeiro', icon: FaDollarSign, link: '/home' },
  { id: 5, name: 'Vendas', icon: FaPaperPlane, link: '/Vendas' },
  { id: 6, name: 'Cadastro de Usuarios', icon: FaUser, link: '/Cadastro' },
  { id: 7, name: 'Configurar Usuario', icon: FaUserCog, link: '/AtualizarUsuario' },
];

const Sidebar = () => {
  const { colorMode, toggleColorMode } = useColorMode();
  const [expanded, setExpanded] = useState(true);
  const { logout, user } = useAuth();

  const handleToggleSidebar = () => {
    setExpanded(!expanded);
  };

  const handleLogout = () => {
    logout();
  };

  return (
    <Box
      w={expanded ? "250px" : "60px"}
      bg={colorMode === 'dark' ? 'gray.900' : 'gray.100'}
      color={colorMode === 'dark' ? 'white' : 'gray.500'}
      h="100vh"
      py="6"
      px="3"
      position="fixed"
      left="0"
      top="0"
      boxShadow="2px 0px 5px rgba(0, 0, 0, 0.5)"
      transition="width 0.3s ease"
    >
      <IconButton
        icon={expanded ? <FaAngleDoubleLeft /> : <FaAngleDoubleRight />}
        onClick={handleToggleSidebar}
        aria-label={expanded ? "Recolher Sidebar" : "Expandir Sidebar"}
        alignSelf="flex-start"
        mb="4"
      />

      <VStack spacing="8" align="stretch">
       

        <Divider />

        <Box>
          {sidebarItems.map(item => (
            <Link key={item.id} to={item.link}>
              <Box
                display="flex"
                alignItems="center"
                cursor="pointer"
                mb={2}
                marginBottom={10}
                _hover={{ color: colorMode === 'dark' ? 'grey' : 'gray.800' }}
              >
                <Icon as={item.icon} boxSize={8} mr="4" />
                <Text fontSize="xl">{expanded ? item.name : ""}</Text>
              </Box>
            </Link>
          ))}
        </Box>

        <Divider />

        <Link to='/'>
          <Box display={expanded ? "flex" : "none"} alignItems="center" cursor="pointer" mb={2} _hover={{ color: colorMode === 'dark' ? 'grey' : 'blue.800' }}>
            <Icon as={FaSignOutAlt} boxSize={8} mr="4" />
            <Text fontSize="xl" onClick={handleLogout}>Sair</Text>
          </Box>
        </Link>
      </VStack>

      <Switch isChecked={colorMode === 'dark'} onChange={toggleColorMode} />
    </Box>
  );
};

export default Sidebar;
