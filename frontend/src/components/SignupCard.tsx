import { FormEvent, useState } from 'react'
import { GraduationCapIcon, EyeOffIcon } from '../assets/icons'
import InputField from './InputField'
import { Page } from '../types'

interface SignupCardProps {
  onNavigate?: (page: Page) => void
}

export default function SignupCard({ onNavigate }: SignupCardProps) {
  const [nome, setNome] = useState('')
  const [email, setEmail] = useState('')
  const [senha, setSenha] = useState('')
  const [confirmarSenha, setConfirmarSenha] = useState('')

  const [showPassword, setShowPassword] = useState(false)
  const [mensagemErro, setMensagemErro] = useState('')
  const [mensagemSucesso, setMensagemSucesso] = useState('')
  
  async function handleSubmit(e: FormEvent<HTMLFormElement>){
    e.preventDefault()

    setMensagemErro('')
    setMensagemSucesso('')

    if (senha !== confirmarSenha) {
      setMensagemErro('As senhas digitadas não coincidem')
      return 
    }

    try {
      const resposta = await fetch('http://localhost:8080/aluno/', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ nome: nome, email: email, senha: senha })
      })

      if (resposta.ok) {
        setMensagemSucesso('Conta criada com sucesso')
        
        setTimeout(() => {
          onNavigate?.('login')
        }, 2000)
      } else {
        const textoDoJava = await resposta.text()
        setMensagemErro(textoDoJava || 'Erro ao criar conta. Tente novamente.')
      }

    } catch (erro) {
      setMensagemErro('Erro de conexão com o servidor')
    }
  }

  return (
    <div className="bg-white border border-[rgba(199,196,216,0.4)] rounded-2xl shadow-[0px_25px_50px_-12px_rgba(79,70,229,0.05)] flex flex-col gap-8 p-6 sm:p-[41px]">
      <div className="flex flex-col items-center w-full">
        <div className="mb-4">
          <div className="bg-[rgba(79,70,229,0.1)] border border-[rgba(79,70,229,0.1)] flex items-center justify-center w-14 h-14 rounded-xl shadow-sm p-px">
            <GraduationCapIcon width={29} height={24} color="#4f46e5" />
          </div>
        </div>

        <h2 className="font-semibold text-[20px] text-brand-accent tracking-[-0.5px] leading-7 text-center mb-1">
          MatriculaFácil
        </h2>

        <h1 className="font-bold text-[28px] sm:text-[32px] text-ui-dark tracking-[-0.64px] leading-tight text-center mb-2">
          Criar Conta
        </h1>

        <p className="font-normal text-base text-ui-medium leading-6 text-center max-w-[320px]">
          Preencha os dados abaixo para iniciar sua jornada acadêmica.
        </p>
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
          label="Nome Completo"
          type="text"
          placeholder="Ex: João da Silva"
          value={nome}
          onChange={(e: any) => setNome(e.target.value)}
        />

        <InputField
          label="E-mail Institucional"
          type="email"
          placeholder="joao.silva@aluno.edu.br"
          value={email}
          onChange={(e: any) => setEmail(e.target.value)}
        />

        <InputField
          label="Senha"
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

        <div className="pb-2">
          <InputField
            label="Confirmar Senha"
            type="password"
            placeholder="••••••••"
            value={confirmarSenha}
            onChange={(e: any) => setConfirmarSenha(e.target.value)}
          />
        </div>

        <button
          type="submit"
          className="w-full bg-brand-accent text-white font-normal text-base leading-6 py-[14px] rounded-lg shadow-[0px_4px_6px_-1px_rgba(79,70,229,0.2),0px_2px_4px_-2px_rgba(79,70,229,0.2)] hover:bg-indigo-700 active:bg-indigo-800 transition-colors"
        >
          Criar Conta
        </button>
      </form>

      <div className="pt-2">
        <div className="border-t border-[rgba(199,196,216,0.2)] pt-6 w-full">
          <div className="flex items-center justify-center gap-1">
            <span className="font-normal text-[15px] text-ui-medium leading-[22.5px]">
              Já tem uma conta?
            </span>
            <button
              type="button"
              onClick={() => onNavigate?.('login')}
              className="font-medium text-[15px] text-brand-accent leading-[22.5px] hover:underline"
            >
              Entre
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}