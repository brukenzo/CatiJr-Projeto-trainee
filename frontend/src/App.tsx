import { useState } from 'react'
import LoginPage from './pages/LoginPage'
import SignupPage from './pages/SignupPage'
import DashboardPage from './pages/DashboardPage'
import ForgotPasswordPage from './pages/ForgotPasswordPage'
import ResetPasswordPage from './pages/ResetPasswordPage'
import { Page } from './types'

export default function App() {
  const [page, setPage] = useState<Page>('login')
  
  // Guarda o e-mail digitado na tela de esqueci a senha
  const [emailRecuperacao, setEmailRecuperacao] = useState('')

  function handleNavigate(nextPage: Page, email?: string) {
    if (email) {
      setEmailRecuperacao(email) // Se veio um e-mail, salva ele na memória
    }
    setPage(nextPage) // Redireciona para a próxima tela
  }

  if (page === 'signup') return <SignupPage onNavigate={handleNavigate} />
  if (page === 'dashboard') return <DashboardPage onNavigate={handleNavigate} />
  if (page === 'forgot-password') return <ForgotPasswordPage onNavigate={handleNavigate} />
  if (page === 'reset-password') return <ResetPasswordPage onNavigate={handleNavigate} emailInicial={emailRecuperacao} />

  return <LoginPage onNavigate={handleNavigate} />
}