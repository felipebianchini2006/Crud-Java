import { api } from './client'

export interface Book {
  id: number
  title: string
  author: string
  genre?: string
  description?: string
  coverImage?: string
  available: boolean
}

export async function getBooks(): Promise<Book[]> {
  const { data } = await api.get<Book[]>('/api/books')
  return data
}

export async function getBook(id: number): Promise<Book> {
  const { data } = await api.get(`/api/books/${id}`)
  // API returns BookResponse; adapt if needed
  return data as Book
}

