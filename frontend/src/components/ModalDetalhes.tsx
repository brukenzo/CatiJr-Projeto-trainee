import { MateriaResponse } from '../types'

interface ModalDetalhesProps {
  materia: MateriaResponse | null
  onClose: () => void
}

export default function ModalDetalhes({ materia, onClose }: ModalDetalhesProps) {
  if (!materia) return null

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm"
      onClick={onClose}
    >
      <div
        className="bg-white rounded-xl shadow-xl border border-ui-border w-full max-w-lg max-h-[85vh] overflow-y-auto"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="p-6 flex flex-col gap-5">
          <div className="flex items-start justify-between gap-3">
            <div>
              <span className="text-xs font-semibold tracking-wider uppercase text-ui-muted">
                {materia.codigoMateria}
              </span>
              <h2 className="font-bold text-xl text-ui-dark mt-1">{materia.nome}</h2>
            </div>
            <button
              onClick={onClose}
              className="text-ui-muted hover:text-ui-dark transition-colors p-1"
            >
              <svg className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          <div className="space-y-3">
            <div>
              <h3 className="text-sm font-semibold text-ui-dark mb-1">Descrição</h3>
              <p className="text-sm text-ui-medium leading-relaxed">{materia.descricao}</p>
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div className="bg-ui-bg rounded-lg p-3">
                <span className="text-xs text-ui-muted">Professor</span>
                <p className="text-sm font-medium text-ui-dark">{materia.professor}</p>
              </div>
              <div className="bg-ui-bg rounded-lg p-3">
                <span className="text-xs text-ui-muted">Pré-requisito</span>
                <p className="text-sm font-medium text-ui-dark">
                  {materia.preRequisito === 'NENHUM' ? 'Nenhum' : materia.preRequisito}
                </p>
              </div>
              <div className="bg-ui-bg rounded-lg p-3">
                <span className="text-xs text-ui-muted">Créditos</span>
                <p className="text-sm font-medium text-ui-dark">{materia.credito}</p>
              </div>
              <div className="bg-ui-bg rounded-lg p-3">
                <span className="text-xs text-ui-muted">Vagas</span>
                <p className="text-sm font-medium text-ui-dark">{materia.qtdVagas}</p>
              </div>
            </div>

            <div>
              <h3 className="text-sm font-semibold text-ui-dark mb-1">Horário</h3>
              <p className="text-sm text-ui-medium">{materia.horario}</p>
            </div>
          </div>

          <div className="pt-2">
            <button
              onClick={onClose}
              className="w-full py-2.5 rounded-lg border border-ui-border text-sm font-medium text-ui-dark hover:bg-ui-bg transition-colors"
            >
              Fechar
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
