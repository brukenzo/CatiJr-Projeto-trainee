export type Page = 'login' | 'signup' | 'dashboard' | 'forgot-password' | 'reset-password'

export interface User {
  id: number
  name: string
  email: string
  matricula: string
  curso: string
  periodo: string
  semestre: string
  password: string
  avatar: string | null
}

export interface MateriaResponse {
  materiaID: string
  codigoMateria: string
  nome: string
  credito: number
  qtdVagas: number
  horario: string
  preRequisito: string
  professor: string
  descricao: string
}

export interface MatriculaResponse {
  matriculaID: string
  materia: MateriaResponse
  status: string
}

export interface LoginResponse {
  token: string
}

export interface AlunoPerfil {
  alunoID: string
  nome: string
  email: string
  creditoDoSemestre: number
}
