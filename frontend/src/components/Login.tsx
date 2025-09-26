import { useState } from 'react'
import { api } from '../api/client'

interface LoginProps {
  onLogin: () => void
}

export default function Login({ onLogin }: LoginProps) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    setLoading(true)
    setError('')

    try {
      await api.post('/api/login', { email, password })
      onLogin()
    } catch (err: any) {
      setError(err.response?.data?.error || 'Erro ao fazer login')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="login-container">
      <div className="login-card">
        <h2>📚 Entrar na Biblioteca</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="email">Email:</label>
            <input
              id="email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              disabled={loading}
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Senha:</label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              disabled={loading}
            />
          </div>
          {error && <div className="error">{error}</div>}
          <button type="submit" disabled={loading} className="btn">
            {loading ? 'Entrando...' : 'Entrar'}
          </button>
        </form>
        <div className="login-help">
          <p>Contas de teste:</p>
          <p><strong>Admin:</strong> admin@biblioteca.com / 123456</p>
          <p><strong>Autor:</strong> autor@biblioteca.com / 123456</p>
          <p><strong>Leitor:</strong> leitor@biblioteca.com / 123456</p>
        </div>
      </div>
    </div>
  )
}
