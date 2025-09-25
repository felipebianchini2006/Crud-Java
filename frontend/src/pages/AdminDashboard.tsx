import { useEffect, useState } from 'react'
import { getMe, Me } from '@/api/me'
import { api } from '@/api/client'

interface UserRow { id:number; name:string; email:string; role:string; profileImage?:string }

export default function AdminDashboard(){
  const [me, setMe] = useState<Me|null>(null)
  const [users, setUsers] = useState<UserRow[]>([])
  const [books, setBooks] = useState<any[]>([])
  const [loans, setLoans] = useState<any[]>([])
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

  async function toggleUser(id:number){
    // tentativa simples: se existe no array, considerar toggle por /activate ou /deactivate
    const found = users.find(u=>u.id===id)
    if (!found) return
    const active = true // não temos campo active no DTO; chamar deactivate por padrão
    try {
      await api.post(`/api/users/${id}/deactivate`)
      alert('Usuário desativado (se estava ativo).')
    } catch {}
  }

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
      <div className="card" style={{padding:16}}>
        <h3>Usuários</h3>
        <table style={{width:'100%', borderCollapse:'collapse'}}>
          <thead><tr><th style={{textAlign:'left'}}>Nome</th><th>Email</th><th>Role</th><th>Ações</th></tr></thead>
          <tbody>
          {users.map(u=> (
            <tr key={u.id}>
              <td style={{padding:8}}>{u.name}</td>
              <td style={{textAlign:'center'}}>{u.email}</td>
              <td style={{textAlign:'center'}}>{u.role}</td>
              <td style={{textAlign:'center'}}>
                <button className="btn secondary" onClick={()=>toggleUser(u.id)}>Ativar/Desativar</button>
              </td>
            </tr>
          ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}

