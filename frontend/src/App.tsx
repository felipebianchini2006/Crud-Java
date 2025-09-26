import { Route, Routes, Link } from 'react-router-dom'
import Home from './pages/Home'
import BooksList from './pages/BooksList'
import BookDetails from './pages/BookDetails'
import MyLoans from './pages/MyLoans'
import Profile from './pages/Profile'
import AdminDashboard from './pages/AdminDashboard'
import AuthorDashboard from './pages/AuthorDashboard'
import Login from './components/Login'
import { useEffect, useState } from 'react'
import { getMe, logout, Me } from './api/me'

export default function App() {
  const [me, setMe] = useState<Me | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    getMe().then((user) => {
      setMe(user)
      setLoading(false)
    })
  }, [])

  async function handleLogin() {
    const user = await getMe()
    setMe(user)
  }

  if (loading) {
    return (
      <div className="app">
        <div className="loading">Carregando Sistema de Biblioteca...</div>
      </div>
    )
  }

  if (!me) {
    return (
      <div className="app">
        <Login onLogin={handleLogin} />
      </div>
    )
  }

  return (
    <div className="app">
      <Navbar me={me} />
      <main className="container">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/books" element={<BooksList />} />
          <Route path="/books/:id" element={<BookDetails />} />
          <Route path="/my-loans" element={<MyLoans />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/admin" element={<AdminDashboard />} />
          <Route path="/author" element={<AuthorDashboard />} />
        </Routes>
      </main>
      <footer className="footer">© 2024 Biblioteca Online</footer>
    </div>
  )
}

function Navbar({ me }: { me: Me }) {
  async function doLogout() {
    await logout()
    window.location.reload()
  }

  return (
    <nav className="navbar">
      <div className="container nav-inner">
        <Link className="brand" to="/">
          📚 Biblioteca Online
        </Link>
        <div className="nav-actions">
          <Link to="/books">Catálogo</Link>
          <Link to="/my-loans" className="btn">Meus Empréstimos</Link>
          {me.role === 'AUTHOR' && <Link to="/author" className="btn secondary">Autor</Link>}
          {me.role === 'ADMIN' && <Link to="/admin" className="btn secondary">Admin</Link>}
          <button className="btn secondary" onClick={doLogout}>Sair</button>
        </div>
      </div>
    </nav>
  )
}
