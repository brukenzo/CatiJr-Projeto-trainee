import ResetPasswordCard from '../components/ResetPasswordCard'
import { Page } from '../types'

interface ResetPasswordPageProps {
  onNavigate?: (page: Page) => void
  emailInicial?: string
}

export default function ResetPasswordPage({ onNavigate, emailInicial }: ResetPasswordPageProps) {
  return (
    <div className="min-h-screen w-full bg-ui-bg flex items-center justify-center px-4 py-8 sm:py-16">
      <div className="w-full max-w-[420px]">
        <ResetPasswordCard onNavigate={onNavigate} emailInicial={emailInicial} />
      </div>
    </div>
  )
}