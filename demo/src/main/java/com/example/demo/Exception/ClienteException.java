package com.example.demo.Exception;



public class ClienteException extends RuntimeException {

    public ClienteException(String message) {
        super(message);
    }

    public ClienteException(String message, Throwable cause) {
        super(message, cause);
    }

   
    public static class ClienteNaoEncontradoException extends ClienteException {
        private final String nomeCliente;

        public ClienteNaoEncontradoException(String nomeCliente) {
            super(null); // mensagem será montada no handler
            this.nomeCliente = nomeCliente;
        }

        public String getNomeCliente() {
            return nomeCliente;
        }
    }

   
    public static class ClienteDuplicadoException extends ClienteException {
        private final String cpfCliente;

        public ClienteDuplicadoException(String cpfCliente) {
            super(null);
            this.cpfCliente = cpfCliente;
        }

        public String getCpfCliente() {
            return cpfCliente;
        }
    }
}
