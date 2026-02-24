import { useState } from "react";
import axios from "axios";

export function Login() {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();

        const loginData = { email, password };

        try {

            const response = await axios.post('http://localhost:8080/login', loginData);

            const token = response.data.accessToken;

            localStorage.setItem('token', token);

            alert('Conectado com sucesso!');
        } catch (error) {
            alert('Erro ao logar: verifique seu email e senha')
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Acessar Catálogo</h2>
            <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
            <input 
                type="password"
                placeholder="Senha"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button type="submit">Entrar</button>
        </form>
    );
}
