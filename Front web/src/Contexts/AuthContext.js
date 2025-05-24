import React, { createContext, useState, useContext, useEffect } from 'react';

// Contexto de autenticação
const AuthContext = createContext();

// Função para converter array de bytes para base64
const arrayBufferToBase64 = (buffer) => {
  let binary = '';
  const bytes = new Uint8Array(buffer);
  for (let i = 0; i < bytes.length; i++) {
    binary += String.fromCharCode(bytes[i]);
  }
  return window.btoa(binary);
};

// Função para decodificar JWT
const parseJwt = (token) => {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    return JSON.parse(atob(base64));
  } catch (e) {
    return null;
  }
};

// Provedor de autenticação
export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(null);
  const [user, setUser] = useState(null);
  const [avatarBase64, setAvatarBase64] = useState(null);

  // Carrega token armazenado
  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    if (storedToken) {
      setToken(storedToken);
      fetchUserInfo(storedToken);
    }
  }, []);



  // Buscar dados do usuário a partir do token
  const fetchUserInfo = async (token) => {
    try {
      const response = await fetch('http://localhost:8080/auth/validate', {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const text = await response.text();
        const data = text ? JSON.parse(text) : null;

        // Se o response não tiver dados, tentar usar o próprio token
        let userId, login;
        if (data && data.id) {
          userId = data.id;
          login = data.login;
        } else {
          const decoded = parseJwt(token);
          userId = decoded?.id || decoded?.sub;
          login = decoded?.sub;
        }

        if (userId) {

          setUser({ id: userId, login });

        }
      } else {
        console.error('Token inválido ou expirado.');
      }
    } catch (error) {
      console.error('Erro ao carregar informações do usuário', error);
    }
  };

  // Função de login
  const login = async (loginData) => {
    try {
      const response = await fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginData),
      });

      if (!response.ok) {
        const errorData = await response.text();
        throw new Error(errorData || 'Login falhou');
      }

      const data = await response.json();
      const { token } = data;

      setToken(token);
      localStorage.setItem('token', token);
      await fetchUserInfo(token);
      return token;
    } catch (error) {
      console.error('Erro ao fazer login:', error);
      throw error;
    }
  };

  // Logout
  const logout = () => {
    localStorage.setItem('token', '')

  };

  return (
    <AuthContext.Provider value={{ token, user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

// Hook de uso do contexto
export const useAuth = () => useContext(AuthContext);
