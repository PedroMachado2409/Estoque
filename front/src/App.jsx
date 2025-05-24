
import { BrowserRouter } from 'react-router-dom';
import { ChakraProvider } from '@chakra-ui/react';
import RoutesApp from './Routes/RoutesApp';
import { ProdutoProvider } from './Contexts/ProdutoContext';


function App() {
  return (
    <BrowserRouter>
      <ProdutoProvider>
        <ChakraProvider>
          <RoutesApp />
        </ChakraProvider>
      </ProdutoProvider>
    </BrowserRouter >
  )
}
export default App;
