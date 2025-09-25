import { useEffect, useState } from 'react'
import { getMe, Me } from '@/api/me'
import { api, img } from '@/api/client'
import type { LoanResponse } from '@/api/loans'

export default function MyLoans(){
  const [me, setMe] = useState<Me | null>(null)
  const [loans, setLoans] = useState<LoanResponse[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(()=>{
    (async ()=>{
      const u = await getMe()
      if (!u) { window.location.href = '/login'; return }
      setMe(u)
      const { data } = await api.get<LoanResponse[]>(`/api/loans/user/${u.id}`)
      setLoans(data)
      setLoading(false)
    })()
  },[])

  async function returnLoan(loanId: number){
    if (!confirm('Confirmar devolução do livro?')) return
    const today = new Date().toISOString().slice(0,10)
    await api.patch(`/api/loans/${loanId}/return`, { returnDate: today })
    setLoans(ls => ls.map(l => l.id===loanId ? { ...l, returned: true, returnDate: today } : l))
  }

  return (
    <div className="container">
      <h2 style={{color:'#fff'}}>Meus Empréstimos</h2>
      {loading ? <p style={{color:'#fff'}}>Carregando...</p> : (
        loans.length === 0 ? (
          <div className="card" style={{padding:24}}>
            <p>Você ainda não fez nenhum empréstimo.</p>
          </div>
        ) : (
          <div className="card" style={{overflowX:'auto'}}>
            <table style={{width:'100%', borderCollapse:'collapse'}}>
              <thead>
                <tr>
                  <th style={{textAlign:'left', padding:12}}>Livro</th>
                  <th>Empréstimo</th>
                  <th>Vencimento</th>
                  <th>Devolução</th>
                  <th>Status</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {loans.map(loan => (
                  <tr key={loan.id}>
                    <td style={{padding:12}}>
                      <div style={{display:'flex', alignItems:'center', gap:8}}>
                        <span>📘</span>
                        <strong>{loan.bookTitle}</strong>
                      </div>
                    </td>
                    <td style={{textAlign:'center'}}>{formatDate(loan.loanDate)}</td>
                    <td style={{textAlign:'center'}}>{formatDate(loan.dueDate)}</td>
                    <td style={{textAlign:'center'}}>{loan.returned ? formatDate(loan.returnDate||'') : '-'}</td>
                    <td style={{textAlign:'center'}}>
                      {loan.returned ? <span className="badge green">Devolvido</span> : (
                        isOverdue(loan.dueDate) ? <span className="badge red">Em atraso</span> : <span className="badge" style={{background:'#fff3cd', color:'#664d03'}}>Em andamento</span>
                      )}
                    </td>
                    <td style={{textAlign:'center'}}>
                      {!loan.returned && (
                        <button className="btn" onClick={()=>returnLoan(loan.id)}>Devolver</button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )
      )}
    </div>
  )
}

function formatDate(d: string){ if(!d) return '-'; const dt = new Date(d); return dt.toLocaleDateString('pt-BR') }
function isOverdue(d: string){ return new Date() > new Date(d) }

