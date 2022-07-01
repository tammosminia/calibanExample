import Cat.Tail

case class Cat(name: String, color: Cat.Color, tail: Tail)

object Cat {
  case class Color(s: String) extends AnyVal

  case class Tail(color: Color)
}