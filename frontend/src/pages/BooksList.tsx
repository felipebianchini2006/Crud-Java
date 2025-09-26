import { useEffect, useMemo, useState } from 'react'
import { getBooksPage, Book, Page } from '@/api/books'
import { Link, useSearchParams } from 'react-router-dom'
import { api, img } from '@/api/client'
import { getMe, Me } from '@/api/me'

export default function BooksList(){
  const [params, setParams] = useSearchParams()
  const [pageData, setPageData] = useState<Page<Book>>({ content: [], totalPages: 1, totalElements: 0, number: 0 })
  const [loading, setLoading] = useState(true)
  const [me, setMe] = useState<Me | null>(null)

  useEffect(()=>{ 
    getBooks().then(setAll).finally(()=>setLoading(false))
    getMe().then(setMe)
  },[])

  const search = params.get('search') || ''
  const genre = params.get('genre') || ''
  const author = params.get('author') || ''
  const current = parseInt(params.get('page')||'1',10)
  const size = parseInt(params.get('size')||'12',10)

  useEffect(()=>{
    setLoading(true)
    getBooksPage({ page: current-1, size, search, genre, author })
      .then(setPageData)
      .finally(()=>setLoading(false))
  }, [current, size, search, genre, author])

  function submitFilters(e: React.FormEvent<HTMLFormElement>){
    e.preventDefault()
    const fd = new FormData(e.currentTarget)
    const s = (fd.get('search') as string)||''
    const g = (fd.get('genre') as string)||''
    const a = (fd.get('author') as string)||''
    const next = new URLSearchParams()
    if (s) next.set('search', s)
    if (g) next.set('genre', g)
    if (a) next.set('author', a)
    next.set('page','1')
    setParams(next)
  }

  return (
    <div className="container">
      <h2 style={{color:'#fff'}}>Catálogo</h2>
      <div className="filters">
        <form onSubmit={submitFilters} className="grid" style={{gridTemplateColumns:'repeat(12,1fr)', gap:12}}>
          <div className="col-6">
            <label>Busca
              <input name="search" defaultValue={search} placeholder="Título ou Autor" />
            </label>
          </div>
          <div className="col-3">
            <label>Gênero
              <input name="genre" defaultValue={genre} placeholder="Ex: Ficção" />
            </label>
          </div>
          <div className="col-3">
            <label>Autor
              <input name="author" defaultValue={author} placeholder="Nome" />
            </label>
          </div>
          <div className="col-12 actions">
            <button className="btn" type="submit">Filtrar</button>
            <button className="btn secondary" type="button" onClick={()=>setParams(new URLSearchParams())}>Limpar</button>
          </div>
        </form>
      </div>

      {loading ? <p style={{color:'#fff'}}>Carregando...</p> : (
        pageData.content.length === 0 ? (
          <div className="card" style={{padding:24}}>
            <p>Nenhum livro encontrado.</p>
          </div>
        ) : (
          <div className="grid cards">
            {pageData.content.map(b => (
              <div className="card" key={b.id}>
                <img className="cover" src={img(b.coverImage)} alt={b.title} />
                <div className="body">
                  <h4 style={{margin:'6px 0'}}>{b.title}</h4>
                  <div className="muted">{b.author}</div>
                  {b.genre && <div className="badge" style={{background:'#eef'}}>{b.genre}</div>}
                  <div className="spacer" />
                  <div className="actions" style={{justifyContent:'space-between'}}>
                    <span className={`badge ${b.available ? 'green':'red'}`}>{b.available?'Disponível':'Emprestado'}</span>
                    <Link className="btn" to={`/books/${b.id}`}>Detalhes</Link>
                  </div>
                  {me && b.available && (
                    <div className="actions" style={{justifyContent:'flex-end', marginTop:8}}>
                      <button className="btn secondary" onClick={async ()=>{
                        const loanDate = new Date()
                        const dueDate = new Date(); dueDate.setDate(loanDate.getDate()+14)
                        await api.post('/api/loans', {
                          bookId: b.id,
                          readerId: me.id,
                          loanDate: loanDate.toISOString().slice(0,10),
                          dueDate: dueDate.toISOString().slice(0,10)
                        })
                        alert('Livro emprestado!')
                      }}>Emprestar</button>
                    </div>
                  )}
                </div>
              </div>
            ))}
          </div>
        )
      )}

      <div className="spacer" />
      <Pagination total={pageData.totalPages} current={pageData.number+1} onPage={(p)=>{
        const next = new URLSearchParams(params)
        next.set('page', String(p))
        setParams(next)
      }} />
    </div>
  )
}

function Pagination({ total, current, onPage }:{ total:number; current:number; onPage:(p:number)=>void}){
  if (total <= 1) return null
  const seq = Array.from({length: total},(_,i)=>i+1)
  return (
    <div className="card" style={{padding:12, marginTop:12}}>
      <div className="actions" style={{justifyContent:'center', flexWrap:'wrap'}}>
        <button className="btn secondary" disabled={current===1} onClick={()=>onPage(current-1)}>‹</button>
        {seq.map(i => (
          <button key={i} className="btn secondary" style={{background: i===current ? 'rgba(26,115,232,.15)':'transparent'}} onClick={()=>onPage(i)}>{i}</button>
        ))}
        <button className="btn secondary" disabled={current===total} onClick={()=>onPage(current+1)}>›</button>
      </div>
    </div>
  )
}
