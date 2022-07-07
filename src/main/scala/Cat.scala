import Cat.Pattern
import enumeratum.{Enum, EnumEntry}

case class Cat(name: String, color: Cat.Color, pattern: Pattern)

object Cat {
  case class Color(s: String) extends AnyVal

  sealed abstract class Pattern extends EnumEntry
  object Pattern extends Enum[Pattern] {
    case object Stripes extends Pattern
    case object Plain extends Pattern

    val values: IndexedSeq[Pattern] = findValues
  }
}