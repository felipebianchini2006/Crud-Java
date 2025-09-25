import axios from 'axios'

export const api = axios.create({
  baseURL: '/',
  withCredentials: true,
})

api.interceptors.response.use(
  (res) => res,
  (err) => {
    // Map common errors to user-friendly messages
    if (err.response) {
      const status = err.response.status
      if (status === 401) {
        err.message = 'Você precisa estar autenticado.'
      } else if (status === 403) {
        err.message = 'Acesso negado.'
      } else if (status >= 500) {
        err.message = 'Erro no servidor. Tente novamente.'
      }
    }
    return Promise.reject(err)
  }
)

export function img(src?: string) {
  return src && src.trim().length > 0 ? src : '/static/images/default-book-cover.jpg'
}

