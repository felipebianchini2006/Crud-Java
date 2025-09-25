import { useEffect, useState } from 'react'
import { getMe, Me } from '@/api/me'
import { api } from '@/api/client'

export default function Profile(){
  const [me, setMe] = useState<Me | null>(null)
  const [loading, setLoading] = useState(true)
  const [msg, setMsg] = useState<string>('')

  useEffect(()=>{
    getMe().then(u=>{ if(!u){ window.location.href = '/login'; return } setMe(u); setLoading(false) })
  },[])

  async function save(e: React.FormEvent<HTMLFormElement>){
    e.preventDefault()
    if (!me) return
    const fd = new FormData(e.currentTarget)
    const payload: any = {
      name: fd.get('name') || undefined,
      email: fd.get('email') || undefined,
      phone: fd.get('phone') || undefined,
      bio: fd.get('bio') || undefined,
    }
    const pwd = (fd.get('password') as string)||''
    if (pwd) payload.password = pwd
    await api.put(`/api/users/${me.id}`, payload)
    setMsg('Perfil atualizado!')
  }

  async function upload(e: React.FormEvent<HTMLFormElement>){
    e.preventDefault()
    if (!me) return
    const fd = new FormData(e.currentTarget)
    const file = fd.get('file') as File
    if (!file || file.size === 0) { setMsg('Selecione uma imagem'); return }
    const form = new FormData()
    form.append('file', file)
    await api.post(`/api/users/${me.id}/upload-image`, form)
    setMsg('Foto atualizada! Atualize a página para ver a nova imagem.')
  }

  if (loading) return <div className="container"><p style={{color:'#fff'}}>Carregando...</p></div>
  if (!me) return null

  return (
    <div className="container">
      <h2 style={{color:'#fff'}}>Meu Perfil</h2>
      {msg && <div className="card" style={{padding:12, marginBottom:12}}>{msg}</div>}
      <div className="grid" style={{gridTemplateColumns:'1fr 1fr', gap:16}}>
        <div className="card" style={{padding:16}}>
          <form onSubmit={save} className="grid" style={{gridTemplateColumns:'1fr 1fr', gap:12}}>
            <div className="col-12"><label>Nome<input name="name" defaultValue={me.name} /></label></div>
            <div className="col-12"><label>Email<input name="email" defaultValue={me.email} /></label></div>
            <div className="col-6"><label>Telefone<input name="phone" /></label></div>
            <div className="col-6"><label>Nova Senha<input name="password" type="password" placeholder="Opcional"/></label></div>
            <div className="col-12"><label>Bio<textarea name="bio" rows={4}></textarea></label></div>
            <div className="col-12 actions"><button className="btn" type="submit">Salvar</button></div>
          </form>
        </div>
        <div className="card" style={{padding:16}}>
          <form onSubmit={upload}>
            <label>Foto de Perfil
              <input type="file" name="file" accept="image/*" />
            </label>
            <div className="spacer" />
            <button className="btn" type="submit">Enviar</button>
          </form>
        </div>
      </div>
    </div>
  )
}

