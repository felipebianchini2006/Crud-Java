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
    const { data } = await api.get<Me>('/api/me')
    return data
  } catch (e: any) {
    return null
  }
}

export async function logout() {
  await api.post('/api/logout')
}

