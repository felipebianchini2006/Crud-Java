import { useEffect, useState } from 'react'
import { getBooks, Book } from '@/api/books'
import { img } from '@/api/client'
import { Link } from 'react-router-dom'

export default function Home() {
  const [books, setBooks] = useState<Book[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    getBooks().then(setBooks).finally(() => setLoading(false))
  }, [])

  const featured = books.slice(0, 6)

  return (
    <div className="home">
      <section className="hero container">
        <h1>Bem-vindo à Biblioteca Online</h1>
        <p>Descubra e gerencie seus livros favoritos.</p>
        <div className="actions" style={{justifyContent:'center'}}>
          <Link to="/books" className="btn">Explorar Catálogo</Link>
          <a className="btn secondary" href="/register">Criar Conta</a>
        </div>
      </section>

      <div className="container">
        <h2 style={{color:'#fff', marginTop:24}}>Destaques</h2>
        {loading ? (
          <p style={{color:'#fff'}}>Carregando...</p>
        ) : (
          <div className="grid cards">
            {featured.map(b => (
              <div key={b.id} className="card">
                <img className="cover" src={img(b.coverImage)} alt={b.title} />
                <div className="body">
                  <h4 style={{margin:'6px 0'}}>{b.title}</h4>
                  <div className="muted">{b.author}</div>
                  <div className="spacer"></div>
                  <div className="actions" style={{justifyContent:'space-between'}}>
                    <span className={`badge ${b.available ? 'green':'red'}`}>
                      {b.available ? 'Disponível' : 'Emprestado'}
                    </span>
                    <Link className="btn" to={`/books/${b.id}`}>Detalhes</Link>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

