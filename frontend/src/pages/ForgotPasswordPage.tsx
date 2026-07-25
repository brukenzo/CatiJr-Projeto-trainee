import ForgotPasswordCard from '../components/ForgotPasswordCard'
import { Page } from '../types'

interface ForgotPasswordPageProps {
  onNavigate?: (page: Page, email?: string) => void
}

export default function ForgotPasswordPage({ onNavigate }: ForgotPasswordPageProps) {
  return (
    <div className="min-h-screen w-full bg-ui-bg flex items-center justify-center px-4 py-8 sm:py-16">
      <div className="w-full max-w-[420px]">
        <ForgotPasswordCard onNavigate={onNavigate} />
      </div>
    </div>
  )
}