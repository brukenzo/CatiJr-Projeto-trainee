import { FormEvent, useState } from 'react'
import { GraduationCapIcon, EmailIcon, LockIcon, EyeOffIcon } from '../assets/icons'
import InputField from './InputField'
import { api } from '../services/api'
import { Page } from '../types'

interface ResetPasswordCardProps {
  onNavigate?: (page: Page) => void
  emailInicial?: string
}

export default function ResetPasswordCard({ onNavigate, emailInicial = '' }: ResetPasswordCardProps) {
  const [email, setEmail] = useState(emailInicial)
  const [codigo, setCodigo] = useState('')
  const [senha, setSenha] = useState('')
  const [confirmarSenha, setConfirmarSenha] = useState('')

  const [showPassword, setShowPassword] = useState(false)
  const [mensagemErro, setMensagemErro] = useState('')
  const [mensagemSucesso, setMensagemSucesso] = useState('')
  const [carregando, setCarregando] = useState(false)

  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    setMensagemErro('')
    setMensagemSucesso('')

    if (senha !== confirmarSenha) {
      setMensagemErro('As senhas digitadas não coincidem')
      return
    }

    setCarregando(true)

    try {
      await api.auth.redefinirSenha(email, codigo, senha)
      setMensagemSucesso('Senha redefinida com sucesso')
      
      setTimeout(() => {
        onNavigate?.('login')
      }, 2000)
    } catch (erro: unknown) {
      if (erro instanceof Error) {
        setMensagemErro(erro.message || 'Código inválido ou expirado.')
      } else {
        setMensagemErro('Erro de conexão com o servidor')
      }
    } finally {
      setCarregando(false)
    }
  }

  return (
    <div className="bg-white border border-ui-border rounded-xl drop-shadow-[0px_1px_1px_rgba(0,0,0,0.05)] flex flex-col gap-6 p-6 sm:p-[33px] max-w-md w-full">
      <div className="flex flex-col items-center gap-1 w-full">
        <div className="bg-brand-light flex items-center justify-center w-12 py-[10px] rounded-xl">
          <GraduationCapIcon />
        </div>

        <div className="flex flex-col items-center w-full pt-2">
          <h1 className="font-bold text-[28px] text-brand-primary tracking-[-0.6px] leading-[36px] text-center w-full">
            Criar Nova Senha
          </h1>
          <p className="text-base text-ui-medium leading-6 text-center max-w-[300px] pt-1">
            Digite o código de verificação e escolha sua nova senha.
          </p>
        </div>
      </div>

      {mensagemErro && (
        <div className="bg-red-50 text-red-600 p-3 rounded-lg text-sm border border-red-100 text-center font-medium">
          {mensagemErro}
        </div>
      )}

      {mensagemSucesso && (
        <div className="bg-green-50 text-green-700 p-3 rounded-lg text-sm border border-green-200 text-center font-medium">
          {mensagemSucesso}
        </div>
      )}

      <form className="flex flex-col gap-5 w-full" onSubmit={handleSubmit}>
        <InputField
          label="E-mail Institucional"
          icon={<EmailIcon />}
          type="email"
          placeholder="joao.silva@aluno.edu.br"
          value={email}
          onChange={(e: any) => setEmail(e.target.value)}
        />

        <InputField
          label="Código de Verificação"
          type="text"
          placeholder="Ex: 123456"
          value={codigo}
          onChange={(e: any) => setCodigo(e.target.value)}
        />

        <InputField
          label="Nova Senha"
          icon={<LockIcon />}
          type={showPassword ? 'text' : 'password'}
          placeholder="••••••••"
          value={senha}
          onChange={(e: any) => setSenha(e.target.value)}
          rightIcon={
            <button
              type="button"
              onClick={() => setShowPassword((v) => !v)}
              className="flex items-center justify-center"
            >
              <EyeOffIcon />
            </button>
          }
        />

        <div className="pb-1">
          <InputField
            label="Confirmar Nova Senha"
            icon={<LockIcon />}
            type="password"
            placeholder="••••••••"
            value={confirmarSenha}
            onChange={(e: any) => setConfirmarSenha(e.target.value)}
          />
        </div>

        <button
          type="submit"
          disabled={carregando}
          className="w-full bg-brand-primary text-white font-medium text-sm leading-5 py-3 rounded-lg hover:bg-indigo-700 active:bg-indigo-800 disabled:opacity-50 transition-colors"
        >
          {carregando ? 'Salvando...' : 'Redefinir Senha'}
        </button>
      </form>

      <div className="border-t border-ui-border w-full pt-4">
        <div className="flex items-center justify-center gap-1">
          <button
            type="button"
            onClick={() => onNavigate?.('login')}
            className="font-medium text-sm text-brand-primary leading-5 hover:underline"
          >
            Voltar para o login
          </button>
        </div>
      </div>
    </div>
  )
}