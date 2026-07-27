import { AlunoPerfil, MateriaResponse, MatriculaResponse } from '../types'

interface MeuPerfilViewProps {
  perfil: AlunoPerfil | null
  materias: MateriaResponse[]
  historico: MatriculaResponse[]
}

function getInitials(name: string): string {
  return name
    .split(' ')
    .filter((_, i, arr) => i === 0 || i === arr.length - 1)
    .map((n) => n[0].toUpperCase())
    .join('')
}

function formatarHorario(horario: string) {
  return horario
    .split(',')
    .map((h) => h.trim())
    .join('  |  ')
}

const statusLabel: Record<string, { label: string; color: string }> = {
  CONCLUIDA: { label: 'Aprovado', color: 'bg-green-50 text-green-700 border-green-200' },
  REPROVADA: { label: 'Reprovado', color: 'bg-red-50 text-red-700 border-red-200' },
}

export default function MeuPerfilView({ perfil, materias, historico }: MeuPerfilViewProps) {
  const materiasComHistorico = historico
    .filter((h) => h.status === 'CONCLUIDA' || h.status === 'REPROVADA')
    .map((h) => {
      const materia = materias.find((m) => m.materiaID === h.materiaID)
      return { ...h, materia }
    })
    .filter((item) => item.materia != null)

  const creditos = perfil?.creditoDoSemestre ?? 0

  return (
    <div>
      <h1 className="font-bold text-[28px] sm:text-[32px] text-ui-dark tracking-tight leading-tight">
        Meu Perfil
      </h1>

      <div className="mt-6 bg-white border border-ui-border rounded-xl p-5 sm:p-6 flex flex-col sm:flex-row sm:items-center gap-4">
        <div className="w-14 h-14 rounded-full bg-brand-accent flex items-center justify-center shrink-0">
          <span className="text-white text-xl font-semibold">
            {getInitials(perfil?.nome ?? '')}
          </span>
        </div>
        <div className="flex-1 min-w-0">
          <h2 className="font-semibold text-lg text-ui-dark">{perfil?.nome ?? '—'}</h2>
          <p className="text-sm text-ui-muted mt-0.5">{perfil?.email ?? '—'}</p>
          <div className="flex items-center gap-4 mt-2 text-sm">
            <span className="text-ui-medium">
              <span className="font-semibold text-ui-dark">{creditos}</span> / 24 créditos no semestre
            </span>
          </div>
        </div>
      </div>

      <div className="mt-8">
        <h2 className="font-bold text-xl sm:text-2xl text-ui-dark tracking-tight">
          Histórico Acadêmico
        </h2>
        <p className="text-sm text-ui-muted mt-1">
          {materiasComHistorico.length === 0
            ? 'Nenhuma matéria concluída ou reprovada até o momento.'
            : `${materiasComHistorico.length} ${materiasComHistorico.length === 1 ? 'matéria registrada' : 'matérias registradas'} no histórico`}
        </p>

        {materiasComHistorico.length > 0 && (
          <div className="mt-4 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 sm:gap-5">
            {materiasComHistorico.map(({ matriculaID, status, materia }) => {
              const st = statusLabel[status] ?? { label: status, color: 'bg-gray-50 text-gray-500 border-gray-200' }
              return (
                <div
                  key={matriculaID}
                  className="bg-white border border-ui-border rounded-xl p-5 flex flex-col gap-3"
                >
                  <div className="flex items-start justify-between gap-2">
                    <span className="text-xs font-semibold tracking-wider uppercase text-ui-muted">
                      {materia!.codigoMateria}
                    </span>
                    <span className={`text-xs font-medium px-2.5 py-0.5 rounded-full border shrink-0 ${st.color}`}>
                      {st.label}
                    </span>
                  </div>

                  <h3 className="font-semibold text-base text-ui-dark leading-snug">
                    {materia!.nome}
                  </h3>

                  <div className="flex flex-col gap-1.5 text-sm text-ui-medium">
                    <div className="flex items-center gap-2">
                      <svg className="w-4 h-4 shrink-0 text-ui-muted" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                        <path strokeLinecap="round" strokeLinejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z" />
                      </svg>
                      <span>{formatarHorario(materia!.horario)}</span>
                    </div>
                    <div className="flex items-center gap-1.5">
                      <svg className="w-4 h-4 shrink-0 text-ui-muted" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                        <path strokeLinecap="round" strokeLinejoin="round" d="M4.26 10.147a60.438 60.438 0 00-.491 6.347A48.627 48.627 0 0112 20.904a48.627 48.627 0 018.232-4.41 60.46 60.46 0 00-.491-6.347m-15.482 0a50.57 50.57 0 00-2.658-.813A59.905 59.905 0 0112 3.493a59.902 59.902 0 0110.399 5.84c-.896.248-1.783.52-2.658.814m-15.482 0A50.697 50.697 0 0112 13.489a50.702 50.702 0 017.74-3.342" />
                      </svg>
                      <span>{materia!.credito} créditos</span>
                    </div>
                    <div className="flex items-center gap-1.5">
                      <svg className="w-4 h-4 shrink-0 text-ui-muted" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                        <path strokeLinecap="round" strokeLinejoin="round" d="M18 18.72a9.094 9.094 0 003.741-.479 3 3 0 00-4.682-2.72m.94 3.198l.001.031c0 .225-.012.447-.037.666A11.944 11.944 0 0112 21c-2.17 0-4.207-.576-5.963-1.584A6.062 6.062 0 016 18.719m12 0a5.971 5.971 0 00-.941-3.197m0 0A5.995 5.995 0 0012 12.75a5.995 5.995 0 00-5.058 2.772m0 0a3 3 0 00-4.681 2.72 8.986 8.986 0 003.74.477m.94-3.197a5.971 5.971 0 00-.94 3.197M15 6.75a3 3 0 11-6 0 3 3 0 016 0zm6 3a2.25 2.25 0 11-4.5 0 2.25 2.25 0 014.5 0zm-13.5 0a2.25 2.25 0 11-4.5 0 2.25 2.25 0 014.5 0z" />
                      </svg>
                      <span>{materia!.professor}</span>
                    </div>
                  </div>
                </div>
              )
            })}
          </div>
        )}

        {materiasComHistorico.length === 0 && (
          <div className="mt-4 bg-white border border-ui-border rounded-xl p-10 text-center flex flex-col items-center gap-3">
            <div className="bg-brand-light rounded-full p-4">
              <svg className="w-8 h-8 text-brand-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M12 6.042A8.967 8.967 0 006 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 016 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 016-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0018 18a8.967 8.967 0 00-6 2.292m0-14.25v14.25" />
              </svg>
            </div>
            <p className="text-sm text-ui-muted max-w-xs">
              Seu histórico acadêmico aparecerá aqui conforme você for concluindo matérias.
            </p>
          </div>
        )}
      </div>
    </div>
  )
}
