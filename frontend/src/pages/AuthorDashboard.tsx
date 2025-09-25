import { useEffect, useState } from 'react'
import { getMe, Me } from '@/api/me'
import { api, img } from '@/api/client'

export default function AuthorDashboard(){
  const [me, setMe] = useState<Me|null>(null)
  const [books, setBooks] = useState<any[]>([])
  const [editing, setEditing] = useState<any|null>(null)
  const [history, setHistory] = useState<Record<number, any[]>>({})
  const [loading, setLoading] = useState(true)

  async function load(){
    const { data } = await api.get('/api/books/mine')
    setBooks(data)
  }

  useEffect(()=>{
    (async()=>{
      const u = await getMe(); if(!u){ window.location.href='/login'; return }
      setMe(u)
      await load()
      setLoading(false)
    })()
  },[])

  async function create(e: React.FormEvent<HTMLFormElement>){
    e.preventDefault()
    const fd = new FormData(e.currentTarget)
    await api.post('/api/books', {
      title: fd.get('title'),
      author: fd.get('author'),
      genre: fd.get('genre')
    })
    await load()
    e.currentTarget.reset()
  }

  async function uploadCover(id:number, file: File){
    const form = new FormData(); form.append('file', file)
    await api.post(`/api/books/${id}/cover`, form)
    await load()
  }

  async function toggleAvailability(id:number, available:boolean){
    await api.patch(`/api/books/${id}/availability`, undefined, { params: { available: !available }})
    await load()
  }

  async function removeBook(id:number){
    if (!confirm('Excluir este livro?')) return
    await api.delete(`/api/books/${id}`)
    await load()
  }

  async function saveEdit(e: React.FormEvent<HTMLFormElement>){
    e.preventDefault(); if (!editing) return
    const fd = new FormData(e.currentTarget)
    await api.put(`/api/books/${editing.id}`, {
      title: fd.get('title'), author: fd.get('author'), genre: fd.get('genre')
    })
    setEditing(null); await load()
  }

  async function loadHistory(bookId:number){
    const { data } = await api.get(`/api/loans/book/${bookId}`)
    setHistory(h => ({...h, [bookId]: data}))
  }

  if (loading) return <div className="container"><p style={{color:'#fff'}}>Carregando...</p></div>
  if (!me) return null

  return (
    <div className="container">
      <h2 style={{color:'#fff'}}>Autor</h2>
      <div className="card" style={{padding:16, marginBottom:16}}>
        <h3>Novo Livro</h3>
        <form onSubmit={create} className="grid" style={{gridTemplateColumns:'1fr 1fr 1fr auto', gap:12}}>
          <input name="title" placeholder="Título" required />
          <input name="author" placeholder="Autor" required defaultValue={me.name} />
          <input name="genre" placeholder="Gênero" />
          <button className="btn" type="submit">Criar</button>
        </form>
      </div>

      <div className="grid cards">
        {books.map(b => (
          <div key={b.id} className="card">
            <img className="cover" src={img(b.coverImage)} alt={b.title} />
            <div className="body">
              <h4 style={{margin:'6px 0'}}>{b.title}</h4>
              <div className="muted">{b.author}</div>
              <div className="actions" style={{justifyContent:'space-between'}}>
                <span className={`badge ${b.available?'green':'red'}`}>{b.available?'Disponível':'Emprestado'}</span>
                <label className="btn secondary" style={{cursor:'pointer'}}>Capa
                  <input type="file" accept="image/*" style={{display:'none'}} onChange={e=>{ const f=e.target.files?.[0]; if(f) uploadCover(b.id, f) }} />
                </label>
              </div>
              <div className="actions" style={{gap:8, marginTop:8}}>
                <button className="btn" onClick={()=>setEditing(b)}>Editar</button>
                <button className="btn secondary" onClick={()=>toggleAvailability(b.id, b.available)}>{b.available?'Marcar indisponível':'Marcar disponível'}</button>
                <button className="btn secondary" onClick={()=>loadHistory(b.id)}>Histórico</button>
                <button className="btn secondary" onClick={()=>removeBook(b.id)}>Excluir</button>
              </div>
              {history[b.id] && (
                <div style={{marginTop:8}}>
                  <strong>Empréstimos:</strong>
                  <ul>
                    {history[b.id].map((l:any)=> (
                      <li key={l.id}>{l.userName} — {new Date(l.loanDate).toLocaleDateString('pt-BR')} → {new Date(l.dueDate).toLocaleDateString('pt-BR')} {l.returned? '(devolvido)':''}</li>
                    ))}
                  </ul>
                </div>
              )}
            </div>
          </div>
        ))}
      </div>

      {editing && (
        <div className="card" style={{padding:16, marginTop:16}}>
          <h3>Editar Livro</h3>
          <form onSubmit={saveEdit} className="grid" style={{gridTemplateColumns:'1fr 1fr 1fr auto', gap:12}}>
            <input name="title" defaultValue={editing.title} required />
            <input name="author" defaultValue={editing.author} required />
            <input name="genre" defaultValue={editing.genre||''} />
            <button className="btn" type="submit">Salvar</button>
          </form>
          <div className="actions" style={{marginTop:8}}>
            <button className="btn secondary" onClick={()=>setEditing(null)}>Cancelar</button>
          </div>
        </div>
      )}
    </div>
  )
}
