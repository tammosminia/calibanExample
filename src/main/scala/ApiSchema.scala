import Cat.Color
import caliban.Value.StringValue
import caliban.schema.{GenericSchema, Schema}

object ApiSchema extends GenericSchema[CatRepo] {
  def shortName(name: String)(maxLength: Int) = name.substring(0, maxLength)

  implicit val colorSchema: Schema[Any, Color] = scalarSchema[Color]("CatColor", Some("the color of the cat"), None, c => StringValue(c.s))
  implicit val catSchema: Schema[CatRepo, Cat] = obj[CatRepo, Cat]("Cat")(
    implicit ft =>
      List(
        fieldWithArgs[Cat, Int]("name")(cat => shortName(cat.name)),
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