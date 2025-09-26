import { useEffect, useState } from 'react'
import { getMe, Me } from '@/api/me'
import { api } from '@/api/client'
import ConfirmDialog from '@/components/ConfirmDialog'

interface UserRow { id:number; name:string; email:string; role:string; profileImage?:string }

type Book = { id:number; title:string; author:string; genre?:string; available:boolean }
type Loan = { id:number; bookId:number; bookTitle:string; userName:string; loanDate:string; dueDate:string; returned:boolean }

export default function AdminDashboard(){
  const [me, setMe] = useState<Me|null>(null)
  const [tab, setTab] = useState<'users'|'books'|'loans'|'reports'>('users')
  const [users, setUsers] = useState<UserRow[]>([])
  const [books, setBooks] = useState<Book[]>([])
  const [loans, setLoans] = useState<Loan[]>([])
  const [editUser, setEditUser] = useState<UserRow|null>(null)
  const [editBook, setEditBook] = useState<Book|null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(()=>{
    (async()=>{
      const u = await getMe(); if (!u) { window.location.href='/login'; return }
      setMe(u)
      const [us, bs, ls] = await Promise.all([
        api.get('/api/users'),
        api.get('/api/books'),
        api.get('/api/loans')
      ])
      setUsers(us.data); setBooks(bs.data); setLoans(ls.data)
      setLoading(false)
    })()
  },[])

  async function saveUser(u: UserRow){
    await api.put(`/api/users/${u.id}`, { name: u.name, email: u.email })
  }
  const [confirm, setConfirm] = useState<{open:boolean; title:string; msg:string; action: ()=>void}>({open:false, title:'', msg:'', action: ()=>{}})
  function ask(title:string, msg:string, action:()=>void){ setConfirm({open:true, title, msg, action}) }
  async function deleteUser(id:number){ ask('Excluir usuário', 'Tem certeza que deseja excluir?', async ()=>{ await api.delete(`/api/users/${id}`); const us=await api.get('/api/users'); setUsers(us.data) }) }
  async function changeRole(id:number, role:string){ await api.post(`/api/users/${id}/role`, undefined, { params: { role } }); const us=await api.get('/api/users'); setUsers(us.data) }
  async function toggleAvailability(b:Book){ await api.patch(`/api/books/${b.id}/availability`, undefined, { params: { available: !b.available } }); const bs=await api.get('/api/books'); setBooks(bs.data) }
  async function saveBook(b:Book){ await api.put(`/api/books/${b.id}`, { title: b.title, author: b.author, genre: b.genre }); const bs=await api.get('/api/books'); setBooks(bs.data) }
  async function addBook(e: React.FormEvent<HTMLFormElement>){ e.preventDefault(); const fd=new FormData(e.currentTarget); await api.post('/api/books', { title: fd.get('title'), author: fd.get('author'), genre: fd.get('genre') }); const bs=await api.get('/api/books'); setBooks(bs.data); e.currentTarget.reset() }
  async function deleteBook(id:number){ ask('Excluir livro', 'Tem certeza que deseja excluir?', async ()=>{ await api.delete(`/api/books/${id}`); const bs=await api.get('/api/books'); setBooks(bs.data) }) }
  async function returnLoan(id:number){ const today=new Date().toISOString().slice(0,10); await api.patch(`/api/loans/${id}/return`, { returnDate: today }); const ls=await api.get('/api/loans'); setLoans(ls.data) }
  async function deleteLoan(id:number){ ask('Excluir empréstimo', 'Tem certeza que deseja excluir?', async ()=>{ await api.delete(`/api/loans/${id}`); const ls=await api.get('/api/loans'); setLoans(ls.data) }) }

  if (loading) return <div className="container"><p style={{color:'#fff'}}>Carregando...</p></div>
  if (!me || me.role!=='ADMIN') return <div className="container"><p style={{color:'#fff'}}>Acesso restrito.</p></div>

  return (
    <div className="container">
      <h2 style={{color:'#fff'}}>Admin</h2>
      <div className="grid" style={{gridTemplateColumns:'1fr 1fr 1fr', gap:12}}>
        <div className="card" style={{padding:16}}>
          <strong>Usuários</strong>
          <div style={{fontSize:'2rem'}}>{users.length}</div>
        </div>
        <div className="card" style={{padding:16}}>
          <strong>Livros</strong>
          <div style={{fontSize:'2rem'}}>{books.length}</div>
        </div>
        <div className="card" style={{padding:16}}>
          <strong>Empréstimos</strong>
          <div style={{fontSize:'2rem'}}>{loans.length}</div>
        </div>
      </div>

      <div className="spacer" />
      <div className="card" style={{padding:12}}>
        <div className="actions" style={{gap:8}}>
          <button className="btn" onClick={()=>setTab('users')}>Usuários</button>
          <button className="btn" onClick={()=>setTab('books')}>Livros</button>
          <button className="btn" onClick={()=>setTab('loans')}>Empréstimos</button>
          <button className="btn" onClick={()=>setTab('reports')}>Relatórios</button>
        </div>
      </div>

      {tab==='users' && (
        <div className="card" style={{padding:16}}>
          <h3>Usuários</h3>
          <table style={{width:'100%', borderCollapse:'collapse'}}>
            <thead><tr><th style={{textAlign:'left'}}>Nome</th><th>Email</th><th>Role</th><th>Ações</th></tr></thead>
            <tbody>
            {users.map(u=> (
              <tr key={u.id}>
                <td style={{padding:8}}><input defaultValue={u.name} onChange={e=>u.name=e.target.value} /></td>
                <td style={{textAlign:'center'}}><input defaultValue={u.email} onChange={e=>u.email=e.target.value} /></td>
                <td style={{textAlign:'center'}}>
                  <select defaultValue={u.role} onChange={e=>changeRole(u.id, e.target.value)}>
                    <option>READER</option><option>AUTHOR</option><option>ADMIN</option>
                  </select>
                </td>
                <td style={{textAlign:'center'}}>
                  <button className="btn" onClick={()=>saveUser(u)}>Salvar</button>
                  <button className="btn secondary" onClick={()=>deleteUser(u.id)}>Excluir</button>
                </td>
              </tr>
            ))}
            </tbody>
          </table>
        </div>
      )}

      {tab==='books' && (
        <div className="card" style={{padding:16}}>
          <h3>Livros</h3>
          <form onSubmit={addBook} className="actions" style={{gap:8, marginBottom:8}}>
            <input name="title" placeholder="Título" required />
            <input name="author" placeholder="Autor" required />
            <input name="genre" placeholder="Gênero" />
            <button className="btn" type="submit">Adicionar</button>
          </form>
          <table style={{width:'100%', borderCollapse:'collapse'}}>
            <thead><tr><th>Título</th><th>Autor</th><th>Gênero</th><th>Disp.</th><th>Ações</th></tr></thead>
            <tbody>
              {books.map(b => (
                <tr key={b.id}>
                  <td><input defaultValue={b.title} onChange={e=>b.title=e.target.value} /></td>
                  <td><input defaultValue={b.author} onChange={e=>b.author=e.target.value} /></td>
                  <td><input defaultValue={b.genre||''} onChange={e=>b.genre=e.target.value} /></td>
                  <td style={{textAlign:'center'}}>{b.available? 'Sim':'Não'}</td>
                  <td className="actions" style={{gap:8, justifyContent:'center'}}>
                    <button className="btn" onClick={()=>saveBook(b)}>Salvar</button>
                    <button className="btn secondary" onClick={()=>toggleAvailability(b)}>{b.available?'Indisp.':'Disp.'}</button>
                    <button className="btn secondary" onClick={()=>deleteBook(b.id)}>Excluir</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {tab==='loans' && (
        <div className="card" style={{padding:16}}>
          <h3>Empréstimos</h3>
          <table style={{width:'100%', borderCollapse:'collapse'}}>
            <thead><tr><th>Livro</th><th>Usuário</th><th>Empréstimo</th><th>Vencimento</th><th>Status</th><th>Ações</th></tr></thead>
            <tbody>
              {loans.map(l => (
                <tr key={l.id}>
                  <td>{l.bookTitle}</td>
                  <td>{l.userName}</td>
                  <td>{new Date(l.loanDate).toLocaleDateString('pt-BR')}</td>
                  <td>{new Date(l.dueDate).toLocaleDateString('pt-BR')}</td>
                  <td>{l.returned? 'Devolvido' : 'Em andamento'}</td>
                  <td className="actions" style={{gap:8, justifyContent:'center'}}>
                    {!l.returned && <button className="btn" onClick={()=>returnLoan(l.id)}>Devolver</button>}
                    <button className="btn secondary" onClick={()=>deleteLoan(l.id)}>Excluir</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {tab==='reports' && (
        <div className="card" style={{padding:16}}>
          <h3>Relatórios</h3>
          <p>Total de usuários: {users.length}</p>
          <p>Total de livros: {books.length}</p>
          <p>Total de empréstimos: {loans.length}</p>
          <p>Em andamento: {loans.filter(l=>!l.returned).length}</p>
          <p>Em atraso: {loans.filter(l=>!l.returned && new Date() > new Date(l.dueDate)).length}</p>
        </div>
      )}
      <ConfirmDialog open={confirm.open} title={confirm.title} message={confirm.msg} onClose={()=>setConfirm({...confirm, open:false})} onConfirm={confirm.action} />
    </div>
  )
}
