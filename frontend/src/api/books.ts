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

export interface Page<T> { content: T[]; totalPages: number; totalElements: number; number: number }

export async function getBooksPage(params: { page?: number; size?: number; search?: string; genre?: string; author?: string }): Promise<Page<Book>> {
  const { data } = await api.get<Page<Book>>('/api/books/page', { params })
  return data
}
