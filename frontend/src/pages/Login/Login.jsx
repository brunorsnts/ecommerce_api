import { useState } from "react";
import axios from "axios";
import { User, Mail, Lock } from "lucide-react"
import './Login.css';

export function Login() {
    const [email, setEmail] = useState('')
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
            console.error("Erro detalhado:", error);
            alert('Erro ao logar: verifique seu email e senha');
        }
    };

    return (
        <div className="login-wrapper">
            <div className="login-card">

                <div className="profile-icon-container">
                    <User color="white" size={40}/>
                </div>

                <form onSubmit={handleSubmit} style={{ width: '100%' }}>

                    <div className="input-group">
                        <Mail />
                        <input 
                            type="email"
                            placeholder="Email ID"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>

                    <div className="input-group">
                        <Lock />
                        <input 
                            type="password"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required 
                        />
                    </div>

                    <div className="options-row">
                        <label>
                            <input type="checkbox" />
                            Remember me
                        </label>
                        <span className="forgot-password">Forgot Password</span>
                    </div>

                    <button type="submit" className="btn-login">LOGIN</button>
                    <button type="button" className="btn-register">REGISTER</button>
                </form>
            </div>
        </div>
    )
}