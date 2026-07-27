import { MateriaResponse } from '../types'

interface CatalogCardProps {
  materia: MateriaResponse
  inscrito: boolean
  preRequisitoBloqueado: boolean
  creditosEstourados: boolean
  conflitoHorario: boolean
  creditosAtuais: number
  onInscrever: (materiaID: string) => void
  onVerDetalhes: (materia: MateriaResponse) => void
}

const diasSemana: Record<string, string> = {
  Seg: 'Seg', Ter: 'Ter', Qua: 'Qua', Qui: 'Qui', Sex: 'Sex', Sab: 'Sab',
}

function formatarHorario(horario: string) {
  return horario
    .split(',')
    .map((h) => h.trim())
    .join('  |  ')
}

function creditoLabel(credito: number) {
  return `${credito} ${credito === 1 ? 'Crédito' : 'Créditos'}`
}

function vagaLabel(vagas: number) {
  return `${vagas} ${vagas === 1 ? 'Vaga' : 'Vagas'}`
}

export default function CatalogCard({
  materia,
  inscrito,
  preRequisitoBloqueado,
  creditosEstourados,
  conflitoHorario,
  creditosAtuais,
  onInscrever,
  onVerDetalhes,
}: CatalogCardProps) {
  const indisponivel = !materia.disponivel
  const bloqueado = preRequisitoBloqueado || creditosEstourados || conflitoHorario || indisponivel

  return (
    <div className={`bg-white border border-ui-border rounded-xl shadow-sm hover:shadow-md transition-shadow flex flex-col ${indisponivel ? 'opacity-60' : ''}`}>
      <div className="p-5 flex flex-col gap-3 flex-1">
        <div className="flex items-start justify-between gap-2">
          <span className="text-xs font-semibold tracking-wider uppercase text-ui-muted">
            {materia.codigoMateria}
          </span>
          {inscrito && (
            <span className="text-xs font-medium px-2.5 py-0.5 rounded-full bg-brand-light text-brand-primary shrink-0">
              Inscrito
            </span>
          )}
        </div>

        <h3 className="font-semibold text-base text-ui-dark leading-snug">
          {materia.nome}
        </h3>

        <div className="flex flex-col gap-1.5 text-sm text-ui-medium">
          <div className="flex items-center gap-2">
            <svg className="w-4 h-4 shrink-0 text-ui-muted" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <span>{formatarHorario(materia.horario)}</span>
          </div>

          <div className="flex items-center gap-4 flex-wrap">
            <div className="flex items-center gap-1.5">
              <svg className="w-4 h-4 shrink-0 text-ui-muted" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M4.26 10.147a60.438 60.438 0 00-.491 6.347A48.627 48.627 0 0112 20.904a48.627 48.627 0 018.232-4.41 60.46 60.46 0 00-.491-6.347m-15.482 0a50.57 50.57 0 00-2.658-.813A59.905 59.905 0 0112 3.493a59.902 59.902 0 0110.399 5.84c-.896.248-1.783.52-2.658.814m-15.482 0A50.697 50.697 0 0112 13.489a50.702 50.702 0 017.74-3.342" />
            </svg>
            <span>{creditoLabel(materia.credito)}</span>
            </div>
            <div className="flex items-center gap-1.5">
              <svg className="w-4 h-4 shrink-0 text-ui-muted" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M15 19.128a9.38 9.38 0 002.625.372 9.337 9.337 0 004.121-.952 4.125 4.125 0 00-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 018.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0111.964-3.07M12 6.375a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zm8.25 2.25a2.625 2.625 0 11-5.25 0 2.625 2.625 0 015.25 0z" />
              </svg>
              <span>{vagaLabel(materia.qtdVagas)}</span>
            </div>
          </div>

          <div className="flex items-center gap-1.5">
            <svg className="w-4 h-4 shrink-0 text-ui-muted" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M18 18.72a9.094 9.094 0 003.741-.479 3 3 0 00-4.682-2.72m.94 3.198l.001.031c0 .225-.012.447-.037.666A11.944 11.944 0 0112 21c-2.17 0-4.207-.576-5.963-1.584A6.062 6.062 0 016 18.719m12 0a5.971 5.971 0 00-.941-3.197m0 0A5.995 5.995 0 0012 12.75a5.995 5.995 0 00-5.058 2.772m0 0a3 3 0 00-4.681 2.72 8.986 8.986 0 003.74.477m.94-3.197a5.971 5.971 0 00-.94 3.197M15 6.75a3 3 0 11-6 0 3 3 0 016 0zm6 3a2.25 2.25 0 11-4.5 0 2.25 2.25 0 014.5 0zm-13.5 0a2.25 2.25 0 11-4.5 0 2.25 2.25 0 014.5 0z" />
            </svg>
            <span>{materia.professor}</span>
          </div>
        </div>

        <div className="flex items-center gap-1.5 text-sm text-ui-muted">
          <span>Pré-requisito:</span>
          <span className={`font-medium ${materia.preRequisito === 'NENHUM' ? 'text-green-600' : 'text-ui-dark'}`}>
            {materia.preRequisito === 'NENHUM' ? 'Nenhum' : materia.preRequisito}
          </span>
        </div>

        <div className="flex flex-col gap-1.5 mt-auto pt-1">
          <div className="text-xs text-ui-muted">
            Créditos do semestre: {creditosAtuais} / 24
          </div>

          {indisponivel && (
            <div className="text-xs font-medium px-2.5 py-1 rounded-md bg-gray-100 text-gray-500 border border-gray-200">
              Não ofertada este semestre
            </div>
          )}
          {preRequisitoBloqueado && !indisponivel && (
            <div className="text-xs font-medium px-2.5 py-1 rounded-md bg-red-50 text-red-700 border border-red-100">
              Pré-requisito não concluído
            </div>
          )}
          {creditosEstourados && !indisponivel && (
            <div className="text-xs font-medium px-2.5 py-1 rounded-md bg-orange-50 text-orange-700 border border-orange-100">
              Limite de créditos excedido
            </div>
          )}
          {conflitoHorario && !indisponivel && (
            <div className="text-xs font-medium px-2.5 py-1 rounded-md bg-red-50 text-red-700 border border-red-100">
              Conflito de horário detectado
            </div>
          )}
        </div>
      </div>

      <div className="border-t border-ui-border px-5 py-3 flex items-center gap-2">
        <button
          onClick={() => onVerDetalhes(materia)}
          className="text-sm font-medium text-brand-accent hover:text-brand-primary transition-colors"
        >
          Ver detalhes
        </button>

        <div className="flex-1" />

        <button
          onClick={() => onInscrever(materia.materiaID)}
          disabled={bloqueado || inscrito}
          className="text-sm font-medium px-4 py-1.5 rounded-lg bg-brand-accent text-white hover:bg-indigo-700 active:bg-indigo-800 disabled:bg-ui-border disabled:text-ui-muted disabled:cursor-not-allowed transition-colors"
        >
          {inscrito ? 'Inscrito' : 'Inscrever'}
        </button>
      </div>
    </div>
  )
}
