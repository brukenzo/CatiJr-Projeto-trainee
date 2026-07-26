import type { LoginResponse, MateriaResponse, MatriculaResponse, AlunoPerfil } from '../types'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

function headers(comToken?: boolean): Record<string, string> {
  const h: Record<string, string> = { 'Content-Type': 'application/json' }
  if (comToken) {
    const token = localStorage.getItem('token_jwt')
    if (token) h['Authorization'] = `Bearer ${token}`
  }
  return h
}

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const resposta = await fetch(`${API_URL}${path}`, options)
  const texto = await resposta.text()

  if (!resposta.ok) {
    throw new Error(texto || `Erro ${resposta.status}`)
  }

  return texto ? (JSON.parse(texto) as T) : (undefined as T)
}

export const api = {
  auth: {
    login(email: string, senha: string) {
      return request<LoginResponse>('/aluno/login', {
        method: 'POST',
        headers: headers(),
        body: JSON.stringify({ email, senha }),
      })
    },

    cadastrar(nome: string, email: string, senha: string) {
      return request<void>('/aluno', {
        method: 'POST',
        headers: headers(),
        body: JSON.stringify({ nome, email, senha }),
      })
    },

    esqueciSenha(email: string) {
      return request<void>('/aluno/esqueci-senha', {
        method: 'POST',
        headers: headers(),
        body: JSON.stringify({ email }),
      })
    },

    redefinirSenha(email: string, codigo: string, novaSenha: string) {
      return request<void>('/aluno/redefinir-senha', {
        method: 'PUT',
        headers: headers(),
        body: JSON.stringify({ email, codigo, novaSenha }),
      })
    },

    perfil() {
      return request<AlunoPerfil>('/aluno/perfil', {
        headers: headers(true),
      })
    },
  },

  materia: {
    listar() {
      return request<MateriaResponse[]>('/materia', {
        headers: headers(true),
      })
    },
  },

  matricula: {
    inscrever(materiaID: string) {
      return request<MatriculaResponse>('/matricula/inscrever', {
        method: 'POST',
        headers: headers(true),
        body: JSON.stringify({ materiaID }),
      })
    },

    minhasMatriculas() {
      return request<MatriculaResponse[]>('/matricula/minhas-matriculas', {
        headers: headers(true),
      })
    },

    desinscrever(id: string) {
      return request<void>(`/matricula/${id}`, {
        method: 'DELETE',
        headers: headers(true),
      })
    },
  },
}
