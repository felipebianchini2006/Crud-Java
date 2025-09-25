import { Route, Routes, Link } from 'react-router-dom'
import Home from './pages/Home'
import BooksList from './pages/BooksList'
import BookDetails from './pages/BookDetails'
import MyLoans from './pages/MyLoans'
import { useEffect, useState } from 'react'
import { getMe, logout, Me } from './api/me'

export default function App() {
  return (
    <div className="app">
      <Navbar />
      <main className="container">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/books" element={<BooksList />} />
          <Route path="/books/:id" element={<BookDetails />} />
          <Route path="/my-loans" element={<MyLoans />} />
        </Routes>
      </main>
      <footer className="footer">© 2024 Biblioteca Online</footer>
    </div>
  )
}

function Navbar() {
  const [me, setMe] = useState<Me | null>(null)
  useEffect(()=>{ getMe().then(setMe) },[])
  async function doLogout(){
    await logout()
    window.location.href = '/app'
  }
  return (
    <nav className="navbar">
      <div className="container nav-inner">
        <Link className="brand" to="/">
          📚 Biblioteca Online
        </Link>
        <div className="nav-actions">
          <Link to="/books">Catálogo</Link>
          {me ? (
            <>
              <a href="/dashboard" className="btn">Dashboard</a>
              <button className="btn secondary" onClick={doLogout}>Sair</button>
            </>
          ) : (
            <>
              <a href="/login" className="btn secondary">Entrar</a>
              <a href="/register" className="btn">Cadastrar</a>
            </>
          )}
        </div>
      </div>
    </nav>
  )
}
