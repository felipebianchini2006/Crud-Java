import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { getBook, Book } from '@/api/books'
import { getMe } from '@/api/me'
import { api } from '@/api/client'
import { img } from '@/api/client'

export default function BookDetails(){
  const { id } = useParams()
  const [book, setBook] = useState<Book | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(()=>{
    if (!id) return
    getBook(Number(id)).then(setBook).catch(e=>setError(e.message)).finally(()=>setLoading(false))
  },[id])

  async function borrow(){
    if (!book) return
    if (!confirm('Deseja emprestar este livro por 14 dias?')) return
    try {
      const me = await getMe()
      if (!me) { window.location.href = '/login'; return }
      const loanDate = new Date()
      const dueDate = new Date(); dueDate.setDate(loanDate.getDate()+14)
      await api.post('/api/loans', {
        bookId: book.id,
        readerId: me.id,
        loanDate: loanDate.toISOString().slice(0,10),
        dueDate: dueDate.toISOString().slice(0,10)
      })
      alert('Livro emprestado com sucesso!')
      window.location.href = '/app/my-loans'
    } catch (e:any) {
      alert(e.message || 'Erro ao emprestar.')
    }
  }

  if (loading) return <div className="container"><p>Carregando...</p></div>
  if (error) return <div className="container"><p>Erro: {error}</p></div>
  if (!book) return <div className="container"><p>Livro não encontrado.</p></div>

  return (
    <div className="container">
      <div className="card" style={{overflow:'hidden'}}>
        <img className="cover" src={img(book.coverImage)} alt={book.title} />
        <div className="body">
          <Link to="/books" className="btn secondary">← Voltar</Link>
          <h2 style={{margin:'8px 0'}}>{book.title}</h2>
          <div className="muted">{book.author}</div>
          {book.genre && <div className="badge" style={{background:'#eef', marginTop:8}}>{book.genre}</div>}
          {book.description && <p style={{marginTop:12}}>{book.description}</p>}
          <div className="spacer" />
          <div className="actions" style={{justifyContent:'space-between'}}>
            <span className={`badge ${book.available ? 'green':'red'}`}>{book.available?'Disponível':'Emprestado'}</span>
            {book.available ? (
              <button className="btn" onClick={borrow}>Emprestar</button>
            ) : (
              <a className="btn secondary" href="/reader/my-loans">Ver meus empréstimos</a>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
