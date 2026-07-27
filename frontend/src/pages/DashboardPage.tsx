import { useEffect, useState } from 'react'
import DashboardHeader, { type DashboardTab } from '../components/DashboardHeader'
import CatalogHeading from '../components/CatalogHeading'
import CatalogGrid from '../components/CatalogGrid'
import MinhasMatriculasView from '../components/MinhasMatriculasView'
import ModalDetalhes from '../components/ModalDetalhes'
import { api } from '../services/api'
import type { MateriaResponse, AlunoPerfil, MatriculaResponse, Page } from '../types'

interface DashboardPageProps {
  onNavigate?: (page: Page) => void
}

export default function DashboardPage({ onNavigate }: DashboardPageProps) {
  const [activeTab, setActiveTab] = useState<DashboardTab>('minhas-matriculas')
  const [perfil, setPerfil] = useState<AlunoPerfil | null>(null)
  const [materias, setMaterias] = useState<MateriaResponse[]>([])
  const [inscritas, setInscritas] = useState<MateriaResponse[]>([])
  const [historico, setHistorico] = useState<MatriculaResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [erro, setErro] = useState('')
  const [mensagem, setMensagem] = useState('')
  const [mensagemCatalogo, setMensagemCatalogo] = useState('')
  const [carregandoAcao, setCarregandoAcao] = useState(false)
  const [materiaDetalhe, setMateriaDetalhe] = useState<MateriaResponse | null>(null)

  async function carregarDados() {
    try {
      setLoading(true)
      setErro('')

      const [perfilData, materiasData, inscritasData, historicoData] = await Promise.all([
        api.auth.perfil(),
        api.materia.listar(),
        api.matricula.minhasMatriculas(),
        api.matricula.historico(),
      ])

      setPerfil(perfilData)
      setMaterias(materiasData)
      setInscritas(inscritasData)
      setHistorico(historicoData)
    } catch (e: unknown) {
      if (e instanceof Error) {
        setErro(e.message)
      } else {
        setErro('Erro ao carregar dados')
      }
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    carregarDados()
  }, [])

  async function handleInscrever(materiaID: string) {
    try {
      setMensagemCatalogo('')
      await api.matricula.inscrever(materiaID)
      setMensagemCatalogo('Inscrição realizada com sucesso')
      setMensagem('Inscrição realizada com sucesso')
      await carregarDados()
    } catch (e: unknown) {
      if (e instanceof Error) {
        setMensagemCatalogo(e.message)
      } else {
        setMensagemCatalogo('Erro ao realizar inscrição')
      }
    }
  }

  async function handleCancelarInscricao(materiaID: string) {
    try {
      setMensagem('')
      setCarregandoAcao(true)
      await api.matricula.desinscrever(materiaID)
      setMensagem('Inscrição cancelada com sucesso')
      setMensagemCatalogo('')
      await carregarDados()
    } catch (e: unknown) {
      if (e instanceof Error) {
        setMensagem(e.message)
      } else {
        setMensagem('Erro ao cancelar inscrição')
      }
    } finally {
      setCarregandoAcao(false)
    }
  }

  function handleLogout() {
    localStorage.removeItem('token_jwt')
    onNavigate?.('login')
  }

  const codigosInscritos = inscritas.map((m) => m.codigoMateria)

  const codigosConcluidos = materias
    .filter((materia) =>
      historico.some(
        (h) => h.materiaID === materia.materiaID && h.status === 'CONCLUIDA'
      )
    )
    .map((m) => m.codigoMateria)

  if (loading) {
    return (
      <div className="min-h-screen bg-ui-bg flex items-center justify-center">
        <p className="text-ui-muted text-lg">Carregando...</p>
      </div>
    )
  }

  if (erro) {
    return (
      <div className="min-h-screen bg-ui-bg flex flex-col items-center justify-center gap-4 px-4">
        <div className="bg-red-50 text-red-700 p-4 rounded-lg text-sm border border-red-200 text-center max-w-md">
          {erro}
        </div>
        <button
          onClick={() => handleLogout()}
          className="text-sm font-medium text-brand-accent hover:underline"
        >
          Voltar ao login
        </button>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-ui-bg">
      <DashboardHeader
        nome={perfil?.nome ?? 'Aluno'}
        activeTab={activeTab}
        onTabChange={setActiveTab}
        onLogout={handleLogout}
      />

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 sm:py-10">
        {activeTab === 'minhas-matriculas' ? (
          <MinhasMatriculasView
            inscritas={inscritas}
            creditosAtuais={perfil?.creditoDoSemestre ?? 0}
            mensagem={mensagem}
            carregando={carregandoAcao}
            onCancelar={handleCancelarInscricao}
            onVerCatalogo={() => setActiveTab('catalogo')}
            onFecharMensagem={() => setMensagem('')}
          />
        ) : (
          <>
            <CatalogHeading semestre="2026.1" />

            {mensagemCatalogo && (
              <div
                className={`mt-4 p-3 rounded-lg text-sm border text-center font-medium ${
                  mensagemCatalogo.includes('sucesso') || mensagemCatalogo.includes('realizada')
                    ? 'bg-green-50 text-green-700 border-green-200'
                    : 'bg-red-50 text-red-600 border-red-100'
                }`}
              >
                {mensagemCatalogo}
                <button
                  onClick={() => setMensagemCatalogo('')}
                  className="ml-3 underline text-xs"
                >
                  Fechar
                </button>
              </div>
            )}

            <div className="mt-6">
              <CatalogGrid
                materias={materias}
                codigosConcluidos={codigosConcluidos}
                codigosInscritos={codigosInscritos}
                creditosAtuais={perfil?.creditoDoSemestre ?? 0}
                onInscrever={handleInscrever}
                onVerDetalhes={setMateriaDetalhe}
              />
            </div>
          </>
        )}
      </main>

      <ModalDetalhes
        materia={materiaDetalhe}
        onClose={() => setMateriaDetalhe(null)}
      />
    </div>
  )
}
