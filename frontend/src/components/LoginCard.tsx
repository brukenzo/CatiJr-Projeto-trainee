import { FormEvent, useState } from 'react'
import { GraduationCapIcon, EmailIcon, LockIcon, ArrowRightIcon } from '../assets/icons'
import InputField from './InputField'
import { Page } from '../types'

interface LoginCardProps {
  onNavigate?: (page: Page) => void
}

export default function LoginCard({ onNavigate }: LoginCardProps) {
  const [email, setEmail] = useState('')
  const [senha, setSenha] = useState('')
  const [mensagem, setMensagem] = useState('')

  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()

    try{
      setMensagem('')

      const resposta = await fetch('http://localhost:8080/aluno/login', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ email: email, senha: senha })
      })

      if (resposta.ok){
        const token = await resposta.text()
        localStorage.setItem('token_jwt', token)
        onNavigate?.('dashboard')
      }
      else{
        const mensagem = await resposta.text()
        setMensagem(mensagem)
      }

    } catch (erro) {
      setMensagem('Erro de conexão com o servidor')
    }
  }

  return (
    <div className="bg-white border border-ui-border rounded-xl drop-shadow-[0px_1px_1px_rgba(0,0,0,0.05)] flex flex-col gap-8 p-6 sm:p-[33px]">
      <div className="flex flex-col items-center gap-1 w-full">
        <div className="bg-brand-light flex items-center justify-center w-12 py-[10px] rounded-xl">
          <GraduationCapIcon />
        </div>

        <div className="flex flex-col items-center w-full pt-3">
          <h1 className="font-bold text-[30px] text-brand-primary tracking-[-0.6px] leading-[38px] text-center w-full">
            MatriculaFácil
          </h1>
          <p className="text-base text-ui-medium leading-6 text-center">
            Acesse o Portal do Aluno
          </p>
        </div>
      </div>

      {mensagem && (
        <div className="bg-red-50 text-red-600 p-3 rounded-lg text-sm border border-red-100 text-center font-medium">
          {mensagem}
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

        <InputField
          label="Senha"
          icon={<LockIcon />}
          type="password"
          placeholder="••••••••"
          rightElement="Esqueceu a senha?"
          value={senha}
          onChange={(e: any) => setSenha(e.target.value)}
        />

        <div className="pt-2">
          <button
            type="submit"
            className="w-full flex items-center justify-center gap-2 bg-brand-primary text-white font-medium text-sm leading-5 px-4 py-2 rounded-lg hover:bg-indigo-700 active:bg-indigo-800 transition-colors"
          >
            Entrar
            <ArrowRightIcon />
          </button>
        </div>
      </form>

      <div className="border-t border-ui-border w-full pt-[25px]">
        <div className="flex items-center justify-center gap-1">
          <span className="text-base text-ui-medium leading-6">
            Não tem uma conta?
          </span>
          <button
            type="button"
            onClick={() => onNavigate?.('signup')}
            className="font-medium text-sm text-brand-primary leading-5 hover:underline"
          >
            Cadastre-se
          </button>
        </div>
      </div>
    </div>
  )
}