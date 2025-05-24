import { Route, Routes } from "react-router-dom";
import Home from "../Pages/Home";
import ListaProdutos from "../Pages/Produtos";




function RoutesApp() {
  return (
    <Routes>
      <Route path="/" element={<Home/>} />
      <Route path="/Produtos" element={<ListaProdutos/>} />
    </Routes>
  );
}

export default RoutesApp;
