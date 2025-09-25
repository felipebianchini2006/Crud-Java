import { api } from './client'

export interface LoanResponse {
  id: number
  bookId: number
  bookTitle: string
  userId: number
  userName: string
  loanDate: string
  dueDate: string
  returnDate?: string
  returned: boolean
}

export interface LoanRequest {
  bookId: number
  readerId: number
  loanDate: string
  dueDate: string
}

export async function getLoans(): Promise<LoanResponse[]> {
  const { data } = await api.get<LoanResponse[]>('/api/loans')
  return data
}

// Nota: SPA não conhece readerId sem endpoint /me. Preferível usar endpoint web /loans/borrow/{bookId}.
export async function borrowViaWebEndpoint(bookId: number) {
  // Executa POST simples e deixa o backend redirecionar
  await api.post(`/loans/borrow/${bookId}`)
}

export async function returnLoan(loanId: number) {
  const today = new Date().toISOString().slice(0, 10)
  await api.patch(`/api/loans/${loanId}/return`, { returnDate: today })
}

