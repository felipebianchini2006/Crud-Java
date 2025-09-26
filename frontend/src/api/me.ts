import { api } from './client'

export interface Me {
  id: number
  name: string
  email: string
  role: 'ADMIN' | 'AUTHOR' | 'READER' | string
  profileImage?: string
}

export async function getMe(): Promise<Me | null> {
  try {
    const { data } = await api.get<{user: Me | null}>('/api/me')
    return data.user
  } catch (e: any) {
    console.error('Erro ao obter dados do usuário:', e)
    return null
  }
}

export async function logout() {
  await api.post('/api/logout')
}

