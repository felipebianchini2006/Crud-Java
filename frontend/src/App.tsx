import { Route, Routes, Link, useNavigate } from 'react-router-dom'
import Home from './pages/Home'
import BooksList from './pages/BooksList'
import BookDetails from './pages/BookDetails'
import MyLoans from './pages/MyLoans'

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
  const navigate = useNavigate()
  return (
    <nav className="navbar">
      <div className="container nav-inner">
        <Link className="brand" to="/">
          📚 Biblioteca Online
        </Link>
        <div className="nav-actions">
          <Link to="/books">Catálogo</Link>
          <a href="/dashboard" className="btn">Dashboard</a>
          <a href="/login" className="btn secondary">Entrar</a>
        </div>
      </div>
    </nav>
  )
}

