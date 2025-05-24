import { Route, Routes } from "react-router-dom";
import LoginScreen from "../Pages/LoginScreen";
import Home from "../Pages/Home";
import Cadastro from "../Pages/CadastroUsuarios";
import AtualizarUsuario from "../Pages/AtualizarUsuario";
import ListaProdutos from "../Pages/ListaProdutos";
import ClienteList from "../Pages/Cliente";
import VendaList from "../Pages/VendaList";



function RoutesApp() {
  return (
    <Routes>
      <Route path="/" element={<LoginScreen />} />
      <Route path="/Home" element={<Home/>} />
      <Route path="/Cadastro" element={<Cadastro/>} />
      <Route path="/AtualizarUsuario" element={<AtualizarUsuario/>} />
      <Route path="/Produtos" element={<ListaProdutos/>} />
      <Route path="/Clientes" element={<ClienteList/>} />
      <Route path="/Vendas" element={<VendaList/>} />
  
    </Routes>
  );
}

export default RoutesApp;
