import { useEffect, useState } from 'react'
import { getMe, Me } from '@/api/me'
import { api, img } from '@/api/client'

export default function AuthorDashboard(){
  const [me, setMe] = useState<Me|null>(null)
  const [books, setBooks] = useState<any[]>([])
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
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}

