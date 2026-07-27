import { MateriaResponse } from '../types'
import CatalogCard from './CatalogCard'

interface CatalogGridProps {
  materias: MateriaResponse[]
  codigosConcluidos: string[]
  codigosInscritos: string[]
  creditosAtuais: number
  onInscrever: (materiaID: string) => void
  onVerDetalhes: (materia: MateriaResponse) => void
}

function parseHorarios(horario: string): { dia: string; inicio: string; fim: string }[] {
  return horario.split(',').map((h) => {
    const [dia, ...resto] = h.trim().split(' ')
    const [inicio, , fim] = resto.join(' ').split(' ')
    return { dia, inicio, fim }
  })
}

function temConflito(
  horariosA: string,
  horariosB: string
): boolean {
  const slotsA = parseHorarios(horariosA)
  const slotsB = parseHorarios(horariosB)

  for (const a of slotsA) {
    for (const b of slotsB) {
      if (a.dia === b.dia) {
        if (a.inicio < b.fim && b.inicio < a.fim) {
          return true
        }
      }
    }
  }
  return false
}

export default function CatalogGrid({
  materias,
  codigosConcluidos,
  codigosInscritos,
  creditosAtuais,
  onInscrever,
  onVerDetalhes,
}: CatalogGridProps) {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 sm:gap-5">
      {materias.map((materia) => {
        const inscrito = codigosInscritos.includes(materia.codigoMateria)
        const indisponivel = !materia.disponivel
        const prereqOk =
          materia.preRequisito === 'NENHUM' ||
          codigosConcluidos.includes(materia.preRequisito)

        const creditosFuturos = creditosAtuais + materia.credito
        const creditosEstourados = !inscrito && !indisponivel && creditosFuturos > 24

        const conflitoHorario = !inscrito && !indisponivel && codigosInscritos.some((codigo) => {
          const inscrita = materias.find((m) => m.codigoMateria === codigo)
          return inscrita ? temConflito(materia.horario, inscrita.horario) : false
        })

        return (
          <CatalogCard
            key={materia.materiaID}
            materia={materia}
            inscrito={inscrito}
            preRequisitoBloqueado={!prereqOk}
            creditosEstourados={creditosEstourados}
            conflitoHorario={conflitoHorario}
            creditosAtuais={creditosAtuais}
            onInscrever={onInscrever}
            onVerDetalhes={onVerDetalhes}
          />
        )
      })}
    </div>
  )
}
