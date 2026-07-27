import { MateriaResponse } from '../types'

interface MinhasMatriculasViewProps {
  inscritas: MateriaResponse[]
  creditosAtuais: number
  mensagem: string
  carregando: boolean
  onCancelar: (materiaID: string) => void
  onVerCatalogo: () => void
  onFecharMensagem: () => void
}

function formatarHorario(horario: string) {
  return horario.split(',').map((h) => h.trim()).join('  |  ')
}

export default function MinhasMatriculasView({
  inscritas,
  creditosAtuais,
  mensagem,
  carregando,
  onCancelar,
  onVerCatalogo,
  onFecharMensagem,
}: MinhasMatriculasViewProps) {
  return (
    <div>
      <div className="flex flex-col gap-1">
        <h1 className="font-bold text-[28px] sm:text-[32px] text-ui-dark tracking-tight leading-tight">
          Minhas Inscrições
        </h1>
        <p className="text-base text-ui-muted leading-6">
          {creditosAtuais} / 24 créditos utilizados neste semestre
        </p>
      </div>

      {mensagem && (
        <div
          className={`mt-4 p-3 rounded-lg text-sm border text-center font-medium ${
            mensagem.includes('sucesso') || mensagem.includes('cancelad')
              ? 'bg-green-50 text-green-700 border-green-200'
              : 'bg-red-50 text-red-600 border-red-100'
          }`}
        >
          {mensagem}
          <button onClick={onFecharMensagem} className="ml-3 underline text-xs">
            Fechar
          </button>
        </div>
      )}

      <div className="mt-6">
        {inscritas.length === 0 ? (
          <div className="bg-white border border-ui-border rounded-xl p-10 text-center flex flex-col items-center gap-4">
            <div className="bg-brand-light rounded-full p-4">
              <svg className="w-8 h-8 text-brand-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.5}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M12 6.042A8.967 8.967 0 006 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 016 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 016-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0018 18a8.967 8.967 0 00-6 2.292m0-14.25v14.25" />
              </svg>
            </div>
            <h2 className="text-lg font-semibold text-ui-dark">Nenhuma matrícula ativa</h2>
            <p className="text-sm text-ui-muted max-w-sm">
              Você ainda não está inscrito em nenhuma disciplina neste semestre.
            </p>
            <button
              onClick={onVerCatalogo}
              className="mt-2 px-6 py-2.5 bg-brand-accent text-white font-medium text-sm rounded-lg hover:bg-indigo-700 transition-colors"
            >
              Ver Catálogo de Matérias
            </button>
          </div>
        ) : (
          <>
            <div className="flex items-center justify-between mb-4">
              <span className="text-sm text-ui-muted">
                {inscritas.length} {inscritas.length === 1 ? 'matéria inscrita' : 'matérias inscritas'}
              </span>
              <button
                onClick={onVerCatalogo}
                className="px-4 py-2 bg-brand-accent text-white font-medium text-sm rounded-lg hover:bg-indigo-700 transition-colors"
              >
                Inscrever em mais matérias
              </button>
            </div>

            <div className="flex flex-col gap-3">
              {inscritas.map((materia) => (
                <div
                  key={materia.materiaID}
                  className="bg-white border border-ui-border rounded-xl p-5 flex flex-col sm:flex-row sm:items-center gap-3"
                >
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2 mb-1">
                      <span className="text-xs font-semibold tracking-wider uppercase text-ui-muted">
                        {materia.codigoMateria}
                      </span>
                      <span className="text-xs font-medium px-2 py-0.5 rounded-full bg-brand-light text-brand-primary">
                        Inscrito
                      </span>
                    </div>
                    <h3 className="font-semibold text-base text-ui-dark">{materia.nome}</h3>
                    <div className="flex flex-wrap items-center gap-x-4 gap-y-1 mt-1.5 text-sm text-ui-medium">
                      <span>{formatarHorario(materia.horario)}</span>
                      <span className="hidden sm:inline text-ui-muted">|</span>
                      <span>{materia.credito} créditos</span>
                      <span className="hidden sm:inline text-ui-muted">|</span>
                      <span>{materia.professor}</span>
                    </div>
                  </div>

                  <button
                    onClick={() => onCancelar(materia.materiaID)}
                    disabled={carregando}
                    className="shrink-0 px-4 py-2 text-sm font-medium text-red-600 border border-red-200 rounded-lg hover:bg-red-50 disabled:opacity-50 transition-colors"
                  >
                    Cancelar inscrição
                  </button>
                </div>
              ))}
            </div>
          </>
        )}
      </div>
    </div>
  )
}
