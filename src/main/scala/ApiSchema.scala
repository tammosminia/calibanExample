import Cat.Color
import caliban.Value.StringValue
import caliban.schema.{GenericSchema, Schema}

object ApiSchema extends GenericSchema[CatRepo] {
  case class DurationArgs(maxLength: Int, pad: Boolean)
  def shortName(name: String)(args: DurationArgs): String = name.substring(0, args.maxLength) + (if (args.pad) "..." else "")
//  def shortName(name: String)(i: (Int, Boolean)): String = name.substring(0, i._1) + (if (i._2) "..." else "")
//  def shortName(name: String)(maxLength: Int, pad: Boolean): String = name.substring(0, maxLength) + (if (pad) "..." else "")

  implicit val colorSchema: Schema[Any, Color] = scalarSchema[Color]("CatColor", Some("the color of the cat"), None, c => StringValue(c.s))
  implicit val catSchema: Schema[CatRepo, Cat] = obj[CatRepo, Cat]("Cat")(
    implicit ft =>
      List(
        fieldWithArgs[Cat, DurationArgs]("name")(cat => shortName(cat.name)),
        field("color")(_.color),
        field("tail")(_.tail),
        field("speed")(cat => CatRepo.getSpeed(cat.name))
      )
  )
  implicit val queriesSchema: Schema[CatRepo, Queries] = obj[CatRepo, Queries]("Queries", Some("manually made queries"))(
    implicit ft =>
      List(
        field[Queries]("cats")(_.cats)
      )
  )
}