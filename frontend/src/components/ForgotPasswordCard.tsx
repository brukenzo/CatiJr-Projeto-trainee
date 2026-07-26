import { FormEvent, useState } from 'react'
import { GraduationCapIcon, EmailIcon, ArrowRightIcon } from '../assets/icons'
import InputField from './InputField'
import { api } from '../services/api'
import { Page } from '../types'

interface ForgotPasswordCardProps {
  onNavigate?: (page: Page, email?: string) => void
}

export default function ForgotPasswordCard({ onNavigate }: ForgotPasswordCardProps) {
  const [email, setEmail] = useState('')
  const [mensagemErro, setMensagemErro] = useState('')
  const [mensagemSucesso, setMensagemSucesso] = useState('')
  const [carregando, setCarregando] = useState(false)

  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    setMensagemErro('')
    setMensagemSucesso('')
    setCarregando(true)

    try {
      await api.auth.esqueciSenha(email)
      setMensagemSucesso('Código gerado com sucesso')
      
      setTimeout(() => {
        onNavigate?.('reset-password', email)
      }, 1500)
    } catch (erro: unknown) {
      if (erro instanceof Error) {
        setMensagemErro(erro.message || 'E-mail não encontrado no sistema.')
      } else {
        setMensagemErro('Erro de conexão com o servidor')
      }
    } finally {
      setCarregando(false)
    }
  }

  return (
    <div className="bg-white border border-ui-border rounded-xl drop-shadow-[0px_1px_1px_rgba(0,0,0,0.05)] flex flex-col gap-8 p-6 sm:p-[33px] max-w-md w-full">
      <div className="flex flex-col items-center gap-1 w-full">
        <div className="bg-brand-light flex items-center justify-center w-12 py-[10px] rounded-xl">
          <GraduationCapIcon />
        </div>

        <div className="flex flex-col items-center w-full pt-3">
          <h1 className="font-bold text-[28px] text-brand-primary tracking-[-0.6px] leading-[36px] text-center w-full">
            Recuperar Senha
          </h1>
          <p className="text-base text-ui-medium leading-6 text-center max-w-[280px] pt-1">
            Digite seu e-mail cadastrado para gerarmos um código de verificação.
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

      <form className="flex flex-col gap-6 w-full" onSubmit={handleSubmit}>
        <InputField
          label="E-mail"
          icon={<EmailIcon />}
          type="email"
          placeholder="seu@email.com"
          value={email}
          onChange={(e: any) => setEmail(e.target.value)}
        />

        <div className="pt-2">
          <button
            type="submit"
            disabled={carregando}
            className="w-full flex items-center justify-center gap-2 bg-brand-primary text-white font-medium text-sm leading-5 px-4 py-[10px] rounded-lg hover:bg-indigo-700 active:bg-indigo-800 disabled:opacity-50 transition-colors"
          >
            {carregando ? 'Gerando...' : 'Gerar Código'}
            <ArrowRightIcon />
          </button>
        </div>
      </form>

      <div className="border-t border-ui-border w-full pt-[25px]">
        <div className="flex items-center justify-center gap-1">
          <span className="text-base text-ui-medium leading-6">
            Lembrou sua senha?
          </span>
          <button
            type="button"
            onClick={() => onNavigate?.('login')}
            className="font-medium text-sm text-brand-primary leading-5 hover:underline"
          >
            Voltar ao login
          </button>
        </div>
      </div>
    </div>
  )
}