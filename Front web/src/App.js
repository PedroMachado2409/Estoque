
import { BrowserRouter } from 'react-router-dom';
import { ChakraProvider } from '@chakra-ui/react';
import RoutesApp from './Routes/RoutesApp';
import { AuthProvider } from './Contexts/AuthContext';
import { RegisterProvider } from './Contexts/RegisterContext';
import { UpdateUserProvider } from './Contexts/UpdateUserContext';
import { ProductProvider } from './Contexts/ProductContext';
import { ClienteProvider } from './Contexts/ClienteContext';
import { VendaProvider } from './Contexts/VendaContext';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <VendaProvider>
          <ClienteProvider>
            <ProductProvider>
              <UpdateUserProvider>
                <RegisterProvider>
                  <ChakraProvider>
                    <RoutesApp />
                  </ChakraProvider>
                </RegisterProvider>
              </UpdateUserProvider>
            </ProductProvider>
          </ClienteProvider>
          </VendaProvider>
      </AuthProvider>
   
    </BrowserRouter >
  )
}
export default App;
