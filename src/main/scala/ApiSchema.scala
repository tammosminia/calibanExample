import CalibanUtils.SchemaFunctions
import Cat.Color
import caliban.Value.StringValue
import caliban.schema.{GenericSchema, Schema}

object ApiSchema extends GenericSchema[CatRepo] {
  case class ShortNameArgs(maxLength: Int, pad: Boolean)
  def shortName(name: String)(args: ShortNameArgs): String = name.substring(0, args.maxLength) + (if (args.pad) "..." else "")

  implicit val colorSchema: Schema[Any, Color] = scalarSchema[Color]("CatColor", Some("the color of the cat"), None, c => StringValue(c.s))
  implicit val catSchema: Schema[CatRepo, Cat] = obj[CatRepo, Cat]("Cat")(
    implicit ft =>
      List(
        fieldWithArgs("name")(cat => shortName(cat.name)),
        field("color")(_.color),
        field("pattern")(_.pattern),
        field("similar")(cat => CatRepo.similar(cat))
      )
  ) //.addDescription("The cat sits on the mat")
}